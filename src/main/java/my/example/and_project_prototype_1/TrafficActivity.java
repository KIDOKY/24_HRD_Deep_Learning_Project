package my.example.and_project_prototype_1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrafficActivity extends AppCompatActivity {
    private Interpreter tflite;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
        requestCameraPermission();

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "모델 로드 실패", Toast.LENGTH_SHORT).show();
        }

        cameraExecutor = Executors.newSingleThreadExecutor();

        Button captureButton = findViewById(R.id.capture_button);
        Button backButton = findViewById(R.id.back_button);

        // 설정된 언어에 따라 버튼 텍스트 변경
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString("selected_language", "한국어");

        switch (selectedLanguage) {
            case "영어":
                captureButton.setText("Take Photo");
                backButton.setText("Back");
                break;
            case "중국어":
                captureButton.setText("拍照");
                backButton.setText("返回");
                break;
            default:
                captureButton.setText("사진 찍기");
                backButton.setText("뒤로 가기");
                break;
        }

        captureButton.setOnClickListener(v -> takePhoto());

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrafficActivity.this, StartActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
        });
    }

    private void requestCameraPermission() {
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        startCamera();
                    } else {
                        Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                PreviewView previewView = findViewById(R.id.viewFinder);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                imageCapture = new ImageCapture.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .build();

                cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraError", "카메라 초기화 실패: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture != null) {
            imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
                @Override
                public void onCaptureSuccess(ImageProxy imageProxy) {
                    Log.d("ImageCapture", "이미지 크기: " + imageProxy.getWidth() + "x" + imageProxy.getHeight());

                    ByteBuffer buffer = convertImageToModelInput(imageProxy);
                    int result = runInference(buffer);
                    handleInferenceResult(result);
                    imageProxy.close();
                }

                @Override
                public void onError(ImageCaptureException exception) {
                    Log.e("ImageCapture", "이미지 캡처 실패: " + exception.getMessage());
                }
            });
        }
    }

    private int runInference(ByteBuffer buffer) {
        float[][] output = new float[1][21]; // 클래스 수에 따라 조정 필요
        tflite.run(buffer, output);

        int predictedIndex = getMaxProbableIndex(output);
        Log.d("InferenceResult", "인식된 클래스: " + predictedIndex + ", 확률: " + output[0][predictedIndex]);

        return output[0][predictedIndex] >= 0.8 ? predictedIndex : -1; // 임계값 조정 가능
    }

    private int getMaxProbableIndex(float[][] output) {
        int maxIndex = 0;
        float maxProb = output[0][0];
        for (int i = 1; i < output[0].length; i++) {
            if (output[0][i] > maxProb) {
                maxProb = output[0][i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private void handleInferenceResult(int classIndex) {
        Intent intent = new Intent(TrafficActivity.this, WarningTrafficActivity.class);
        intent.putExtra("recognizedSign", classIndex);
        startActivity(intent);
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("sign_model.tflite");
        FileInputStream inputStream = fileDescriptor.createInputStream();
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private ByteBuffer convertImageToModelInput(ImageProxy image) {
        int modelInputWidth = 150;
        int modelInputHeight = 150;
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * modelInputWidth * modelInputHeight * 3);
        inputBuffer.order(ByteOrder.nativeOrder());
        Bitmap bitmap = imageToBitmap(image);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, modelInputWidth, modelInputHeight, true);
        for (int y = 0; y < resizedBitmap.getHeight(); y++) {
            for (int x = 0; x < resizedBitmap.getWidth(); x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                inputBuffer.putFloat(((pixel >> 16) & 0xFF) / 255.0f); // 빨강
                inputBuffer.putFloat(((pixel >> 8) & 0xFF) / 255.0f);  // 초록
                inputBuffer.putFloat((pixel & 0xFF) / 255.0f);          // 파랑
            }
        }
        return inputBuffer;
    }

    private Bitmap imageToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        // BitmapFactory.decodeByteArray를 사용하여 이미지를 디코딩
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        if (tflite != null) {
            tflite.close();
        }
    }
}

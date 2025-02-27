package my.example.and_project_prototype_1;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences; // SharedPreferences 임포트

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity_duyoung extends AppCompatActivity {

    private Interpreter tflite;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    // 클래스 이름 배열
    private String[] classNames = {
            "Explosive", "Flammable", "Oxidizing", "CompressedGas",
            "Corrosive", "Toxic", "Health_hazard", "Serious_Health_hazard"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_duyoung);

        requestCameraPermission();
        // 카메라 실행
        startCamera();

        // 캡처 버튼 클릭 이벤트
        findViewById(R.id.camera_capture_button).setOnClickListener(v -> {
            takePhoto();
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity_duyoung.this, StartActivity.class);
            startActivity(intent);
            finish(); // Optionally close the current activity
        });

        // 언어 설정에 따라 버튼 텍스트 변경
        Button captureButton = findViewById(R.id.camera_capture_button);
        setButtonText(captureButton, backButton);
        
        // TFLite 모델 초기화
        initializeModel();
    }

    // 언어 설정에 따라 버튼 텍스트 변경
    private void setButtonText(Button captureButton, Button backButton) {
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
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        } else {
            startCamera(); // 권한이 있으면 카메라 시작
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // ImageCapture 설정
                imageCapture = new ImageCapture.Builder().build();

                // 카메라 설정
                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                // 미리보기
                preview.setSurfaceProvider(((PreviewView) findViewById(R.id.viewFinder)).getSurfaceProvider());
            } catch (Exception e) {
                Log.e(TAG, "CameraX 초기화 실패: " + e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture != null) {
            File photoFile = new File(getExternalFilesDir(null), "GHS_image.jpg");

            ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

            imageCapture.takePicture(outputOptions, Executors.newSingleThreadExecutor(), new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    Log.d(TAG, "사진이 저장되었습니다: " + photoFile.getAbsolutePath());
                    // 저장된 사진으로 추론 진행
                    runModelInference(photoFile);
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    Log.e(TAG, "사진 캡처 실패: " + exception.getMessage());
                    Toast.makeText(MainActivity_duyoung.this, "인식 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initializeModel() {
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            Log.e(TAG, "모델 파일 로드 실패: " + e.getMessage());
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("GHS_model5.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void runModelInference(File imageFile) {
        try {
            // 이미지 로드 및 전처리
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
            float[][][][] input = new float[1][128][128][3];

            for (int y = 0; y < 128; y++) {
                for (int x = 0; x < 128; x++) {
                    int pixel = resizedBitmap.getPixel(x, y);
                    input[0][y][x][0] = (Color.red(pixel)) / 255.0f;
                    input[0][y][x][1] = (Color.green(pixel)) / 255.0f;
                    input[0][y][x][2] = (Color.blue(pixel)) / 255.0f;
                }
            }

            // 추론 결과 저장
            float[][] output = new float[1][8];
            tflite.run(input, output);

            // 예측 결과 처리
            int predictedClass = getMaxIndex(output[0]);
            float confidence = output[0][predictedClass];

            // 최소 정확도(임계값) 설정
            float minConfidenceThreshold = 0.5f; // 예: 50% 이상일 때만 이동

            if (confidence >= minConfidenceThreshold) {
                goToResultActivity(predictedClass);
            } else {
                // 정확도가 낮으면 경고 메시지 출력
                runOnUiThread(() -> Toast.makeText(MainActivity_duyoung.this, "재촬영 필요", Toast.LENGTH_SHORT).show());
            }

        } catch (Exception e) {
            Log.e(TAG, "모델 추론 실패: " + e.getMessage());
        }
    }

    private int getMaxIndex(float[] probs) {
        int maxIndex = 0;
        for (int i = 1; i < probs.length; i++) {
            if (probs[i] > probs[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private void goToResultActivity(int predictedClass) {
        runOnUiThread(() -> {
            Intent intent = null;
            if (predictedClass >= 0 && predictedClass < classNames.length) {
                String className = classNames[predictedClass];
                switch (className) {
                    case "Explosive":
                        intent = new Intent(this, Explosive.class);
                        break;
                    // 필요한 경우 다른 클래스도 추가하세요.
                    case "CompressedGas":
                        intent = new Intent(this, CompressedGas.class);
                        break;
                    case "Corrosive":
                        intent = new Intent(this, Corrosive.class);
                        break;
                    case "Flammable":
                        intent = new Intent(this, Flammable.class);
                        break;
                    case "Health_hazard":
                        intent = new Intent(this, Health_hazard.class);
                        break;
                    case "Oxidizing":
                        intent = new Intent(this, Oxidizing.class);
                        break;
                    case "Serious_Health_hazard":
                        intent = new Intent(this, Serious_Health_hazard.class);
                        break;
                    case "Toxic":
                        intent = new Intent(this, Toxic.class);
                        break;

                    default:
                        Toast.makeText(this, "recapture", Toast.LENGTH_SHORT).show();
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

}
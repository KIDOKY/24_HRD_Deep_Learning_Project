package my.example.and_project_prototype_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.Manifest;

import com.google.common.util.concurrent.ListenableFuture;

public class StartActivity extends AppCompatActivity {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture; // CameraX 프로바이더
    private PreviewView previewView; // CameraX PreviewView
    private boolean isCameraActive = false; // 카메라 활성화 상태 저장 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_startactivity);

        // settings_button 클릭 리스너 추가
        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, SettingActivity1.class); // MainActivity3로 변경
            startActivity(intent);
        });

        // light_button 클릭 리스너 추가
        ImageButton lightButton = findViewById(R.id.light_button);
        lightButton.setOnClickListener(v -> toggleFlashlight(lightButton));

        // 교통 버튼 클릭 리스너 추가
        Button trafficButton = findViewById(R.id.top_left_button);
        trafficButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, TrafficActivity.class);
            startActivity(intent);
        });

        // 위험물 버튼 클릭 리스너 추가
        Button hazardousButton = findViewById(R.id.top_right_button);
        hazardousButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, MainActivity_duyoung.class);
            startActivity(intent);
        });

        TextView titleText = findViewById(R.id.title_text);

        // 설정된 언어에 따라 텍스트 변경
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String selectedLanguage = sharedPreferences.getString("selected_language", "영어");

        switch (selectedLanguage) {
            case "영어":
                titleText.setText("Traffic/Hazard Sign Recognition App");
                trafficButton.setText("Traffic");
                hazardousButton.setText("Hazard");
                break;
            case "중국어":
                titleText.setText("交通/危险标志识别应用");
                trafficButton.setText("交通");
                hazardousButton.setText("危险物");
                break;
            default:
                titleText.setText("교통/위험물 표지판 인식 앱");
                trafficButton.setText("교통");
                hazardousButton.setText("위험물");
                break;
        }
    }

    private boolean checkCameraPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
    }

    private void toggleFlashlight(ImageButton lightButton) {
        Toast.makeText(this, "Flash toggle은 CameraX에서 기본 지원되지 않습니다.", Toast.LENGTH_SHORT).show();
    }
}

package my.example.and_project_prototype_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.graphics.Insets;

public class SettingActivity1 extends AppCompatActivity {

    private TextView languageSelectionText;
    private Button languageButton;
    private Button displayButton;
    private SharedPreferences sharedPreferences;
    private String currentLanguage; // 현재 선택된 언어

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 테마 로드 (컨텐츠 뷰 설정 전에 호출)
        loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting1); // activity_main4으로 변경

        // UI 요소 초기화
        initializeUIElements();

        // WindowInsets 설정 (시스템 바 여백 설정)
        applyWindowInsets();

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        // 저장된 언어 정보 로드
        currentLanguage = sharedPreferences.getString("selected_language", "한국어");

        // 언어에 맞는 UI 업데이트
        updateUIBasedOnLanguage();

        // 버튼 클릭 리스너 설정
        setupButtonListeners();
    }

    // UI 요소 초기화 메서드
    private void initializeUIElements() {
        languageSelectionText = findViewById(R.id.language_selection_text);
        languageButton = findViewById(R.id.language_button);
        displayButton = findViewById(R.id.display_button);
    }

    // WindowInsets 적용 메서드
    private void applyWindowInsets() {
        // 'main'이라는 id를 가진 뷰를 찾습니다.
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            // 익명 클래스를 사용해 WindowInsetsListener를 설정
            ViewCompat.setOnApplyWindowInsetsListener(mainView, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    // 시스템 바 인셋을 가져와서 패딩을 적용
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets; // 인셋을 반환하여 다른 뷰에서도 사용할 수 있게 함
                }
            });
        }
    }

    // 언어에 따른 UI 업데이트
    private void updateUIBasedOnLanguage() {
        if (currentLanguage.equals("영어")) {
            languageSelectionText.setText("Settings");
            languageButton.setText("Language");
            displayButton.setText("Display");
        } else if (currentLanguage.equals("중국어")) {
            languageSelectionText.setText("设定");
            languageButton.setText("语言");
            displayButton.setText("显示");
        } else { // 기본값은 한국어
            languageSelectionText.setText("설정");
            languageButton.setText("언어");
            displayButton.setText("디스플레이");
        }
    }

    // 버튼 클릭 리스너 설정 메서드
    private void setupButtonListeners() {
        languageButton.setOnClickListener(v -> {
            // MainActivity3로 이동
            Intent intent = new Intent(SettingActivity1.this, SettingActivity3.class);
            startActivity(intent);
        });

        displayButton.setOnClickListener(v -> {
            // MainActivity5로 이동
            Intent intent = new Intent(SettingActivity1.this, SettingActivity2.class);
            startActivity(intent);
        });
    }

    // 테마 로드 메서드
    private void loadTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        if (darkModeEnabled) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }
}
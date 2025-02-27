package my.example.and_project_prototype_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity2 extends AppCompatActivity {

    private CheckBox darkModeCheckbox;
    private CheckBox lightModeCheckbox;
    private Button completeButton;
    private EditText editText;
    private TextView displayTextView; // 디스플레이 텍스트뷰 추가
    private SharedPreferences sharedPreferences;
    private String currentLanguage; // 현재 선택된 언어

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadTheme(); // 콘텐츠 뷰를 설정하기 전에 테마 로드
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);

        // 뷰 초기화
        editText = findViewById(R.id.edit_test);
        darkModeCheckbox = findViewById(R.id.dark_mode_checkbox);
        lightModeCheckbox = findViewById(R.id.light_mode_checkbox);
        completeButton = findViewById(R.id.complete_button);
        displayTextView = findViewById(R.id.display_text_view); // 디스플레이 텍스트 뷰 초기화

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        // 저장된 언어 정보 로드
        currentLanguage = sharedPreferences.getString("selected_language", "한국어");

        // 언어에 맞는 UI 업데이트
        updateUIBasedOnLanguage();

        // 저장된 모드 정보 로드하여 UI 업데이트
        loadSelectedMode();

        // 체크박스 리스너 설정
        darkModeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                lightModeCheckbox.setChecked(false);
                editText.setText(currentLanguage.equals("영어") ? "Dark Mode" : currentLanguage.equals("중국어") ? "黑暗模式" : "다크 모드");
                saveThemePreference(true); // 다크 모드로 저장
            } else {
                editText.setText(""); // 체크 해제 시 텍스트 삭제
            }
        });

        lightModeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                darkModeCheckbox.setChecked(false);
                editText.setText(currentLanguage.equals("영어") ? "Light Mode" : currentLanguage.equals("중국어") ? "明亮模式" : "라이트 모드");
                saveThemePreference(false); // 라이트 모드로 저장
            } else {
                editText.setText(""); // 체크 해제 시 텍스트 삭제
            }
        });

        // 완료 버튼 클릭 리스너
        completeButton.setOnClickListener(v -> {
            boolean darkModeEnabled = darkModeCheckbox.isChecked();
            boolean lightModeEnabled = lightModeCheckbox.isChecked();

            if (!darkModeEnabled && !lightModeEnabled) {
                Toast.makeText(SettingActivity2.this, getToastMessage("select_mode"), Toast.LENGTH_SHORT).show();
            } else {
                String modeMessage = darkModeEnabled ? getToastMessage("dark_mode_selected") : getToastMessage("light_mode_selected");
                Toast.makeText(SettingActivity2.this, modeMessage, Toast.LENGTH_SHORT).show();

                // MainActivity로 돌아가기
                Intent intent = new Intent(SettingActivity2.this, StartActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }
        });
    }

    // 언어에 따른 UI 업데이트
    private void updateUIBasedOnLanguage() {
        if (currentLanguage.equals("영어")) {
            darkModeCheckbox.setText("Dark Mode");
            lightModeCheckbox.setText("Light Mode");
            completeButton.setText("Complete");
            displayTextView.setText("Display"); // 디스플레이 텍스트 변경
            editText.setHint("Please select a mode"); // 모드 선택 힌트 변경
        } else if (currentLanguage.equals("중국어")) {
            darkModeCheckbox.setText("黑暗模式");
            lightModeCheckbox.setText("明亮模式");
            completeButton.setText("完成");
            displayTextView.setText("显示"); // 디스플레이 텍스트 변경
            editText.setHint("请选择模式"); // 모드 선택 힌트 변경
        } else { // 기본은 한국어
            darkModeCheckbox.setText("다크 모드");
            lightModeCheckbox.setText("라이트 모드");
            completeButton.setText("완료");
            displayTextView.setText("디스플레이"); // 디스플레이 텍스트 변경
            editText.setHint("모드를 선택해주세요"); // 모드 선택 힌트 변경
        }
    }

    // 저장된 모드 로드 및 UI 업데이트
    private void loadSelectedMode() {
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);

        if (isDarkMode) {
            darkModeCheckbox.setChecked(true);
            editText.setText(currentLanguage.equals("영어") ? "Dark Mode" : currentLanguage.equals("중국어") ? "黑暗模式" : "다크 모드");
        } else {
            lightModeCheckbox.setChecked(true);
            editText.setText(currentLanguage.equals("영어") ? "Light Mode" : currentLanguage.equals("중국어") ? "明亮模式" : "라이트 모드");
        }
    }

    // 언어에 따라 토스트 메시지 반환
    private String getToastMessage(String key) {
        switch (key) {
            case "select_mode":
                if (currentLanguage.equals("영어")) {
                    return "Please select a mode";
                } else if (currentLanguage.equals("중국어")) {
                    return "请选择模式";
                } else {
                    return "모드를 선택하십시오";
                }
            case "dark_mode_selected":
                if (currentLanguage.equals("영어")) {
                    return "Dark Mode has been selected";
                } else if (currentLanguage.equals("중국어")) {
                    return "已选择黑暗模式";
                } else {
                    return "다크 모드로 변경되었습니다";
                }
            case "light_mode_selected":
                if (currentLanguage.equals("영어")) {
                    return "Light Mode has been selected";
                } else if (currentLanguage.equals("중국어")) {
                    return "已选择明亮模式";
                } else {
                    return "라이트 모드로 변경되었습니다";
                }
            default:
                return "";
        }
    }

    // 테마 설정 로드
    private void loadTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean darkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        if (darkModeEnabled) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    // 테마 설정 저장
    private void saveThemePreference(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dark_mode", isDarkMode);
        editor.apply();
    }
}
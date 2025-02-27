package my.example.and_project_prototype_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingActivity3 extends AppCompatActivity {

    private EditText currentLanguageEditText;
    private CheckBox checkboxEnglish;
    private CheckBox checkboxChinese;
    private CheckBox checkboxKorean;
    private Button finishButton;
    private TextView languageSelectionText;
    private SharedPreferences sharedPreferences;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadTheme(); // 다크 모드 로드
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting3);

        // UI 요소 초기화
        currentLanguageEditText = findViewById(R.id.current_language_edit_text);
        checkboxEnglish = findViewById(R.id.checkbox_english);
        checkboxChinese = findViewById(R.id.checkbox_chinese);
        checkboxKorean = findViewById(R.id.checkbox_korean);
        finishButton = findViewById(R.id.finish_button);
        languageSelectionText = findViewById(R.id.language_selection_text);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        // TextToSpeech 초기화
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.KOREAN); // 기본 언어 설정
            }
        });

        // 저장된 언어 정보 가져오기
        loadSavedLanguage();

        // 체크박스 상태에 따라 EditText 업데이트 (한 번에 하나의 언어만 선택 가능)
        checkboxEnglish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxChinese.setChecked(false);
                checkboxKorean.setChecked(false);
                currentLanguageEditText.setText("English");
            } else {
                currentLanguageEditText.setText("");
            }
        });

        checkboxChinese.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxEnglish.setChecked(false);
                checkboxKorean.setChecked(false);
                currentLanguageEditText.setText("简体中文"); // 간체 중국어
            } else {
                currentLanguageEditText.setText("");
            }
        });

        checkboxKorean.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxEnglish.setChecked(false);
                checkboxChinese.setChecked(false);
                currentLanguageEditText.setText("한국어");
            } else {
                currentLanguageEditText.setText("");
            }
        });

        // 완료 버튼 클릭 리스너
        finishButton.setOnClickListener(v -> {
            String selectedLanguage = "";

            // 선택된 언어에 따라 변수 설정
            if (checkboxEnglish.isChecked()) {
                selectedLanguage = "영어";
            } else if (checkboxChinese.isChecked()) {
                selectedLanguage = "중국어";
            } else if (checkboxKorean.isChecked()) {
                selectedLanguage = "한국어";
            }

            // 선택된 언어에 따라 토스트 메시지 및 음성 출력
            if (!selectedLanguage.isEmpty()) {
                String toastMessage;
                switch (selectedLanguage) {
                    case "영어":
                        toastMessage = "English Selected";
                        textToSpeech.setLanguage(Locale.ENGLISH);
                        break;
                    case "중국어":
                        toastMessage = "中文选择完成";
                        textToSpeech.setLanguage(Locale.CHINESE);
                        break;
                    case "한국어":
                        toastMessage = "한국어 선택 완료";
                        textToSpeech.setLanguage(Locale.KOREAN);
                        break;
                    default:
                        toastMessage = "언어를 선택해 주세요";
                        break;
                }
                Toast.makeText(SettingActivity3.this, toastMessage, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toastMessage, TextToSpeech.QUEUE_FLUSH, null, null);

                // 선택된 언어 저장
                saveSelectedLanguage(selectedLanguage);

                // MainActivity로 돌아가기
                Intent intent = new Intent(SettingActivity3.this, StartActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            } else {
                Toast.makeText(SettingActivity3.this, "언어를 선택해 주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 저장된 언어 로드
    private void loadSavedLanguage() {
        String savedLanguage = sharedPreferences.getString("selected_language", "");
        if (savedLanguage.equals("영어")) {
            checkboxEnglish.setChecked(true);
            currentLanguageEditText.setText("English");
            currentLanguageEditText.setHint("Please select a language"); // 힌트 변경
            finishButton.setText("Finish");
            languageSelectionText.setText("Language Selection");
        } else if (savedLanguage.equals("중국어")) {
            checkboxChinese.setChecked(true);
            currentLanguageEditText.setText("简体中文");
            currentLanguageEditText.setHint("请选择语言"); // 힌트 변경
            finishButton.setText("完成");
            languageSelectionText.setText("语言选择");
        } else if (savedLanguage.equals("한국어")) {
            checkboxKorean.setChecked(true);
            currentLanguageEditText.setText("한국어");
            currentLanguageEditText.setHint("언어를 선택하세요"); // 힌트 변경
            finishButton.setText("완료");
            languageSelectionText.setText("언어 선택");
        } else {
            // 기본값 설정
            checkboxEnglish.setChecked(true);
            currentLanguageEditText.setText("English");
            currentLanguageEditText.setHint("Please select a language"); // 기본 힌트 설정
        }
    }

    // 선택된 언어 저장
    private void saveSelectedLanguage(String language) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_language", language);
        editor.apply();
    }

    // 테마 로드
    private void loadTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);
        if (darkModeEnabled) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
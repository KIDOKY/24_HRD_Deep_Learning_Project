// Corrosive.java
package my.example.and_project_prototype_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Corrosive extends AppCompatActivity {

    private TextView warningText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_corrosive);

        // UI 요소 초기화
        warningText = findViewById(R.id.warningText_corrosive);
        backButton = findViewById(R.id.back_button_corrosive);

        // SharedPreferences에서 언어 읽기
        String selectedLanguage = getSharedPreferences("settings", MODE_PRIVATE)
                .getString("selected_language", "한국어");

        // 언어에 따라 텍스트 설정
        setWarningText(selectedLanguage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setWarningText(String language) {
        switch (language) {
            case "영어":
                warningText.setText("Corrosive Substance Warning\n\n" +
                        "Protective Equipment: Wear appropriate PPE (e.g., gloves, goggles, dust mask) when handling.\n\n" +
                        "Ventilation: Ensure proper ventilation in the workspace when handling corrosive substances.\n\n" +
                        "Skin Contact: Rinse immediately with running water for at least 15 minutes and seek medical advice.\n\n" +
                        "Eye Contact: Flush with water for at least 15 minutes and seek immediate medical attention.\n\n" +
                        "Disposal: Follow local regulations for safe disposal in dedicated containers.");
                backButton.setText("Back");
                break;
            case "중국어":
                warningText.setText("腐蚀性物质注意事项\n\n" +
                        "保护设备：操作时应穿戴适当的个人防护装备 (PPE)（如手套、护目镜、防尘口罩）。\n\n" +
                        "通风：操作腐蚀性物质时，确保工作空间通风良好。\n\n" +
                        "皮肤接触：如接触皮肤，立即用流动水冲洗至少15分钟，并咨询医生。\n\n" +
                        "眼睛接触：如接触眼睛，立即用水冲洗至少15分钟，并立即就医。\n\n" +
                        "处置：按照当地规定将腐蚀性物质放入专用容器中安全处置。");
                backButton.setText("返回");
                break;
            case "한국어":
            default:
                warningText.setText("부식성 물질에 대한 주의사항\n\n" +
                        "보호 장비 착용: 작업 시 적절한 개인 보호 장비(PPE), 즉 보호 장갑, 고글, 방진 마스크 등을 착용해야 합니다.\n\n" +
                        "환기: 부식성 물질을 다룰 때는 작업 공간을 적절히 환기해야 합니다.\n\n" +
                        "피부 접촉: 피부에 닿은 경우 즉시 흐르는 물로 15분 이상 씻어내고, 의사와 상담해야 합니다.\n\n" +
                        "눈 접촉: 눈에 들어간 경우 즉시 물로 15분 이상 세척하고, 즉시 의사의 치료를 받아야 합니다.\n\n" +
                        "폐기방법: 부식성 물질을 안전하게 폐기하기 위해서는 현지 규정에 따라 전용 용기에 담아 분리 폐기해야 합니다.");
                backButton.setText("뒤로가기");
                break;
        }
    }

    public void onBackPressed(View view) {
        // 이전 화면으로 돌아가는 동작
        finish(); // 현재 Activity를 종료하고 이전 Activity로 돌아갑니다.
    }
}

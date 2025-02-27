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

public class Health_hazard extends AppCompatActivity {

    private TextView warningText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_health_hazard);

        // UI 요소 초기화
        warningText = findViewById(R.id.warningText_health);
        backButton = findViewById(R.id.back_button_health);

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
                warningText.setText("Health Hazard Warnings\n\n" +
                        "Acute Toxicity / Skin Corrosion or Irritation / Serious Eye Damage or Irritation / Skin Sensitization / Specific Target Organ Toxicity\n\n" +
                        "Acute Toxicity:\n" +
                        "Hazard: Exposure to acute toxic substances can cause immediate health problems, including vomiting, seizures, confusion, and in severe cases, death.\n\n" +
                        "Skin Corrosion or Irritation:\n" +
                        "Hazard: Contact may cause chemical burns or inflammation, potentially leading to peeling skin or permanent damage.\n\n" +
                        "Serious Eye Damage or Irritation:\n" +
                        "Hazard: Eye contact may lead to severe injury, burns, or even blindness.\n\n" +
                        "Skin Sensitization:\n" +
                        "Hazard: Repeated exposure may cause allergic reactions such as rashes and itching.\n\n" +
                        "Specific Target Organ Toxicity:\n" +
                        "Hazard: This may affect specific organs (e.g., liver, kidneys, nervous system), leading to chronic issues.");
                backButton.setText("Back");
                break;
            case "중국어":
                warningText.setText("健康危害警告\n\n" +
                        "急性毒性 / 皮肤腐蚀或刺激 / 严重眼损伤或刺激 / 皮肤致敏 / 特定目标器官毒性\n\n" +
                        "急性毒性:\n" +
                        "危险: 暴露于急性毒性物质可能引起呕吐、抽搐、混乱，严重时可致死。\n\n" +
                        "皮肤腐蚀或刺激:\n" +
                        "危险: 接触可能导致化学烧伤或炎症，严重时可能导致脱皮或永久损伤。\n\n" +
                        "严重眼损伤或刺激:\n" +
                        "危险: 接触眼睛可能导致严重伤害、烧伤甚至失明。\n\n" +
                        "皮肤致敏:\n" +
                        "危险: 重复接触可能引发过敏反应，如皮疹和瘙痒。\n\n" +
                        "特定目标器官毒性:\n" +
                        "危险: 可能影响特定器官（如肝、肾、神经系统），导致慢性问题。");
                backButton.setText("返回");
                break;
            case "한국어":
            default:
                warningText.setText("경고성 물질에 대한 주의사항\n\n" +
                        "급성독성 우려 / 피부부식성 또는 자극성 / 심한 눈손상 또는 자극성 / 피부과민성 / 특정표적장기독성\n\n" +
                        "급성독성 우려:\n" +
                        "위험: 급성 독성 물질에 노출되면 즉각적인 건강 문제를 일으킬 수 있습니다. 이는 구토, 경련, 혼란, 심한 경우 사망을 초래할 수 있습니다.\n\n" +
                        "피부부식성 또는 자극성:\n" +
                        "위험: 피부에 접촉할 경우 화학적 화상이나 염증을 일으킬 수 있습니다. 심한 경우 피부가 벗겨지거나 영구적인 손상을 초래할 수 있습니다.\n\n" +
                        "심한 눈손상 또는 자극성:\n" +
                        "위험: 눈에 접촉할 경우 심각한 손상이나 화상을 초래할 수 있으며, 실명에 이를 수 있습니다.\n\n" +
                        "피부과민성:\n" +
                        "위험: 해당 물질에 반복적으로 노출될 경우 알레르기 반응을 유발할 수 있습니다. 이는 피부 발진, 가려움증 등을 포함합니다.\n\n" +
                        "특정표적장기독성:\n" +
                        "위험: 특정 장기(예: 간, 신장, 신경계)에 유해한 영향을 미칠 수 있으며, 만성적인 문제를 일으킬 수 있습니다.");
                backButton.setText("뒤로가기");
                break;
        }
    }

    public void onBackPressed(View view) {
        // 이전 화면으로 돌아가는 동작
        finish(); // 현재 Activity를 종료하고 이전 Activity로 돌아갑니다.
    }
}

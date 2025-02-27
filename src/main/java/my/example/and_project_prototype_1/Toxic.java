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

public class Toxic extends AppCompatActivity {

    private TextView warningText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_toxic);

        // UI 요소 초기화
        warningText = findViewById(R.id.warningText_toxic);
        backButton = findViewById(R.id.back_button_toxic);

        // SharedPreferences에서 언어 읽기
        String selectedLanguage = getSharedPreferences("settings", MODE_PRIVATE)
                .getString("selected_language", "영어");

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
                warningText.setText("Toxic Substance Warning\n\n" +
                        "Storage: Toxic substances must be stored in sealed containers and kept in areas restricted from public or animal access.\n\n" +
                        "Avoid mixing with other chemicals and store in well-ventilated areas.\n\n" +
                        "Wear appropriate personal protective equipment (PPE) when handling (e.g., gloves, goggles, protective clothing).\n\n" +
                        "The workspace must be well-ventilated, and exhaust systems should be in place in situations where inhalation risk exists.\n\n" +
                        "Familiarize yourself with the safety data sheet (SDS) for the toxic substance and understand emergency procedures.\n\n" +
                        "In case of an accident: Evacuate immediately and administer first aid appropriately if toxic substances are spilled.\n\n" +
                        "In case of a leak, use appropriate absorbents immediately and report to professional personnel.");
                backButton.setText("Back"); // 뒤로가기 버튼 텍스트
                break;
            case "중국어":
                warningText.setText("毒物注意事项\n\n" +
                        "储存：毒物必须存放在密封的容器中，放置在限制公众或动物接触的区域。\n\n" +
                        "避免与其他化学物质混合，并存放在通风良好的地方。\n\n" +
                        "操作时必须穿戴适当的个人防护装备（PPE）（例如：手套、护目镜、防护服）。\n\n" +
                        "工作场所必须良好通风，存在吸入风险的情况应配备排气系统。\n\n" +
                        "熟悉毒物的安全数据表（SDS）并了解紧急处理程序。\n\n" +
                        "事故处理：一旦毒物泄漏或溢出，应立即撤离并进行适当的急救。\n\n" +
                        "泄漏时，应立即使用适当的吸附剂进行处理，并向专业人员报告。");
                backButton.setText("返回"); // 뒤로가기 버튼 텍스트
                break;
            case "한국어":
            default:
                warningText.setText("독성 물질에 대한 주의사항\n\n" +
                        "보관: 독성 물질은 밀폐된 용기에 안전하게 보관해야 하며, 일반인이나 동물의 접근이 제한된 곳에 두어야 한다.\n\n" +
                        "다른 화학 물질과의 혼합을 피하고, 통풍이 잘 되는 장소에 보관해야 한다.\n\n" +
                        "작업 시 적절한 개인 보호 장비(PPE)를 착용해야 한다. (예: 장갑, 고글, 방호복)\n\n" +
                        "작업장은 잘 환기되어야 하며, 흡입의 위험이 있는 상황에서는 배기 시스템을 갖추어야 한다.\n\n" +
                        "독성 물질에 대한 안전 데이터 시트(SDS)를 숙지하고 비상 시 대처 방안을 이해해야 한다.\n\n" +
                        "사고 대처: 독성 물질이 누출되거나 쏟아진 경우, 즉시 대피하고 적절한 방법으로 응급 처치를 수행해야 한다.\n\n" +
                        "누출 시 즉시 적절한 흡착제를 사용하여 처리하고, 전문 인력에 신고해야 한다.");
                backButton.setText("뒤로가기"); // 뒤로가기 버튼 텍스트
                break;
        }
    }

    public void onBackPressed(View view) {
        // 이전 화면으로 돌아가는 동작
        finish(); // 현재 Activity를 종료하고 이전 Activity로 돌아갑니다.
    }
}

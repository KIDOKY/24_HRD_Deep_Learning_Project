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

public class Flammable extends AppCompatActivity {

    private TextView warningText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flammable);

        // UI 요소 초기화
        warningText = findViewById(R.id.warningText_flammable);
        backButton = findViewById(R.id.back_button_flammable);

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
                warningText.setText("Flammable Substance Warning\n\n" +
                        "Handling: Prevent exposure to ignition sources such as static electricity and sparks. " +
                        "Powder mixtures in air may cause dust explosions.\n\n" +
                        "Storage: Store in a cool, dry place, separated from combustible materials. " +
                        "Ensure good ventilation and keep container temperature below 40°C.\n\n" +
                        "Work Safety: Avoid smoking and welding in handling areas, ground materials to avoid static.\n\n" +
                        "Fire Safety Measures:\n" +
                        "1. Separate work areas to minimize fire damage.\n" +
                        "2. Install fire hydrants far from possible heat sources.\n" +
                        "3. Quickly report fires, transfer contents to empty tanks if possible.\n" +
                        "4. Focus on external cooling to prevent steam-induced flame spread.\n" +
                        "5. Prevent firefighting water from entering drains to avoid explosions.");
                backButton.setText("Back");
                break;
            case "중국어":
                warningText.setText("可燃物质注意事项\n\n" +
                        "处理: 防止暴露于静电、火花等点火源。 粉末在空气中混合时可能导致粉尘爆炸。\n\n" +
                        "存储: 存放在阴凉、干燥的地方，远离可燃物。确保良好通风，容器温度保持在40°C以下。\n\n" +
                        "工作安全: 在处理区避免吸烟和焊接，通过接地消除静电。\n\n" +
                        "火灾安全措施:\n" +
                        "1. 分离工作区域以最小化火灾损害。\n" +
                        "2. 安装消防栓远离可能的热源。\n" +
                        "3. 快速报告火灾，如果可能，将内容物转移到空罐中。\n" +
                        "4. 重点外部冷却以防止蒸汽引发火焰蔓延。\n" +
                        "5. 防止消防水进入下水道以避免爆炸。");
                backButton.setText("返回");
                break;
            case "한국어":
            default:
                warningText.setText("Flammable에 대한 주의사항\n\n" +
                        "취급: 정전기, 불꽃 등 점화원에 노출되는 것을 방지해야 하며 분말이 공기에 혼합되면 분진폭발이 일어날 수 있으므로 주의해야 합니다.\n\n" +
                        "보관: 건조하고 서늘한 곳에서 가연성 물질 등과 분리해서 보관, 환기가 잘 되는 곳에 저장하며 용기 온도를 40℃ 이하로 유지해서 보관합니다.\n\n" +
                        "화기 작업: 인화성 액체와 그 증기를 제거하고 환기한 후, 허가를 얻어 작업합니다. 취급 장소에서는 흡연, 용접 등을 금지하며, 접지를 통해 정전기를 없애야 합니다.\n\n" +
                        "화재 초기 진압 방법:\n" +
                        "1. 피해를 최소화하기 위해 다른 작업 장소와 분리하여 방화 구획을 설정합니다.\n" +
                        "2. 소화전은 충분히 먼 곳에 설치하여 열로 인해 무용지물이 되지 않도록 합니다.\n" +
                        "3. 신속하게 신고 후 화재 발생 탱크의 내용물을 빈 탱크로 유도 저장하거나 배출합니다.\n" +
                        "4. 물이 수증기로 변하며 화염이 증가할 수 있으므로 외부 냉각에 주력합니다.\n" +
                        "5. 화재에 사용된 물이 하수구로 유입되면 화재나 폭발이 일어날 수 있으니 주의가 필요합니다.");
                backButton.setText("뒤로가기");
                break;
        }
    }

    public void onBackPressed(View view) {
        // 이전 화면으로 돌아가는 동작
        finish(); // 현재 Activity를 종료하고 이전 Activity로 돌아갑니다.
    }
}

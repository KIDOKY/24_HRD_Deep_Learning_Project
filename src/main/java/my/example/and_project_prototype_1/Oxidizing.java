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

public class Oxidizing extends AppCompatActivity {

    private TextView warningText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_oxidizing);

        // UI 요소 초기화
        warningText = findViewById(R.id.warningText_oxidizing);
        backButton = findViewById(R.id.back_button_oxidizing);

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
                warningText.setText("Precautions for Oxidizing Substances\n\n" +
                        "Oxidizing substances can promote combustion, posing a high risk of fire or explosion when mixed with fuel.\n\n" +
                        "1. Store oxidizing gases in a well-ventilated area.\n" +
                        "2. Be cautious of friction or collision between oxidizing gases and metals, especially with flammable substances.\n" +
                        "3. Use acid-resistant containers for oxidizing liquids, and wear protective equipment.\n" +
                        "4. Keep oxidizing solids in a cool, ventilated place, away from sunlight and ignition sources.");
                backButton.setText("Back");
                break;
            case "중국어":
                warningText.setText("氧化性物质的注意事项\n\n" +
                        "氧化性物质可以促进燃烧，与燃料混合时存在火灾或爆炸的高风险。\n\n" +
                        "1. 将氧化性气体存放在通风良好的地方。\n" +
                        "2. 注意氧化性气体与金属之间的摩擦或碰撞，尤其是与可燃物接触时。\n" +
                        "3. 使用耐酸容器储存氧化性液体，并穿戴防护装备。\n" +
                        "4. 将氧化性固体存放在凉爽、通风的地方，远离阳光和火源。");
                backButton.setText("返回");
                break;
            case "한국어":
            default:
                warningText.setText("산화성 물질에 대한 주의사항\n\n" +
                        "산화성 물질은 연소를 촉진할 수 있는 물질로, 연료와의 혼합 시 화재 또는 폭발의 위험이 큽니다.\n\n" +
                        "1. 산화성 가스는 통풍이 잘되는 곳에 보관하여야 합니다.\n" +
                        "2. 산화성 가스와 금속과의 마찰, 충돌 등으로 인한 발화를 주의해야 합니다.\n" +
                        "3. 산화성 액체는 내산성 용기를 사용하고 보호구를 착용해야 합니다.\n" +
                        "4. 산화성 고체는 통풍이 잘되는 차가운 곳에 보관하고 직사광선이나 점화원과 멀리 떨어져야 합니다.");
                backButton.setText("뒤로가기");
                break;
        }
    }

    public void onBackPressed(View view) {
        // 이전 화면으로 돌아가는 동작
        finish(); // 현재 Activity를 종료하고 이전 Activity로 돌아갑니다.
    }
}

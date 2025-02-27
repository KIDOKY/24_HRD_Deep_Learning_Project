// Explosive.java
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

public class Explosive extends AppCompatActivity {

    private TextView warningText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_explosive);

        // UI 요소 초기화
        warningText = findViewById(R.id.warningText);
        backButton = findViewById(R.id.back_button);

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
                warningText.setText("Explosive Substance Warning\n\n" +
                        "This substance is explosive under heat, shock, or friction.\n\n" +
                        "Storage: Store explosive substances in a cool, dry place, avoiding heat and direct sunlight.\n\n" +
                        "Avoid long-term exposure of organic nitrogen compounds to air.\n\n" +
                        "Nitro compounds must avoid fire, heat, shock, friction, and impact.\n\n" +
                        "Keep away from strong oxidizing agents and acids, as contact may ignite.\n\n" +
                        "Liquid explosives are highly flammable; avoid heating.\n\n" +
                        "Handle organic peroxides carefully to prevent exposure to heat, shock, friction, or sunlight.\n\n" +
                        "Store explosive substances in a well-ventilated, sun-blocked, dry place.");
                backButton.setText("Back");
                break;
            case "중국어":
                warningText.setText("爆炸性物质注意事项\n\n" +
                        "该物质在受热、冲击或摩擦下可能爆炸。\n\n" +
                        "储存：将爆炸性物质存放在阴凉干燥的地方，避免热源和阳光直射。\n\n" +
                        "避免有机氮化合物长期暴露在空气中。\n\n" +
                        "硝基化合物应避免火源、加热、冲击、摩擦和碰撞。\n\n" +
                        "请勿与强氧化剂和强酸接触，可能会引起燃烧。\n\n" +
                        "液体爆炸性物质容易燃烧，避免加热。\n\n" +
                        "请小心操作有机过氧化物，防止暴露于热、冲击、摩擦或阳光下。\n\n" +
                        "在通风良好、干燥并避免阳光直射的地方储存爆炸性物质。");
                backButton.setText("返回");
                break;
            case "한국어":
            default:
                warningText.setText("폭발성 물질에 대한 주의사항\n\n" +
                        "이 물질은 열, 충격, 마찰 등에 의해 폭발할 수 있는 성질이 있습니다.\n\n" +
                        "보관: 폭발성 물질은 서늘하고 건조한 장소에 보관하며, 열원 및 직사광선을 피한다.\n\n" +
                        "유기질소화물은 공기 중에서 장시간 보관을 금한다.\n\n" +
                        "니트로화합물은 화기, 가열, 충격, 마찰, 타격을 절대 금한다.\n\n" +
                        "폭발성 물질은 강산화제나 강산류와 접촉시 발화가 용이하므로 주의한다.\n\n" +
                        "액체상태 폭발성 물질은 인화되기 쉬우므로 가열을 금한다.\n\n" +
                        "고농도의 유기과산화물류는 가열, 충격, 마찰, 직사광선에 노출되지 않도록 주의한다.\n\n" +
                        "고농도의 유기과산화물은 강산화성 물질이므로 다른 물질과 접촉되지 않도록 주의한다.\n\n" +
                        "폭발성 물질은 직사광선이 차단되고 건조, 화기가 양호한 곳에 저장한다.");
                backButton.setText("뒤로가기");
                break;
        }
    }

    public void onBackPressed(View view) {
        // 이전 화면으로 돌아가는 동작
        finish(); // 현재 Activity를 종료하고 이전 Activity로 돌아갑니다.
    }
}

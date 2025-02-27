package my.example.and_project_prototype_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WarningTrafficActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        TextView warningText = findViewById(R.id.warningText);
        Button backButton = findViewById(R.id.backButton); // 버튼 인스턴스 가져오기

        Intent intent = getIntent();
        int recognizedSign = intent.getIntExtra("recognizedSign", -1);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);

        // 버튼 텍스트 설정
        setButtonText(backButton); // 버튼 텍스트 설정 호출

        // 인식된 신호가 유효한지 확인
        if (recognizedSign != -1) {
            setWarningMessage(warningText, recognizedSign);
        } else {
            // 알 수 없는 신호 처리
            warningText.setText("알 수 없는 표지판입니다.");
        }

        // 뒤로가기 버튼 클릭 리스너 설정
        backButton.setOnClickListener(v -> {
            Intent backintent = new Intent(WarningTrafficActivity.this, TrafficActivity.class);
            startActivity(backintent);
            finish(); // 현재 액티비티 종료
        });
    }

    private void setButtonText(Button backButton) {
        String selectedLanguage = sharedPreferences.getString("selected_language", "한국어"); // 기본값은 영어로 설정
        switch (selectedLanguage) {
            case "영어":
                backButton.setText("Back");
                break;
            case "중국어":
                backButton.setText("返回");
                break;
            case "한국어":
                backButton.setText("뒤로가기");
                break;
            default:
                backButton.setText("Back"); // 기본값으로 영어로 설정
        }
    }

    private void setWarningMessage(TextView warningText, int classIndex) {
        String warningMessage;
        String selectedLanguage = sharedPreferences.getString("selected_language", "영어"); // 기본값은 영어로 설정

        switch (selectedLanguage) {
            case "영어":
                warningMessage = getEnglishWarningMessage(classIndex);
                break;
            case "중국어":
                warningMessage = getChineseWarningMessage(classIndex);
                break;
            case "한국어":
                warningMessage = getKoreanWarningMessage(classIndex);
                break;
            default:
                warningMessage = "잘못된 인식 결과입니다.";
        }
        warningText.setText(warningMessage);
    }

    private String getEnglishWarningMessage(int classIndex) {
        switch (classIndex) {
            case 0: return "Caution: Bicycle!";
            case 1: return "Caution: Speed bump!";
            case 2: return "Caution: T-intersection!";
            case 3: return "Caution: Y-intersection!";
            case 4: return "Caution: Merging road!";
            case 5: return "Caution: Crosswalk!";
            case 6: return "Caution: Roundabout!";
            case 7: return "Caution: Crosswalk!";
            case 8: return "Caution: Crosswind!";
            case 9: return "Caution: Riverside road!";
            case 10: return "Caution: Road work!";
            case 11: return "Caution: Falling rocks!";
            case 12: return "Caution: Slippery road!";
            case 13: return "Caution: Uneven surface!";
            case 14: return "Caution: Stop!";
            case 15: return "Caution: Children at play!";
            case 16: return "Caution: 50 km/h speed limit!";
            case 17: return "Caution: No parking!";
            case 18: return "Caution: 30 km/h speed limit!";
            case 19: return "Caution: No motorcycles!";
            case 20: return "Caution: No bicycles!";
            case 21: return "Warning: Unknown sign!";
            default: return "Invalid recognition result.";
        }
    }

    private String getChineseWarningMessage(int classIndex) {
        switch (classIndex) {
            case 0: return "注意: 自行车！";
            case 1: return "注意: 减速带！";
            case 2: return "注意: T字形交叉路口！";
            case 3: return "注意: Y字形交叉路口！";
            case 4: return "注意: 合并道路！";
            case 5: return "注意: 人行横道！";
            case 6: return "注意: 环形交叉路口！";
            case 7: return "注意: 人行横道！";
            case 8: return "注意: 横风！";
            case 9: return "注意: 河边道路！";
            case 10: return "注意: 道路施工中！";
            case 11: return "注意: 落石！";
            case 12: return "注意: 滑溜的道路！";
            case 13: return "注意: 不平的路面！";
            case 14: return "注意: 停止！";
            case 15: return "注意: 儿童玩耍！";
            case 16: return "注意: 50 公里/小时速度限制！";
            case 17: return "注意: 禁止停车！";
            case 18: return "注意: 30 公里/小时速度限制！";
            case 19: return "注意: 禁止摩托车！";
            case 20: return "注意: 禁止自行车！";
            case 21: return "警告: 未知标志！";
            default: return "无效的识别结果。";
        }
    }

    private String getKoreanWarningMessage(int classIndex) {
        switch (classIndex) {
            case 0: return "자전거 주의!";
            case 1: return "과속 방지턱 주의!";
            case 2: return "ㅏ자형 교차로 주의!";
            case 3: return "ㅓ자형 교차로 주의!";
            case 4: return "합류 도로 주의!";
            case 5: return "횡단 보도 주의!";
            case 6: return "회전 교차로 주의!";
            case 7: return "횡단 보도 주의!";
            case 8: return "횡풍 주의!";
            case 9: return "강변 도로 주의!";
            case 10: return "도로 공사중 주의!";
            case 11: return "낙석 주의!";
            case 12: return "미끄러운 도로 주의!";
            case 13: return "노면 고르지 못함 주의!";
            case 14: return "정지 주의!";
            case 15: return "어린이 보호 주의!";
            case 16: return "50 최대 속도 제한!";
            case 17: return "주정차 금지!";
            case 18: return "30 최대 속도 제한!";
            case 19: return "오토바이 금지!";
            case 20: return "자전거 금지!";
            case 21: return "경고: 알 수 없는 표지판!";
            default: return "잘못된 인식 결과입니다.";
        }
    }
}

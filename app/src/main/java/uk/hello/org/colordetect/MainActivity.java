package uk.hello.org.colordetect;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button[] colorBtns = new Button[9];
    private boolean onVibrator = true;

    private static final int WRONG = 0xFFDF4949; // 뭔지모를 빨강
    private static final int RIGHT = 0xFF85B14B; // 그리너리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 핸드폰의 화면 사이즈 가져오기
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int screenWidth = size.x;
        int screenHeight = size.y;

        String LogStr = "Width : " + screenWidth + ", Height : " + screenHeight;
        Log.e("Screen Size", LogStr);
        // 핸드폰 화면 사이즈 가져오기

        // 사이즈에 따른 버튼 1개의 값 (버튼은 총 3개)
        int refSize = Math.min(screenWidth, screenHeight);
        refSize -= 20; refSize /= 3;
        // 사이즈에 따른 버튼 1개의 값

        setBtnSize(refSize);
    }

    // 저장용 + 버튼 크기 재설정
    public void setBtnSize(int refSize){
        for(int i = 0; i < 9; i++) {
            int btnIDs = getResources().getIdentifier("btn" + (i + 1), "id", "uk.hello.org.colordetect");
            this.colorBtns[i] = (Button) findViewById(btnIDs);
            this.colorBtns[i].setWidth(refSize);
            this.colorBtns[i].setHeight(refSize);
        }
    }
    // 저장용 + 버튼 크기 재설정

    public void initColor(){}

    // 잘못 눌렀을 경우
    public void wrongClick(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(WRONG));
                int second = 0;
                while(second <= 500){
                    try{
                        if(onVibrator){
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            assert vibrator != null;
                            vibrator.vibrate(1);
                            Thread.sleep(1);
                            second++;
                        }
                    }
                    catch(InterruptedException e) {
                        String errorMessage = e.getMessage();
                        Log.e("InterruptedException", errorMessage);
                    }
                    catch(NullPointerException e) {
                        String errorMessage = e.getMessage();
                        Log.e("NullPointerException", errorMessage);
                    }
                }

                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(RIGHT));
            }
        }).start();
    }
    // 잘못 눌렀을 경우

    // 버튼 이벤트
    public void onClickButton(View v){
        int clickBtnID = v.getId();
        int refButton = this.colorBtns[0].getId();

        if(clickBtnID - refButton != 0) wrongClick();
        else {
            initColor();
            Toast.makeText(getApplicationContext(), "맞았습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    // 버튼 이벤트
}

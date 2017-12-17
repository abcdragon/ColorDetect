package uk.hello.org.colordetect;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private Button[] colorBtns = new Button[9];
    private boolean onVibrator = true;

    private static final int WRONG = 0xFFDF4949; // 뭔지모를 빨강
    private static final int RIGHT = 0xFF85B14B; // 그리너리
    private static ActionBar mActionBar;

    // static에서 get~~을 사용할 수 없기 때문에 만든 코드
    public static ActionBar getmActionBar() {
        return mActionBar;
    }
    // getmActionBar 끝

    // 핸들러 사용시 메모리 누수를 방지하는 코드 (약한 참조를 사용)
    private static class WrongSignHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        private WrongSignHandler(MainActivity activity){
            this.mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            MainActivity activity = this.mActivity.get();
            if(activity != null){
                if(msg.what == 0){
                    getmActionBar().setBackgroundDrawable(new ColorDrawable(WRONG));
                    this.sendEmptyMessageDelayed(1, 500);
                }
                else getmActionBar().setBackgroundDrawable(new ColorDrawable(RIGHT));
            }
        }
    }
    // 핸들러 사용시 메모리 누수를 방지하는 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar(); // getmActionBar 사용을 위한 코드

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
    // 버튼 저장용 + 버튼 크기 재설정

    public void initColor(){}

    // 잘못 눌렀을 경우
    public void wrongClick(){
        if(onVibrator) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            assert vibrator != null;
            vibrator.vibrate(500);
        }

        WrongSignHandler handler = new WrongSignHandler(this);
        handler.sendEmptyMessage(0);
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

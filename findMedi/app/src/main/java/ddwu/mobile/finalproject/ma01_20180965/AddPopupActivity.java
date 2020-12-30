package ddwu.mobile.finalproject.ma01_20180965;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class AddPopupActivity extends Activity {
    private EditText memo;
    private String photoUrl;
    private static final int PHOTO_ACTIVITY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_medi);

        //UI 객체생성
        memo = findViewById(R.id.memo);
        photoUrl = null;
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        intent.putExtra("memo", memo.getText().toString());
        intent.putExtra("photoUrl", photoUrl);
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo:
                Intent intent = new Intent(this, PhotoActivity.class);
                startActivityForResult(intent, PHOTO_ACTIVITY);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case PHOTO_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    photoUrl = intent.getStringExtra("photoUrl");
                }
                break;
        }
    }
}

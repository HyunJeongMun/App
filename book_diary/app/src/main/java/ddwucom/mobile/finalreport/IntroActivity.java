package ddwucom.mobile.finalreport;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        button = findViewById(R.id.back);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.back :
                finish();
                break;
        }
    }
}

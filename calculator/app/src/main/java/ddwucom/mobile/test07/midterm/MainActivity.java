package ddwucom.mobile.test07.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
    * 과제명 : 중간고사과제
    * 분반 : 01
    * 학번 : 20180965, 이름 : 문현정
    * 제출일 : 2020년 05월 14일
    * 추가 구현 : 화면디자인개선, 오류처리, 연속계산기능
     */

    EditText editText;
    String str;
    public double num;
    public int type;
    public Button plus;
    public Button subtract;
    public Button multiply;
    public Button division;
    public Button clear;
    public Button equal;
    public Button power;
    public Button root;
    public Button sin;
    public LinearLayout add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        str = "";
        type = 0;
        plus = findViewById(R.id.plus);
        subtract = findViewById(R.id.subtract);
        multiply = findViewById(R.id.multiply);
        division = findViewById(R.id.division);
        clear = findViewById(R.id.clear);
        equal = findViewById(R.id.equal);
        add = findViewById(R.id.layout);
        power = findViewById(R.id.power);
        root = findViewById(R.id.root);
        sin = findViewById(R.id.sin);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연산 오류시 toast 띄우기
                if (str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                //연속계산을 가능하게 한다.
                if (type == 0)
                    num = Double.parseDouble(editText.getText().toString());
                else {
                    num = cal(num, type);
                    editText.setText(Double.toString(num));
                }

                type = 1;
                str = "";
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연산 오류시 toast 띄우기
                if (str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                //연속계산을 가능하게 한다.
                if (type == 0)
                    num = Double.parseDouble(editText.getText().toString());
                else {
                    num = cal(num, type);
                    editText.setText(Double.toString(num));
                }

                type = 2;
                str = "";
            }
        });

        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연산 오류시 toast 띄우기
                if (str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                //연속계산을 가능하게 한다.
                if (type == 0)
                    num = Double.parseDouble(editText.getText().toString());
                else {
                    num = cal(num, type);
                    editText.setText(Double.toString(num));
                }

                type = 3;
                str = "";
            }
        });

        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연산 오류시 toast 띄우기
                if (str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                //연속계산을 가능하게 한다.
                if (type == 0)
                    num = Double.parseDouble(editText.getText().toString());
                else {
                    num = cal(num, type);
                    editText.setText(Double.toString(num));
                }

                type = 4;
                str = "";
            }
        });

        power.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //연산 오류시 toast 띄우기
               if(str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                //연속계산을 가능하게 한다.
                editText.setHint("POWER");
                if(type == 0)
                    num = Double.parseDouble(str);
                else
                    num = cal(num, type);

                type = 5;
                str = "";
                editText.setText(str);
            }
        });

        root.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //연산 오류시 toast 띄우기
                if(str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                editText.setHint("S_ROOT");
                if(type == 0)
                    num = Double.parseDouble(str);
                else
                    num = cal(num, type);

                type = 6;
                str = "";
                editText.setText(str);
            }
        });

        sin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //연산 오류시 toast 띄우기
                if(str == "") {
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                    return;
                }

                editText.setHint("SIN(X)");
                if(type == 0)
                    num = Double.parseDouble(str);
                else
                    num = cal(num, type);

                type = 7;
                str = "";
                editText.setText(str);
            }
        });

        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editText.setHint("");
                str = "";
                num = 0;
                type = 0;
                editText.setText(str);
            }
        });

        View.OnClickListener res = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //연산 오류시 toast 띄우기
                if(!(type == 7 || type == 6)) {
                    if(str == "" || type == 0) {
                        Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(type == 6 || type == 7)
                    str = Double.toString(calScientific(num, type));
                else
                    str = Double.toString(cal(num, type));

                num = 0;
                type = 0;
                editText.setText(str);
            }
        };

        equal.setOnClickListener(res);
}

    public Double cal(double num1, int type){
        Double num2 = Double.parseDouble(editText.getText().toString());
        Double result = 0.0;

        switch (type){
            case 1:
                result = num1 + num2;
                break;
            case 2:
                result = num1 - num2;
                break;
            case 3:
                result = num1 * num2;
                break;
            case 4:
                //오류처리
               if(num2 != 0)
                    result = num1 / num2;
                else
                    Toast.makeText(getApplicationContext(), "연산 오류 발생", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                result =  Math.pow(num, num2);
                break;
        }

        return result;
    }

    public double calScientific(double num, int type){
        double result = 0.0;
        switch(type){
            case 6:
                result = Math.sqrt(num);
                break;
            case 7:
                num = Math.toRadians(num);
                result = Math.sin(num);
                break;
        }

        return result;
    }

    public void onClick(View v){

        switch(v.getId()){
            case R.id.zero:
                str += "0";
                break;
            case R.id.one:
                str += "1";
                break;
            case R.id.two:
                str += "2";
                break;
            case R.id.three:
                str += "3";
                break;
            case R.id.four:
                str += "4";
                break;
            case R.id.five:
                str += "5";
                break;
            case R.id.six:
                str += "6";
                break;
            case R.id.seven:
                str += "7";
                break;
            case R.id.eight:
                str += "8";
                break;
            case R.id.nine:
                str += "9";
                break;
        }

        editText.setText(str);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.general:
                add.setVisibility(View.GONE);
                break;
            case R.id.engineering:
                add.setVisibility(View.VISIBLE);
                break;
        }
        item.setChecked(true);
        return true;
    }
}

package ddwucom.mobile.finalreport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etAuthor;
    EditText etPrice;
    EditText etDate;
    EditText etPublisher;
    ImageButton dateBtn;
    String dateString;

    BookDBManager bookDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etTitle = findViewById(R.id.addTitle);
        etAuthor = findViewById(R.id.addAuthor);
        etPrice = findViewById(R.id.addPrice);
        etDate = findViewById(R.id.addDate);
        etPublisher = findViewById(R.id.addPub);
        dateBtn = findViewById(R.id.dateBtn);

        bookDBManager = new BookDBManager(this);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateString = year + "/" + (month + 1) + "/" + dayOfMonth;
                        etDate.setText(dateString);
                    }
                }, 2019, 02, 21);

                datePickerDialog.setMessage("날짜 선택");
                datePickerDialog.show();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                int flagAdd = 1;
                int flag;
                try {
                    Integer.parseInt(etPrice.getText().toString());
                    flag = 1;
                } catch (NumberFormatException e) {
                    flag = 0;
                }

                if(etTitle.getText().toString().length() == 0) {
                    Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_LONG).show();
                    flagAdd = 0;
                }

                if(etAuthor.getText().toString().length() == 0){
                    Toast.makeText(this, "작가를 입력하세요", Toast.LENGTH_LONG).show();
                    flagAdd = 0;
                }

                if(etPrice.getText().toString().length() == 0){
                    Toast.makeText(this, "가격을 입력하세요", Toast.LENGTH_LONG).show();
                    flagAdd = 0;
                }
                else if(flag == 0)
                    Toast.makeText(this, "가격을 잘못 입력하셨습니다.\n다시 입력해 주세요", Toast.LENGTH_LONG).show();

                if(flag != 0 && flagAdd != 0) {
                    BookDTO book = new BookDTO(etTitle.getText().toString(), etAuthor.getText().toString(),
                            Integer.parseInt(etPrice.getText().toString()), dateString, etPublisher.getText().toString(), R.mipmap.book_simple);

                    boolean result = bookDBManager.addNewBook(book);
                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("title", etTitle.getText().toString());
                        setResult(RESULT_OK, resultIntent);
                    }

                    finish();
                }
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }


}

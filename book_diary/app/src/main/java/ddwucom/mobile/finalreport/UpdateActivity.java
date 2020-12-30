package ddwucom.mobile.finalreport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etAuthor;
    EditText etPrice;
    EditText etDate;
    EditText etPublisher;
    ImageButton dateBtn;
    ImageView imageView;
    String dateString;
    BookDTO book;

    BookDBManager bookDBManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        book = (BookDTO)getIntent().getSerializableExtra("book");

        etTitle = findViewById(R.id.upTitle);
        etAuthor = findViewById(R.id.upAuthor);
        etPrice = findViewById(R.id.upPrice);
        etDate = findViewById(R.id.upDate);
        etPublisher = findViewById(R.id.upPub);
        dateBtn = findViewById(R.id.dateBtn);
        imageView = findViewById(R.id.image);

        etTitle.setText(book.getTitle());
        etAuthor.setText(book.getAuthor());
        etPrice.setText(String.valueOf(book.getPrice()));
        etDate.setText(book.getPublishDate());
        etPublisher.setText(book.getPublisher());
        imageView.setImageResource(book.getCover());

        bookDBManager = new BookDBManager(this);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
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
            case R.id.update:
                int flagUP = 1;
                int flag;
                try {
                    Integer.parseInt(etPrice.getText().toString());
                    flag = 1;
                } catch (NumberFormatException e) {
                    flag = 0;
                }

                if(etTitle.getText().toString().length() == 0) {
                    Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_LONG).show();
                    flagUP = 0;
                }

                if(etAuthor.getText().toString().length() == 0){
                    Toast.makeText(this, "작가를 입력하세요", Toast.LENGTH_LONG).show();
                    flagUP = 0;
                }

                if(etPrice.getText().toString().length() == 0){
                    Toast.makeText(this, "가격을 입력하세요", Toast.LENGTH_LONG).show();
                    flagUP = 0;
                }
                else if(flag == 0)
                    Toast.makeText(this, "가격을 잘못 입력하셨습니다.\n다시 입력해 주세요", Toast.LENGTH_LONG).show();

                if(flag != 0 && flagUP != 0) {
                    book.setTitle(etTitle.getText().toString());
                    book.setAuthor(etAuthor.getText().toString());
                    book.setPrice(Integer.parseInt(etPrice.getText().toString()));
                    book.setPublishDate(etDate.getText().toString());
                    book.setPublisher(etPublisher.getText().toString());

                    boolean result = bookDBManager.modifyBook(book);
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

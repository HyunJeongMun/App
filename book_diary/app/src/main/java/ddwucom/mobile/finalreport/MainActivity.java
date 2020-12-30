package ddwucom.mobile.finalreport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 과제명 : 도서 정보 관리 앱
    // 분반 : 01분반
    // 제출일 2020년 07월 01일
    // 추가 기능 : 검색 기능 / 위젯 사용 - 날씨입력 위젯, searchView , imageButton 사용 / 기본 기능 이외의 추가 기능 - 리스트뷰 항목에 이미지 사용

    private ArrayList<BookDTO> bookList = null;
    private BookAdapter bookAdapter;
    private ListView listView;
    private BookDBManager bookDBManager;
    final static int CODE_ADD = 100;
    final static int CODE_UP = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookList = new ArrayList<BookDTO>();
        bookDBManager = new BookDBManager(this);
        bookAdapter = new BookAdapter(this, R.layout.custom_adapter_view, bookList);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDTO book = bookList.get(position);
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("book", book);
                startActivityForResult(intent, CODE_UP);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("책 정보 삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean result = bookDBManager.removeBook(bookList.get(pos).get_id());
                                if (result) {
                                    Toast.makeText(MainActivity.this, "성공", Toast.LENGTH_LONG);
                                    bookList.clear();
                                    bookList.addAll(bookDBManager.getAllBook());
                                    bookAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_LONG);
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(true)
                        .show();
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addBook:
                Intent intentAdd = new Intent(this, AddActivity.class);
                startActivityForResult(intentAdd, CODE_ADD);
                break;
            case R.id.search :
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                break;
            case R.id.intro:
                Intent intentIntro = new Intent(this, IntroActivity.class);
                startActivity(intentIntro);
                break;
            case R.id.finish:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("앱 종료")
                        .setMessage("종료하시겠습니까?")
                        .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(true)
                        .show();
                break;
        }

        return true;
    }

   protected void onResume(){
        super.onResume();
        bookList.clear();
        bookList.addAll(bookDBManager.getAllBook());
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_ADD) {
            switch (resultCode) {
                case RESULT_OK:
                    String title = data.getStringExtra("title");
                    Toast.makeText(this, title + " 책 정보 추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "책 정보 추가 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (requestCode == CODE_UP) {    // UpdateActivity 호출 후 결과 확인
            switch (resultCode) {
                case RESULT_OK:
                    Toast.makeText(this, "책 정보 수정 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "책 정보 수정 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
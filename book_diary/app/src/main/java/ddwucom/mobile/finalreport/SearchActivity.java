package ddwucom.mobile.finalreport;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<BookDTO> bookList;
    BookAdapter bookAdapter;
    ListView listView;
    TextView kind;
    Button back;
    BookDBManager bookDBManager;
    SearchView searchView;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search);
        back = findViewById(R.id.back);
        kind = findViewById(R.id.kind);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                bookList = null;
                bookDBManager = new BookDBManager(SearchActivity.this);
                int flagSearch = 1;

                if (flag == 0) {
                   try{
                        bookList = bookDBManager.getBookById(Long.parseLong(query));
                    } catch (NumberFormatException e){
                        Toast.makeText(SearchActivity.this, "id를 잘못 입력하셨습니다.", android.widget.Toast.LENGTH_LONG).show();
                        flagSearch = 0;
                    }
                } else if (flag == 1) {
                    bookList = bookDBManager.getBookByTitle(query);
                } else if (flag == 2) {
                    bookList = bookDBManager.getBookByAuthor(query);
                } else {
                    bookList = bookDBManager.getBookByPublisher(query);
                }

                if(flagSearch != 0) {
                    bookAdapter = new BookAdapter(SearchActivity.this, R.layout.custom_adapter_view, bookList);
                    listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(bookAdapter);

                    if(bookList.size() == 0) {
                        Toast.makeText(SearchActivity.this, "검색 결과가 없습니다.", android.widget.Toast.LENGTH_LONG).show();
                    }
                }

                searchView.setQuery("", false);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id:
                flag = 0;
                kind.setText("Kind : Id");
                searchView.setQueryHint("Id를 입력하세요");
                break;
            case R.id.title:
                flag = 1;
                kind.setText("Kind : Title");
                searchView.setQueryHint("제목을 입력하세요");
                break;
            case R.id.author:
                flag = 2;
                kind.setText("Kind : Author");
                searchView.setQueryHint("작가를 입력하세요");
                break;
            case R.id.publisher:
                flag = 3;
                kind.setText("Kind : Publisher");
                searchView.setQueryHint("출판사를 입력하세요");
                break;
        }

        item.setChecked(true);

        return true;
    }
}
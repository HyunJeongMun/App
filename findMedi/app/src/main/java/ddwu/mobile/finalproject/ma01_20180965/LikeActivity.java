package ddwu.mobile.finalproject.ma01_20180965;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class LikeActivity extends AppCompatActivity {
    LikeAdapter adapter;
    Intent intent;

    ListView lvList;

    Cursor cursor;
    MediDBHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi);
        lvList = findViewById(R.id.medi_list);
        TextView textView = findViewById(R.id.title);

        textView.setText("즐겨찾기");
        helper = new MediDBHelper(this);

        adapter = new LikeAdapter(this, R.layout.like_item, null);

        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] time = {cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_START)), cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_END))};
                MediDTO data = new MediDTO(cursor.getInt(cursor.getColumnIndex(MediDBHelper.COL_ID)), cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_TYPE)),
                                            cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_ADDR)), cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_DIVNAME)),
                        cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_NAME)), cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_TEL)), time, cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_MEMO)),
                        Double.valueOf(cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_LAT))), Double.valueOf(cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_LNG))), cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_PHOTO)));

                intent = new Intent(LikeActivity.this, LikeDetail.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        lvList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(LikeActivity.this);

                builder.setTitle("정보 삭제")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int _id = cursor.getInt(cursor.getColumnIndex(MediDBHelper.COL_ID));
                                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                                String whereClause = helper.COL_ID + "=?";
                                String[] whereArgs = new String[]{String.valueOf(_id)};
                                int result = sqLiteDatabase.delete(helper.TABLE_NAME, whereClause, whereArgs);
                                helper.close();
                                if (result > 0) {
                                    String path =  cursor.getString(cursor.getColumnIndex(MediDBHelper.COL_PHOTO));
                                    if(path != null && fileDelete(path))
                                        Toast.makeText(LikeActivity.this, "삭제되었습니다.", Toast.LENGTH_LONG).show();
                                    onResume();
                                } else {
                                    Toast.makeText(LikeActivity.this, "실패하였습니다.", Toast.LENGTH_LONG).show();
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

    public boolean fileDelete(String filePath){
        File file = new File(filePath);

        if(file.exists()){
            file.delete();
            return true;
        }

        return false;
    }

    protected void onResume() {
        super.onResume();

        // DB에서 데이터를 읽어와 Adapter에 설정
        SQLiteDatabase db = helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + MediDBHelper.TABLE_NAME, null);
        adapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // cursor 사용 종료
        if (cursor != null) cursor.close();
    }

}

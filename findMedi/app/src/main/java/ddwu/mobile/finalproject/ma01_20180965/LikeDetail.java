package ddwu.mobile.finalproject.ma01_20180965;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LikeDetail extends AppCompatActivity {
    private final static int UPDATE_ACTIVIT = 900;
    private final static int REQUEST_SHARE = 1000;

    private TextView tvName;
    private TextView tvAddress;
    private TextView tvDivName;
    private TextView tvTel;
    private TextView tvTime;
    private TextView tvMemo;
    private ImageView ivImg;
    private MediDBHelper helper;
    private MediDTO data;
    String path;
    String captureImagePath = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_like);
        Intent intent = getIntent();

        data = (MediDTO) intent.getSerializableExtra("data");

        helper = new MediDBHelper(this);

        tvName = findViewById(R.id.lName);
        tvAddress = findViewById(R.id.lAddr);
        tvDivName = findViewById(R.id.lDivName);
        tvTel = findViewById(R.id.lTel);
        tvTime = findViewById(R.id.lTime);
        tvMemo = findViewById(R.id.lMemo);
        ivImg = findViewById(R.id.limage);

        String time = "진료시간 " + data.getTime()[0] + " - " + data.getTime()[1];
        String tel = "전화번호 " + data.getTel();

        tvName.setText(data.getName());
        tvAddress.setText(data.getAddress());
        tvDivName.setText(data.getDivName());
        tvTel.setText(tel);
        tvTime.setText(time);
        tvMemo.setText(data.getMemo());
        ivImg = findViewById(R.id.limage);

        path = data.getPhotoUrl();
        if (!TextUtils.isEmpty(path)) {
            Bitmap bitmap = BitmapFactory.decodeFile(data.getPhotoUrl());
            ivImg.setImageBitmap(bitmap);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.update:
                startActivityForResult(new Intent(this, AddPopupActivity.class), UPDATE_ACTIVIT);
                break;
            case R.id.sharePage:
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                View container;
                container = getWindow().getDecorView();
                container.buildDrawingCache();
                Bitmap bitmap = container.getDrawingCache();
                File file;

                try {
                    file = createImageFile(bitmap);
                    captureImagePath = file.getAbsolutePath();
                    Uri uri = FileProvider.getUriForFile(this, "ddwu.mobile.finalproject.ma01_20180965.fileprovider", file);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(shareIntent, "공유하기"), REQUEST_SHARE);

                } catch (Throwable e) {
                    e.printStackTrace();
                }

                break;
        }

        return false;
    }

    private File createImageFile(Bitmap bitmap) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        FileOutputStream out = new FileOutputStream(image);  // 파일을 쓸 수 있는 스트림을 준비하기
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
        path = image.getAbsolutePath();

        out.close();    // 스트림 닫아주기

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case UPDATE_ACTIVIT:
                if (resultCode == RESULT_OK) {
                    String memo = intent.getExtras().getString("memo");
                    if (!TextUtils.isEmpty(memo))
                        data.setMemo(memo);
                    String photoUrl = intent.getStringExtra("photoUrl");
                    if (!TextUtils.isEmpty(photoUrl))
                        data.setPhotoUrl(photoUrl);
                    updateLike();
                }
            case REQUEST_SHARE:
                fileDelete(captureImagePath);
                break;
        }

    }

    public void updateLike() {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(helper.COL_MEMO, data.getMemo());
        value.put(helper.COL_PHOTO, data.getPhotoUrl());

        String whereClause = helper.COL_ID + "=?";
        String[] whereArgs = new String[]{String.valueOf(data.get_id())};


        int result = sqLiteDatabase.update(MediDBHelper.TABLE_NAME, value, whereClause, whereArgs);

        helper.close();

        if (result > 0) {
            Toast.makeText(LikeDetail.this, "수정하였습니다.", Toast.LENGTH_SHORT).show();
            fileDelete(path);
            finish();
        } else
            Toast.makeText(LikeDetail.this, "실패하였습니다.", Toast.LENGTH_LONG).show();

    }

    public boolean fileDelete(String path) {
        if(path == null)
            return false;

        File file = new File(path);

        if (file.exists()) {
            file.delete();
            return true;
        }

        return false;
    }

}

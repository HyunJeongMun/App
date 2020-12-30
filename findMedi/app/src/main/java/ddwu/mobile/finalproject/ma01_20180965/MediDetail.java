package ddwu.mobile.finalproject.ma01_20180965;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MediDetail extends AppCompatActivity implements OnMapReadyCallback {

    private final static int ADD_ACTIVIT = 400;
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvDivName;
    private TextView tvTel;
    private TextView tvTime;
    private MediDTO data;
    private String type;

    private GoogleMap mGoogleMap;

    private MediDBHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_medi);
        Intent intent = getIntent();

        data = (MediDTO) intent.getSerializableExtra("data");
        type = intent.getStringExtra("type");

        tvName = (TextView) findViewById(R.id.mName);
        tvAddress = (TextView) findViewById(R.id.mAddr);
        tvDivName = (TextView) findViewById(R.id.mDivName);
        tvTel = (TextView) findViewById(R.id.mTel);
        tvTime = (TextView) findViewById(R.id.mTime);

        String time =  "진료시간 " + data.getTime()[0] + " - " +  data.getTime()[1];
        String tel = "전화번호 " + data.getTel();

        tvName.setText(data.getName());
        tvAddress.setText(data.getAddress());
        tvDivName.setText(data.getDivName());
        tvTel.setText(tel);
        tvTime.setText(time);

        helper = new MediDBHelper(this);
        mapLoad();
    }

    private void mapLoad() {
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map_medi);
        mapFragment.getMapAsync(this);      // 매배변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }


    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng latLng = new LatLng(data.getLat(), data.getLng());
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("선택 위치");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mGoogleMap.addMarker(options);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.like_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.like :
                if(is_in() == 0) {
                    startActivityForResult(new Intent(this, AddPopupActivity.class), ADD_ACTIVIT);
                }
                else {
                    Toast.makeText(MediDetail.this, "이미 즐겨찾기에 추가된 정보입니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return false;
    }

    public void insertLike() {
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(helper.COL_TYPE, type);
        value.put(helper.COL_ADDR, data.getAddress());
        value.put(helper.COL_NAME, data.getName());
        value.put(helper.COL_DIVNAME, data.getDivName());
        value.put(helper.COL_TEL, data.getTel());
        value.put(helper.COL_START, data.getTime()[0]);
        value.put(helper.COL_END, data.getTime()[1]);
        value.put(helper.COL_LAT, String.valueOf(data.getLat()));
        value.put(helper.COL_LNG, String.valueOf(data.getLng()));
        value.put(helper.COL_MEMO, data.getMemo());
        value.put(helper.COL_PHOTO, data.getPhotoUrl());

        long result = sqLiteDatabase.insert(helper.TABLE_NAME, null, value);

        if(result > 0) {
            Toast.makeText(MediDetail.this, "추가하였습니다.", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(MediDetail.this, "실패하였습니다.", Toast.LENGTH_LONG).show();

        helper.close();
    }

    public int is_in() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql ="select * from " + helper.TABLE_NAME + " where addr = '"+ data.getAddress() + "'";
        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.moveToNext()) {
            if(cursor.getString(0) == null)
                return 0;
            else
               return 1;
        }

        return 0;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case ADD_ACTIVIT :
                if (resultCode == RESULT_OK) {
                    String memo = intent.getExtras().getString("memo");
                    data.setMemo(memo);
                    String photoUrl = intent.getStringExtra("photoUrl");
                    data.setPhotoUrl(photoUrl);
                    insertLike();
                }
                break;
        }
    }

}

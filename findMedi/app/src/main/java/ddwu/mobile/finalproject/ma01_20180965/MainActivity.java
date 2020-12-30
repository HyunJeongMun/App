package ddwu.mobile.finalproject.ma01_20180965;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    // 위치지정 - 디폴트 현재 위치
    // 병원과 약국, 즐겨찾기 목록

    private static final int SEARCH_ADDRESS_ACTIVITY = 200;
    final static int PERMISSION_REQ_CODE = 100;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private Button mediBtn;
    private Button favoritesBtn;
    private Button searchBtn;
    private EditText address;
    private AddressDTO selectAddr;
    private Marker centerMarker;
    private Geocoder geocoder;
    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediBtn = findViewById(R.id.medi);
        favoritesBtn = findViewById(R.id.favorites);
        searchBtn = findViewById(R.id.search);
        address = findViewById(R.id.address);
        selectAddr = new AddressDTO();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Locale locale = new Locale("ko", "KR");
        geocoder = new Geocoder(getApplicationContext(), locale);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        if (checkPermission()) {
            requestMyLocation();
        }

    }

    //나의 위치 요청
    public void requestMyLocation() {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);
        }

        return;
    }

    private void mapLoad() {
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // 매배변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (checkPermission()) {
                locationManager.removeUpdates(locationListener);
                //위도 경도
                selectAddr.setLat(location.getLatitude());
                selectAddr.setLng(location.getLongitude());
                new AsynGeocoder().execute();
                mapLoad();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("gps", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //구글맵 생성 콜백
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        //화면중앙의 위치와 카메라 줌비율

        if(centerMarker != null)
          centerMarker.remove();
        LatLng latLng = new LatLng(selectAddr.getLat(), selectAddr.getLng());
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("현재 위치");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        centerMarker = mGoogleMap.addMarker(options);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                it = new Intent(MainActivity.this, SearchActivity.class);
                it.putExtra("currentAddr", selectAddr);
                startActivityForResult(it, SEARCH_ADDRESS_ACTIVITY);
                break;
            case R.id.medi:
                it = new Intent(MainActivity.this, MediActivity.class);
                it.putExtra("address", selectAddr);
                startActivity(it);
                break;
            case R.id.favorites:
                it = new Intent(MainActivity.this, LikeActivity.class);
                startActivity(it);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    selectAddr = (AddressDTO) intent.getExtras().getSerializable("selectAddr");
                    address.setText(selectAddr.getAddress());
                    mapLoad();
                }
                break;
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQ_CODE);
                return false;
            } else
                return true;
        }
        return false;
    }


    /*권한승인 요청에 대한 사용자의 응답 결과에 따른 수행*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*권한을 승인받았을 때 수행하여야 하는 동작 지정*/
                requestMyLocation();
            } else {
                /*사용자에게 권한 제약에 따른 안내*/
                Toast.makeText(this, "Permissions are not granted.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public class AsynGeocoder extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDlg;
        Geocoder geocoder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MainActivity.this, "Wait", "Geocoding...");
            Locale locale = new Locale("ko", "KR");
            geocoder = new Geocoder(MainActivity.this, locale);
        }

        @Override
        protected String doInBackground(Void... voids) {
            List<Address> addresses = null;
            String result = null;
            try {
                addresses = geocoder.getFromLocation(selectAddr.getLat(), selectAddr.getLng(), 1);
                if (addresses == null || addresses.size() == 0)
                    return null;

                result = addresses.get(0).getAddressLine(0);
                selectAddr.setAddress(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            address.setText(result);
            progressDlg.dismiss();
        }

    }

}

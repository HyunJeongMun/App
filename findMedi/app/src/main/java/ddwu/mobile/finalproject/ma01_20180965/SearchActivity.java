package ddwu.mobile.finalproject.ma01_20180965;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;
    private AddressDTO current;
    private Geocoder geocoder;
    private TextView searchAddr;
    private Button select;
    private ImageButton searchPlace;
    private static final int SEARCH_ACTIVITY = 300;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();

        current = (AddressDTO) intent.getExtras().getSerializable("currentAddr");
        searchAddr = findViewById(R.id.searchAddr);
        select = findViewById(R.id.select);
        searchPlace = findViewById(R.id.searchPlace);

        searchAddr.setText(current.getAddress());
        Locale locale = new Locale("ko", "KR");
        geocoder = new Geocoder(getApplicationContext(), locale);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapLoad();
    }

    private void mapLoad() {
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.searchMap);
        mapFragment.getMapAsync(this);      // 매배변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.searchPlace :
                startActivityForResult(new Intent(this, SearchPopupActivity.class), SEARCH_ACTIVITY);
                break;
            case R.id.select :
                Intent intent = new Intent();
                intent.putExtra("selectAddr", current);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String searchKey = intent.getExtras().getString("searchKey");
                    new AsynGeocoder().execute(searchKey);
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng latLng = new LatLng(current.getLat(), current.getLng());
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("선택 위치");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mGoogleMap.addMarker(options);
    }

    public class AsynGeocoder extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;
        Geocoder geocoder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearchActivity.this, "Wait", "Geocoding...");
            Locale locale = new Locale("ko", "KR");
            geocoder = new Geocoder(SearchActivity.this, locale);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<Address> addresses = null;
            String result = null;
            try {
                addresses = geocoder.getFromLocationName(strings[0], 1);
                if (addresses == null || addresses.size()  == 0)    Toast.makeText(SearchActivity.this, "ERROR!", Toast.LENGTH_LONG);
                else {
                    Address addr = addresses.get(0); // Address형태
                    current.setLng(addr.getLongitude());
                    current.setLat(addr.getLatitude());
                    current.setAddress(addresses.get(0).getAddressLine(0));
                    result = addresses.get(0).getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {

            if(result != null)
                searchAddr.setText(result);
            onMapReady(mGoogleMap);
            progressDlg.dismiss();
        }

    }

}
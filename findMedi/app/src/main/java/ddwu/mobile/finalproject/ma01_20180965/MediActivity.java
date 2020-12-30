package ddwu.mobile.finalproject.ma01_20180965;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class MediActivity extends AppCompatActivity {

    String latTag = "&WGS84_LAT";
    String lngTag = "&WGS84_LON";
    String anotherTag = "&numOfRows=15&pageNo=";
    String apiAddress_hospital;
    String apiAddress_pharmacy;
    ArrayList<MediDTO> resultList;
    ArrayList<MediDTO> hospitalList;
    ArrayList<MediDTO> pharmacyList;
    MediXmlParser parser;
    NetworkManager networkManager;
    MediAdapter adapter;
    Intent intent;
    String type;

    ListView lvList;
    ScrollView scrollView;
    int pageNumH;
    int pageNumP;
    String query;
    String key;

    boolean lastitemVisibleFlag = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi);
        lvList = findViewById(R.id.medi_list);
        scrollView = findViewById(R.id.scroll);
        pageNumH = pageNumP = 1;

        intent = getIntent();
        AddressDTO address = (AddressDTO) intent.getSerializableExtra("address");
        apiAddress_hospital = getResources().getString(R.string.hospital_url);

        key = getResources().getString(R.string.api_key);
        try {
            key = URLDecoder.decode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        query = latTag + "=" + address.getLat() + lngTag + "=" + address.getLng() + anotherTag;

        apiAddress_hospital += (key + query);
        apiAddress_pharmacy = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyLcinfoInqire?serviceKey=KwD496IzJUThcGRjhfLJ6Pnacto1zSMHuaOqNHVUEt62kIV5qyzOCxr23qLgtLeN0k871wOm91qIEygRlyuxwA%3D%3D" + query;

        hospitalList = new ArrayList();
        pharmacyList = new ArrayList();
        resultList = new ArrayList();

        parser = new MediXmlParser();
        networkManager = new NetworkManager(this);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(MediActivity.this, MediDetail.class);
                intent.putExtra("data", resultList.get(position));
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        new HospitalAsyncTask().execute(apiAddress_hospital + pageNumH);
        type = "hospital";
        adapter = new MediAdapter(this, R.layout.medi_item, resultList);
        lvList.setAdapter(adapter);

        lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {

                    if(type.equals("hospital")) {
                        if(isEnd(pageNumH))
                            Toast.makeText(MediActivity.this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                        else {
                            pageNumH++;
                            new HospitalAsyncTask().execute(apiAddress_hospital + pageNumH);
                        }
                    }

                    if(type.equals("pharmacy")){
                        if(isEnd(pageNumP))
                            Toast.makeText(MediActivity.this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                        else {
                            pageNumP++;
                            new PharmacyAsyncTask().execute(apiAddress_pharmacy + pageNumP);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

    }

    public boolean isEnd(int page){
        if(page * 15 > resultList.size())
            return true;

        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medi_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_hospital:
                type = "hospital";
                resultList = hospitalList;
                adapter.setList(resultList);
                break;
            case R.id.item_pharmacy:
                type = "pharmacy";
                if (pharmacyList.size() == 0) {
                    new PharmacyAsyncTask().execute(apiAddress_pharmacy + pageNumP);
                } else {
                    resultList = pharmacyList;
                    adapter.setList(resultList);
                }
                break;
        }

        return false;
    }


    class HospitalAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MediActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;

            Log.d("MAINCC", address);
            result = networkManager.downloadContents(address);
            if (result == null) return "Error!";

            hospitalList.addAll(parser.parse(result));
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Error!"))
                return;

            resultList = hospitalList;
            adapter.setList(resultList);
            progressDlg.dismiss();
        }

    }

    class PharmacyAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(MediActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;

            Log.d("MAINCC", address);
            result = networkManager.downloadContents(address);
            if (result == null) return "Error!";

            pharmacyList.addAll(parser.parse(result));

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Error!"))
                return;
            resultList = pharmacyList;
            adapter.setList(resultList);
            progressDlg.dismiss();
        }

    }

}

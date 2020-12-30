package ddwu.mobile.finalproject.ma01_20180965;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 600;
    private static final int REQUEST_Gallery = 700;
    private static final int PERMISSION_REQ_CODE = 800;

    private ImageView img;
    private String path;
    Intent intent;
    boolean before;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        img = findViewById(R.id.img);
        before = false;
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.gallery :
                if(before){
                    fileDelete();
                    before = true;
                }

                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_Gallery);

                break;
            case R.id.camera :
                if(checkPermission()) {
                    if(before){
                        fileDelete();
                        before = true;
                    }

                    dispatchTakePictureIntent();
                }
                break;
            case R.id.selectImg :
                intent = new Intent();
                intent.putExtra("photoUrl", path);
                setResult(RESULT_OK, intent);
                finish();
        }
    }

    public boolean fileDelete(){
        File file = new File(path);

        if(file.exists()){
            file.delete();
            return true;
        }

        return false;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this, "ddwu.mobile.finalproject.ma01_20180965.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }


    /*사진의 크기를 ImageView에서 표시할 수 있는 크기로 변경*/
    private void setPic() {
        // Get the dimensions of the View
        int targetW = img.getWidth();
        int targetH = img.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        img.setImageBitmap(bitmap);
    }


    /*현재 시간 정보를 사용하여 파일 정보 생성*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        path = image.getAbsolutePath();
        return image;
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
        path = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            setPic();
            before = true;
        }
       if(requestCode == REQUEST_Gallery&& resultCode == RESULT_OK){
           Uri fileUri = data.getData();
           ContentResolver resolver = getContentResolver();
           try {
               InputStream instream = resolver.openInputStream(fileUri);
               Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
               instream.close();   // 스트림 닫아주기
               createImageFile(imgBitmap);
               setPic();
               Toast.makeText(getApplicationContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
           } catch (Exception e) {
               Toast.makeText(getApplicationContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
           }

           before = true;
       }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

}

package com.example.kw.cam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import akiniyalocts.imgurapiexample.R;

public class MainActivity extends AppCompatActivity {

    public static final int Camera_request = 7;
    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void buttonclicked(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Camera_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Camera_request) {
                Bitmap cameraimg = (Bitmap) data.getExtras().get("data");
            }
        }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            //2
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            //3
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //4
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                //5
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        uploadImage(file);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    }

    public void uploadImage(File upFile) {
    /*
      Create the @Upload object
     */
        if (upFile  == null) return;
        createUpload(upFile );

    /*
      Start upload
     */
        new akiniyalocts.imgurapiexample.services.UploadService(this).Execute(upload, new UiCallback());
    }

    private void createUpload(File image) {
        upload = new akiniyalocts.imgurapiexample.imgurmodel.Upload();

        upload.image = image;
        upload.title = "ceva";
        upload.description = "altceva";
    }

    private class UiCallback implements Callback<akiniyalocts.imgurapiexample.imgurmodel.ImageResponse> {

        @Override
        public void success(akiniyalocts.imgurapiexample.imgurmodel.ImageResponse imageResponse, Response response) {
            String url = "https://www.google.com/searchbyimage?&image_url=";
            WebView view = (WebView) this.findViewById(R.id.webView);
            view.getSettings().setJavaScriptEnabled(true);
            view.loadUrl(url);
        }

        @Override
        public void failure(RetrofitError error) {


        }
    }



        }

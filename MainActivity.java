package com.magzhub.threadtest;

import android.app.ProgressDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler=new Handler();
        pd = ProgressDialog.show(this, "Working..", "Calculating Pi", true,
                false);
        startDownload();
    }
    private void startDownload(){
        new Thread(new Task()).start();
    }

    private class Task implements  Runnable{

        @Override
        public void run() {

            downloadFile();

            handler.post(new Runnable() {
                @Override
                public void run() {
                   pd.dismiss();
                }
            });
        }
    }
    public void downloadFile(){
        try {
            URL url = new URL("https://docs.google.com/uc?id=0B1peh-y5ILoUMHpFa0dUcnBTbWc&export=download");
           // URL url=new URL("http://coderzheaven.com/sample_folder/sample_file.png");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download/Book.pdf");
            FileOutputStream fileOutput=new FileOutputStream(file);
            InputStream inputStream=urlConnection.getInputStream();
            int totalSize=urlConnection.getContentLength();
            Log.e("Main Activity","Total size"+totalSize);
            int downloadsize=0;
            byte[] buffer=new byte[1024];
            int bufferLength=0;
            while ((bufferLength = inputStream.read(buffer))>0){
                fileOutput.write(buffer,0,bufferLength);
                downloadsize+=bufferLength;
                Log.e("Main Activity","File Downloaded"+downloadsize);
            }
            fileOutput.close();
        }
        catch (Exception e){
        e.printStackTrace();
        }
    }


}

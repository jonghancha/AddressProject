package com.android.addressproject.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewImageNetworkTask extends AsyncTask<Integer, String, Integer> {

    final static String TAG = "NetworkTask";
    Context context = null; // mainactivity에서 부를때
    String mAddr = null; // 접속할 웹 파일
    ProgressDialog progressDialog = null; //다운받을 동안 띄울 다이얼로그
    String devicePath;

    ImageView imageView;

    public ViewImageNetworkTask(Context context, String mAddr, ImageView imageView) {
        this.context = context;
        this.mAddr = mAddr;
        this.imageView = imageView;
    }

    // 돌고있다는 걸 보여주기 위해 progress 세팅
    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context); // 요기에다 부를거다
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Dialog");
        progressDialog.setMessage("down ....");
        progressDialog.show();
    }


    // 중간중간 상태를 체크
    @Override
    protected void onProgressUpdate(String... values) { // String
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    // 끝났다. 사진 다운로드 다 받으면 보여주기
    @Override
    protected void onPostExecute(Integer integer) { // 마지막 INTEGER
        Log.v(TAG, "onPostExecute");
        Bitmap bitmap = BitmapFactory.decodeFile(devicePath);
        imageView.setImageBitmap(bitmap);
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCanceled");
        super.onCancelled();
    }

    // 여기서 작업
    @Override
    protected Integer doInBackground(Integer... integers) { // URL. 첫번째 Integer
        Log.v(TAG, "doInBackground()");

        int index = mAddr.lastIndexOf("/");
        String imgName = mAddr.substring(index + 1); // filename
        Log.v(TAG, "urlAddress : " + mAddr);
        Log.v(TAG, "index : " + index);
        Log.v(TAG, "image name : " + imgName);

        devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.addressproject/files/" + imgName; // 받아와서 요기에 넣겠다
        Log.v(TAG, "device path : " + devicePath);
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            int len = httpURLConnection.getContentLength();
            byte[] bs = new byte[len];

            // http 로 연결이 잘 됐다
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                fileOutputStream = context.openFileOutput(imgName, 0); // 패키지까지 포함한 경로.

                while (true){
                    int i = inputStream.read(bs);
                    if (i < 0) break; // 읽어올 데이터가 없다. i 값이 데이터 읽는 것
                    fileOutputStream.write(bs, 0, i);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) inputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}

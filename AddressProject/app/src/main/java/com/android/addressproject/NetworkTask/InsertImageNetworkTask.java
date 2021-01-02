package com.android.addressproject.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.addressproject.Bean.InsertData;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//          AsyncTask의 Generic 구성 : AsyncTask<Params, Progress, Result>
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class InsertImageNetworkTask extends AsyncTask<Integer, String, Integer> {

    final static String TAG = "NetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ImageView imageView;
    InsertData insertData;

    public InsertImageNetworkTask(Context context, ImageView imageView, InsertData insertData, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.insertData = insertData;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Status");
        progressDialog.setMessage("Uploading ....");
        progressDialog.show();


    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        Log.v(TAG, "onPostExecute()");
        progressDialog.dismiss();

    }

    @Override
    protected void onCancelled() {
        Log.v(TAG,"onCancelled()");
        super.onCancelled();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //              File Upload 작업이 Background에서 동작함
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Integer doInBackground(Integer... integers) {
        File file = new File(insertData.getImg_path());
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //
                //              RequestBody.create의 parameter의 순서가 바뀜
                //
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .addFormDataPart("image", file.getName(), RequestBody.create(file, MediaType.parse("image/jpeg")))
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                .addFormDataPart("user_userId", insertData.getUser_userId())
                .addFormDataPart("addressName", insertData.getInsertAddressName())
                .addFormDataPart("addressPhone", insertData.getInsertAddressPhone())
                .addFormDataPart("addressGroup", insertData.getInsertAddressGroup())
                .addFormDataPart("addressEmail", insertData.getInsertAddressEmail())
                .addFormDataPart("addressText", insertData.getInsertAddressText())
                .addFormDataPart("addressBirth", insertData.getInsertAddressBirth())
                .build();

        Request request = new Request.Builder()
                .url(mAddr)
                .post(requestBody)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            Log.v(TAG, "Success");
            return 1;

        }catch (Exception e){
            Log.v(TAG, "Error");
            e.printStackTrace();
            return 0;
        }
    }


} // -----


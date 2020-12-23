package com.android.addressproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "NetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<Student> members;

    public NetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.members = new ArrayList<Student>();
        Log.v(TAG, "Start" + mAddr);
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute");
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Data Fetch");
        progressDialog.setMessage("Get ....");
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "onPostExecute");
        super.onPostExecute(o);
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled()");
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");
        StringBuffer stringBuffer = new StringBuffer();

        InputStream inputStream = null; //위치
        InputStreamReader inputStreamReader = null; // 읽어오는거
        BufferedReader bufferedReader = null; // 받아와서 읽어주는 애

        try {
            URL url = new URL(mAddr); // 주소. 어디가서 가져와라
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                // while 통과됐니 안됐니 확인. http 에서 신호를 받아옴
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader); // 버퍼에 차곡차곡 담는다. 여기서 이미 데이터는 버퍼드 리더에 들어가 있음

                while (true){
                    String strline = bufferedReader.readLine();
                    if (strline == null) break; // 더이상 읽을 게 없다.
                    stringBuffer.append(strline + "\n");
                }
                parser(stringBuffer.toString());
            }







        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) bufferedReader.close();// 데이터가 들어가 있으면
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return members;
    }

    // StringBuffer에 있는거 넘어옴
    private void parser(String s){
        Log.v(TAG, "parser()");
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("students_info"));
            members.clear();

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                String code = jsonObject1.getString("code");
                String name = jsonObject1.getString("name");
                String dept = jsonObject1.getString("dept");
                String phone = jsonObject1.getString("phone");

                Student student = new Student(code, name, dept, phone);
                members.add(student);
            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }



} // -----

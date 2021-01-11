package com.android.addressproject.NetworkTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.android.addressproject.Bean.Address;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// 20.12.29 지은 추가
public class AddressNetworkTask extends AsyncTask<Integer, String, Object> {

    final static String TAG = "NetworkTask";
    Context context = null;
    String mAddr = null;
    ProgressDialog progressDialog = null;
    ArrayList<Address> addresses; // 불러와야 해서

    //Constructor
    public AddressNetworkTask(Context context, String mAddr) {
        this.context = context;
        this.mAddr = mAddr;
        this.addresses = new ArrayList<Address>();    //직접 침 : 이유는 꼭 쓸 필요성은 없지만 arraylist를 사용하기 위해 생성해줌
        Log.v(TAG, "Start : "+ mAddr);
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute()");
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Data Fetch");
//        progressDialog.setMessage("Get ....");
//        progressDialog.show();
    }


    //String 의 약점은 +=
    //String 보다 StringBuffer 가 더 빠르다
    //StringBuffer 와 StringBuild 와 같음
    @Override
    protected Object doInBackground(Integer... integers) {
        Log.v(TAG, "doInBackground()");
        StringBuffer stringBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;   //  inputStreamReader 로 써도 무관하지만 더 느리기 때문에 BufferReader

        try {
            URL url = new URL(mAddr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000); //10초를 줌 (연결하는 시간=connect 시간)

            //위에서 준 connection 이 연결이 되었다면
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);   // 한번에 불러온다


                while (true){
                    String strline = bufferedReader.readLine();
                    if (strline ==  null)break;
                    stringBuffer.append(strline + "\n");
                }
                parser(stringBuffer.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return addresses;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.v(TAG, "onProgressUpdate()");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.v(TAG, "onPostExecute()");
        super.onPostExecute(o);
//        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        Log.v(TAG, "onCancelled()");
        super.onCancelled();
    }

    private void parser(String s){
        Log.v(TAG, "Parser()");
        try {
            // 배열이기 때문에 [] 이렇게 시작
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = new JSONArray(jsonObject.getString("address_select"));
            // object 가 읽어줌
            //students_info는 테이블 명이라고 생각할 것

            addresses.clear();    // 하는 이유는

// 20.12.30 지은 수정
            // object 별로 불러오는 것 {이 안의 묶음}
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);


                // 20.12.30 세미 변경 ------------
                int addressNo = jsonObject1.getInt("addressNo");

                String addressName = jsonObject1.getString("addressName");
                String addressPhone = jsonObject1.getString("addressPhone");
                String addressGroup = jsonObject1.getString("addressGroup");
                String addressEmail = jsonObject1.getString("addressEmail");
                String addressText = jsonObject1.getString("addressText");
                String addressBirth = jsonObject1.getString("addressBirth");
                String addressImage = jsonObject1.getString("addressImage");
                String addressStar = jsonObject1.getString("addressStar");



                // 20.12.30 세미 변경 ------------
//                Address address = new Address(addressName, addressPhone, addressGroup,
//                        addressEmail, addressText, addressBirth, addressImage, addressStar,addressNo);
                Address address = new Address(addressName,addressPhone,addressGroup,
                        addressEmail,addressText,addressBirth,addressImage,addressStar,addressNo);

                addresses.add(address);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}//-- Buffer 에 만들고 ArrayList 를 만듦

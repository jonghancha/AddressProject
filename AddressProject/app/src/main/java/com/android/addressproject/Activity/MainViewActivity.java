package com.android.addressproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.Bean.MainviewData;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.MainViewNetworkTask;
import com.android.addressproject.NetworkTask.ViewImageNetworkTask;
import com.android.addressproject.R;

import java.util.concurrent.ExecutionException;

//20.12.30 지은 수정
public class MainViewActivity extends AppCompatActivity {

    // 20.12.30 세미 추가 ------------------------------
    // 21.01.01 지은 수정 ----------------------------==
    ImageButton mainview_call, mainview_sms, mainview_email, mainview_btndel, mainview_btnupd,mainview_star,mainview_star2;
    String addno, favorite;
    ImageView mainviewImage;

    // 끝 --------------------------------------------

    final static String TAG = "MainViewActivity";
    String urlAddr = null;
    TextView Vname, Vphone, Vphone1, Vgroup, Vemail, Vtext, Vbirth;

    String modifyNo;
    String modifyImg;
    MainviewData mainviewData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);
        setTitle("상세보기 화면");


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // intent 를 받아온다.
        Intent intent = getIntent();

        Vname = findViewById(R.id.view_name);
        Vphone = findViewById(R.id.view_phone);
        Vphone1 = findViewById(R.id.view_phone1);
        Vgroup = findViewById(R.id.view_group);
        Vemail = findViewById(R.id.view_email);
        Vtext = findViewById(R.id.view_text);
        Vbirth = findViewById(R.id.view_birth);


        mainviewImage = findViewById(R.id.mainview_image);

        modifyImg = intent.getStringExtra("img");
        modifyNo = Integer.toString(intent.getIntExtra("no",0));
//        urlAddr = "http://" + ShareVar.macIP + ":8080/test/";
//        urlAddr = urlAddr + intent.getStringExtra("img");
//        Log.v("AddressAdapter", "urlAddr = " + urlAddr);
//        ViewImageNetworkTask networkTask = new ViewImageNetworkTask(MainViewActivity.this, urlAddr, mainviewImage);
//        networkTask.execute(100); // 10초. 이것만 쓰면 pre post do back 등 알아서 실행
//
//
//        modifyImg = intent.getStringExtra("img");
//        modifyNo = Integer.toString(intent.getIntExtra("no",0));
//        // 정보 받아오기
//        connectData(modifyNo);




        // 20.12.30 세미 전화, 문자, 이메일 버튼 및 연락처 삭제 ------------------------------

        // 연결
        mainview_call = findViewById(R.id.mainview_call);
        mainview_sms = findViewById(R.id.mainview_sms);
        mainview_email = findViewById(R.id.mainview_email);
        mainview_btndel = findViewById(R.id.mainview_btndel);   // 삭제버튼
        mainview_btnupd = findViewById(R.id.mainview_btnupd);   // 수정버튼
        mainview_star = findViewById(R.id.mainview_star);       // 즐겨찾기버튼
        mainview_star2 = findViewById(R.id.mainview_star2);     // 즐겨찾기해제

        // 삭제버튼, 즐겨찾기 버튼
        addno = Integer.toString(intent.getIntExtra("no",0));


        mainview_call.setOnClickListener(btnClickListener);
        mainview_sms.setOnClickListener(btnClickListener);
        mainview_email.setOnClickListener(btnClickListener);
        mainview_btndel.setOnClickListener(btnClickListener);
        mainview_btnupd.setOnClickListener(btnClickListener);
        mainview_star.setOnClickListener(btnClickListener);
        mainview_star2.setOnClickListener(btnClickListener);

        // 끝 -----------------------------------------------------------------------

    }

    private void connectData(String modifyNo) {


        String url = "http://" + ShareVar.macIP + ":8080/test/select_query_one.jsp?modifyNo=" + modifyNo; ;
        MainViewNetworkTask mainViewNetworkTask = new MainViewNetworkTask(MainViewActivity.this, url);
        try {
            Object obj = mainViewNetworkTask.execute().get();
            mainviewData = (MainviewData) obj;


            Vname.setText(mainviewData.getViewName());
            Vphone.setText(mainviewData.getViewPhone());
            Vphone1.setText(mainviewData.getViewPhone());
            Vgroup.setText(mainviewData.getViewGroup());
            Vemail.setText(mainviewData.getViewEmail());
            Vtext.setText(mainviewData.getViewText());
            Vbirth.setText(mainviewData.getViewBirth());



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        urlAddr = "http://" + ShareVar.macIP + ":8080/test/";
        urlAddr = urlAddr + mainviewData.getViewImg();
        Log.v("AddressAdapter", "urlAddr = " + urlAddr);
        ViewImageNetworkTask networkTask = new ViewImageNetworkTask(MainViewActivity.this, urlAddr, mainviewImage);
        networkTask.execute(100); // 10초. 이것만 쓰면 pre post do back 등 알아서 실행

    }


    // 20.12.30 세미 전화, 문자, 이메일 버튼 추가 ------------------------------

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = null;
            String phonedata = Vphone.getText().toString();
            String emaildata = Vemail.getText().toString();
            Log.v(TAG, "phonedata 값 : " + phonedata);

            // 전화
            switch (v.getId()){
                case R.id.mainview_call:
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phonedata));
                    startActivity(intent);
                    break;

                // 문자
                case R.id.mainview_sms:

                    intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phonedata));
                    startActivity(intent);
                    break;

                // 이메일
                case R.id.mainview_email:
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/Text");
                    intent.putExtra(Intent.EXTRA_EMAIL, emaildata);
                    intent.setType("message/rfc822");
                    startActivity(intent);
                    break;

                // 삭제 버튼 클릭
                case R.id.mainview_btndel:
                    urlAddr = "http://" + ShareVar.macIP + ":8080/test/AddressDelete.jsp?addno="+addno;
                    connectDeleteData();
                    Toast.makeText(MainViewActivity.this, "삭제되었습니다." + addno, Toast.LENGTH_SHORT).show();
                    break;

                // 21.01.01 편집 버튼 클릭 ------
                case R.id.mainview_btnupd:
                    intent = new Intent(MainViewActivity.this, UpdateActivity.class);
                    intent.putExtra("name", Vname.getText());
                    intent.putExtra("phone", Vphone.getText());
                    intent.putExtra("group", Vgroup.getText());
                    intent.putExtra("email", Vemail.getText());
                    intent.putExtra("text", Vtext.getText());
                    intent.putExtra("birth", Vbirth.getText());
                    intent.putExtra("img", modifyImg);
                    intent.putExtra("no", modifyNo);

                    startActivity(intent);

                    break;
                // 21.01.01 star 버튼 클릭 -> 즐겨찾기 등록 기능 추가 ———————
                case R.id.mainview_star:
                    urlAddr = "http://" + ShareVar.macIP + ":8080/test/starClick.jsp?addno="+addno;
                    connectUpdateData();
                    Toast.makeText(MainViewActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

                // 21.01.03 star 버튼 클릭 -> 즐겨찾기 해제 기능 추가 ---------------
                case R.id.mainview_star2:
                    urlAddr = "http://" + ShareVar.macIP + ":8080/test/starClickDel.jsp?addno="+addno;
                    connectUpdateData();
                    Toast.makeText(MainViewActivity.this, "즐겨찾기가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        connectData(modifyNo);
    }


    // 끝 ---------------------------------------------------------------

    //뒤로가기 ---------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // 뒤로가기
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
    // ---------------------------------



    // 20.12.30 삭제 세미 ---------------------------------------------------
    private void connectDeleteData(){
        try{
            CUDNetworkTask deleteworkTask = new CUDNetworkTask(MainViewActivity.this, urlAddr);
            deleteworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }

    // 끝 ------------------------------------------------------------------

    // 21.01.01 즐겨찾기 세미 -------------------------------------------------


    private void connectUpdateData(){
        try{
            CUDNetworkTask updateworkTask = new CUDNetworkTask(MainViewActivity.this, urlAddr);
            updateworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }


    // 끝 ------------------------------------------------------------------

}//--------------
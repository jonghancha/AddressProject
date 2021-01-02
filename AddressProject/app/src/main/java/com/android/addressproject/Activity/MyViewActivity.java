package com.android.addressproject.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.Bean.User;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.UserNetworkTask;
import com.android.addressproject.R;

import java.util.ArrayList;

//21.01.01 지은 수정
public class MyViewActivity extends AppCompatActivity {

    Button myview_btnpw, myview_btnupd;
    ArrayList<User> users;
    String urlAddr1 = null;

    final static String TAG = "MainViewActivity";
    TextView VMname, VMphone, VMemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myview);
        setTitle("나의정보 상세보기 화면");


        // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
        String checkId = PreferenceManager.getString(MyViewActivity.this,"id");

        // 로그인 한 id에 대한 이름 과 연락처를 띄우는 jsp
        urlAddr1 = "http://" + ShareVar.macIP + ":8080/test/mySelect.jsp?user_userId=" + checkId;
        getUserDate();  // 띄우기 위한 메소드
        VMname = findViewById(R.id.myv_name);
        VMphone = findViewById(R.id.myv_phone);
        VMemail = findViewById(R.id.myv_email);

        VMname.setText(users.get(0).getUserName());
        VMphone.setText(users.get(0).getUserPhone());
        VMemail.setText(users.get(0).getUserEmail());


        // 연결
        myview_btnpw = findViewById(R.id.myv_btnpw);    // 비밀번호 수정 버튼
        myview_btnupd = findViewById(R.id.myv_btnupd);   // 정보 수정 버튼


        myview_btnupd.setOnClickListener(myvClickListener);
        myview_btnupd.setOnClickListener(myvClickListener);


    }


    // 버튼 클릭시 이벤트
    View.OnClickListener myvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 비밀번호 수정 페이지로 이동 혹은 비밀번호 수정 다이어로그 출력
                case R.id.myv_btnpw:


                    break;

                    // 정보 수정 페이지로 이동
                case R.id.myv_btnupd:
                    // 상세정보 화면으로 이동하기(인텐트 날리기)
                    Intent intent = new Intent(
                            MyViewActivity.this, // 현재화면의 제어권자
                            MyUpdateActivity.class); // 다음넘어갈 화면

                    // intent 객체에 데이터를 실어서 보내기
                    // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                    intent.putExtra("userName", users.get(0).getUserName());
                    intent.putExtra("userPhone", users.get(0).getUserPhone());
                    intent.putExtra("userEmail", users.get(0).getUserEmail());

                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };



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

    // 내가 로그인한 id값에 대한 이름과 연락처를 불러옴
    private void getUserDate(){
        try {
            UserNetworkTask networkTask = new UserNetworkTask(MyViewActivity.this, urlAddr1);
            Object obj = networkTask.execute().get();
            users = (ArrayList<User>) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // ---------------------------------
}//--------------
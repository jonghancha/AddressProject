package com.android.addressproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.Bean.User;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.UserNetworkTask;
import com.android.addressproject.R;

import java.util.ArrayList;

//21.01.03 지은 수정
public class MyViewActivity extends AppCompatActivity {

    Button myview_btnpw, myview_btnupd;
    ArrayList<User> users;
    String urlAddr1 = null; // 로그인한 아이디에 대한 정보 띄움
    String urlAddr = null;  // 로그인한 아이디에 대한 비밀번호 수정 창

    final static String TAG = "MainViewActivity";
    TextView VMname, VMphone, VMemail, pass_pass;

    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myview);
        setTitle("나의정보 상세보기 화면");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


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

        pass = users.get(0).getUserPw();

        // 연결
        myview_btnpw = findViewById(R.id.myv_btnpw);    // 비밀번호 수정 버튼
        myview_btnupd = findViewById(R.id.myv_btnupd);   // 정보 수정 버튼


        myview_btnpw.setOnClickListener(myvClickListener);
        myview_btnupd.setOnClickListener(myvClickListener);


    }


    // 버튼 클릭시 이벤트
    View.OnClickListener myvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 비밀번호 수정 다이어로그 출력
                case R.id.myv_btnpw:

                    final LinearLayout linear = (LinearLayout) View.inflate(MyViewActivity.this, R.layout.pass, null);
                    pass_pass = linear.findViewById(R.id.pass_pass);
                    pass_pass.setText(pass);

                    new AlertDialog.Builder(MyViewActivity.this)
                            .setTitle("나의 비밀번호 수정")
                            .setIcon(R.drawable.group)

                            // 위에서 선언한
                            .setView(linear)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String checkId = PreferenceManager.getString(MyViewActivity.this,"id");
                                    // 로그인 한 id에 대한 이름 과 연락처를 띄우는 jsp
                                    urlAddr1 = "http://" + ShareVar.macIP + ":8080/test/mySelect.jsp?user_userId=" + checkId;
                                    getUserDate();  // 띄우기 위한 메소드
                                    // linear에 있는 아이 이므로 앞에 넣어줘야한다.



                                    EditText paUpda = linear.findViewById(R.id.pass_Upda); // 현재 비밀번호를 띄울곳


                                    String passupdate = paUpda.getText().toString();

                                    urlAddr = "http://" + ShareVar.macIP + ":8080/test/passUpdate.jsp?user_userId=" + checkId;  // ?(물음표) 주의

                                    urlAddr = urlAddr +"&userPw="+ passupdate;
                                    connectUpdatePass();
                                    Toast.makeText(MyViewActivity.this, checkId+" 님의 비밀번호가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MyViewActivity.this, pass, Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();

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


    private void connectUpdatePass(){
        try {
            CUDNetworkTask insnetworkTask = new CUDNetworkTask(MyViewActivity.this, urlAddr);
            insnetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }



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
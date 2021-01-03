package com.android.addressproject.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.Bean.User;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.UserNetworkTask;
import com.android.addressproject.R;

import java.util.ArrayList;

//21.01.01 지은 수정
public class MyUpdateActivity extends AppCompatActivity {

    Button myup_btncan, myvup_btnupd;
    ArrayList<User> users;
    String urlAddr = null;

    final static String TAG = "MyUpdateActivity";
    EditText VUname, VUphone, VUemail;
    String myname, myphone, myemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupdate);
        setTitle("나의정보 상세보기 화면");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
        String checkId = PreferenceManager.getString(MyUpdateActivity.this,"id");

        urlAddr = "http://" + ShareVar.macIP + ":8080/test/myUpdate.jsp?user_userId=" + checkId;


        VUname =  findViewById(R.id.myu_name);
        VUphone =  findViewById(R.id.myu_phone);
        VUemail =  findViewById(R.id.myu_email);

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        VUname.setText(intent.getStringExtra("userName"));
        VUphone.setText(intent.getStringExtra("userPhone"));
        VUemail.setText(intent.getStringExtra("userEmail"));
        // 연결
        myup_btncan = findViewById(R.id.myu_btncan);    // 취소 버튼
        myvup_btnupd = findViewById(R.id.myu_btnupd);   // 정보 수정 버튼


        myup_btncan.setOnClickListener(myuClickListener);
        myvup_btnupd.setOnClickListener(myuClickListener);


    }


    // 버튼 클릭시 이벤트
    View.OnClickListener myuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                // 취소 버튼
                case R.id.myu_btncan:
                    finish();
                    break;

                    // 정보 수정 하기 버튼
                case R.id.myu_btnupd:
                    myname = VUname.getText().toString();
                    myphone = VUphone.getText().toString();
                    myemail = VUemail.getText().toString();

                    urlAddr = urlAddr + "&userName=" + myname + "&userPhone=" + myphone + "&userEmail=" + myemail;
                    connectUpdateMy();
                    break;
            }
        }
    };

    private void connectUpdateMy(){
        try {
            CUDNetworkTask updateworkTask = new CUDNetworkTask(MyUpdateActivity.this, urlAddr);
            updateworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }

        finish();
    }





    //editText 외의 화면 클릭시 키보드 숨기기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
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


    // ---------------------------------
}//--------------
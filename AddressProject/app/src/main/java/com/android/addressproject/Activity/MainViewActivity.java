package com.android.addressproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.R;


public class MainViewActivity extends AppCompatActivity {

    final static String TAG = "MainViewActivity";
    String urlAddr = null;
    String scode, sname, sdept, sphone, macIP;
    TextView Ucode, Uname, Udept, Uphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);
        setTitle("상세보기 화면");

        // intent 를 받아온다.
        Intent intent = getIntent();
        macIP = intent.getStringExtra("macIP");
        urlAddr = "http://localhost:8080/test/studentUpdate.jsp?";  // ?(물음표) 주의

        Ucode = findViewById(R.id.update_code);
        Uname = findViewById(R.id.update_name);
        Udept = findViewById(R.id.update_dept);
        Uphone = findViewById(R.id.update_phone);



        Ucode.setText(intent.getStringExtra("code"));
        Uname.setText(intent.getStringExtra("name"));
        Udept.setText(intent.getStringExtra("dept"));
        Uphone.setText(intent.getStringExtra("phone"));


    }

}
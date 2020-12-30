package com.android.addressproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.R;

//20.12.30 지은 수정
public class MainViewActivity extends AppCompatActivity {

    final static String TAG = "MainViewActivity";
    String urlAddr = null;
    String scode, sname, sdept, sphone;
//    String macIp = "192.168.43.220";
    TextView Vname, Vphone, Vgroup, Vemail, Vtext, Vbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);
        setTitle("상세보기 화면");

        // intent 를 받아온다.
        Intent intent = getIntent();

        Vname = findViewById(R.id.view_name);
        Vphone = findViewById(R.id.view_phone);
        Vgroup = findViewById(R.id.view_group);
        Vemail = findViewById(R.id.view_email);
        Vtext = findViewById(R.id.view_text);
        Vbirth = findViewById(R.id.view_birth);


        Vname.setText(intent.getStringExtra("name"));
        Vphone.setText(intent.getStringExtra("phone"));
        Vgroup.setText(intent.getStringExtra("group"));
        Vemail.setText(intent.getStringExtra("email"));
        Vtext.setText(intent.getStringExtra("text"));
        Vbirth.setText(intent.getStringExtra("birth"));


    }


    //뒤로가기
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


}
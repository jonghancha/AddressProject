package com.android.addressproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.addressproject.R;

public class Memdel extends AppCompatActivity {

    Button btn_memdel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memdel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // 20.12.31 세미추가 회원탈퇴기능 ---------------------------------

        btn_memdel = findViewById(R.id.btn_memdel);
        btn_memdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //




            }
        });
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
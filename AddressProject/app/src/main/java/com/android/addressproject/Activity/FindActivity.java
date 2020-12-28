package com.android.addressproject.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.addressproject.R;

public class FindActivity extends AppCompatActivity {

    LinearLayout Vid, Vpw;
    Button btnId, btnPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Vid = findViewById(R.id.view_Fid);
        Vpw = findViewById(R.id.view_Fpw);

        btnId = findViewById(R.id.btn_Fid);
        btnPw = findViewById(R.id.btn_Fpw);

        btnId.setOnClickListener(FonclickListener);
        btnPw.setOnClickListener(FonclickListener);
    }
    View.OnClickListener FonclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_Fid:
                    Vid.setVisibility(v.VISIBLE);
                    Vpw.setVisibility(v.INVISIBLE);
                    break;
                case R.id.btn_Fpw:
                    Vid.setVisibility(v.INVISIBLE);
                    Vpw.setVisibility(v.VISIBLE);
                    break;


            }
        }
    };


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
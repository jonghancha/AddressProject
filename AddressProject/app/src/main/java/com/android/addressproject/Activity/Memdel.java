package com.android.addressproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.R;

public class Memdel extends AppCompatActivity {

    Button btn_memdel;
    String urlAddr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memdel);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // 20.12.31 세미추가 회원탈퇴기능 ---------------------------------
        //  Intent intent = getIntent();

        String checkId = PreferenceManager.getString(Memdel.this,"id");

        btn_memdel = findViewById(R.id.btn_memdel);
        btn_memdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(Memdel.this, "addno값 : " +  checkId, Toast.LENGTH_SHORT).show();
                urlAddr = "http://" + ShareVar.macIP + ":8080/test/memDel.jsp?checkId="+checkId;
                connectDeleteData();
                Toast.makeText(Memdel.this, "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intentMD = new Intent(Memdel.this, LoginActivity.class);
                startActivity(intentMD);

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


    // 20.12.31 회원탈퇴 세미 ---------------------------------------------------
    private void connectDeleteData(){
        try{
            CUDNetworkTask deleteworkTask = new CUDNetworkTask(Memdel.this, urlAddr);
            deleteworkTask.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }

    // 끝 ------------------------------------------------------------------
}
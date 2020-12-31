package com.android.addressproject.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.addressproject.R;

public class FindActivity extends AppCompatActivity {

    LinearLayout Vid, Vpw;
    Button btnId, btnPw, btn_FindId, btn_FindPw;

    // 20.12.30 세미 아이디찾기 추가 -------------------------------------
    String urlAddr = null;
    EditText etFI_name, etFI_phone;

    // 끝 ---------------------------------------------------

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

        // 20.12.30 세미 추가 -------------------------------------

        // 연결
        etFI_name = findViewById(R.id.etFI_name);
        etFI_phone = findViewById(R.id.etFI_phone);
        btn_FindId = findViewById(R.id.btn_FindId);



        // 클릭시
        btn_FindId.setOnClickListener(findClickListener);



        // ------------------------------------------------------

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


    // 20.12.30 세미 추가 -------------------------------------

    View.OnClickListener findClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // 값 가져오기
            String tempFIname = etFI_name.getText().toString();
            String tempFIphone = etFI_phone.getText().toString();

        urlAddr = "http://192.168.43.220:8080/test/findidUser.jsp?find_name=" + tempFIname + "&find_phone=" + tempFIphone;
        }
    };


    // 끝 ---------------------------------------------------



}
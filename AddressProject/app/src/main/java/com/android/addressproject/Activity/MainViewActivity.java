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

import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.R;

//20.12.30 지은 수정
public class MainViewActivity extends AppCompatActivity {

    // 20.12.30 세미 추가 ------------------------------

    Button mainview_call, mainview_sms, mainview_email, mainview_btndel;
    String addno;

    // 끝 --------------------------------------------

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

        // 20.12.30 세미 전화, 문자, 이메일 버튼 및 연락처 삭제 ------------------------------

        // 연결
        mainview_call = findViewById(R.id.mainview_call);
        mainview_sms = findViewById(R.id.mainview_sms);
        mainview_email = findViewById(R.id.mainview_email);
        mainview_btndel = findViewById(R.id.mainview_btndel);   // 삭제버튼

        // 삭제버튼
        addno = Integer.toString(intent.getIntExtra("no",0));


        mainview_call.setOnClickListener(btnClickListener);
        mainview_sms.setOnClickListener(btnClickListener);
        mainview_email.setOnClickListener(btnClickListener);
        mainview_btndel.setOnClickListener(btnClickListener);

        // 끝 -----------------------------------------------------------------------


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
            }
        }
    };

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








}//--------------
package com.android.addressproject.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.addressproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    // 20.12.29 세미 추가 --------------------------------------
    TextView main_id, main_pw;
    Context mContext;
    // -------------------------------------------------------


    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;
    FloatingActionButton floatingActionButton;

    private static final int SETTINGS_CODE = 234;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setThemeOfApp();

        setContentView(R.layout.activity_main);


        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new FABClickListener());


        // 20.12.29 세미 로그인 결과 테스트 --------------------------------------

        mContext = this;

//        main_id = findViewById(R.id.main_id);
//        main_pw = findViewById(R.id.main_pw);
//
//        Intent intent = getIntent();
//        String userid = intent.getStringExtra("id");
//        String userpw = intent.getStringExtra("pw");
//        String pfid = com.android.addressproject.Activity.PreferenceManager.getString(mContext,"id");
//        String pfpw = com.android.addressproject.Activity.PreferenceManager.getString(mContext, "pw");
//
//        main_id.setText("userid : " + userid + " / pfID :  " + pfid);
//        main_pw.setText("userpw : " + userpw + " / pfPW :  " + pfpw);


        //--------------------------------------------------------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Osettings:
                Intent intentS = new Intent(this, SettingsActivity.class);
                startActivityForResult(intentS, SETTINGS_CODE);
                break;
            case R.id.Ogroup:
                Intent intentG = new Intent(this, GroupActivity.class);
                startActivity(intentG);
                break;
            case R.id.Ologout:


                // 20.12.29 세미 로그아웃 추가 ---------------------------------------------------------

                // 로그아웃 버튼을 누르면 SharedPreferences에 저장된 값들을 로그아웃 버튼을 누르면 삭제하기 위해
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                // editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
                break;

                // 끝 ------------------------------------------------------------------------------
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE){
            this.recreate();
        }
    }

    ///
    private void setThemeOfApp(){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        if (sharedPreferences.getString("color_option", "BLUE").equals("BLUE")){
            setTheme(R.style.BlueTheme);
        }else{
            setTheme(R.style.RedTheme);
        }

    }







    ///"플로팅 버튼 클릭
    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Intent intentI = new Intent(MainActivity.this, InsertActivity.class);
            startActivity(intentI);


            Toast.makeText(MainActivity.this, "플로팅 버튼 클릭", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }
}
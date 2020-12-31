package com.android.addressproject.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.android.addressproject.Adapter.ViewPageAdapter;

import com.android.addressproject.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {


    // 20.12.29 지은 추가 --------------------------------------
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    // -------------------------------------------------------

    // 20.12.29 세미 추가 --------------------------------------
    TextView main_id, main_pw;
    Context mContext;
    // -------------------------------------------------------

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    private static final int SETTINGS_CODE = 234;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_main);

        //20.12.30 지은 실험
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        TextView mainid = findViewById(R.id.mainid);
        mainid.setText(id);


        //-----------------
        // 20.12.30 종한 로그인 성공 시 파일 관련 사용자 권한 물어보기 추가
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE); //사용자에게 사진 사용 권한 받기 (가장중요함)


        // 20.12.29 지은 viewPager + tablayout 추가 --------------------------------------
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        viewPager = (ViewPager) findViewById(R.id.pageViewId);

        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        //     Add Fragment
        viewPageAdapter.AddFrmt(new Frmt_call(),"");
        viewPageAdapter.AddFrmt(new FrmtContact(),"");
        viewPageAdapter.AddFrmt(new Frmt_fav(),"");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);
        // -------------------------------------------------------

        // 20.12.29 세미 로그인 결과 테스트 --------------------------------------

        mContext = this;

//        main_id = findViewById(R.id.main_id);
//        main_pw = findViewById(R.id.main_pw);
//
//        Intent intent = getIntent();
//        String userid = intent.getStringExtra("id");
//        String userpw = intent.getStringExtra("pw");
//        String pfid = PreferenceManager.getString(mContext,"id");
//        String pfpw = PreferenceManager.getString(mContext, "pw");
//
//        main_id.setText("userid : " + userid + " / pfID :  " + pfid);
//        main_pw.setText("userpw : " + userpw + " / pfPW :  " + pfpw);


        //--------------------------------------------------------------------------------

        // 20.12.29 지은 tablayout 추가 --------------------------------------
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_call_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_black_24dp);

        //Remove ActionBar Shadow

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
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

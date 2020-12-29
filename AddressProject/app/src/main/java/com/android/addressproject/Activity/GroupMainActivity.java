package com.android.addressproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.addressproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupMainActivity extends AppCompatActivity {



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

        setContentView(R.layout.activity_group_main);
//
//
//        floatingActionButton = findViewById(R.id.floatingActionButton);
//
//        floatingActionButton.setOnClickListener(new FABClickListener());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings)
        {
            Intent intentS = new Intent(this, SettingsActivity.class);
            startActivityForResult(intentS, SETTINGS_CODE);
        }
        if (item.getItemId() == R.id.Ogroup)
        {
            Intent intentG = new Intent(this, GroupMainActivity.class);
            startActivityForResult(intentG, SETTINGS_CODE);
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
            // FAB Click 이벤트 처리 구간 ( 다이어로그로 그룹추가 할수 있도록 설계 에정)


            Toast.makeText(GroupMainActivity.this, "플로팅 버튼 클릭", Toast.LENGTH_SHORT).show();
        }
    }



}
package com.android.addressproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroupActivity extends AppCompatActivity {



    FloatingActionButton floatingActionButton;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("그룹메인");
        setThemeOfApp();

        setContentView(R.layout.activity_group);


        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new FABClickListener());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Ogroup:
                Intent intentG = new Intent(this, GroupActivity.class);
                startActivity(intentG);
                break;
            case R.id.Ologout:
                Intent intentL = new Intent(this, LoginActivity.class);
                startActivity(intentL);
                break;
        }

        return super.onOptionsItemSelected(item);
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
            Intent intentI = new Intent(GroupActivity.this, InsertActivity.class);
            startActivity(intentI);


            Toast.makeText(GroupActivity.this, "그룹 추가", Toast.LENGTH_SHORT).show();
        }
    }



}
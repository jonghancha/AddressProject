package com.android.addressproject.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InsertGroupActivity extends AppCompatActivity {

    // 20.12.30 지은 추가
    final  static String TAG = "InsertGroupActivity";
    String urlAddr = null;
    String groupInsert;
    EditText Gname;
    Button btnGinsert, btnGcancel;
    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("그룹 입력 화면");
        setThemeOfApp();
        setContentView(R.layout.activity_insert_group);

        String checkId = com.android.addressproject.Activity.PreferenceManager.getString(this,"id");

        urlAddr = "http://192.168.200.178:8080/test/groupInsert.jsp?user_userId=" + checkId;  // ?(물음표) 주의



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Gname = findViewById(R.id.insert_Newgroup);

        btnGcancel = findViewById(R.id.btn_Gcancel);
        btnGinsert = findViewById(R.id.btn_Ginsert);

        btnGcancel.setOnClickListener(GroupClickListener);
        btnGinsert.setOnClickListener(GroupClickListener);


    }//--onCreate 끝

    View.OnClickListener GroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_Ginsert:
                    groupInsert = Gname.getText().toString();


                    urlAddr = urlAddr +"&addressGroup="+ groupInsert;
                    connectInsertGroupData();
                    Toast.makeText(InsertGroupActivity.this, groupInsert+" 가 입력 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case R.id.btn_Gcancel:
                    Toast.makeText(InsertGroupActivity.this, "입력을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

            }

        }
    };


    private void connectInsertGroupData(){
        try {
            CUDNetworkTask insnetworkTask = new CUDNetworkTask(InsertGroupActivity.this, urlAddr);
            insnetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;

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

    ///테마
    private void setThemeOfApp(){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        if (sharedPreferences.getString("color_option", "BLUE").equals("BLUE")){
            setTheme(R.style.BlueTheme);
        }else{
            setTheme(R.style.RedTheme);
        }

    }



    //editText 외의 화면 클릭시 키보드 숨기기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }



}//-- 전체 끝
package com.android.addressproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.addressproject.Adapter.AddressAdapter;
import com.android.addressproject.Adapter.GroupAdapter;
import com.android.addressproject.Bean.Address;
import com.android.addressproject.NetworkTask.AddressNetworkTask;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.GroupNetworkTask;
import com.android.addressproject.R;
import java.util.ArrayList;

public class GroupViewActivity extends AppCompatActivity {

    // 20.12.31 지은 추가
    final  static String TAG = "GroupViewActivity";
    String urlAddr = null;  // 그룹 띄우기
    String urlAddr1 = null; // 구룹 삭제
    String urlAddr2 = null; // 그룹명 수정
    ArrayList<Address> group;
    AddressAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    String macIP;   //macIP 를 이곳에서 넘겨주는 것을 알아둬야 한다.
    // 검색창
    EditText search_EdT;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("그룹 리스트");
        setThemeOfApp();

        setContentView(R.layout.activity_group_view);

        recyclerView = findViewById(R.id.group_recycleView);
        TextView mygroup = findViewById(R.id.mygroup);

        Button group_delete = findViewById(R.id.group_delete);
        group_delete.setOnClickListener(GDonClickListener);


        Button group_update = findViewById(R.id.group_update);
        group_update.setOnClickListener(GDonClickListener);


        Intent intent = getIntent();
        String addressGroup = intent.getStringExtra("group");
        mygroup.setText(addressGroup);

        // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
        String checkId = com.android.addressproject.Activity.PreferenceManager.getString(GroupViewActivity.this,"id");

        urlAddr = "http://" + ShareVar.macIP + ":8080/test/groupSelect.jsp?user_userId=" + checkId +"&addressGroup=" + addressGroup +"&search_text=";
//
////        urlAddr1 = "http://" + ShareVar.macIP + ":8080/test/groupDelete.jsp?user_userId=" + checkId +"&addressGroup=" + addressGroup;
//        urlAddr2 =  "http://" + ShareVar.macIP + ":8080/test/groupUpdate.jsp?user_userId=" + checkId +"&addressGroup=";

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }//--onCreate 끝

    // 삭제 버튼
    View.OnClickListener GDonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //삭제
                case R.id.group_delete:
                    Intent intent = getIntent();
                    String addressGroup = intent.getStringExtra("group");
                    String checkId = com.android.addressproject.Activity.PreferenceManager.getString(GroupViewActivity.this,"id");

                    urlAddr1 = "http://" + ShareVar.macIP + ":8080/test/groupDelete.jsp?user_userId=" + checkId +"&addressGroup=" + addressGroup;

                    connectDeleteData();
                    Toast.makeText(GroupViewActivity.this, urlAddr1+" 가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    break;



                    //수정 다이어로그
                case R.id.group_update:
                    final LinearLayout linear = (LinearLayout) View.inflate(GroupViewActivity.this, R.layout.group_update, null);
                    new AlertDialog.Builder(GroupViewActivity.this)
                            .setTitle("그룹명 수정")
                            .setIcon(R.drawable.group)

                            // 위에서 선언한
                            .setView(linear)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = getIntent();
                                    String addressGroup = intent.getStringExtra("group");
                                    String checkId = com.android.addressproject.Activity.PreferenceManager.getString(GroupViewActivity.this,"id");

                                    EditText di_groupUpdate = linear.findViewById(R.id.di_groupUpdate);
                                    String groupUpdate = di_groupUpdate.getText().toString();

                                    urlAddr2 = "http://" + ShareVar.macIP + ":8080/test/groupUpdate.jsp?user_userId=" + checkId +"&addressGroup=" + groupUpdate;

                                    groupUpdateData();
                                    Toast.makeText(GroupViewActivity.this, "그룹명이 수정되었습니다.", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(GroupViewActivity.this, "그룹명 수정을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();

                    break;
            }

        }
    };



    private void groupUpdateData(){
        try {
            CUDNetworkTask deleteworkTask = new CUDNetworkTask(GroupViewActivity.this, urlAddr2);
            deleteworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }

        finish();
    }



    private void connectDeleteData(){
        try {
            CUDNetworkTask deleteworkTask = new CUDNetworkTask(GroupViewActivity.this, urlAddr1);
            deleteworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }

        finish();
    }



    //메소드
    private void connectGetData(){
        try {
            AddressNetworkTask networkTask = new AddressNetworkTask(this, urlAddr); //onCreate 에 urlAddr 이 선언된것이 들어옴

            // object 에서 선언은 되었지만 실질적으로 리턴한것은 arraylist
            Object object = networkTask.execute().get();
            group = (ArrayList<Address>) object;

            //StudentAdapter.java 의 생성자를 받아온다.
            adapter = new AddressAdapter(GroupViewActivity.this, R.layout.item_contact, group);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    //  textChanged 시 검색기능 쓰기
    TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        // 텍스트가 변할때마다 조건검색이 실행 됩니다.
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //20.12.30 지은 수정 -----------------
            // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
            String checkId = com.android.addressproject.Activity.PreferenceManager.getString(GroupViewActivity.this,"id");


            // 텍스트가 변할때마다 urlAddr에 덮어씌워져서 그때마다 그냥 초기화시켜줌

            Intent intent = getIntent();
            String addressGroup = intent.getStringExtra("group");
            urlAddr = "http://" + ShareVar.macIP + ":8080/test/groupSelect.jsp?user_userId=" + checkId +"&addressGroup=" + addressGroup +"&search_text=";


            String searchText = search_EdT.getText().toString().trim();
            urlAddr = urlAddr + searchText;
            connectGetData();
        }//--------------------

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    //resume 에 써주는 것이 좋다. create 에 써도 무관하지만
    @Override
    public void onResume() {
        super.onResume();
        connectGetData();
        Log.v(TAG, "onResume()");
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
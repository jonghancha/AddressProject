package com.android.addressproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.addressproject.Adapter.AddressAdapter;
import com.android.addressproject.Bean.Address;
import com.android.addressproject.Bean.User;
import com.android.addressproject.NetworkTask.AddressNetworkTask;
import com.android.addressproject.NetworkTask.UserNetworkTask;
import com.android.addressproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Frmt_call extends Fragment {

    View v;
    final static String TAG = "Frmt_call";
    String urlAddr = null;
    String urlAddr1 = null;
    ArrayList<Address> addresses;
    AddressAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ArrayList<User> users;

    // 검색창
    EditText search_EdT;



    public Frmt_call() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.call_frmt,container,false);
        //20.12.30 지은 추가 -----------------
        // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
        String checkId = PreferenceManager.getString(getContext(),"id");

        //-----------

        FloatingActionButton floatingActionButton = v.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(floatCliclListener);
        recyclerView = (RecyclerView) v.findViewById(R.id.call_recycleView);


        //20.12.30 지은 추가 -----------------
        //조건 검색 .jsp 를 따로 만들어서 연결시켜줌.
        //search_text가 검색되는 단어(번호도 가능)

        // 검색 + 띄우는 jsp
        urlAddr = "http://192.168.43.220:8080/test/addressSelectWithCondition.jsp?user_userId=" + checkId +"&search_text=";

        search_EdT = v.findViewById(R.id.search_ET);
        search_EdT.addTextChangedListener(textChangedListener);
        //-----------------------------------------------------



        //++++++++++++++++++++++++++++++++++++++++++
        // 로그인 한 id에 대한 이름 과 연락처를 띄우는 jsp
        urlAddr1 = "http://192.168.43.220:8080/test/mySelect.jsp?user_userId=" + checkId;
        getUserDate();  // 띄우기 위한 메소드

        TextView myid = v.findViewById(R.id.myid);
        myid.setText(users.get(0).getUserName());
        TextView myphone = v.findViewById(R.id.myphone);
        myphone.setText(users.get(0).getUserPhone());
        //++++++++++++++++++++++++++++++++++++++++++

        // 나의 정보 상세보기를 위한
        LinearLayout mymain = v.findViewById(R.id.mymain);
        mymain.setOnClickListener(myClickListener);


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //메소드 = 로그인한 아이디값에 저장된 연락처 띄워주는
    private void connectGetData(){
        try {
            AddressNetworkTask networkTask = new AddressNetworkTask(getActivity(), urlAddr); //onCreate 에 urlAddr 이 선언된것이 들어옴

            // object 에서 선언은 되었지만 실질적으로 리턴한것은 arraylist
            Object object = networkTask.execute().get();
            addresses = (ArrayList<Address>) object;

            //StudentAdapter.java 의 생성자를 받아온다.
            adapter = new AddressAdapter(getActivity(), R.layout.item_contact, addresses);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


//-------------------------------------------------------
    //  textChanged 시 검색기능 쓰기
    TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        // 텍스트가 변할때마다 조건검색이 실행 됩니다.
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //20.12.30 지은 추가 -----------------
            // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
            String checkId = PreferenceManager.getString(getContext(),"id");

            // 텍스트가 변할때마다 urlAddr에 덮어씌워져서 그때마다 그냥 초기화시켜줌

            urlAddr = "http://192.168.43.220:8080/test/addressSelectWithCondition.jsp?user_userId=" + checkId +"&search_text=";

            //----------------------


            String searchText = search_EdT.getText().toString().trim();
            urlAddr = urlAddr + searchText;
            connectGetData();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

//-------------------------------------------------------


    //입력 되어도 리스트에 실시간으로 추가 되게 만들어줌(유지)
    @Override
    public void onResume() {
        super.onResume();
        connectGetData();
        Log.v(TAG, "onResume()");
    }


    // 플로팅 버튼( 새로운 연락처 추가하는 액티비티로 옮김 )
    View.OnClickListener floatCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), InsertActivity.class);
            startActivity(intent);
        }
    };


    // 내가 로그인한 id값에 대한 이름과 연락처를 불러옴
    private void getUserDate(){
        try {
            UserNetworkTask networkTask = new UserNetworkTask(getContext(), urlAddr1);
            Object obj = networkTask.execute().get();
            users = (ArrayList<User>) obj;
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    // 나의 정보 상세보기
    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MyViewActivity.class);
            startActivity(intent);
        }
    };

}

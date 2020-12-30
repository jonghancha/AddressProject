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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.addressproject.Adapter.AddressAdapter;
import com.android.addressproject.Bean.Address;
import com.android.addressproject.NetworkTask.AddressNetworkTask;
import com.android.addressproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Frmt_call extends Fragment {

    View v;
    final static String TAG = "FrmtContact";
    String urlAddr = null;
    ArrayList<Address> addresses;
    AddressAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

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

        TextView myid = v.findViewById(R.id.myid);
        myid.setText(checkId);
        //-----------

        FloatingActionButton floatingActionButton = v.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(floatCliclListener);
        recyclerView = (RecyclerView) v.findViewById(R.id.call_recycleView);

        AddressAdapter viewAdapter = new AddressAdapter(getContext(), R.layout.item_contact, addresses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(viewAdapter);

        //20.12.30 지은 추가 -----------------
        //조건 검색 .jsp 를 따로 만들어서 연결시켜줌.
        //search_text가 검색되는 단어(번호도 가능)
        urlAddr = "http://192.168.43.220:8080/test/addressSelectWithCondition.jsp?user_userId=" + checkId +"&search_text=";
        //----------
        search_EdT = v.findViewById(R.id.search_ET);
        search_EdT.addTextChangedListener(textChangedListener);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //메소드
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


    //resume 에 써주는 것이 좋다. create 에 써도 무관하지만
    @Override
    public void onResume() {
        super.onResume();
        connectGetData();
        Log.v(TAG, "onResume()");
    }

    View.OnClickListener floatCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), InsertActivity.class);
            startActivity(intent);
        }
    };

}

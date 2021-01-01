package com.android.addressproject.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.addressproject.Adapter.GroupAdapter;
import com.android.addressproject.Bean.Address;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.GroupNetworkTask;
import com.android.addressproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

// 20.12.29 지은 추가
public class FrmtContact extends Fragment {

    View v;
    final static String TAG = "FrmtContact";
    String urlAddr = null;
    ArrayList<Address> group;
    GroupAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    String groupInsert;

    // 검색창
    EditText search_EdT;



    public FrmtContact() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.contact_frmt,container,false);

        //20.12.30 지은 수정 -----------------




        FloatingActionButton floatingActionButton = v.findViewById(R.id.contact_float);
        floatingActionButton.setOnClickListener(CfloatCliclListener);


        recyclerView = v.findViewById(R.id.contact_recycleView);


        search_EdT = v.findViewById(R.id.search_ET);
        search_EdT.addTextChangedListener(textChangedListener);
        return v;
    }
    //=--------------------------

     @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //메소드
    private void connectGetData(){
        try {
            // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
            String checkId = PreferenceManager.getString(getContext(),"id");

            urlAddr = "http://" + ShareVar.macIP + ":8080/test/addressGroupCondition.jsp?user_userId=" + checkId +"&search_text=";
            GroupNetworkTask networkTask = new GroupNetworkTask(getActivity(), urlAddr); //onCreate 에 urlAddr 이 선언된것이 들어옴

            // object 에서 선언은 되었지만 실질적으로 리턴한것은 arraylist
            Object object = networkTask.execute().get();
            group = (ArrayList<Address>) object;

            //StudentAdapter.java 의 생성자를 받아온다.
            adapter = new GroupAdapter(getActivity(), R.layout.item_con, group);
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
            //20.12.30 지은 수정 -----------------
            // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
            String checkId = PreferenceManager.getString(getContext(),"id");


            // 텍스트가 변할때마다 urlAddr에 덮어씌워져서 그때마다 그냥 초기화시켜줌


            urlAddr = "http://" + ShareVar.macIP + ":8080/test/addressGroupCondition.jsp?user_userId=" + checkId +"&search_text=";

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


    View.OnClickListener CfloatCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(getActivity(), InsertGroupActivity.class);
//            startActivity(intent);

            final LinearLayout linear = (LinearLayout) View.inflate(getContext(), R.layout.group, null);
            new AlertDialog.Builder(getContext())
                    .setTitle("추가할 그룹 명을 입력하세요")
                    .setIcon(R.mipmap.ic_launcher_round)

                    // 위에서 선언한
                    .setView(linear)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // linear에 있는 아이 이므로 앞에 넣어줘야한다.
                            EditText product = linear.findViewById(R.id.di_group);

                            groupInsert = product.getText().toString();

                            String checkId = com.android.addressproject.Activity.PreferenceManager.getString(getContext(),"id");


                            urlAddr = "http://" + ShareVar.macIP + ":8080/test/groupInsert.jsp?user_userId=" + checkId;  // ?(물음표) 주의

                            urlAddr = urlAddr +"&addressGroup="+ groupInsert;
                            connectInsertGroupData();
                            Toast.makeText(getContext(), groupInsert+" 가 입력 되었습니다.", Toast.LENGTH_SHORT).show();
                            connectGetData();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "그룹 추가를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
    };
//
//    private void connGetGroup(){
//        String checkId = com.android.addressproject.Activity.PreferenceManager.getString(getContext(),"id");
//
//
//        urlAddr = "http://" + ShareVar.macIP + ":8080/test/groupInsert.jsp?user_userId=" + checkId;  // ?(물음표) 주의
//
//        urlAddr = urlAddr +"&addressGroup="+ groupInsert;
//        connectInsertGroupData();
//        Toast.makeText(getContext(), groupInsert+" 가 입력 되었습니다.", Toast.LENGTH_SHORT).show();
//    }
//



    private void connectInsertGroupData(){
        try {
            CUDNetworkTask insnetworkTask = new CUDNetworkTask(getContext(), urlAddr);
            insnetworkTask.execute().get();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

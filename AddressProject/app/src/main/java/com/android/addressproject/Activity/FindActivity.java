package com.android.addressproject.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.NetworkTask.FindNetworkTask;
import com.android.addressproject.R;

import static android.app.ProgressDialog.show;

public class FindActivity extends AppCompatActivity {


    //이강후.

    final static String TAG = "FindActivity";
    String urlAddr = null;
    String urlAddr2 = null;

    String snameid, snamepw, sphone, sid, semail;
    EditText EnameId, Ephone, Eid, EnamePw, Eemail;
    Button btn_Fid, btn_Fpw, btn_FindId, btn_FindPw;
    String macIP = ShareVar.macIP;

    LinearLayout Vid, Vpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        //텍스트 필드 연결

        //아이디 찾기.
        EnameId = findViewById(R.id.etFI_name);
        Ephone = findViewById(R.id.etFI_phone);
        //패스워드 찾기.
        Eid = findViewById(R.id.etFP_id);
        EnamePw = findViewById(R.id.etFP_name);
        Eemail = findViewById(R.id.etFP_email);

        //입력시 자릿수 제한.
        EnameId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        Ephone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        Eid.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        EnamePw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        Eemail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(22)});


        //버튼 연결.
        btn_Fid = findViewById(R.id.btn_Fid); //위쪽 버튼
        btn_Fpw = findViewById(R.id.btn_Fpw);
        btn_Fid.setOnClickListener(mclickListener);
        btn_Fpw.setOnClickListener(mclickListener);

        btn_FindId = findViewById(R.id.btn_FindId); //아래쪽 버튼
        btn_FindPw = findViewById(R.id.btn_FindPw);
        btn_FindId.setOnClickListener(onClickListener);
        btn_FindPw.setOnClickListener(onClickListener);

        Vid = findViewById(R.id.view_Fid);
        Vpw = findViewById(R.id.view_Fpw);



    }

    View.OnClickListener mclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_Fid:
                    Vid.setVisibility(v.VISIBLE);
                    Vpw.setVisibility(v.INVISIBLE);
                    break;
                case R.id.btn_Fpw:
                    Vid.setVisibility(v.INVISIBLE);
                    Vpw.setVisibility(v.VISIBLE);
                    break;


            }
        }
    };



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_FindId:
                    snameid = EnameId.getText().toString();
                    sphone = Ephone.getText().toString();

                    if (snameid.length() == 0) {

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle("이름을 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(sphone.length() ==0){

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle("연락처를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else{

                    Intent intent = getIntent();
                    urlAddr = "http://" + macIP + ":8080/test/findID.jsp?";
                    urlAddr = urlAddr + "name=" + snameid + "&phone=" + sphone;

                    //jsp를 실행해서 Json code를 String으로 받음
                    String ID = connectFindIDdata();

                    if(ID == null) {

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle(snameid + "님의 ID의 아이디를 찾을 수 없습니다.")
                                .setMessage("입력값을 다시 확인해주세요.")
                                .setPositiveButton("확인", null)
                                .show();

                    }else{

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle(snameid + "님의 ID는 [" + ID + "] 입니다.")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();
                        }

                    }

                        break;



                case R.id.btn_FindPw:
                    sid = Eid.getText().toString();
                    snamepw = EnamePw.getText().toString();
                    semail = Eemail.getText().toString();


                    if (sid.length() == 0) {

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle("아이디를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(snamepw.length() ==0){

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle("이름을 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if (semail.length() == 0){

                        new AlertDialog.Builder(FindActivity.this)
                                .setTitle("이메일을 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else {


                        Intent intent2 = getIntent();
                        urlAddr2 = "http://" + macIP + ":8080/test/findPw.jsp?";
                        urlAddr2 = urlAddr2 + "id=" + sid + "&name=" + snamepw + "&email=" + semail;

                        //jsp를 실행해서 Json code를 String으로 받음
                        String Pw = connectFindPWdata();

                        if (Pw == null) {

                            new AlertDialog.Builder(FindActivity.this)
                                    .setTitle(snamepw + "님의 패스워드를 찾을 수 없습니다.")
                                    .setMessage("입력값을 다시 확인해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show();


                        } else {

                            new AlertDialog.Builder(FindActivity.this)
                                    .setTitle(snamepw + "님의 패스워드는 [" + Pw + "] 입니다.")
                                    .setMessage("")
                                    .setPositiveButton("확인", null)
                                    .show();

                        }
                    }
                        break;
        }
    }
    };

    private String connectFindIDdata(){
        String result = null;
        Log.v(TAG, urlAddr);

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            //NetworkTask 생성자.
            FindNetworkTask findNetworkTask = new FindNetworkTask(FindActivity.this, urlAddr, "findId");
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = findNetworkTask.execute().get();
            result = (String) obj;
            Log.v(TAG," :"+result);
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    //----------

    private String connectFindPWdata(){
        String result2 = null;
        Log.v(TAG, urlAddr2);

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            //NetworkTask 생성자.
            FindNetworkTask findNetworkTask2 = new FindNetworkTask(FindActivity.this, urlAddr2, "findPw");
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj2 = findNetworkTask2.execute().get();
            result2 = (String) obj2;
            Log.v(TAG," :"+result2);
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result2;
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

}//---
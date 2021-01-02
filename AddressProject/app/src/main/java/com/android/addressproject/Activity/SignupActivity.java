package com.android.addressproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.addressproject.NetworkTask.NetworkTask_LogIn;
import com.android.addressproject.R;

public class SignupActivity extends AppCompatActivity { //이강후


    final static String TAG = "SignupActivity";
    String urlAddr = null;
    String urlAddr2 = null;

    String sid, spw, sname, sphone, semail;
    EditText Eid, Epw, Ename, Ephone, Eemail;
    Button btn_IdCheck, btn_SignUp;
    String macIP = ShareVar.macIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    /*    Intent intent = getIntent();
        urlAddr = "http://" + macIP + ":8080/test/idCheck.jsp?";
        */urlAddr2 = "http://" + macIP + ":8080/test/memberInsertReturn.jsp?";

        //activity_signup 텍스트필드 연결.
        Eid = findViewById(R.id.etS_id);
        Epw = findViewById(R.id.etS_pw);
        Ename = findViewById(R.id.etS_name);
        Ephone = findViewById(R.id.etS_phone);
        Eemail = findViewById(R.id.etS_email);

        // 입력시 자릿수 제한
        Eid.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Epw.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Ename.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Ephone.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Eemail.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(22)});

        //버튼 연결.
        btn_IdCheck = findViewById(R.id.btn_SidCheck);
        btn_SignUp = findViewById(R.id.btn_SignUp);

        btn_IdCheck.setOnClickListener(onClickListener); //IdCheck버튼 클릭시.
        btn_SignUp.setOnClickListener(onClickListener); //가입버튼 클릭시.
    }

    View.OnClickListener onClickListener = new View.OnClickListener() { //IdCheck버튼 클릭시.
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_SidCheck: //Id중복버튼 클릭.
                    sid = Eid.getText().toString();

                    Intent intent = getIntent();
                    urlAddr = "http://" + macIP + ":8080/test/idCheck.jsp?";
                    urlAddr = urlAddr +  "id=" + sid;

                    int count = connectIdCheckData();

                    if(count==1){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[이미 사용중인 ID입니다!!]")
                                .setMessage("사용할 수 없으니 다른 아이디를 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .show();

                      //  Toast.makeText(SignupActivity.this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();

                    }else if(count==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[사용가능한 ID입니다!!]")
                                .setMessage("계속 진행해주세요.")
                                .setPositiveButton("확인", null)
                                .show();

                     //   Toast.makeText(SignupActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btn_SignUp: //가입버튼 클릭.
                    sid = Eid.getText().toString();
                    spw = Epw.getText().toString();
                    sname = Ename.getText().toString();
                    sphone = Ephone.getText().toString();
                    semail = Eemail.getText().toString();

                    if(sid.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("아이디를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(spw.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("비밀번호를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(sname.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("이름을 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(sphone.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("연락처를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(semail.length()==0) {

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("이메일을 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else {

                        intent = getIntent();
                        urlAddr2 = "http://" + macIP + ":8080/test/memberInsertReturn.jsp?";
                        urlAddr2 = urlAddr2 + "id=" + sid + "&pw=" + spw + "&name=" + sname + "&phone=" + sphone + "&email=" + semail;

                        ///////////////////////////////////////////////////////////////////////////////////////
                        // Date : 2020.12.24
                        //
                        // Description:
                        //  - studentInsertReturn.jsp를 실행하여 Json Code 받은 값이 1인 경우에는 입력 성공 아니면 입력 실패
                        //
                        ///////////////////////////////////////////////////////////////////////////////////////
                        String count2 = connectInsertData();
                        Log.v(TAG, "count2 = " + count2);


                        if (count2.equals("1")) {

                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("[회원가입을 축하드립니다!!!]")
                                    .setMessage("로그인 페이지에서 로그인해주세요.")
                                    .setPositiveButton("이동", mClick)
                                    .show();
                            finish();

                            finish();
                        } else {

                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("[이미 사용중인 ID입니다!!]")
                                    .setMessage("사용할 수 없으니 다른 아이디를 입력해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show();

                            //  Toast.makeText(SignupActivity.this, "중복된 아이디입니. 다른 아이디를 사용하세요!!", Toast.LENGTH_SHORT).show();

                        }
                        ///////////////////////////////////////////////////////////////////////////////////////
                        break;
                    }
            }
        }
    };
    DialogInterface.OnClickListener mClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            Intent intentIP = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intentIP);

        }
    };


    private int connectIdCheckData(){
        int result = 0;

        try{

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            //NetworkTask 생성자.
            NetworkTask_LogIn networkTask_LogIn = new NetworkTask_LogIn(SignupActivity.this, urlAddr, "idcheck");
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask_LogIn.execute().get();
            result = (int) obj;
            Log.v(TAG,"connectIdCheckData :"+result);
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    private String connectInsertData(){
        String result2 = null;
        Log.v(TAG, urlAddr2);

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            //NetworkTask 생성자.
            NetworkTask_LogIn networkTask_LogIn2 = new NetworkTask_LogIn(SignupActivity.this, urlAddr2, "signup");
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj2 = networkTask_LogIn2.execute().get();
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

}//----
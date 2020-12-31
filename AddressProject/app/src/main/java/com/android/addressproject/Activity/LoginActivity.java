package com.android.addressproject.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.addressproject.NetworkTask.NetworkTask_LogIn;
import com.android.addressproject.R;

public class LoginActivity extends AppCompatActivity {

    //로그인 field - 이강후.

    final static String TAG = "LoginActivity";
    String urlAddr3 = null;
    String urlAddr4 = null;

    String sid, spw, sname, sphone, semail;
    EditText edName, edPhone, edEmail;


    String macIP = "192.168.219.104";

    //----------------------------------------------------------------------------------

    //세미
    Button btnLogin, btnSignup;
    EditText edId, edPw;
    TextView findIP;

    //20.12.29 세미 추가 ------------------------------------------------------------------
    CheckBox cb_atlogin;
    Context mContext;
    // ---------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findIP = findViewById(R.id.tv_findIP);
        btnLogin = findViewById(R.id.btn_Login);
        btnSignup = findViewById(R.id.btn_LSignUp);

        findIP.setOnClickListener(LonClickListener);
        btnLogin.setOnClickListener(LonClickListener);
        btnSignup.setOnClickListener(LonClickListener);


        // 20.12.29 세미 자동로그인 추가 ----------------------------------------------------------------

        mContext = this;

        // 연결
        edId = findViewById(R.id.edit_Lid);
        edPw = findViewById(R.id.edit_Lpw);
        cb_atlogin = findViewById(R.id.cb_atlogin);

        //입력시 자릿수 제한.
        edId.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        edPw.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});

        // 체크박스 클릭시
        cb_atlogin.setOnClickListener(cbClickListener);

        // 로그인 정보 기억하기 체크 유무 확인
        // 자동로그인 체크된 상태로 앱을 종료하고 다시 실행했을때, 체크된 상태로 종료했기 때문에 boo는 true가 나오고,
        // 저장되어있던 id, pw 키 값을 불러와 아이디, 암호 입력창에 셋팅
        boolean boo = PreferenceManager.getBoolean(mContext,"check");
        if(boo){ // 체크가 되어있다면 아래 코드를 수행
            // 저장된 아이디와 암호를 가져와 셋팅한다.
            edId.setText(PreferenceManager.getString(mContext,"id"));
            edPw.setText(PreferenceManager.getString(mContext, "pw"));
            cb_atlogin.setChecked(true);    // 체크박스는 여전히 체크표시 하도록 셋팅
        }

        // 끝 ---------------------------------------------------------------------------------------

    }

    View.OnClickListener LonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                    // id, pw 찾기 클릭 시
                case R.id.tv_findIP:
                    Intent intentIP = new Intent(LoginActivity.this, FindActivity.class);
                    startActivity(intentIP);
                    break;

                    // 로그인 버튼 클릭 시
                case R.id.btn_Login:

                    //-----------------------로그인 메인기능- 이강후--------------------------------------
                    
                    sid = edId.getText().toString();
                    spw = edPw.getText().toString();

                    if(sid.length()==0){

                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("아이디를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(spw.length()==0){

                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("패스워드를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else{
                            Intent intent = getIntent();
                            urlAddr3 = "http://" + macIP + ":8080/test/loginCheck.jsp?";
                            urlAddr3 = urlAddr3 + "id=" + sid + "&pw=" + spw;

        /*                    urlAddr4 = "http://" + macIP + ":8080/test/loginGetData.jsp?";
                            urlAddr4 = urlAddr4 + "id=" + sid + "&pw=" + spw;*/
                            //이 방식 대신 PreferenceManager.setString방식을 쓰기로 함.

                            int count = connectLoginCheckData();
                            Log.v(TAG,"count =" +count);

                            switch (count) {
                                case 0: //아이디와 패스워드 일치하는 정보 없음.

                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("[ID와 Password 불일치!!]")
                                            .setMessage("- ID와 Password가 맞는지 다시 확인해주세요. -")
                                            .setPositiveButton("확인", null)
                                            .show();
                                    break;

                                case 1: //아이디와 패스워드 일치.

                                    // 20.12.29 세미 자동로그인 추가 ------------------------------------

                                    // id, pw 입력창에서 텍스트를 가져와  PrefrerenceManager에 저장함
                                    PreferenceManager.setString(mContext, "id", edId.getText().toString()); //id라는 키값으로 저장
                                    PreferenceManager.setString(mContext, "pw", edPw.getText().toString()); // pw라는 키값으로 저장

                                    Intent intentLogIn = new Intent(LoginActivity.this, MainActivity.class);
                                    // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
                                    String checkId = PreferenceManager.getString(mContext, "id");
                                    String checkPw = PreferenceManager.getString(mContext, "pw");
                                    startActivity(intentLogIn);

                                    break;
                            }
                    }
                    //---------------------------------------------------------------------------------------

/*                    String checkId = null;
                    String checkPw = null;

                    // 아이디와 패스워가 비어있는 경우 체크, TextUtils는 안드로이드에서 제공하는 null체크 함수
                    if(TextUtils.isEmpty(checkId) || TextUtils.isEmpty(checkPw)){
                        // 아이디나 암호 둘 중 하나가 비어있으면 토스트메세지 띄움

                        if(TextUtils.isEmpty(checkId)){
                            Toast.makeText(LoginActivity.this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                            // 커서 위치 옮기기
                            edId.requestFocus();
                            edId.setCursorVisible(true);
                        }else {
                            // 커서 위치 옮기기
                            Toast.makeText(LoginActivity.this,"패스워드를 입력해주세요.",Toast.LENGTH_SHORT).show();
                            edPw.requestFocus();
                            edPw.setCursorVisible(true);
                        }
                        // 둘 다 충족하면 다음 동작을 구현
                    }else {
                        intent.putExtra("id",checkId);
                        intent.putExtra("pw",checkPw);
                        startActivity(intent);
                        finish();
                    }*/

                    // 끝 ---------------------------------------------------------------------------


//                    Intent intentL = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intentL);
                    //finish();
//                    break;

                    // 회원가입 클릭 시
/*                case R.id.btn_LSignUp:
                    Intent intentS = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intentS);
                    break;*/
            }
        }
    };

    private int connectLoginCheckData(){   //--------------이강후-------------------------------------
        int result = 0;

        try{

            /////////////////////////////////////////////////////////////////////////////////////
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where
            //
            ///////////////////////////////////////////////////////////////////////////////////////

            //NetworkTask 생성자.
            NetworkTask_LogIn networkTask_LogIn3 = new NetworkTask_LogIn(LoginActivity.this, urlAddr3, "loginCheck");
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask_LogIn3.execute().get();
            result = (int) obj;
            Log.v(TAG,"connectLoginCheckData :"+result);
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }
    //----------------------------------------------------------------------------------------------

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


    // 20.12.29 세미 추가 ----------------------------------------------------------------------------

    View.OnClickListener cbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 로그인 기억하기 체크박스 유무에 따른 동작 구현
            if (((CheckBox)v).isChecked()){ //체크박스 체크 되어 있으면
                //editText에서 아이디와 암호 가져와 PreferenceManager에 저장
                PreferenceManager.setString(mContext,"id",edId.getText().toString());  // id 키값으로 저장
                PreferenceManager.setString(mContext,"pw",edPw.getText().toString());  // pw 키값으로 저장
                PreferenceManager.setBoolean(mContext,"check",cb_atlogin.isChecked());      // 현재 체크박스 상태 값 저장
            }else { // 체크박스가 해제되어있으면
                PreferenceManager.setBoolean(mContext,"check", cb_atlogin.isChecked());     // 현재 체크박스 상태 값 저장
                PreferenceManager.clear(mContext);      // 로그인 정보를 모두 날림
            }

        }
    };

    // 끝 -------------------------------------------------------------------------------------------





}//-------------------
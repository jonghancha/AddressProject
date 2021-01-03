package com.android.addressproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.StrictMode;
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

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SignupActivity extends AppCompatActivity {


         //이강후


    final static String TAG = "SignupActivity";
    String urlAddr = null;
    String urlAddr2 = null;
    int count = 0; //가입확인용.//@@@@@@@
    int count3; //인증확인용.//@@@@@@@

    String sid, spw, sname, sphone, semail;
    EditText Eid, Epw, Ename, Ephone, Eemail;
    EditText Eauth; //@@@@@@@@ 인증번호 입력
    Button btn_IdCheck, btn_SignUp;
    Button btn_SendEmail; //@@@@@인증메일보내기 버튼.
    Button btn_ChkAuth; //@@@@@@인증 버튼.

    String macIP = ShareVar.macIP;
    String emailCode = createEmailCode(); //생성된 랜덤 이메일 코드 저장.

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
        Eemail = findViewById(R.id.etS_email); //@@@@@@ 받는 사람의 이메일
        Eauth = findViewById(R.id.etS_Auth); //@@@@@@@@ 인증번호 입력.

        // 입력시 자릿수 제한
        Eid.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Epw.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Ename.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Ephone.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15)});
        Eemail.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(22)});
        Eauth.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(8)}); //인증번호 8자리로 제한둠.


        //버튼 연결.
        btn_IdCheck = findViewById(R.id.btn_SidCheck);
        btn_SignUp = findViewById(R.id.btn_SignUp);
        btn_SendEmail = findViewById(R.id.btn_SendEmail);
        btn_ChkAuth = findViewById(R.id.btn_ChkAuth);

        //ClickListener.
        btn_IdCheck.setOnClickListener(onClickListener); //IdCheck버튼 클릭시.
        btn_SignUp.setOnClickListener(onClickListener); //가입버튼 클릭시.

        btn_SendEmail.setOnClickListener(mailClickListener); //@@@@@ 메일로 인증번호 보내개 버튼.
        btn_ChkAuth.setOnClickListener(mailClickListener); //@@@@@@@ 인증버튼.

        //--------------------------------------------------------------------
        //onCreat에 인터넷 사용을 위한 권한을 허용해주는것.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        //여기까지 이메일 사용을 위한 준비 끝남.---------------------------------------

    }


 //---이메일 인증관련   --------------------------------------------------------------------------------------------------------------------------

    View.OnClickListener mailClickListener = new View.OnClickListener() {
     @Override
     public void onClick(View v) {

         switch(v.getId()) {

             // 이메일로 인증번호 전송 버튼 클릭.
             case R.id.btn_SendEmail:

                 try {
                     GMailSender gMailSender = new GMailSender("imkanghoo@gmail.com", "magneto1203");
                     //GMailSender.sendMail(제목, 본문내용, 받는사람);
                     gMailSender.sendMail("인증번호입니다.", "인증번호 : " + emailCode, Eemail.getText().toString()); //@@@@@@@@@@@@
                     Log.v(TAG, "emailCode(send) :" + emailCode);

                     new AlertDialog.Builder(SignupActivity.this)
                             .setTitle("[이메일로 인증번호를 보냈습니다!!]")
                             .setMessage("인증번호를 확인하고 인증해주세요.")
                             .setPositiveButton("확인", null)
                             .show();

                     // Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();

                 } catch (SendFailedException e) {

                     new AlertDialog.Builder(SignupActivity.this)
                             .setTitle("[이메일 형식이 잘못되었습니다!!]")
                             .setMessage("이메일 형식을 확인하시고 다시 입력해주세요.")
                             .setPositiveButton("확인", null)
                             .show();

                     //Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();

                 } catch (MessagingException e) {

                     new AlertDialog.Builder(SignupActivity.this)
                             .setTitle("[인터넷 연결을 확인해주세요!!]")
                             .setMessage("")
                             .setPositiveButton("확인", null)
                             .show();

                     //  Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();

                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                break;


             //인증버튼 클릭---------------------------------------------------------------------------

             case R.id.btn_ChkAuth:

                 Eauth = findViewById(R.id.etS_Auth);
                 String etAuchResult = Eauth.getText().toString();


                 if(emailCode.equals(etAuchResult)){
                     Log.v(TAG, "emailCode(Auth) :" + emailCode);
                     Log.v(TAG, "euAuth(Auth) :" + Eauth);

                     count3 = 1; //@@@@@ 인증확인용(1인면 인증됨)
                     Log.v(TAG, "Auth1 :" + count3);


                     new AlertDialog.Builder(SignupActivity.this)
                             .setTitle("[인증되었습니다!!]")
                             .setMessage("")
                             .setPositiveButton("확인", null)
                             .show();

                     break;

                 }else{

                     Log.v(TAG, "emailCode(Autherror) :" + emailCode);
                     Log.v(TAG, "euAuth(Autherror) :" + Eauth);

                     count3 = 0; //@@@@ 인증확인용(0이면 인증안됨)
                     Log.v(TAG, "Auth2 :" + count3);

                     new AlertDialog.Builder(SignupActivity.this)
                             .setTitle("[인증번호 불일치!!]")
                             .setMessage("이메일로 받은 인증번호를 다시 확인하고, 재입력해주세요.")
                             .setPositiveButton("확인", null)
                             .show();

                 }
                 break;
         }
     }};

 //---생성된 이메일 랜덤 인증코드 반환.---------------------------------------------------------------------

    public String getEmailCode() {
        return emailCode;
    } //생성된 이메일 인증코드 반환

 //--- 이메인 랜덤 인증코드 생성.-------------------------------------------------------------------------

    private String createEmailCode() { //이메일 랜덤 인증코드 생성
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for (int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }

        return newCode;
    }


//---- 회원가입 및 아이디 중복 체크 -------------------------------------------------------------------------------------------------------------------

    View.OnClickListener onClickListener = new View.OnClickListener() { //IdCheck버튼 클릭시.
        @Override
        public void onClick(View v) {

            switch (v.getId()){


                //Id중복버튼 클릭.----------------------------------------------------------------------

                case R.id.btn_SidCheck: //Id중복버튼 클릭.
                    sid = Eid.getText().toString();

                    if(sid.length()==0) {

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("아이디를 입력해주세요!!")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();
                    }else {

                        Intent intent = getIntent();
                        urlAddr = "http://" + macIP + ":8080/test/idCheck.jsp?";
                        urlAddr = urlAddr + "id=" + sid;

                        int count = connectIdCheckData();

                        if (count == 1) {

                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("[이미 사용중인 ID입니다!!]")
                                    .setMessage("사용할 수 없으니 다른 아이디를 입력해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show();

                            //  Toast.makeText(SignupActivity.this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();

                        } else if (count == 0) {

                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("[사용가능한 ID입니다!!]")
                                    .setMessage("계속 진행해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show();

                            //   Toast.makeText(SignupActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                        break;


                //회원가입 버튼 클릭 ------------------------------------------------------------------------------

                case R.id.btn_SignUp: //가입버튼 클릭.

                    sid = Eid.getText().toString();
                    spw = Epw.getText().toString();
                    sname = Ename.getText().toString();
                    sphone = Ephone.getText().toString();
                    semail = Eemail.getText().toString();

                    if(sid.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[아이디를 입력해주세요!!]")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(spw.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[비밀번호를 입력해주세요!!]")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(sname.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[이름을 입력해주세요!!]")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(sphone.length()==0){

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[연락처를 입력해주세요!!]")
                                .setMessage("")
                                .setPositiveButton("확인", null)
                                .show();

                    }else if(semail.length()==0) {

                        new AlertDialog.Builder(SignupActivity.this)
                                .setTitle("[이메일을 입력해주세요!!]")
                                .setMessage("이메일 인증도 필수입니다.")
                                .setPositiveButton("확인", null)
                                .show();

                    }else {

                        Log.v(TAG, "countAuth3 : "+ count3);

                        if(count3 == 0){

                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("이메일 인증을 해주세요!!")
                                    .setMessage("")
                                    .setPositiveButton("확인", null)
                                    .show();


                        }else if (count3 == 1) {

                        Intent intent = getIntent();
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
                        //      finish();

                        } else {

                            new AlertDialog.Builder(SignupActivity.this)
                                    .setTitle("[이미 사용중인 ID입니다!!]")
                                    .setMessage("사용할 수 없으니 다른 아이디를 입력해주세요.")
                                    .setPositiveButton("확인", null)
                                    .show();
                         //   finish();

                            //  Toast.makeText(SignupActivity.this, "중복된 아이디입니. 다른 아이디를 사용하세요!!", Toast.LENGTH_SHORT).show();

                        }
                        }
                    }
                     //   break;
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
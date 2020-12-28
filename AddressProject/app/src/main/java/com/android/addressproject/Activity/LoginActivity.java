package com.android.addressproject.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.addressproject.R;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnSignup;
    EditText edId, edPw;
    TextView findIP;

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
                    Intent intentL = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intentL);
                    finish();
                    break;

                    // 회원가입 클릭 시
                case R.id.btn_LSignUp:
                    Intent intentS = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intentS);
                    break;
            }
        }
    };




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
}
package com.android.addressproject.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.android.addressproject.Bean.InsertData;
import com.android.addressproject.NetworkTask.InsertImageNetworkTask;
import com.android.addressproject.NetworkTask.ViewImageNetworkTask;
import com.android.addressproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {


    private static final int SETTINGS_CODE = 234;
    SharedPreferences sharedPreferences;

    final static String TAG = "UpdateActivity";

    TextView modifyViewPhone;
    EditText modifyName, modifyPhone, modifyEmail, modifyText, modifyBirth;
    Spinner modifyAddressGroup;
    ImageView modifyImg;
    String modifyNo;
    Button modifyImgBtn;
    ImageButton modifyCancel;
    ImageButton modifyAction;
    Intent intent;

    ArrayAdapter<CharSequence> adapter = null;

    private final int REQ_CODE_SELECT_IMAGE = 300; // Gallery Return Code
    private String img_path = new String(); // 최종 file name
    private String f_ext = null;    // 최종 file extension
    File tempSelectFile;

    String user_userId;
    String devicePath = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.addressproject/";
    InsertData updateData;
    String urladdr = "http://" + ShareVar.macIP + ":8080/test/updateMultipart.jsp";
    String url;

    String modifyBfImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("수정화면");
        setThemeOfApp();

        setContentView(R.layout.activity_update);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        modifyName = findViewById(R.id.modify_name);
        modifyPhone = findViewById(R.id.modify_phone);
        modifyViewPhone = findViewById(R.id.modify_view_phone);
        modifyAddressGroup = findViewById(R.id.modify_groupname); // spinner
        modifyEmail = findViewById(R.id.modify_email);
        modifyText = findViewById(R.id.modify_text);
        modifyBirth = findViewById(R.id.modify_birth);
        modifyImg = findViewById(R.id.modify_image_view);

        modifyImgBtn = findViewById(R.id.modify_image);
        modifyCancel = findViewById(R.id.modify_cancel_btn);
        modifyAction = findViewById(R.id.modify_action_btn);

        intent = getIntent();

        modifyName.setText(intent.getStringExtra("name"));
        modifyPhone.setText(intent.getStringExtra("phone"));
        modifyViewPhone.setText(intent.getStringExtra("phone"));


        modifyPhone.addTextChangedListener(textChangedListener);


        adapter = ArrayAdapter.createFromResource(this, R.array.groupname, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modifyAddressGroup.setAdapter(adapter);

        modifyAddressGroup.setSelection(getIndex(modifyAddressGroup, intent.getStringExtra("group")));
        Log.v(TAG, intent.getStringExtra("group"));
        Log.v(TAG, String.valueOf(getIndex(modifyAddressGroup, intent.getStringExtra("group"))));


        modifyEmail.setText(intent.getStringExtra("email"));
        modifyText.setText(intent.getStringExtra("text"));
        modifyBirth.setText(intent.getStringExtra("birth"));

        modifyNo = intent.getStringExtra("no");

        modifyBfImg = intent.getStringExtra("img");
        url = "http://" + ShareVar.macIP + ":8080/test/";
        url = url + intent.getStringExtra("img");
        Log.v("AddressAdapter", "urlAddr = " + url);
        ViewImageNetworkTask networkTask = new ViewImageNetworkTask(UpdateActivity.this, url, modifyImg);
        networkTask.execute(100); // 10초. 이것만 쓰면 pre post do back 등 알아서 실행


        modifyImgBtn.setOnClickListener(mClickListener);
        modifyCancel.setOnClickListener(mClickListener);
        modifyAction.setOnClickListener(mClickListener);


        modifyName.requestFocus();
        modifyName.setCursorVisible(true);

    }//-- onCreate 끝

    TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            modifyViewPhone.setText(modifyPhone.getText());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.modify_image:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
                    break;
                case R.id.modify_cancel_btn:
                    onBackPressed();
                    break;
                case R.id.modify_action_btn:
                    // 이름과 패스워가 비어있는 경우 체크, TextUtils는 안드로이드에서 제공하는 null체크 함수
                    if(TextUtils.isEmpty(modifyName.getText()) || TextUtils.isEmpty(modifyPhone.getText())){
                        // 아이디나 암호 둘 중 하나가 비어있으면 토스트메세지 띄움

                        if(TextUtils.isEmpty(modifyName.getText())){
                            Toast.makeText(UpdateActivity.this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                            // 커서 위치 옮기기
                            modifyName.requestFocus();
                            modifyName.setCursorVisible(true);
                        }else {
                            // 커서 위치 옮기기
                            Toast.makeText(UpdateActivity.this,"전화번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                            modifyPhone.requestFocus();
                            modifyPhone.setCursorVisible(true);
                        }
                        // 둘 다 충족하면 다음 동작을 구현
                    }else {


                        if (img_path.length() == 0){
                            img_path = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.addressproject/files/" + modifyBfImg;
                        }

                        // 등록할 정보 담기
                        makeData();




                        InsertImageNetworkTask networkTask = new InsertImageNetworkTask(UpdateActivity.this, modifyImg, updateData, urladdr);
                        //////////////////////////////////////////////////////////////////////////////////////////////
                        //
                        //              NetworkTask Class의 doInBackground Method의 결과값을 가져온다.
                        //
                        //////////////////////////////////////////////////////////////////////////////////////////////
                        try {
                            Integer result = networkTask.execute(100).get();
                            //////////////////////////////////////////////////////////////////////////////////////////////
                            //
                            //              doInBackground의 결과값으로 Toast생성
                            //
                            //////////////////////////////////////////////////////////////////////////////////////////////
                            switch (result){
                                case 1:
                                    Toast.makeText(UpdateActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                                    //////////////////////////////////////////////////////////////////////////////////////////////
                                    //
                                    //              Device에 생성한 임시 파일 삭제 (기본사진이 아닌 경우만)
                                    //
                                    //////////////////////////////////////////////////////////////////////////////////////////////
                                    if (img_path.contains("baseline_account_circle_black_48")){

                                    }else {
                                        File file = new File(img_path);
                                        file.delete();
                                    }
                                    new AlertDialog.Builder(UpdateActivity.this)
                                            .setTitle("연락처 수정")
                                            .setMessage("수정에 성공하셨습니다")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }


                                            }).show();

                                    break;
                                case 0:
                                    Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            //////////////////////////////////////////////////////////////////////////////////////////////
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;



                    }
            }
        }
    };



    private void makeData() {

        String addressNo = modifyNo;
        String addressName = String.valueOf(modifyName.getText());
        String addressPhone = String.valueOf(modifyPhone.getText());
        String addressGroup = modifyAddressGroup.getSelectedItem().toString();
        String addressEmail = String.valueOf(modifyEmail.getText());
        String addressText = String.valueOf(modifyText.getText());
        String addressBirth = String.valueOf(modifyBirth.getText());

        user_userId = com.android.addressproject.Activity.PreferenceManager.getString(UpdateActivity.this,"id"); // 로그인한 id 받아오기
        Log.v(TAG, addressNo);
        Log.v(TAG, img_path);
        Log.v(TAG, user_userId);
        Log.v(TAG, addressName);
        Log.v(TAG, addressPhone);
        Log.v(TAG, addressGroup);
        Log.v(TAG, addressEmail);
        Log.v(TAG, addressText);
        Log.v(TAG, addressBirth);

        updateData = new InsertData(addressNo, img_path, user_userId, addressName, addressPhone, addressGroup, addressEmail, addressText, addressBirth);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //                   Photo App.에서 Image 선택후 작업내용
    //
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.v(TAG, "Data :" + String.valueOf(data));

        if (requestCode == REQ_CODE_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                //이미지의 URI를 얻어 경로값으로 반환.
                img_path = getImagePathToUri(data.getData());
                Log.v(TAG, "image path :" + img_path);
                Log.v(TAG, "Data :" +String.valueOf(data.getData()));

                //이미지를 비트맵형식으로 반환
                Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                RoundedBitmapDrawable roundBitmap = RoundedBitmapDrawableFactory.create(getResources(), image_bitmap_copy);
                roundBitmap.setCircular(true);
                modifyImg.setImageDrawable(roundBitmap);


                // 파일 이름 및 경로 바꾸기(임시 저장, 경로는 임의로 지정 가능)
                String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                String imageName = date + "." + f_ext;
                tempSelectFile = new File(devicePath , imageName);
                OutputStream out = new FileOutputStream(tempSelectFile);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                // 임시 파일 경로로 위의 img_path 재정의
                img_path = devicePath + imageName;
                Log.v(TAG,"fileName :" + img_path);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePathToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.v(TAG, "Image Path :" + imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());

        return imgPath;
    }


    // 액션바 ( 옵션메뉴)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SETTINGS_CODE){
//            this.recreate();
//        }
//    }

    // 액션바 ( 옵션메뉴)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    //뒤로가기 (액션바)
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
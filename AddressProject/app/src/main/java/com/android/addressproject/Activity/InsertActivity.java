package com.android.addressproject.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.TextUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.android.addressproject.Bean.InsertData;
import com.android.addressproject.NetworkTask.InsertImageNetworkTask;
import com.android.addressproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InsertActivity extends AppCompatActivity {

    // 20.12.30 종한 추가
    final  static String TAG = "InsertActivity";

    ImageView ivInsertImage = null;
    Button btnInsert = null;
    Button btnInsertCancel = null;
    private final int REQ_CODE_SELECT_IMAGE = 100;
    private String img_path = new String();
    private Bitmap image_bitmap_copy = null;
    private Bitmap image_bitmap = null;
    private String imageName = null;
    private String f_ext = null;
    File tempSelectFile;
    private String user_userId;
    EditText insertAddressName, insertAddressPhone, insertAddressEmail, insertAddressText, insertAddressBirth;
    Spinner insertAddressGroup;

    String result;
    // 20.12.29 세미 추가, 20.12.30 세미 수정 ------------------------------

    // 20.12.29 세미 추가 ------------------------------

    ArrayAdapter<CharSequence> adapter = null;
    Spinner spinner = null;
    EditText insert_birth;
    Calendar birthCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener birthDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, month);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            birthupdate();
        }
    };


    // 끝 --------------------------------------------------------------


    private static final int SETTINGS_CODE = 234;
    SharedPreferences sharedPreferences;

    InsertData insertData;
    String url = "http://" + ShareVar.macIP + ":8080/test/insertMultipart.jsp"; // 본인 아이피 주소 써야합니다. localhost or 127.0.0.1 은 안먹음

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("입력화면");
        setThemeOfApp();

        setContentView(R.layout.activity_insert);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // 20.12.29 세미 추가, 20.12.30 세미 수정 ------------------------------

        adapter = ArrayAdapter.createFromResource(this, R.array.groupname, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = findViewById(R.id.insert_groupname);
        spinner.setAdapter(adapter);

        // 생일
        insert_birth = findViewById(R.id.insert_birth);
        insert_birth.setOnClickListener(birthClickListener);
        insert_birth.setTextIsSelectable(true); // 커서는 살아있음
        insert_birth.setShowSoftInputOnFocus(false); // 키보드는 숨겨짐


        // 끝 -------------------------------------------------------------

        // 20.12.30 종한 추가
        user_userId = com.android.addressproject.Activity.PreferenceManager.getString(InsertActivity.this,"id"); // 로그인한 id 받아오기
        insertAddressName = findViewById(R.id.insert_name);
        insertAddressPhone = findViewById(R.id.insert_phone);
        insertAddressGroup = findViewById(R.id.insert_groupname);
        insertAddressEmail = findViewById(R.id.insert_email);
        insertAddressText = findViewById(R.id.insert_text);
        insertAddressBirth = findViewById(R.id.insert_birth);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        ivInsertImage =  findViewById(R.id.insert_image);
        //이미지를 띄울 위젯
        ivInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        btnInsert = findViewById(R.id.btn_insert);
        btnInsertCancel = findViewById(R.id.btn_insert_cancel);
        btnInsertCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //데이터 입력 버튼
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 이름과 패스워가 비어있는 경우 체크, TextUtils는 안드로이드에서 제공하는 null체크 함수
                if(TextUtils.isEmpty(insertAddressName.getText()) || TextUtils.isEmpty(insertAddressPhone.getText())){
                    // 아이디나 암호 둘 중 하나가 비어있으면 토스트메세지 띄움

                    if(TextUtils.isEmpty(insertAddressName.getText())){
                        Toast.makeText(InsertActivity.this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                        // 커서 위치 옮기기
                        insertAddressName.requestFocus();
                        insertAddressName.setCursorVisible(true);
                    }else {
                        // 커서 위치 옮기기
                        Toast.makeText(InsertActivity.this,"전화번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        insertAddressPhone.requestFocus();
                        insertAddressPhone.setCursorVisible(true);
                    }
                    // 둘 다 충족하면 다음 동작을 구현
                }else {

                    Log.v(TAG, "======================================================================" + img_path.length());
                    // 업로드할 이미지를 지정해주기
                    makeImgPath();
                    Log.v(TAG, "======================================================================" + img_path.length());

                    // 등록할 정보 담기
                    makeData();

                    InsertImageNetworkTask networkTask = new InsertImageNetworkTask(InsertActivity.this, ivInsertImage, insertData, url);
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
                                Toast.makeText(InsertActivity.this, "Success!", Toast.LENGTH_SHORT).show();

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
                                new AlertDialog.Builder(InsertActivity.this)
                                        .setTitle("연락처 등록")
                                        .setMessage("등록에 성공하셨습니다")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }


                                        }).show();

                                break;
                            case 0:
                                Toast.makeText(InsertActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        //////////////////////////////////////////////////////////////////////////////////////////////
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }



            }
        });

    }//--onCreate 끝












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

    // onActivityResult 종한.이 가지고있던거 사용 함
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SETTINGS_CODE){
//            this.recreate();
//        }
//    }

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

    // 20.12.30 종한 추가
    // 갤러리에서 선택한 사진 경로 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show();
                    Log.v("test", String.valueOf(data.getData()));
                    //이미지를 비트맵형식으로 반환
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //사용자 단말기의 width , height 값 반환
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    //image_bitmap 으로 받아온 이미지의 사이즈를 임의적으로 조절함. width: 400 , height: 300
                    image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true);
                    ImageView image = findViewById(R.id.insert_image);  //이미지를 띄울 위젯 ID값
                    RoundedBitmapDrawable roundBitmap = RoundedBitmapDrawableFactory.create(getResources(), image_bitmap_copy);
                    roundBitmap.setCircular(true);
                    image.setImageDrawable(roundBitmap);


                    // 파일 이름 및 경로 바꾸기(임시 저장)
                    String date = new SimpleDateFormat("yyyyMMddHmsS").format(new Date());
                    imageName = date+"."+f_ext;
                    tempSelectFile = new File("/data/data/com.android.addressproject/", imageName);
                    OutputStream out = new FileOutputStream(tempSelectFile);
                    image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    // 임시 파일 경로로 위의 img_path 재정의
                    img_path = "/data/data/com.android.addressproject/"+imageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }//end of onActivityResult()

    public String getImagePathToUri(Uri data) {
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);

        //이미지의 이름 값
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

        // 확장자 명 저장
        f_ext = imgPath.substring(imgPath.length()-3, imgPath.length());
        Toast.makeText(InsertActivity.this, "이미지 이름 : " + imgName, Toast.LENGTH_SHORT).show();
        this.imageName = imgName;

        return imgPath;
    }//end of getImagePathToUri()



    //파일 변환
    private void makeImgPath() {

        Log.v(TAG, "imgPath의 길이는" +String.valueOf(img_path.length()));
        if (img_path.length() == 0){ // 사용자가 갤러리에서 아무 사진도 선택하지 않았을 경우

            // 기본 프로필 사진의 존재 여부
            String dataDirectory = Environment.getDataDirectory().getAbsolutePath() + "/data/com.android.addressproject/files/";
            if (new File(dataDirectory + "baseline_account_circle_black_48.png").exists()){
                Log.v(TAG, "BasePhoto exists");
                img_path = dataDirectory + "baseline_account_circle_black_48.png";
            // 기본 프로필 사진이 존재하지 않는 경우 mipmap리소스를 받아 사진파일을 생성한다
            }else{

                // BitmapFactory class
                Bitmap bm = BitmapFactory.decodeResource( getResources(), R.mipmap.baseline_account_circle_black_48);
                File f;
                f = new File(dataDirectory, "baseline_account_circle_black_48.png");
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                img_path = dataDirectory + "baseline_account_circle_black_48.png";
            }


        }else{ // 사용자가 갤러리에서 사진을 선택한 경우
            //
        }


    }

    private void makeData(){


        String addressName = String.valueOf(insertAddressName.getText());
        String addressPhone = String.valueOf(insertAddressPhone.getText());
        String addressGroup = insertAddressGroup.getSelectedItem().toString();
        String addressEmail = String.valueOf(insertAddressEmail.getText());
        String addressText = String.valueOf(insertAddressText.getText());
        String addressBirth = String.valueOf(insertAddressBirth.getText());



        insertData = new InsertData("",img_path, user_userId, addressName, addressPhone, addressGroup, addressEmail, addressText, addressBirth);


    }




    // 20.12.30 세미 추가 ------------------------------
    View.OnClickListener birthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(InsertActivity.this, birthDatePicker, birthCalendar.get(Calendar.YEAR),  birthCalendar.get(Calendar.MONTH), birthCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private void birthupdate(){
        String myFormat = "yyyy/MM/dd"; //출력형식 2020/12/30
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText etbirth = findViewById(R.id.insert_birth);
        etbirth.setText(sdf.format(birthCalendar.getTime()));
    }



    // 끝 --------------------------------------------




}//-- 전체 끝
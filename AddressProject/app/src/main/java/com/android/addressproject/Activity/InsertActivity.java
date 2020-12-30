package com.android.addressproject.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.addressproject.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
        //이미지 전송 버튼
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 네트워크 연결 (Thread = asynctask)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doMultiPartRequest();
                    }
                }).start();
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
                    image.setImageBitmap(image_bitmap_copy);

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
    private void doMultiPartRequest() {
        Log.v(TAG, "이미지패스 는 !" + img_path);
        img_path = "";
        File f;
        if (img_path == ""){
            f = new File(String.valueOf(R.drawable.ic_baseline_group_24));
        }else{
            f = new File(img_path);
        }

        Log.v(TAG, "사진 이름은" + f.getName());
            DoActualRequest(f);

    }

    //서버 보내기
    private void DoActualRequest(File file) {
        OkHttpClient client = new OkHttpClient();
        Log.v(TAG,"Called actual request");

        String url = "http://192.168.43.220:8080/test/insertMultipart.jsp"; // 본인 아이피 주소 써야합니다. localhost or 127.0.0.1 은 안먹음


        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file))
                .addFormDataPart("user_userId", user_userId)
                .addFormDataPart("addressName", String.valueOf(insertAddressName.getText()))
                .addFormDataPart("addressPhone", String.valueOf(insertAddressPhone.getText()))
                .addFormDataPart("addressGroup", insertAddressGroup.getSelectedItem().toString())
                .addFormDataPart("addressEmail", String.valueOf(insertAddressEmail.getText()))
                .addFormDataPart("addressText", String.valueOf(insertAddressText.getText()))
                .addFormDataPart("addressBirth", String.valueOf(insertAddressBirth.getText()))
                .build();
        Log.v(TAG,"Request body generated");
        Log.v(TAG, "user_userId : " + user_userId);
        Log.v(TAG, "addressName : " + String.valueOf(insertAddressName.getText()));
        Log.v(TAG, "addressPhone : " + String.valueOf(insertAddressPhone.getText()));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.v(TAG,"Request successful");
            Log.v(TAG,response.body().string());
        } catch (IOException e) {
            Log.v(TAG,"Exception occured :" + e.toString());
            e.printStackTrace();
        }
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
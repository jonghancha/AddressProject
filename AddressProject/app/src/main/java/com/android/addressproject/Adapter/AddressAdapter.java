package com.android.addressproject.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.addressproject.Activity.Frmt_fav;
import com.android.addressproject.Activity.MainActivity;
import com.android.addressproject.Activity.MainViewActivity;
import com.android.addressproject.Activity.Memdel;
import com.android.addressproject.Activity.PreferenceManager;
import com.android.addressproject.Activity.ShareVar;
import com.android.addressproject.Bean.Address;
import com.android.addressproject.NetworkTask.CUDNetworkTask;
import com.android.addressproject.NetworkTask.ViewImageNetworkTask;
import com.android.addressproject.R;

import java.util.ArrayList;

// 20.12.29 지은 추가
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    Context mContext = null;
    int layout = 0;
    ArrayList<Address> data = null;
    LayoutInflater inflater = null;
    String urlAddr;

    public AddressAdapter(Context mContext, int layout, ArrayList<Address> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // 프로필 사진에 아무것도 설정 안해놨을 경우. 그냥 mipmap 사진 갖다 씀
        if (data.get(position).getAddressImage().contains("baseline_account_circle_black_48")){
            holder.img.setImageResource(R.mipmap.baseline_account_circle_black_48);
        }else{ // 사진을 설정해 놓은 경우. network 통해 다운받아서 보여줌. 이때 이미 폰에 존재하면 중복생성X

        urlAddr = "http://" + ShareVar.macIP + ":8080/test/";
        urlAddr = urlAddr + data.get(position).getAddressImage();
        Log.v("AddressAdapter", "urlAddr = " + urlAddr);

            String URL = urlAddr;
            String html = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "\n<html><body><img src=\"" + URL + "\"/></body></html>";
//            String style = "<style>img{display: inline;height: auto;max-width: 100%;}</style>";
//            String html = "<html><body><img src=\"" + URL + "\"/></body></html>";

//            String html = "<html><body><img src=\"" + URL + "\"width=\"100%\"height=\"100%\"/></body></html>";
//            holder.webView.loadData(html, "text/html", null);
            holder.webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null );

//        ViewImageNetworkTask networkTask = new ViewImageNetworkTask(mContext, urlAddr, holder.img);
//        networkTask.execute(100); // 10초. 이것만 쓰면 pre post do back 등 알아서 실행

//            holder.webView.loadUrl(urlAddr);
        }


        holder.name.setText(data.get(position).getAddressName());
        holder.phone_num.setText(data.get(position).getAddressPhone());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainViewActivity.class);
                intent.putExtra("name", data.get(position).getAddressName());
                intent.putExtra("phone", data.get(position).getAddressPhone());
                intent.putExtra("group", data.get(position).getAddressGroup());
                intent.putExtra("email", data.get(position).getAddressEmail());
                intent.putExtra("text", data.get(position).getAddressText());
                intent.putExtra("birth", data.get(position).getAddressBirth());
                intent.putExtra("img", data.get(position).getAddressImage());
                intent.putExtra("star", data.get(position).getAddressStar());

                // 20.12.30 세미 추가 -------------
                intent.putExtra("no", data.get(position).getAddressNo());

                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "상세보기 페이지 이동", Toast.LENGTH_SHORT).show();

            }
        });

        // 21.01.02 세미 즐겨찾기 해제 추가 ---------------------

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int add = data.get(position).getAddressNo();
                new AlertDialog.Builder(v.getContext())
                        .setTitle("즐겨찾기를 해제하시겠습니까?" + add)
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int addno = data.get(position).getAddressNo();
                                urlAddr = "http://" + ShareVar.macIP + ":8080/test/starDel.jsp?addno="+addno;
                                //connectUpdateData();
                            }
                        })
                        .show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
//        Log.v("AddressAdapter", "data.size = " + String.valueOf(data.size()));
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phone_num;
        ImageView img;
        WebView webView;

        public MyViewHolder(View itemView) {
            super(itemView);


//            img = itemView.findViewById(R.id.img_view);
            name = itemView.findViewById(R.id.name_contact);
            phone_num = itemView.findViewById(R.id.ph_number);
            webView = itemView.findViewById(R.id.web_view);

//             Initial webview
//            webView.setWebViewClient(new WebViewClient());
//
//            // Enable JavaScript
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//
//            // WebView 세팅
            WebSettings webSettings = webView.getSettings();
            webSettings.setUseWideViewPort(true);       // wide viewport를 사용하도록 설정
            webSettings.setLoadWithOverviewMode(true);  // 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
            //iv_viewPeople.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

//            webView.setBackgroundColor(0); //배경색
//            webView.setHorizontalScrollBarEnabled(false); //가로 스크롤
//            webView.setVerticalScrollBarEnabled(false);   //세로 스크롤
//            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 스크롤 노출 타입
//            webView.setScrollbarFadingEnabled(false);
            webView.setInitialScale(1);

            // 웹뷰 멀티 터치 가능하게 (줌기능)
//            webSettings.setBuiltInZoomControls(false);   // 줌 아이콘 사용
//            webSettings.setSupportZoom(false);
        }
    }

    // 21.01.03 즐겨찾기 삭제 세미 -------------------------------------------------

//    public void connectUpdateData(){
//        try{
//            CUDNetworkTask updateworkTask = new CUDNetworkTask(getContext(), urlAddr);
//            updateworkTask.execute().get();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        //finish();
   // }


    // 끝 ------------------------------------------------------------------


}

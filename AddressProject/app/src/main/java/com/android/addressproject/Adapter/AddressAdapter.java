package com.android.addressproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.addressproject.Activity.MainActivity;
import com.android.addressproject.Activity.MainViewActivity;
import com.android.addressproject.Activity.ShareVar;
import com.android.addressproject.Bean.Address;
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

        //
        // img 불러와야 함..
        //
        urlAddr = "http://" + ShareVar.macIP + ":8080/test/";

        urlAddr = urlAddr + data.get(position).getAddressImage();
        Log.v("AddressAdapter", "urlAddr = " + urlAddr);
        ViewImageNetworkTask networkTask = new ViewImageNetworkTask(mContext, urlAddr, holder.img);
        networkTask.execute(100); // 10초. 이것만 쓰면 pre post do back 등 알아서 실행
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

        public MyViewHolder(View itemView) {
            super(itemView);


            img = itemView.findViewById(R.id.img_view);
            name = itemView.findViewById(R.id.name_contact);
            phone_num = itemView.findViewById(R.id.ph_number);
        }
    }
}

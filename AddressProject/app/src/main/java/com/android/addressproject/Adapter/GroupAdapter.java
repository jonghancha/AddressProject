package com.android.addressproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.android.addressproject.Activity.GroupViewActivity;
import com.android.addressproject.Activity.MainViewActivity;
import com.android.addressproject.Bean.Address;
import com.android.addressproject.R;

import java.util.ArrayList;

// 20.12.29 지은 추가
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    Context mContext = null;
    int layout = 0;
    ArrayList<Address> data = null;
    LayoutInflater inflater = null;

    public GroupAdapter(Context mContext, int layout, ArrayList<Address> data) {
        this.mContext = mContext;
        this.layout = layout;
        this.data = data;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_con, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        //
        // img 불러와야 함..
        //
        holder.group.setText(data.get(position).getAddressGroup());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroupViewActivity.class);
                intent.putExtra("name", data.get(position).getAddressName());
                intent.putExtra("phone", data.get(position).getAddressPhone());
                intent.putExtra("group", data.get(position).getAddressGroup());
                intent.putExtra("email", data.get(position).getAddressEmail());
                intent.putExtra("text", data.get(position).getAddressText());
                intent.putExtra("birth", data.get(position).getAddressBirth());
                intent.putExtra("img", data.get(position).getAddressImage());
                intent.putExtra("star", data.get(position).getAddressStar());

                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "그룹 페이지 이동", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView group;
        public MyViewHolder(View itemView) {
            super(itemView);

            group = itemView.findViewById(R.id.group_contact);

        }
    }
}

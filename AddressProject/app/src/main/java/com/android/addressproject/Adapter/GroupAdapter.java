package com.android.addressproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


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

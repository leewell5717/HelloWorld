package com.liwei.greendao.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liwei.greendao.Customer;
import com.liwei.mystudy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GreenDaoAdapter extends RecyclerView.Adapter<GreenDaoAdapter.MyViewHolder> {

    private List<Customer> mDatas;

    public GreenDaoAdapter(List<Customer> mDatas){
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_green_dao,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.id.setText(String.valueOf(mDatas.get(position).getId()));
        holder.name.setText(mDatas.get(position).getCustomerName());
        holder.phone.setText(mDatas.get(position).getCustomerPhone());
        holder.age.setText(String.valueOf(mDatas.get(position).getCustomerAge()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.id)
        public TextView id;
        @BindView(R.id.name)
        public TextView name;
        @BindView(R.id.phone)
        public TextView phone;
        @BindView(R.id.age)
        public TextView age;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void updataData(List<Customer> datas){
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }
}
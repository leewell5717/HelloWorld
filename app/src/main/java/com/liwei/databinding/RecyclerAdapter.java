package com.liwei.databinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liwei.mystudy.BR;
import com.liwei.mystudy.R;
import com.liwei.mystudy.databinding.ItemRecyclerViewBinding;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<People> mList;

    public RecyclerAdapter(List<People> mList){
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
//                R.layout.item_recycler_view,viewGroup,false);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.setBinding(BR.people,mList.get(i));
        myViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAAAAAA","ItemView");
            }
        });

        myViewHolder.getBinding().age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAAAAAA","age");
            }
        });

        myViewHolder.getBinding().name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAAAAAA","name");
            }
        });

        myViewHolder.getBinding().weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AAAAAAA","weight");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ItemRecyclerViewBinding dataBinding;
        private View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            dataBinding = DataBindingUtil.bind(itemView);
        }

        public void setBinding(int variableId , Object object){
            dataBinding.setVariable(variableId,object);
            dataBinding.setSize(30);
            dataBinding.executePendingBindings();
        }

        public ItemRecyclerViewBinding getBinding(){
            return dataBinding;
        }

    }
}
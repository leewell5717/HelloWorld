package com.liwei.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liwei.mystudy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 列表数据adapter
 */
public class ListAdapter extends BaseAdapter {
    private List<BluetoothDevice> mDatas;
    private LayoutInflater inflater;


    public ListAdapter(Context context,List<BluetoothDevice> mDatas){
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_bluetooth,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        BluetoothDevice device = mDatas.get(position);
        if(TextUtils.isEmpty(device.getName())){
            holder.name.setText("无名设备"+position);
        }else{
            holder.name.setText("名称："+device.getName());
        }
        holder.address.setText("地址："+device.getAddress());

        return convertView;
    }

    public class ViewHolder{
        /**设备名*/
        @BindView(R.id.name)
        public TextView name;
        /**设备地址*/
        @BindView(R.id.address)
        public TextView address;

        public ViewHolder(View v){
            ButterKnife.bind(this,v);
        }
    }
}
package com.liwei.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liwei.mystudy.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static com.liwei.bluetooth.BlueToothServerActivity.MyUUID;
import static com.liwei.mystudy.R.id.paired;
import static com.liwei.mystudy.R.id.search;

public class BlueToothActivity extends Activity {
    /**
     * 请求打开蓝牙码
     */
    private static final int Request_Open_Bluetooth = 1;
    /**
     * 30秒后取消搜索
     */
    private static final int Stop_Search_Time = 1000 * 10;
    /**
     * 配对
     */
    private static final int Pair = 2;
    /**
     * 解除配对
     */
    private static final int RemovePair = 3;
    /**
     * 搜索
     */
    private static final int Search = 4;
    /**
     * 接收消息
     */
    private static final int Msg = 5;
    /**
     * UUID
     */
    public static UUID MyUUid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * 开启/关闭蓝牙btn
     */
    @BindView(R.id.open_close)
    public Button openCloseBtn;
    /**
     * 搜索btn
     */
    @BindView(search)
    public Button searchBtn;
    /**
     * 已配对btn
     */
    @BindView(paired)
    public Button pairedBtn;
    /**
     * 跳转到服务端Btn
     */
    @BindView(R.id.server)
    public Button serverBtn;
    /**
     * 已配对设备列表
     */
    @BindView(R.id.paired_listview)
    public ListView pairedListview;
    /**
     * 搜索设备列表
     */
    @BindView(R.id.search_listview)
    public ListView searchListview;
    /**
     * 加载框
     */
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    /**
     * 无结果提示Tv
     */
    @BindView(R.id.no_result)
    public TextView noResultTv;
    /**
     * 标题
     */
    @BindView(R.id.title)
    public TextView titleTv;
    /**
     * 消息内容
     */
    @BindView(R.id.chat)
    public TextView msgTv;

    //蓝牙适配器
    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * 广播收到的设备
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { //发现设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) {
                    progressBar.setVisibility(View.GONE);
                    noResultTv.setVisibility(View.VISIBLE);
                    noResultTv.setText("没有搜索到设备");
                } else {
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) { //判断是否已经配对过
                        //没有配对的话，添加到列表
//                        BlueToothBean blueToothBean = new BlueToothBean();
//                        if(TextUtils.isEmpty(device.getName())){ //设备名可能为空
//                            blueToothBean.setName("设备名为空");
//                        }else{
//                            blueToothBean.setName(device.getName());
//                        }
//                        blueToothBean.setAddress(device.getAddress());
                        searchDeviceList.add(device);
                        searchListAdapter.notifyDataSetChanged();
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { //搜索完成
                progressBar.setVisibility(View.GONE);
                searchBtn.setText("开始搜索");
                Toast.makeText(BlueToothActivity.this, "搜索结束", Toast.LENGTH_SHORT).show();
                if (searchDeviceList.size() == 0) {
                    noResultTv.setVisibility(View.VISIBLE);
                    noResultTv.setText("没有发现可配对的设备");
                }
            }
        }
    };

    /**
     * 搜索数据adapter
     */
    private ListAdapter searchListAdapter;
    /**
     * 已配对数据adapter
     */
    private ListAdapter pairedListAdapter;
    /**
     * 搜索数据集
     */
    private List<BluetoothDevice> searchDeviceList;
    /**
     * 已配对数据集
     */
    private List<BluetoothDevice> pairedDeviceList;

    /**
     * 客户端Socket
     */
    private BluetoothSocket clientSocket;

    /**
     * 搜索时长
     */
    private Runnable stopSearchRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.cancelDiscovery();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Msg:
                    msgTv.setText("消息：" + msg.obj.toString());
                    break;
                case 6:
                    msgTv.setText("服务端已开启");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        ButterKnife.bind(this);

        searchDeviceList = new ArrayList<>();
        pairedDeviceList = new ArrayList<>();
        if (adapter == null) {
            Toast.makeText(BlueToothActivity.this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            openCloseBtn.setEnabled(false);
            searchBtn.setEnabled(false);
            pairedBtn.setEnabled(false);
            serverBtn.setEnabled(false);
            pairedListview.setVisibility(View.GONE);
        } else {
            if (isOpen()) {
                openCloseBtn.setText("关闭蓝牙");
            } else {
                openCloseBtn.setText("开启蓝牙");
            }
            getPairList();
        }
    }


    @OnClick({R.id.open_close, search, paired, R.id.server})
    public void BtnClick(View v) {
        switch (v.getId()) {
            case R.id.open_close: // 开/关蓝牙
                if (adapter == null) {
                    Toast.makeText(BlueToothActivity.this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isOpen()) { //没有开启蓝牙，提示开启
                        Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(mIntent, Request_Open_Bluetooth);
                        //无需提示，强行开启
                        //adapter.enable();
                    } else {
                        adapter.disable();
                        Toast.makeText(BlueToothActivity.this, "蓝牙已关闭", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        pairedListview.setVisibility(View.GONE);
                        searchListview.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        noResultTv.setVisibility(View.GONE);
                        openCloseBtn.setText("开启蓝牙");
                    }
                }
                break;
            case search: //搜索蓝牙
                if (!isOpen()) {
                    Toast.makeText(BlueToothActivity.this, "请先开启蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                titleTv.setText("搜索设备列表");
                if (adapter.isDiscovering()) { //正在搜索
                    searchBtn.setText("开始搜索");
                    adapter.cancelDiscovery();
                    progressBar.setVisibility(View.GONE);
                } else {
                    searchBtn.setText("停止搜索");
                    pairedListview.setVisibility(View.GONE);
                    searchListview.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    noResultTv.setVisibility(View.GONE);
                    // 设置广播信息过滤
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
                    intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                    intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                    intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
                    // 注册发现设备广播
                    registerReceiver(receiver, intentFilter);

                    intentFilter = new IntentFilter();
                    intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    // 注册取消搜索广播
                    registerReceiver(receiver, intentFilter);

                    // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
                    adapter.startDiscovery();

                    new Handler().postDelayed(stopSearchRunnable, Stop_Search_Time);

                    if (searchDeviceList.size() != 0) {
                        searchDeviceList.clear();
                    }
                    searchListAdapter = new ListAdapter(BlueToothActivity.this, searchDeviceList);
                    searchListview.setAdapter(searchListAdapter);
                }
                break;
            case paired: //显示已配对列表
                if (!isOpen()) {
                    Toast.makeText(BlueToothActivity.this, "请先开启蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                titleTv.setText("已配对蓝牙列表");
                getPairList();
                break;
            case R.id.server: //开启服务端
                ServerThread thread = new ServerThread();
                thread.start();
                break;
        }
    }

    /**
     * 配对列表item点击事件
     */
    @OnItemClick(R.id.paired_listview)
    public void onPairedListViewItemClick(int position) {
        showDialog(BlueToothActivity.this, "配对提示", "确定和该设备解除配对吗?", RemovePair, pairedDeviceList.get(position));
    }

    /**
     * 搜索列表item点击事件
     */
    @OnItemClick(R.id.search_listview)
    public void onSearchListViewItemClick(int position) {
        showDialog(BlueToothActivity.this, "配对提示", "确定和该设备进行配对吗?", Pair, searchDeviceList.get(position));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Open_Bluetooth) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(BlueToothActivity.this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
                openCloseBtn.setText("关闭蓝牙");
                getPairList();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(BlueToothActivity.this, "不允许蓝牙开启", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 监测是否开启蓝牙
     */
    private boolean isOpen() {
        if (adapter.isEnabled()) {
            return true;
        }
        return false;
    }

    /**
     * 获取已配对蓝牙列表
     */
    private void getPairList() {
        pairedListview.setVisibility(View.VISIBLE);
        searchListview.setVisibility(View.GONE);
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        titleTv.setText("已配对蓝牙列表");
        if (pairedDeviceList.size() != 0) {
            pairedDeviceList.clear();
        }
        if (devices.size() != 0) {
            for (Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext(); ) {
                BluetoothDevice device = it.next();
//                BlueToothBean bean = new BlueToothBean();
//                bean.setName(device.getName());
//                bean.setAddress(device.getAddress());
                pairedDeviceList.add(device);
            }
            pairedListAdapter = new ListAdapter(BlueToothActivity.this, pairedDeviceList);
            pairedListview.setAdapter(pairedListAdapter);
        } else {
            noResultTv.setVisibility(View.VISIBLE);
            noResultTv.setText("没有已配对的数据");
        }
    }

    /**
     * 提示配对/连接对话框
     */
    private void showDialog(final Context context, final String title, String message, final int flag, final BluetoothDevice device) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (flag) {
                            case Pair: //如果是配对
                                //先取消搜索
                                if (adapter.isDiscovering()) {
                                    adapter.cancelDiscovery();
                                }
                                dialog.dismiss();
                                try {
                                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                                        //蓝牙配对
                                        Method pairMethod = BluetoothDevice.class.getMethod("createBond");
                                        titleTv.setText("蓝牙配对中...");
                                        Boolean isPair = (Boolean) pairMethod.invoke(device);
                                        if (isPair) {
                                            titleTv.setText("配对成功");
                                            showDialog(context, "连接提示", "当前设备已配对成功，是否进行连接?", Search, device);
                                        } else {
                                            titleTv.setText("配对失败");
                                        }
                                    } else {
                                        titleTv.setText("已经配对");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    titleTv.setText("配对异常");
                                }
                                break;
                            case RemovePair: //如果是解除配对
                                try {
                                    Method removePairMethod = BluetoothDevice.class.getMethod("removeBond");
                                    titleTv.setText("解除配对中");
                                    Boolean isRemove = (Boolean) removePairMethod.invoke(device);
                                    if (isRemove) {
                                        titleTv.setText("解除配对成功");
                                        getPairList();
                                    } else {
                                        titleTv.setText("解除失败");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "解除配对异常", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case Search: //如果是连接
                                dialog.dismiss();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
//                                        clientSocket = device.createRfcommSocketToServiceRecord(MyUUid);
                                            clientSocket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
                                            clientSocket.connect();

                                            OutputStream outputStream = clientSocket.getOutputStream();
//                                        InputStream inputStream = clientSocket.getInputStream();
                                            outputStream.write("你好，这是发自于华为Meta 7".getBytes());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 获取UUid
     */
//    private UUID getUUid(Context context){
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String tmDevice, tmSerial ,androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "" + tm.getSimSerialNumber();
//        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        return deviceUuid;
//    }

    private class ServerThread extends Thread {
        private InputStream inputStream;
        private OutputStream outputStream;

        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;


        public ServerThread() {
            try {
                serverSocket = adapter.listenUsingRfcommWithServiceRecord("aa", UUID.fromString(MyUUID));
                handler.sendEmptyMessage(6);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //获取客户端信息
            while (true) {
                try {
                    socket = serverSocket.accept();
                    if (socket != null) {
                        inputStream = socket.getInputStream();
                        outputStream = socket.getOutputStream();
                        if(!socket.isConnected()){
                            socket.connect();
                        }
                        byte[] buffer = new byte[256];
                        int count = inputStream.read();
                        Message msg = new Message();
                        msg.what = Msg;
                        msg.obj = new String(buffer, 0, count, "utf-8");
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        adapter.cancelDiscovery();
    }
}
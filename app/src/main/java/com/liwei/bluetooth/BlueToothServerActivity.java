package com.liwei.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.liwei.mystudy.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.liwei.mystudy.R.id.result;

public class BlueToothServerActivity extends Activity {
    public static final String MyUUID = "00001101-0000-1000-8000-00805F9B34FB";

    @BindView(result)
    public TextView resultTv;
    @BindView(R.id.start_server)
    public Button startServerBtn;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    resultTv.setText("消息："+msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_server);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_server)
    public void buttonClick(){
        Thread thread = new Thread(new ServerThread());
        thread.start();
    }

    private class ServerThread implements Runnable{
        private InputStream inputStream;
        private OutputStream outputStream;

        private BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;

        public ServerThread(){
            try {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                serverSocket = adapter.listenUsingRfcommWithServiceRecord("aa", UUID.fromString(MyUUID));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            //获取客户端信息
            try {
                resultTv.setText("服务端开启服务");
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                byte[] buffer = new byte[1024];
                int count = inputStream.read();
                Message msg = new Message();
                msg.what = 1;
                msg.obj = new String(buffer, 0, count, "utf-8");
                handler.sendMessage(msg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
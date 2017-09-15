package com.liwei.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.liwei.mystudy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends Activity {
    @BindView(R.id.button)
    public Button button;
    @BindView(R.id.button2)
    public Button button2;

    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);
        ButterKnife.bind(this);
        showLog("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLog("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLog("onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showLog("onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showLog("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showLog("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showLog("onDestroy");
    }

    private void showLog(String msg){
        Log.e("XXX",msg);
    }

    @OnClick({R.id.button,R.id.button2})
    public void myOnclick(View v){
        switch (v.getId()){
            case R.id.button:
                dialog = new Dialog(TestActivity.this,R.style.MyDialog);
                dialog.setContentView(R.layout.dialog_test);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                dialog.getWindow().setAttributes(lp);
                Button close = (Button)dialog.findViewById(R.id.close_dialog);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                break;
            case R.id.button2:
                AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
                LayoutInflater inflater = LayoutInflater.from(TestActivity.this);
                View view = inflater.inflate(R.layout.dialog_test,null);
                builder.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLog("点击了确定");
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLog("点击了取消");
                        dialog.dismiss();
                    }
                }).create().show();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        showLog("onConfigurationChanged");
    }
}
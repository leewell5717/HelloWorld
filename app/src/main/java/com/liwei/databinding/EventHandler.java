package com.liwei.databinding;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class EventHandler {
    public void myClick(){
        Log.e("EventHandler","不带参数的myClick");
    }

    public void myClick(View view){
        Log.e("EventHandler","带1个参数的myClick"+"------你好");
    }

    public void myClick(View view,String content){
        Log.e("EventHandler","带2个参数的myClick"+"------你好:"+content);
    }
}
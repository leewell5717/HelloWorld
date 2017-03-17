package com.liwei.mystudy;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class EventHelper {

    private Context context;

    public EventHelper(Context context){
        this.context = context;
    }

    public void onClickMy(View view){
        Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
    }
}
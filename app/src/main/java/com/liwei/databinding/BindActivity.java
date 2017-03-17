package com.liwei.databinding;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.liwei.mystudy.R;
import com.liwei.mystudy.databinding.Bind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BindActivity extends Activity {
    private MyUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bind bind = DataBindingUtil.setContentView(this,R.layout.activity_bind);
        myUser = new MyUser();
        myUser.setName("Lee");
        myUser.setColor(Color.RED);
        bind.setUser(myUser);
        bind.setSize(24);
        bind.setHadlers(new EventHandler());

        HashMap<String,String> map = new HashMap<>();
        map.put("a","Lee");
        map.put("b","Well");
        bind.setMap(map);

        List<People> mList = new ArrayList<>();
        for(int i=0;i<10;i++){
            People people = new People();
            people.name.set("这是"+i);
            people.age.set(i+10);
            people.weight.set(i+100.0f);
            mList.add(people);
        }
        RecyclerAdapter adapter = new RecyclerAdapter(mList);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        bind.recycler.setLayoutManager(llm);
        bind.recycler.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myUser.setName("静静");
                myUser.setColor(Color.BLUE);

                People people = new People();
                people.name.set("您好");
                people.age.set(22);
                people.weight.set(136.3f);
                bind.setPeople(people);
            }
        },3000);
    }
}
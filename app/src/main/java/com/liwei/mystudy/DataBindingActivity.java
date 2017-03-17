package com.liwei.mystudy;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBindingActivity extends Activity {
    private com.liwei.mystudy.databinding.ActivityDataBindingBinding bindingBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);

        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
//        bindingBinding.setList(list);
        bindingBinding.setPos(1);

        Map<String,String> map = new HashMap<>();
        map.put("one","111");
        map.put("two","222");
//        bindingBinding.setMap(map);
        bindingBinding.setKey(map.get("one"));
    }
}
package com.liwei.databinding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.liwei.mystudy.BR;

public class MyUser extends BaseObservable{
    private String name;
    private int color;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyPropertyChanged(BR.color);
    }
}
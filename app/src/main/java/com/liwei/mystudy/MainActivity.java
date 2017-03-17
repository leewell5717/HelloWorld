package com.liwei.mystudy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.liwei.bluetooth.BlueToothActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    @BindView(R.id.button)
    public Button button;
    @BindView(R.id.button2)
    public Button button2;
    @BindView(R.id.image)
    public ImageView image;

    private static final String Tag = "MyRxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button,R.id.button2})
    public void btnOnClick(View v){
        switch (v.getId()){
            case R.id.button:
                Intent intent1 = new Intent(MainActivity.this,DataBindingActivity.class);
                startActivity(intent1);

                String[] s = new String[]{"a","b","c"};
                List<String> in = new ArrayList<>();
                in.add("1");
                in.add("2");
                in.add("2aaaa");
                Observable.from(in).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(Tag,s);
                    }
                });
                Observable.from(s).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(Tag,s);
                    }
                });
                break;
            case R.id.button2:
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BdzInspection"+"/img"+"/bbb.png";
                Observable.just(filePath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<String, Bitmap>() {
                            @Override
                            public Bitmap call(String s) {
                                return BitmapFactory.decodeFile(s);
                            }
                        }).subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                image.setImageBitmap(bitmap);
                            }
                });
                Intent intent2 = new Intent(MainActivity.this,BlueToothActivity.class);
                startActivity(intent2);

                break;
        }
    }

    @OnLongClick({R.id.button,R.id.button2})
    public boolean btnOnLongClick(View v){
        List<Course> cou1 = new ArrayList<>();
        cou1.add(new Course("Java","100"));
        cou1.add(new Course("C++","101"));
        User[] users = new User[]{new User("Lee",22,cou1)};
        switch (v.getId()){
            case R.id.button:
                Intent intent = new Intent(MainActivity.this,TestActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Subscriber<Course> sub = new Subscriber<Course>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {

                    }
                    @Override
                    public void onNext(Course course) {
                        Log.e(Tag,course.getCourseName()+","+course.getCourseID());
                    }
                };
                Observable.from(users).flatMap(new Func1<User, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(User user) {
                        return Observable.from(user.getCourse());
                    }
                }).subscribe(sub);
                break;
        }
        return false;
    }

}
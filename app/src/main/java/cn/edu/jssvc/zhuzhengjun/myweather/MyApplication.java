package cn.edu.jssvc.zhuzhengjun.myweather;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {

    public static RequestQueue queue;
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        queue = Volley.newRequestQueue(getApplicationContext());

    }
}

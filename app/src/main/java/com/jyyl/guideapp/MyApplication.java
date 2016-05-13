package com.jyyl.guideapp;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.service.LocationService;
import com.baidu.location.service.WriteLog;
import com.baidu.mapapi.SDKInitializer;

import java.util.LinkedList;
import java.util.List;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/4/13  13:45
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private List<Activity> mList = new LinkedList<Activity>();

    public LocationService locationService;
    public Vibrator mVibrator;

    /**
     * 获取单例
     */
    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate();
        instance = this;

        WriteLog.getInstance().init(); // 初始化日志
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // remove Activity
    public void removeActivity(Activity activity) {
        mList.add(activity);
    }

    public void exitApp() {
        for (Activity activity : mList) {
            if (activity != null)
                activity.finish();
        }
    }
}

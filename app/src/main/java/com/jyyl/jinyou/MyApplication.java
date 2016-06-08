package com.jyyl.jinyou;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.service.LocationService;
import com.baidu.location.service.WriteLog;
import com.baidu.mapapi.SDKInitializer;
import com.jyyl.jinyou.utils.CrashExceptionHandler;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

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

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setSilenceTime(getApplicationContext(), 23, 30, 7, 30);

        WriteLog.getInstance().init(); // 初始化百度地图日志
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        LeakCanary.install(this);

        configCollectCrashInfo();

    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    // remove Activity
    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }

    public void exitApp() {
        for (Activity activity : mList) {
            if (activity != null)
                activity.finish();
        }
    }


    /**app在sd卡的主目录*/
    public final static String APP_MAIN_FOLDER_NAME = "Guide";
    /**本地存放闪退日志的目录*/
    public final static String CRASH_FOLDER_NAME = "crash";
    /**app在sd卡存放图片的目录*/
    public final static String PHOTO_FOLDER_NAME = "photo";
    /**
     * 配置奔溃信息的搜集
     */
    private void configCollectCrashInfo() {
        CrashExceptionHandler crashExceptionHandler = new CrashExceptionHandler(this, APP_MAIN_FOLDER_NAME, CRASH_FOLDER_NAME);
        Thread.setDefaultUncaughtExceptionHandler(crashExceptionHandler);
    }

}

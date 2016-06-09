/*
 * 
 */
package com.jyyl.jinyou.abardeen.heartbeat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.jyyl.jinyou.abardeen.AbardeenMethod;
import com.jyyl.jinyou.utils.LogUtils;


/**
 * @Fuction: 阿巴町 socket通信
 * @Author: Shang
 * @Date: 2016/6/9  14:35
 */
public class HeartService extends Service {

    private static final String TAG = "HeartService";

    /**
     * 心跳间隔一分钟
     */
    private static final long HEARTBEAT_INTERVAL = 60 * 1000L;

    private AlarmManager mAlarmManager;

    private PendingIntent mPendingIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                Const.ACTION_HEARTBEAT), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isConnect = false;
                while (!isConnect) {
                    isConnect = AbardeenMethod.getInstance().connectServer();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                LogUtils.d(TAG, "腕表服务器登录成功");
                // 发送启动心跳任务的广播
                Intent startIntent = new Intent(Const.ACTION_START_HEART);
                sendBroadcast(startIntent);

                // 启动心跳定时器
                long triggerAtTime = SystemClock.elapsedRealtime() + HEARTBEAT_INTERVAL;
                mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                        triggerAtTime, HEARTBEAT_INTERVAL, mPendingIntent);
            }
        }).start();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Intent startIntent = new Intent(Const.ACTION_STOP_HEART);
        sendBroadcast(startIntent);
        //取消心跳定时器
        mAlarmManager.cancel(mPendingIntent);
        super.onDestroy();
    }
}

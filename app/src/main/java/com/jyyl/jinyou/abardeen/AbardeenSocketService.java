package com.jyyl.jinyou.abardeen;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jyyl.jinyou.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Fuction: 阿巴町 socket通信
 * @Author: Shang
 * @Date: 2016/6/9  14:35
 */
public class AbardeenSocketService extends Service {

    private static Binder mBinder;

    private static String TAG = "AbardeenSocketService";

    private Thread mThread = null;

    private boolean runFlag = true;

    private Context context;

    class MyBinder extends Binder {

        public Service getLocalService() {
            return AbardeenSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new MyBinder();
        }
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "LocalService onCreate");
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "LocalService onDestroy");
        super.onDestroy();
        runFlag = false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = getThread();
        if (thread.isAlive()) {
            Log.i(TAG, "thread is aleady started");
        } else {
            thread.start();
        }
        Log.i(TAG, "LocalService onStartCommand");
        Log.i(TAG, "action is " + intent.getAction());
        return super.onStartCommand(intent, flags, startId);
    }


    private Thread getThread() {

        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest();
                }
            });
        }
        return mThread;
    }


    //网络请求
    private void doRequest() {

        //持续请求服务器
        while (runFlag) {
            try {
                String datas = SocketOpenHelper.getInstance().getResultDatas();
                doResultDatas(datas);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                Log.i(TAG, "over");
            }
        }

    }


    /**
     * 处理接收到的数据
     * @param datas
     *
     * @throws JSONException
     */
    private void doResultDatas(String datas) throws JSONException {
        if (datas != null) {
            JSONObject resultJson = new JSONObject(datas);
            String cmd = (String) resultJson.get("cmd");
            String code = (String) resultJson.get("code");

            if ("KTO".equals(cmd)) {
                Log.d(TAG, "异地登录");
            }
            if ("0".equals(code)) {
                LogUtils.d(TAG, resultJson.toString());
            } else {
                String message = (String) resultJson.get("message");
                Log.d(TAG, message);
            }
        }
    }

    private Context getContext() {
        if (context == null) {
            context = this;
        }
        return context;
    }

}

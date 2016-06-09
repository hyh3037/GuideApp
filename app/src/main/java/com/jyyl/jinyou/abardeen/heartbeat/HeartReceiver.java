/*
 * 
 */
package com.jyyl.jinyou.abardeen.heartbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jyyl.jinyou.abardeen.SocketOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 心跳广播接收器
 * 
 * @author zengjiantao
 * @date 2013-4-18
 */
public class HeartReceiver extends BroadcastReceiver {

	private static final String TAG = "HeartReceiver";
    private HBThread hbThread;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d(TAG, "action" + action);
		if (Const.ACTION_START_HEART.equals(action)) {
			Log.d(TAG, "Start heart");
		} else if (Const.ACTION_HEARTBEAT.equals(action)) {
			Log.d(TAG, "Heartbeat");
			//在此完成心跳需要完成的工作，比如请求远程服务器……
            Socket socket = SocketOpenHelper.getInstance().getConectSocket();
            try {
                if (socket != null) {
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    if (hbThread == null) {
                        hbThread = new HBThread(os, is);
                    }
                    new Thread(hbThread).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

		} else if (Const.ACTION_STOP_HEART.equals(action)) {
			Log.d(TAG, "Stop heart");
		}
	}

}

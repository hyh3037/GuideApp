package com.jyyl.jinyou.abardeen.heartbeat;

import android.util.Log;

import com.jyyl.jinyou.abardeen.AbardeenMethod;
import com.jyyl.jinyou.abardeen.SocketOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HBThread implements Runnable {

    private OutputStream os;
    private InputStream is;

    public HBThread(OutputStream os, InputStream is) {
        this.os = os;
        this.is = is;
    }

    @Override
    public void run() {
        try {

            // 写入头部 CWT
            os.write(SocketOpenHelper.TITLE.getBytes());
            // 写入数据包长度
            os.write(SocketOpenHelper.int0(0));
            os.write(SocketOpenHelper.int1(0));
            os.write(SocketOpenHelper.int2(0));
            os.write(SocketOpenHelper.int3(0));
            os.flush();
            Log.d("HBThread", "发送心跳");

        } catch (Exception e) {

            try {
                os.close();
                is.close();
                Log.d("HBThread", "Socket断开连接");
                boolean isConnect = false;
                while (!isConnect) {
                    isConnect = AbardeenMethod.getInstance().connectServer();
                    Thread.sleep(60000);
                }
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }

    }

}

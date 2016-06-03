package com.jyyl.jinyou.abading;

import android.util.Log;

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
			while (true) {
				// 写入头部 CWT
				os.write(SocketOpenHelper.TITLE.getBytes());
				// 写入数据包长度
				os.write(SocketOpenHelper.int0(0));
				os.write(SocketOpenHelper.int1(0));
				os.write(SocketOpenHelper.int2(0));
				os.write(SocketOpenHelper.int3(0));
				os.flush();
				Log.d("HBThread", "发送心跳");
				Thread.sleep(60000);
			}
		} catch (Exception e) {

			try {
				os.close();
                is.close();
                Log.d("HBThread", "Socket断开连接");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
		
	}

}

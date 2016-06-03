package com.jyyl.jinyou.abading;


import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Fuction: 阿巴町 长连接 接口 辅助类
 * @Author: Shang
 * @Date: 2016/6/2  13:21
 */
public class SocketOpenHelper {
    private static String TAG = "SocketOpenHelper";
    private static volatile SocketOpenHelper instance = null;
    private Context mContext;

    private static final String HOST = "cwtcn-dl.6655.la";
    private static final int PORT = 9991;

    //	private static final String HOST = "192.168.1.11";
    //	private static final int PORT = 9998;

    public static final String TITLE = "CWT";

    public static final byte[] JSON_FORMAT_B = {(byte) 0x80, 0, 0, 0};

    public static final AtomicInteger COMMAND_ID = new AtomicInteger(0);

    public static final int CMD_LENGTH_BIT_LENGTH = 4;

    private SocketOpenHelper() {
    }

    /**
     * 获取单例
     * @return 实例
     */
    public static SocketOpenHelper getInstance() {

        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (SocketOpenHelper.class) {
                if (instance == null) {
                    instance = new SocketOpenHelper();
                }
            }
        }

        return instance;
    }

    private Socket socket;
    private HBThread hbThread;
    private OutputStream os;
    private InputStream is;
    private JSONObject loginJson;


    /**
     * 连接服务器（REQ）
     */
    public void connectServer(JSONObject jsonObject) {
        try {
            socket = this.getSocket();
            if (socket == null || socket.isClosed()) {
                Log.d(TAG, "腕表服务器连接失败");
                return;
            }
            os = socket.getOutputStream();
            is = socket.getInputStream();

            this.loginJson = jsonObject;
            outputWrite(loginJson);
            JSONObject resultJson = inputRead();

            if (resultJson != null){
                if (hbThread == null) {
                    hbThread = new HBThread(os, is);
                }
                Thread thread = new Thread(hbThread);
                thread.start();
            }else {
                Log.d(TAG, "腕表服务器连接失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取返回数据
     */
    public JSONObject getResultDatas(JSONObject jsonObject) {
        try {

            if (socket == null || socket.isClosed()) {
                this.connectServer(loginJson);
                if (socket == null ||socket.isClosed()){
                    Log.d(TAG, "腕表服务器连接失败");
                    return jsonObject;
                }
            }

            outputWrite(jsonObject);
            JSONObject resultJson = inputRead();
            try {

                if (resultJson != null) {
                    JSONObject params = resultJson.getJSONObject("params");
                    Log.d(TAG, params.toString());
                    return params;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultJson;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写如数据
     * @param paramJson
     *
     * @throws IOException
     */
    private void outputWrite(JSONObject paramJson) throws IOException {

        // 写入头部 CWT
        os.write(TITLE.getBytes());

        String json = paramJson.toString();
        byte[] content = json.getBytes(Charset.forName("UTF-8"));
        int totalLength = JSON_FORMAT_B.length + CMD_LENGTH_BIT_LENGTH + content.length;

        // 写入数据包长度
        os.write(int0(totalLength));
        os.write(int1(totalLength));
        os.write(int2(totalLength));
        os.write(int3(totalLength));

        // 写入命令头
        os.write(JSON_FORMAT_B);

        // 写入命令体长度
        os.write(int0(content.length));
        os.write(int1(content.length));
        os.write(int2(content.length));
        os.write(int3(content.length));

        // 写入命令体
        os.write(content);
        os.flush();
    }

    /**
     * 读取服务器返回数据
     * @throws IOException
     */
    private JSONObject inputRead() throws IOException {
        //协议头解析：cwt(3) + body长度(4)
        byte[] buf = new byte[7];
        int n = 0;
        boolean found = false;
        while (true) {
            int b = 0;
            try {
                b = is.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (-1 == b) {// 读到末尾
                break;
            }
            if (n == 6) {
                found = true;
                break;
            }
            buf[n++] = (byte) b;
        }
        if (found) {
            int length = getCount(buf[3], buf[4], buf[5], buf[6]);//得到4个长度的字节
            Log.d(TAG, "收到命令头：" + new String(buf, 0, 3));
            Log.d(TAG, "命令的长度：" + length);
            if (length == 0) {
                return null;
            }
            byte[] temp = new byte[length];
            n = 0;
            while (n < temp.length) {
                int m = is.read(temp, n, temp.length - n);
                if (-1 == m) {
                    break;
                }
                n += m;
            }
            /**
             * temp的前4位是JSON_FORMAT_B,然后紧跟着4位是命令体数据长度
             * 这个命令体数据长度必须读出来，因为如果服务器下发带有附件的数据就需要根据这个长度将命令体读出来
             * 然后剩下的数据就是附件数据了
             */
            int cmdBodyLength = getCount(temp[4], temp[5], temp[6], temp[7]);

            String tempString = new String(temp, 0, temp.length);
            Log.d(TAG, "tempString：" + tempString);
            if (tempString.contains("E105")){
                Log.d(TAG, "无法找到用户");
                return null;
            }

            Log.d(TAG, "命令体长度：" + cmdBodyLength);
            byte[] cmdBody = new byte[cmdBodyLength];
            System.arraycopy(temp, JSON_FORMAT_B.length + 4, cmdBody, 0, cmdBodyLength);
            String datas = new String(cmdBody, 0, cmdBody.length);
            Log.d(TAG, "收到命令的文本：" + datas);

            try {
                JSONObject jsonObject = new JSONObject(datas);
                String cmd = (String) jsonObject.get("cmd");
                String code = (String) jsonObject.get("code");
                if ("KTO".equals(cmd)){
                    Log.d(TAG, "异地登录");
                    return null;
                }
                if ("0".equals(code)){
                    return jsonObject;
                }else {
                    String message = (String) jsonObject.get("message");
                    Log.d(TAG, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * =======================================公用方法START========================================
     */
    private Socket getSocket() {
        try {
            //取得SSL的SSLContext实例
            SSLContext mSSLContext = SSLContext.getInstance("SSL");
            mSSLContext.init(null, new TrustManager[]{new MyX509TrustManager()}, new SecureRandom
                    ());
            SSLSocketFactory factory = mSSLContext.getSocketFactory();
            Socket socket = factory.createSocket(HOST, PORT);
            Log.d("Socket", socket.toString());
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String nextCommandId() {
        return String.valueOf(COMMAND_ID.getAndIncrement());
    }

    public static byte int3(int x) {
        return (byte) (x >> 24);
    }

    public static byte int2(int x) {
        return (byte) (x >> 16);
    }

    public static byte int1(int x) {
        return (byte) (x >> 8);
    }

    public static byte int0(int x) {
        return (byte) (x);
    }

    public static int getCount(byte b0, byte b1) {
        return (changNum(b0) << 8) + changNum(b1);
    }

    public static int getCount(byte b0, byte b1, byte b2, byte b3) {
        return changNum(b0) + (changNum(b1) << 8) + (changNum(b2) << 16) + (changNum(b3) << 24);
    }

    /**
     * 将符号位的byte转化为无符号位的byte
     * @param intValue
     *
     * @return
     */
    public static int changNum(byte intValue) {
        int value = 0;
        int temp = intValue % 256;
        if (intValue < 0) {
            value = temp >= -128 ? 256 + temp : temp;
        } else {
            value = temp > 127 ? temp - 256 : temp;
        }
        return value;
    }

    public static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType)
                throws java.security.cert.CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }
    /**=======================================公用方法END========================================*/
}

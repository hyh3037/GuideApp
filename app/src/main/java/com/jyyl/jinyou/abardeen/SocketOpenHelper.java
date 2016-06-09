package com.jyyl.jinyou.abardeen;


import android.content.Context;
import android.util.Log;

import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.utils.LogUtils;

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

    private static final String HOST = "cwtcn-dl.6655.la";
    private static final int PORT = 9991;

    //	private static final String HOST = "192.168.1.11";
    //	private static final int PORT = 9998;

    public static final String TITLE = "CWT";

    public static final byte[] JSON_FORMAT_B = {(byte) 0x80, 0, 0, 0};

    public static final AtomicInteger COMMAND_ID = new AtomicInteger(0);

    public static final int CMD_LENGTH_BIT_LENGTH = 4;

    private Context appContext;

    private SocketOpenHelper() {
        appContext = MyApplication.getInstance().getApplicationContext();
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
    private OutputStream os;
    private InputStream is;
    private JSONObject loginJson;


    /**
     * 连接服务器（REQ）
     * <p>
     * 连接成功返回值： {"cmd":"ARE","code":"0","id":"1463126051218",
     * "params":{
     *      "sessionId":"1DefaultSession2",   //会话ID，服务端标识此次客户端和服务端的连接
     *      "userId":774672119496704,
     *      "ver":"1.0.4",
     *      "wearerStatus":[   //关联的手表信息对象数组,数组每个元素是一个JSON对象，表示每个IMEI当前状态数据。
     *          {"bVer":"1002", //Build版本
     *          "btMac":"100000000000", //蓝牙mac地址，12位，无分隔符
     *          "fVer":"v02",   //蓝牙估计版本
     *          "fnStatus": //亲情号码状态对象
     *              {"fns":[    //亲情号码数组
     *                  {"mobile":"13533334444","name":"","no":1,"picId":0},
     *                  {"mobile":"13533335555","name":"","no":2,"picId":0},
     *                  {"mobile":"13533335555","name":"", "no":3,"picId":0},
     *                  {"mobile":"13533335555","name":"","no":4,"picId":0},
     *                  {"mobile":"13533335555","name":"", "no":5,"picId":0},
     *                  {"mobile":"13533335555","name":"", "no":6,"picId":0},
     *                  {"mobile":"13533335555","name":"","no":7,"picId":0},
     *                  {"mobile":"13533335555","name":"", "no":8,"picId":0},
     *                  {"mobile":"13533335555","name":"", "no":9,"picId":0}],
     *              "imei":"860860000030000",
     *              "lut":"20150710122140",     //最后修改时间的字符串
     *              "userId":774672119496704, //最后修改亲情号码的用户ID
     *              "userName":""},
     *          "functionStatus":{"all":true},  //功能状态对象
     *          "imei":"860860000030000", //手表的imei
     *          "mrStatus":{    //静音时段状态对象
     *              "imei":"860860000030000",
     *              "lut":"20160507165744",
     *              "mrs":[],//静音时段数组，每个元素都是静音时段对象
     *              "userId":123,   //最后修改静音时段的用户ID
     *              "userName":""}, "online":false, //是否在线
     *          "productId":"KT01W",    //手表型号
     *          "sVer":"KT04_abr_60_hv01_sv90_cn_st20150424", //手表的软件版本
     *          "wifiMac":"200000000000", //Wifi的mac地址，12位，无分隔符
     *          "wmStatus":{"lut":"20150106151838"}//工作模式状态对象 }
     *          ] } }
     */
    public boolean connectServer(JSONObject jsonObject) {
        try {

            socket = getSocket();
            if (socket == null || socket.isClosed()) {
                Log.d(TAG, "腕表服务器Socket连接失败");
                return false;
            }
            os = socket.getOutputStream();
            is = socket.getInputStream();

            this.loginJson = jsonObject;
            outputWrite(loginJson);

            String datas = inputRead();

            if (datas != null) {

                JSONObject resultJson = new JSONObject(datas);
                String cmd = (String) resultJson.get("cmd");
                String code = (String) resultJson.get("code");

                if ("ARE".equals(cmd) && "0".equals(code)) {
                    Log.d(TAG, "腕表服务器连接成功");
                    return true;
                }
            } else {
                Log.d(TAG, "腕表服务器登录失败");
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取返回数据
     */
    public JSONObject getResultDatas(JSONObject jsonObject) {
        try {

            while (!socket.isConnected()) {
                Log.d(TAG, "腕表服务器连接失败,重新连接");
                this.connectServer(loginJson);
                Thread.sleep(60000);
            }

            String datas;
            do {
                outputWrite(jsonObject);
                datas = inputRead();
            }while ("0".equals(datas));

            if (datas != null) {
                JSONObject resultJson = new JSONObject(datas);
                String cmd = (String) resultJson.get("cmd");
                String code = (String) resultJson.get("code");

                if ("KTO".equals(cmd)) {
                    Log.d(TAG, "异地登录");
                    return null;
                }
                if ("0".equals(code)) {
                    LogUtils.d(TAG, resultJson.toString());
                    return resultJson;
                } else {
                    String message = (String) resultJson.get("message");
                    Log.d(TAG, message);
                }
            }
        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取返回数据
     */
    public String getResultDatas() {
        try {

            while (!socket.isConnected()) {
                Log.d(TAG, "腕表服务器连接失败,重新连接");
                this.connectServer(loginJson);
                Thread.sleep(60000);
            }

            String datas = inputRead();
            LogUtils.d(TAG, datas);
            return datas;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入数据
     * @param paramJson
     *
     * @throws IOException
     */
    public void outputWrite(JSONObject paramJson) throws IOException {

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
    public String inputRead() throws IOException {
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
                return "0";
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
            Log.d(TAG, "命令体长度：" + cmdBodyLength);

            //            String tempString = new String(temp, 0, temp.length);
            //            Log.d(TAG, "tempString：" + tempString);

            if (cmdBodyLength > 10000) { //未知错误,太长下面操作会OOM
                return null;
            }
            byte[] cmdBody = new byte[cmdBodyLength];
            System.arraycopy(temp, JSON_FORMAT_B.length + 4, cmdBody, 0, cmdBodyLength);
            String datas = new String(cmdBody, 0, cmdBody.length);
            Log.d(TAG, "收到命令的文本：" + datas);
            return datas;
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
            mSSLContext.init(null, new TrustManager[]{new MyX509TrustManager()},
                    new SecureRandom());
            SSLSocketFactory factory = mSSLContext.getSocketFactory();
            Socket socket = factory.createSocket(HOST, PORT);
            Log.d("Socket", socket.toString());
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Socket getConectSocket(){
        return socket;
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

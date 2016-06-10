package com.jyyl.jinyou.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.jyyl.jinyou.http.Api;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * @Fuction: 七牛上传图片简单封装
 * @Author: Shang
 * @Date: 2016/5/19  11:00
 */
public class QiNiuUploadUtils {

    private String TAG = "QiNiuUploadUtils";
    private UploadManager uploadManager;

    public QiNiuUploadUtils() {
        uploadManager = new UploadManager();
    }

    /**
     * 简单上传文件
     * @param bitmap
     *         上传的文件
     */
    public void upload(final Bitmap bitmap, final String key) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //获得七牛上传凭证uploadToken
                String uploadToken = getUploadToken();
                LogUtils.d(TAG, uploadToken);
                if (uploadToken != null) {
                    Bitmap compressBitmap = ImageUtils.compressImage(bitmap);
                    byte[] bitmapByte = ImageUtils.bitmap2Byte(compressBitmap);
                    uploadManager.put(bitmapByte, key, uploadToken, mUpCompletionHandler, null);
                }
            }
        }).start();
    }

    private QiniuCompleteListener mCompleteListener;
    /**
     * 上传完成的后续处理动作
     */
    private UpCompletionHandler mUpCompletionHandler = new UpCompletionHandler() {
        @Override
        public void complete(String key, ResponseInfo info, JSONObject response) {
            //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
            try {
                Log.e(TAG, info.toString());
                Log.e(TAG, "上传是否成功:" + info.isOK());
                if (info.isOK()) {
                    Log.e(TAG, key);
                    String keyUrl = "http://7xv8gk.com2.z0.glb.qiniucdn.com/" + key;
                    Log.e(TAG, keyUrl);
                    mCompleteListener.callbackImageUrl(keyUrl);
                }
                if (response != null) {
                    Log.e(TAG, response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取上传token
     * @return
     */
    private String getUploadToken() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(Api.QINIU_TOKEN_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                InputStream inStream = conn.getInputStream();
                return inputStream2String(inStream, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String inputStream2String(InputStream in, String encoding) throws Exception {
        StringBuilder out = new StringBuilder();
        InputStreamReader inread = new InputStreamReader(in, encoding);

        char[] b = new char[4096];
        for (int n; (n = inread.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }

        in.close();
        return out.toString();
    }


    public interface QiniuCompleteListener{
        //返回图片地址
        void callbackImageUrl(String keyUrl);
    }

    public QiniuCompleteListener getCompleteListener() {
        return mCompleteListener;
    }

    public void setCompleteListener(QiniuCompleteListener completeListener) {
        mCompleteListener = completeListener;
    }
}

package com.jyyl.guideapp.http;

import android.content.Context;
import android.widget.Toast;

import com.jyyl.guideapp.MyApplication;
import com.jyyl.guideapp.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * @Fuction: 封装 异常处理
 * @Author: Shang
 * @Date: 2016/5/18  14:46
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private static Context mContext = MyApplication.getInstance().getApplicationContext();

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.network_false),
                    Toast.LENGTH_SHORT).show();
        }  else {
            Toast.makeText(mContext, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

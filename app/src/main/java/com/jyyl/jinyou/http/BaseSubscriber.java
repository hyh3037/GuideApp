package com.jyyl.jinyou.http;

import android.content.Context;
import android.widget.Toast;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.http.progress.ProgressCancelListener;
import com.jyyl.jinyou.http.progress.ProgressDialogHandler;
import com.jyyl.jinyou.utils.LogUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @Fuction: 用于在Http请求开始时，自动显示一个ProgressDialog 在Http请求结束是，关闭ProgressDialog 封装 异常处理
 * @Author: Shang
 * @Date: 2016/5/18  14:46
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private ProgressDialogHandler mProgressDialogHandler;

    private Context mContext;

    public BaseSubscriber(Context context) {
        mContext = context;
        mProgressDialogHandler = new ProgressDialogHandler(mContext, this, true);
    }

    private void showProgressDialog() {

        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG)
                    .sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * 对异常进行统一处理 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        LogUtils.d(e.toString());
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            if (504 == httpException.code()) {
                Toast.makeText(mContext, mContext.getString(R.string.toast_error_network),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "网络错误HTTP_" + httpException.code(), Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (e instanceof SocketTimeoutException) {
            Toast.makeText(mContext, mContext.getString(R.string.toast_socket_timeout),
                    Toast.LENGTH_SHORT).show();
        } else if (e instanceof ApiException) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }else if (e instanceof IOException){
            Toast.makeText(mContext, mContext.getString(R.string.toast_error_socket),
                    Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

}

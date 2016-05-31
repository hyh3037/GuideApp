package com.jyyl.jinyou.jpush;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.utils.LogUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @Fuction: Jpush 配置
 * @Author: Shang
 * @Date: 2016/5/25  9:18
 */
public class JpushManager {

    private static String TAG = "JpushManager";

    private static volatile JpushManager instance = null;

    private JpushManager() {
    }

    /**
     * 获取单例
     * @return 实例
     */
    public static JpushManager getInstance() {

        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (JpushManager.class) {
                if (instance == null) {
                    instance = new JpushManager();
                }
            }
        }

        return instance;
    }

    /**
     * 设置别名
     * 一般 App 的设置的调用入口，在任何方便的地方调用都可以。
     * @param context
     * @param alias 别名
     *
     */
    public void setAlias(Context context, String alias) {

        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(context,R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!JpushUtil.isValidTagAndAlias(alias)) {
            Toast.makeText(context,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtils.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtils.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtils.e(TAG, logs);
            }
            JpushUtil.showToast(logs, getApplicationContext());
        }
    };

    private static final int MSG_SET_ALIAS = 1001;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtils.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    LogUtils.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private Context getApplicationContext() {
        return MyApplication.getInstance().getApplicationContext();
    }
    /**======================================别名、标签END==========================================*/


}

package com.jyyl.jinyou.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.utils.SPUtils;
import com.jyyl.jinyou.utils.T;

/**
 * @Fuction: 定时提醒广播
 * @Author: Shang
 * @Date: 2016/5/9  10:05
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        T.showShortToast(context , "集合信息已发送");
        SPUtils.put(context , Sp.SP_KEY_MUSTER_TIME , "定时集合");


    }
}

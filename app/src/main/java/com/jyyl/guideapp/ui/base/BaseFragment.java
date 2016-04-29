package com.jyyl.guideapp.ui.base;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jyyl.guideapp.R;


/**
 * @author ShangBB
 * @Description: (Fragment 基类)
 * @date 2015/12/25 0025 下午 1:57
 */
public class BaseFragment extends Fragment {

    /***************************** 工具方法 **************************/

    /**
     * 通过类名启动Activity
     */
    protected void openActivity(Context context, Class<?> pClass) {
        openActivity(context, pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     */
    protected void openActivity(Context context, Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(context, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in, 0);
    }
}

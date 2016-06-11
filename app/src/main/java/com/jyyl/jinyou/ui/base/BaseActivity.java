package com.jyyl.jinyou.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.utils.ScreenUtils;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * @Fuction: Activity 基类
 * @Author: Shang
 * @Date: 2016/4/18  17:16
 */
public class BaseActivity extends AppCompatActivity
        implements SwipeBackActivityBase, OnClickListener{

    private SwipeBackActivityHelper mHelper;

    public static int EDGE_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setTranslucentStatus();
        initSwipeBack();

        MyApplication.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        processIntent();
        initViews();
        initListener();
    }

    /**
     * @Title initViews
     * @Description 子activity 覆盖这个方法初始化ui控件
     */
    protected void initViews() {
    }

    /**
     * @Title initListener
     * @Description 子activity 覆盖这个方法初始化ui控件的监听事件
     */
    protected void initListener() {
    }


    /**
     * @param v
     *         被点击的view
     *
     * @Title onViewClick
     * @Description 子类在这里面捕获控件的点击事件
     */
    protected void onViewClick(View v) {
    }

    @Override
    public void onClick(View v) {
        onViewClick(v);
    }

    /**
     * @Title processIntent
     * @Description 获取Intent携带数据
     */
    protected void processIntent() {
    }

    /**
     * ==================================Activity滑动切换====================================
     */
    private void initSwipeBack() {
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        //设置可以滑动的区域
        EDGE_SIZE = ScreenUtils.getScreenWidth(this) / 10;
        getSwipeBackLayout().setEdgeSize(EDGE_SIZE);
        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyApplication.getInstance().removeActivity(this);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }

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
        overridePendingTransition(R.anim.push_right_in, 0);
    }

    protected Dialog createLoadingDialog(Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null); // 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view); // 加载布局
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        loadingDialog.setCancelable(false); // 不可以用"返回键"取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }
}


package com.jyyl.guideapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.jyyl.guideapp.R;

/**
 * @Fuction: 选择头像对话框实现类
 * @Author: Shang
 * @Date: 2016/4/28  9:34
 */
public class SelectPhotoDialog extends Dialog implements View.OnClickListener{

    public SelectPhotoDialog(Context context) {
        super(context, R.style.selectPhotoDialog);
        //初始化布局
        setContentView(R.layout.dialog_select_photoview);
        Window dialogWindow = getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);

        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_gallery).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    private OnButtonClickListener onButtonClickListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
                onButtonClickListener.camera();
                break;
            case R.id.btn_gallery:
                onButtonClickListener.gallery();
                break;
            case R.id.btn_cancel:
                onButtonClickListener.cancel();
                break;

            default:
                break;
        }
    }

    /**
     * 按钮的监听器
     * @author Orathee
     * @date 2014年3月20日 下午4:28:39
     */
    public interface OnButtonClickListener{
        void camera();
        void gallery();
        void cancel();
    }



    public OnButtonClickListener getOnButtonClickListener() {
        return onButtonClickListener;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}

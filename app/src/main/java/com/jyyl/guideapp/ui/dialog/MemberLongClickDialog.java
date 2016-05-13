package com.jyyl.guideapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.jyyl.guideapp.R;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/5/11  8:32
 */
public class MemberLongClickDialog extends Dialog implements View.OnClickListener {

    private Button mMemberInfoBtn;
    private Button mDeleteBindingBtn;
    private OnMemberLongClickListener mMemberLongClickListener;

    public MemberLongClickDialog(Context context) {
        super(context, R.style.selectPhotoDialog);
        //初始化布局
        setContentView(R.layout.dialog_member_longclick);
        Window dialogWindow = getWindow();
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogWindow.setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(true);

        mMemberInfoBtn = (Button) findViewById(R.id.btn_member_info);
        mDeleteBindingBtn = (Button) findViewById(R.id.btn_delete_binding);
        mMemberInfoBtn.setOnClickListener(this);
        mDeleteBindingBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_member_info:
                mMemberLongClickListener.onMemberInfo();
                dismiss();
                break;
            case R.id.btn_delete_binding:
                mMemberLongClickListener.onDeleteBinding();
                dismiss();
                break;
        }

    }

    public interface OnMemberLongClickListener{
        void onMemberInfo();
        void onDeleteBinding();
    }

    public OnMemberLongClickListener getOnMemberLongClickListener() {
        return mMemberLongClickListener;
    }

    public void setOnMemberLongClickListener(OnMemberLongClickListener mMemberLongClickListener) {
        this.mMemberLongClickListener = mMemberLongClickListener;
    }
}

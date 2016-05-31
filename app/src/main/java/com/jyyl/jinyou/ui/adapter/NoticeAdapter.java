package com.jyyl.jinyou.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.It;
import com.jyyl.jinyou.entity.NoticeInfo;
import com.jyyl.jinyou.ui.activity.NoticeDetailsActivity;
import com.jyyl.jinyou.ui.activity.RefreshToolbarListener;
import com.jyyl.jinyou.ui.base.BaseAdapterHelper;
import com.jyyl.jinyou.ui.base.ViewHolder;

import java.util.List;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/5/5  17:01
 */
public class NoticeAdapter extends BaseAdapterHelper<NoticeInfo> {

    private boolean flage;
    private RefreshToolbarListener mListener;

    public NoticeAdapter(Context context, List<NoticeInfo> datas, int layoutId) {
        super(context, datas, layoutId);
        mListener = (RefreshToolbarListener) context;
    }

    public void setFlag(boolean flag){
        this.flage = flag;
    }

    @Override
    public void convert(ViewHolder holder, final NoticeInfo noticeInfo) {
        // 设置checkbox的显示状况
        if (flage) {
            holder.setVisible(R.id.cb_item_select, true);
        } else {
            holder.setVisible(R.id.cb_item_select, false);
        }
        holder.setChecked(R.id.cb_item_select, noticeInfo.isCheck());
        holder.setText(R.id.notice_msg, noticeInfo.getContent());

        holder.setOnClickListener(R.id.item_notice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flage) {
                    if (noticeInfo.isCheck()) {
                        noticeInfo.setCheck(false);
                    } else {
                        noticeInfo.setCheck(true);
                    }
                    mListener.refreshToolbar();
                } else {
                    Intent intent = new Intent(mContext , NoticeDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(It.START_INTENT_WITH, It.ACTIVITY_NOTICE);
                    bundle.putString(It.BUNDLE_KEY_NOTICE_MSG, noticeInfo.getContent());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, 0);
                }
            }
        });
    }
}

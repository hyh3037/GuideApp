package com.jyyl.guideapp.ui.adapter;

import android.content.Context;
import android.view.View;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.entity.NoticeInfo;
import com.jyyl.guideapp.ui.activity.RefreshToolbarListener;
import com.jyyl.guideapp.ui.base.BaseAdapterHelper;
import com.jyyl.guideapp.ui.base.ViewHolder;
import com.jyyl.guideapp.utils.T;

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
        holder.setText(R.id.notice_msg, noticeInfo.getMessage());

        holder.setOnClickListener(R.id.item_notice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flage) {
                    if (noticeInfo.isCheck()) {
                        noticeInfo.setIsCheck(false);
                    } else {
                        noticeInfo.setIsCheck(true);
                    }
                    mListener.refreshToolbar();
                } else {
                    T.showShortToast(mContext, noticeInfo.getMessage());
                }
            }
        });

        holder.setOnLongClickListener(R.id.item_notice, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!flage){
                    T.showShortToast(mContext, "删除");
                    return true;
                }
                return false;
            }
        });
    }
}

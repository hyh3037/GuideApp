package com.jyyl.jinyou.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.DeviceInfo;
import com.jyyl.jinyou.ui.activity.DeviceInfoActivity;
import com.jyyl.jinyou.ui.activity.RefreshToolbarListener;
import com.jyyl.jinyou.ui.base.BaseAdapterHelper;
import com.jyyl.jinyou.ui.base.ViewHolder;

import java.util.List;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/5/5  17:01
 */
public class DeviceManageAdapter extends BaseAdapterHelper<DeviceInfo> {

    private boolean flage;
    private RefreshToolbarListener mListener;

    public DeviceManageAdapter(Context context, List<DeviceInfo> datas, int layoutId) {
        super(context, datas, layoutId);
        mListener = (RefreshToolbarListener) context;
    }

    public void setFlag(boolean flag){
        this.flage = flag;
    }

    @Override
    public void convert(ViewHolder holder, final DeviceInfo deviceInfo) {
        // 设置checkbox的显示状况
        if (flage) {
            holder.setVisible(R.id.cb_item_select, true);
        } else {
            holder.setVisible(R.id.cb_item_select, false);
        }
        holder.setChecked(R.id.cb_item_select, deviceInfo.isCheck());
        holder.setText(R.id.tv_number, String.valueOf(deviceInfo.getNumber()));
        holder.setText(R.id.tv_device_id, deviceInfo.getDeviceId());

        holder.setOnClickListener(R.id.item_device_manage, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flage) {
                    if (deviceInfo.isCheck()) {
                        deviceInfo.setIsCheck(false);
                    } else {
                        deviceInfo.setIsCheck(true);
                    }
                    mListener.refreshToolbar();
                } else {
                    Intent intent = new Intent(mContext, DeviceInfoActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });

//        holder.setOnLongClickListener(R.id.item_device_manage, new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (!flage){
//                    T.showShortToast(mContext, "删除");
//                    return true;
//                }
//                return false;
//            }
//        });
    }
}

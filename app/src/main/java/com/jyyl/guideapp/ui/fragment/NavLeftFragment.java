package com.jyyl.guideapp.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.ui.activity.DeviceManageActivity;
import com.jyyl.guideapp.ui.activity.MemberManageActivity;
import com.jyyl.guideapp.ui.activity.NoticesActivity;
import com.jyyl.guideapp.ui.activity.PersonalInformationActivity;
import com.jyyl.guideapp.ui.activity.SettingsActivity;
import com.jyyl.guideapp.ui.base.BaseFragment;

/**
 * @Fuction: 侧滑导航菜单
 * @Author: Shang
 * @Date: 2016/4/18  17:16
 */
public class NavLeftFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.navigation_left, container,false);
        mContext=getActivity();
        initView();
        return view;
    }

    //初始化控件
    private void initView() {
        ImageView photoView = (ImageView) view.findViewById(R.id.iv_nav_photoview);
        ImageView notice = (ImageView) view.findViewById(R.id.iv_nav_notice);
        TextView deviceManagement = (TextView) view.findViewById(R.id.tv_nav_device);
        TextView healthManagement = (TextView) view.findViewById(R.id.tv_nav_health);
        TextView setting = (TextView) view.findViewById(R.id.tv_nav_setting);

        photoView.setOnClickListener(this);
        notice.setOnClickListener(this);
        deviceManagement.setOnClickListener(this);
        healthManagement.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_nav_photoview:
                openActivity(mContext, PersonalInformationActivity.class);
                break;
            case R.id.iv_nav_notice:
                openActivity(mContext, NoticesActivity.class);
                break;
            case R.id.tv_nav_device:
                openActivity(mContext, DeviceManageActivity.class);
                break;
            case R.id.tv_nav_health:
                openActivity(mContext, MemberManageActivity.class);
                break;
            case R.id.tv_nav_setting:
                openActivity(mContext, SettingsActivity.class);
                break;
        }
    }
}

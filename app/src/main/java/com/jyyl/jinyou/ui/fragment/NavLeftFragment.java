package com.jyyl.jinyou.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.entity.GuideInfoResult;
import com.jyyl.jinyou.ui.activity.DeviceManageActivity;
import com.jyyl.jinyou.ui.activity.LoginActivity;
import com.jyyl.jinyou.ui.activity.MemberManageActivity;
import com.jyyl.jinyou.ui.activity.NoticesActivity;
import com.jyyl.jinyou.ui.activity.PersonalInformationActivity;
import com.jyyl.jinyou.ui.base.BaseFragment;
import com.jyyl.jinyou.update.UpdateManager;
import com.jyyl.jinyou.utils.SPUtils;

/**
 * @Fuction: 侧滑导航菜单
 * @Author: Shang
 * @Date: 2016/4/18  17:16
 */
public class NavLeftFragment extends BaseFragment implements View.OnClickListener{
    private View view;
    private Context mContext;

    private ImageView photoView;
    private TextView guideName;

    private NavCallback mNavCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mNavCallback =(NavCallback)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.navigation_left, container,false);
        mContext=getActivity();
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String guideInfoString = (String) SPUtils.get(mContext, Sp.SP_KEY_USER_OBJECT, "-1");
        if (!"-1".equals(guideInfoString)){
            GuideInfoResult guideInfoResult = new Gson()
                    .fromJson(guideInfoString, GuideInfoResult.class);

            Glide.with(this).load(guideInfoResult.getHeadAddrdss())
                    .error(R.drawable.default_photo)
                    .into(photoView);
            guideName.setText(guideInfoResult.getMemberName());
        }
    }

    //初始化控件
    private void initView() {
        photoView = (ImageView) view.findViewById(R.id.iv_nav_photoview);
        guideName = (TextView) view.findViewById(R.id.tv_guide_name);

        ImageView noticeIv = (ImageView) view.findViewById(R.id.iv_nav_notice);
        TextView deviceTv = (TextView) view.findViewById(R.id.tv_nav_device);
        TextView memberTv = (TextView) view.findViewById(R.id.tv_nav_member);
        TextView checkUpdateTv = (TextView) view.findViewById(R.id.tv_nav_ckeck_updates);
        TextView exitLoginTv = (TextView) view.findViewById(R.id.tv_nav_exit_login);

        photoView.setOnClickListener(this);
        noticeIv.setOnClickListener(this);
        deviceTv.setOnClickListener(this);
        memberTv.setOnClickListener(this);
        checkUpdateTv.setOnClickListener(this);
        exitLoginTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_nav_photoview:
                openActivity(mContext, PersonalInformationActivity.class);
                closeLeftMenu();
                break;
            case R.id.iv_nav_notice:
                openActivity(mContext, NoticesActivity.class);
                closeLeftMenu();
                break;
            case R.id.tv_nav_device:
                openActivity(mContext, DeviceManageActivity.class);
                closeLeftMenu();
                break;
            case R.id.tv_nav_member:
                openActivity(mContext, MemberManageActivity.class);
                closeLeftMenu();
                break;
            case R.id.tv_nav_ckeck_updates:
                //检查版本更新
                new UpdateManager(mContext);
                mNavCallback.closeLeftMenu();
                break;
            case R.id.tv_nav_exit_login:
                openActivity(mContext, LoginActivity.class);
                closeLeftMenu();
                SPUtils.put(mContext, Sp.SP_KEY_LOGIN_STATE, false);
                break;
            //            case R.id.tv_nav_setting:
            //                openActivity(mContext, SettingsActivity.class);
            //                break;
        }

    }

    private void closeLeftMenu(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNavCallback.closeLeftMenu();
            }
        }, 500);
    }


    public interface NavCallback{
        void closeLeftMenu();
    }
}

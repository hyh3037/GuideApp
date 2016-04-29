package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.view.SelectPhotoDialog;
import com.jyyl.guideapp.utils.T;
import com.jyyl.guideapp.widget.CircleImageView;

/**
 * @Fuction: 设置界面
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class PersonalInformationActivity extends BaseActivity
        implements SelectPhotoDialog.OnButtonClickListener{

    private Toolbar toolbar;
    private TextView mEditBtn;
    private Context mContext;

    private CircleImageView mPhotoView;

    private SelectPhotoDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        initToolBar();
        mContext = this;
    }
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("个人信息");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        super.initViews();
        mEditBtn = (TextView) findViewById(R.id.toolbar_right_btn);
        mEditBtn.setVisibility(View.VISIBLE);
        mPhotoView = (CircleImageView) findViewById(R.id.iv_personal_photoview);
        dialog = new SelectPhotoDialog(this);
        dialog.setOnButtonClickListener(this);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mEditBtn.setOnClickListener(this);
        mPhotoView.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()){
            case R.id.toolbar_right_btn:
                T.showShortToast(mContext, "编辑个人信息");
                break;
            case R.id.iv_personal_photoview:
                dialog.show();
                break;
        }
    }

    @Override
    public void camera() {
        T.showShortToast(mContext, "拍照");
        dialog.cancel();
    }

    @Override
    public void gallery() {
        T.showShortToast(mContext, "从相册选择");
        dialog.cancel();
    }

    @Override
    public void cancel() {
        dialog.cancel();
    }
}

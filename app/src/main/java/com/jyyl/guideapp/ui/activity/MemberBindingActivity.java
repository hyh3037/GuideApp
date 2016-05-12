package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.bean.DeviceInfo;
import com.jyyl.guideapp.constans.BaseConstans;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.dialog.SelectDeviceDialog;
import com.jyyl.guideapp.utils.BitmapUtils;
import com.jyyl.guideapp.utils.FileUtils;
import com.jyyl.guideapp.utils.SelectPictureUtils;
import com.jyyl.guideapp.utils.T;
import com.jyyl.guideapp.widget.CircleImageView;

import java.io.IOException;

/**
 * @Fuction: 绑定游客信息
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class MemberBindingActivity extends BaseActivity implements SelectDeviceDialog
                                                                        .OnSelectDeviceListener {
    private Toolbar toolbar;
    private Context mContext;

    private CircleImageView mPhotoView;
    private TextView mDeviceNumberTv;
    private TextView mDeviceIdTv;
    private TextView mBindingBtn;

    private String mMemberId;
    private Uri photoUri = null;
    private Bitmap cropBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_member_binding);
        initToolBar();
        initUri();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("新增游客");
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

        mMemberId = getIntent().getStringExtra("memberId");
        mPhotoView = (CircleImageView) findViewById(R.id.iv_member_photoview);
        mDeviceNumberTv = (TextView) findViewById(R.id.tv_member_device_number);
        mDeviceNumberTv.setVisibility(View.GONE);
        mDeviceIdTv = (TextView) findViewById(R.id.tv_member_device_id);
        mBindingBtn = (TextView) findViewById(R.id.btn_binding_member);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mPhotoView.setOnClickListener(this);
        mDeviceIdTv.setOnClickListener(this);
        mBindingBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.iv_member_photoview:
                SelectPictureUtils.startCamearPicCut(this, photoUri);
                break;
            case R.id.tv_member_device_id:
                SelectDeviceDialog selectDeviceDialog = new SelectDeviceDialog();
                selectDeviceDialog.show(getFragmentManager(), "SelectDevice");
                break;
            case R.id.btn_binding_member:
                T.showShortToast(mContext, "新增游客成功");
                openActivity(mContext, MemberManageActivity.class);
                finish();
                break;
        }
    }

    private void initUri() {
        if (!FileUtils.hasSdcard()) {
            T.showShortToast(mContext, "没有找到SD卡，请检查SD卡是否存在");
            return;
        }
        try {
            photoUri = FileUtils.getUriByFileDirAndFileName(BaseConstans.SystemPicture
                    .SAVE_DIRECTORY, "member_" + mMemberId + ".jpg");
        } catch (IOException e) {
            T.showShortToast(mContext, "创建文件失败");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 点击取消按钮
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case BaseConstans.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                SelectPictureUtils.startPhotoZoom(this, photoUri, photoUri, 600);
                break;
            case BaseConstans.SystemPicture.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果
                setPhotoView();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通过URI设置头像
     */
    private void setPhotoView() {
        try {
            cropBitmap = BitmapUtils.getBitmapFromUri(photoUri, this);
            //通过获取uri的方式，直接解决了报空和图片像素高的oom问题

            if (cropBitmap != null) {
                mPhotoView.setImageBitmap(cropBitmap);
            } else {
                mPhotoView.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeviceInfo(DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            mDeviceNumberTv.setVisibility(View.VISIBLE);
            mDeviceNumberTv.setText(String.valueOf(deviceInfo.getNumber()));
            mDeviceIdTv.setText(deviceInfo.getDeviceId());
        }
    }
}

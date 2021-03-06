package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.BaseConstans;
import com.jyyl.jinyou.entity.DeviceInfo;
import com.jyyl.jinyou.http.ApiException;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.http.HttpResult;
import com.jyyl.jinyou.http.ResultStatus;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.ui.dialog.SelectDeviceDialog;
import com.jyyl.jinyou.utils.FileUtils;
import com.jyyl.jinyou.utils.ImageUtils;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.QiNiuUploadUtils;
import com.jyyl.jinyou.utils.SelectPictureUtils;
import com.jyyl.jinyou.utils.T;
import com.jyyl.jinyou.widget.CircleImageView;
import com.jyyl.jinyou.widget.CleanEditText;

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
    private TextView mDeviceImeiTv;
    private TextView mBindingBtn;
    private CleanEditText mMemberNameEt;
    private CleanEditText mLinkmanNameEt;
    private CleanEditText mLinkmanTelEt;
//    private CleanEditText mMemberCardEt;
    private String mMemberName, mLinkmanName, mLinkmanTel, mMemberCard,
            headAddress, teamId, deviceId, mDeviceImei;

    private Uri photoUri = null;
    private Bitmap cropBitmap = null;
    private QiNiuUploadUtils qiNiuUploadUtils;


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
        teamId = getIntent().getExtras().getString("mTeamId");
        LogUtils.d("mTeamId==>>"+ teamId );

//        mMemberCardEt = (CleanEditText) findViewById(R.id.et_member_id_card);
        mLinkmanTelEt = (CleanEditText) findViewById(R.id.et_linkman_tel);
        mLinkmanNameEt = (CleanEditText) findViewById(R.id.et_linkman_name);
        mMemberNameEt = (CleanEditText) findViewById(R.id.et_member_name);

        mPhotoView = (CircleImageView) findViewById(R.id.iv_member_photoview);

        mDeviceNumberTv = (TextView) findViewById(R.id.tv_member_device_number);
        mDeviceNumberTv.setVisibility(View.GONE);
        mDeviceImeiTv = (TextView) findViewById(R.id.tv_member_device_imei);

        mBindingBtn = (TextView) findViewById(R.id.btn_binding_member);
    }

    @Override
    protected void initListener() {
        super.initListener();

        mPhotoView.setOnClickListener(this);
        mDeviceImeiTv.setOnClickListener(this);
        mBindingBtn.setOnClickListener(this);

        initQiniuListener();
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.iv_member_photoview:
                SelectPictureUtils.startCamearPicCut(this, photoUri);
                break;
            case R.id.tv_member_device_imei:
                SelectDeviceDialog selectDeviceDialog = new SelectDeviceDialog();
                selectDeviceDialog.show(getFragmentManager(), "SelectDevice");
                break;
            case R.id.btn_binding_member:
                mMemberName = mMemberNameEt.getText().toString();
                mLinkmanName = mLinkmanNameEt.getText().toString();
                mLinkmanTel = mLinkmanTelEt.getText().toString();
//                mMemberCard = mMemberCardEt.getText().toString();
                mDeviceImei = mDeviceImeiTv.getText().toString();
                if (mMemberName.isEmpty()){
                    T.showShortToast(mContext, "游客姓名不能为空");
                }else if (mLinkmanTel.isEmpty()){
                    T.showShortToast(mContext, "监护人电话不能为空");
                } else if (mDeviceImei.isEmpty()) {
                    T.showShortToast(mContext, "请先选择绑定设备");
                } else {
                    //上传游客信息到服务器
                    HttpMethods.getInstance().addMember(mMemberName,mLinkmanName,mLinkmanTel,
                            null,headAddress,teamId,deviceId)
                            .subscribe(new BaseSubscriber<HttpResult>() {
                                @Override
                                public void onNext(HttpResult httpResult) {
                                    if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                                        T.showShortToast(mContext, "添加游客成功");
                                        openActivity(mContext, MemberManageActivity.class);
                                        finish();
                                    } else {
                                        throw new ApiException(httpResult.getDescritpion());
                                    }
                                }
                            });
                }

                break;
        }
    }

    private void initQiniuListener() {
        qiNiuUploadUtils = new QiNiuUploadUtils();
        qiNiuUploadUtils.setCompleteListener(new QiNiuUploadUtils.QiniuCompleteListener() {
            @Override
            public void callbackImageUrl(String keyUrl) {
                headAddress = keyUrl;
            }
        });
    }

    private void initUri() {
        if (!FileUtils.hasSdcard()) {
            T.showShortToast(mContext, "没有找到SD卡，请检查SD卡是否存在");
            return;
        }
        try {
            photoUri = FileUtils.getUriByFileDir(BaseConstans.SystemPicture
                    .SAVE_DIRECTORY);
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
            cropBitmap = ImageUtils.getBitmapFromUri(photoUri, this);
            //上传图片到七牛云
            String key = FileUtils.getUuidName();
            if (cropBitmap != null) {
                qiNiuUploadUtils.upload(cropBitmap, key);
            }
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
            mDeviceImeiTv.setText(deviceInfo.getDeviceInfoResult().getDeviceIMEI());

            deviceId = deviceInfo.getDeviceInfoResult().getDeviceId();
        }
    }
}

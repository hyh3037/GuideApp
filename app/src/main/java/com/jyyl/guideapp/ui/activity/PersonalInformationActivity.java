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
import com.jyyl.guideapp.constans.BaseConstans;
import com.jyyl.guideapp.constans.Sp;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.dialog.SelectPhotoDialog;
import com.jyyl.guideapp.utils.ImageUtils;
import com.jyyl.guideapp.utils.FileUtils;
import com.jyyl.guideapp.utils.LogUtils;
import com.jyyl.guideapp.utils.QiNiuUploadUtils;
import com.jyyl.guideapp.utils.SPUtils;
import com.jyyl.guideapp.utils.SelectPictureUtils;
import com.jyyl.guideapp.utils.T;
import com.jyyl.guideapp.widget.CircleImageView;

import java.io.IOException;

/**
 * @Fuction: 导游个人信息
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class PersonalInformationActivity extends BaseActivity
        implements SelectPhotoDialog.OnButtonClickListener {

    private Toolbar toolbar;
    private TextView mEditBtn;
    private Context mContext;

    private CircleImageView mPhotoView;

    private SelectPhotoDialog dialog;

    private Uri cameraUri = null;   //拍照保存图片的URI
    private Uri photoUri = null;    //被裁剪的图片URI
    private Uri cutUri = null;      //裁剪后图片的URI
    private Bitmap cropBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_personal_information);
        initToolBar();
        initUri();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPhotoView();
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
        mEditBtn = (TextView) findViewById(R.id.toolbar_right_tv);
        mEditBtn.setText("编辑");
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
        switch (v.getId()) {
            case R.id.toolbar_right_tv:
                openActivity(mContext, PersonalInfoEditActivity.class);
                break;
            case R.id.iv_personal_photoview:
                dialog.show();
                break;
        }
    }

    private void initUri() {
        if (!FileUtils.hasSdcard()) {
            T.showShortToast(mContext, "没有找到SD卡，请检查SD卡是否存在");
            return;
        }
        try {
            cameraUri = FileUtils.getUriByFileDirAndFileName(BaseConstans.SystemPicture
                    .SAVE_DIRECTORY, BaseConstans.SystemPicture.SAVE_PIC_NAME);
            cutUri = FileUtils.getUriByFileDirAndFileName(BaseConstans.SystemPicture
                    .SAVE_DIRECTORY, BaseConstans.SystemPicture.SAVE_CUT_PIC_NAME);
        } catch (IOException e) {
            T.showShortToast(mContext, "创建文件失败");
        }
    }

    @Override
    public void camera() {
        photoUri = cameraUri;
        SelectPictureUtils.startCamearPicCut(this, photoUri);
        dialog.cancel();
    }

    @Override
    public void gallery() {
        SelectPictureUtils.startImageCaptrue(this);
        dialog.cancel();
    }

    @Override
    public void cancel() {
        dialog.cancel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 点击取消按钮
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case BaseConstans.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                photoUri = cameraUri;
                SelectPictureUtils.startPhotoZoom(this, photoUri, cutUri, 600);
                break;
            case BaseConstans.SystemPicture.PHOTO_REQUEST_GALLERY://相册获取

                if (data == null) {
                    T.showShortToast(this, "选择图片文件出错");
                    return;
                }
                photoUri = data.getData();
                if (photoUri == null) {
                    T.showShortToast(this, "获取图片文件Uri有误");
                    return;
                }
                SelectPictureUtils.startPhotoZoom(this, photoUri, cutUri, 600);
                break;
            case BaseConstans.SystemPicture.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果，这个过程比较重要

                /*Bitmap bit = data.getExtras().getParcelable("data");
                //不要再用data的方式了，会出现activity result 的时候data == null的空的情况
                mPhotoView.setImageBitmap(bit);*/
                setPhotoView();

                //上传图片到七牛云
                String key = "guide_"+ SPUtils.get(mContext, Sp.SP_KEY_LAST_LOGIN_ACCOUNT,null);
                if (cropBitmap != null)
                    QiNiuUploadUtils.getInstance().upload(cropBitmap,key);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 通过URI设置头像
     */
    private void setPhotoView() {
        try {
            cropBitmap = ImageUtils.getBitmapFromUri(cutUri, this); //通过获取uri的方式，直接解决了报空和图片像素高的oom问题

            if (cropBitmap != null) {
                String path = FileUtils.getFileByUri(this, cutUri).getAbsolutePath();
                int degree = ImageUtils.getBitmapDegree(path);//检查是否有被旋转，并进行纠正
                LogUtils.d("path:"+path+"\ndegree"+degree);
                if (degree != 0) {
                    cropBitmap = ImageUtils.rotateBitmapByDegree(cropBitmap, degree);
                }
                mPhotoView.setImageBitmap(cropBitmap);
            } else {
                mPhotoView.setImageResource(R.mipmap.ic_launcher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

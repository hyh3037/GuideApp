package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.bean.DeviceInfo;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.base.BaseAdapterHelper;
import com.jyyl.guideapp.ui.base.ViewHolder;
import com.jyyl.guideapp.utils.T;

import java.util.ArrayList;

/**
 * @Fuction: 设备管理
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class DeviceManageActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView mEditBtn;
    private Context mContext;

    private EditText mDeviceIdEt;
    private ImageButton mScanBtn;
    private Button mBinddingBtn;

    private ListView mListView;
    private BaseAdapterHelper mAdapter;

    private ArrayList<DeviceInfo> mDatas = new ArrayList<DeviceInfo>();
    private int number = 1;
    private String deviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_device_management);
        initToolBar();
        initListview();


    }

    private void initListview() {
        mListView = (ListView) findViewById(R.id.device_listview);
        mAdapter = new BaseAdapterHelper<DeviceInfo>(mContext,
                mDatas,R.layout.item_device_listview) {
            @Override
            public void convert(ViewHolder holder, DeviceInfo deviceInfo) {
                holder.setText(R.id.tv_number, String.valueOf(deviceInfo.getNumber()));
                holder.setText(R.id.tv_device_id, deviceInfo.getDeviceId());
            }
        };
        mListView.setAdapter(mAdapter);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("设备管理");
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
        mEditBtn.setText("编辑");
        mEditBtn.setVisibility(View.VISIBLE);
        mDeviceIdEt = (EditText) findViewById(R.id.et_device_id);
        mScanBtn = (ImageButton) findViewById(R.id.btn_scan);
        mBinddingBtn = (Button) findViewById(R.id.btn_binging_device);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mEditBtn.setOnClickListener(this);
        mDeviceIdEt.setOnClickListener(this);
        mScanBtn.setOnClickListener(this);
        mBinddingBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()){
            case R.id.btn_scan:
                T.showShortToast(mContext,"扫描二维码");
                break;
            case R.id.btn_binging_device:
                deviceId = mDeviceIdEt.getText().toString().trim();
                if (TextUtils.isEmpty(deviceId)){
                    T.showShortToast(this,"请输入设备编号");
                }else if (containsDeciveId(deviceId)){
                    T.showShortToast(this,"设备已存在");
                }else {
                    mDatas.add(new DeviceInfo(number,mDeviceIdEt.getText().toString()));
                    number++;
                    mAdapter.notifyDataSetChanged();
                    T.showShortToast(this,"绑定成功");
                }
                break;
            case R.id.toolbar_right_btn:
                T.showShortToast(mContext, "编辑设备");
                break;
        }
    }

    private boolean containsDeciveId(String deviceId){
        for (DeviceInfo deviceInfo : mDatas){
            if (deviceId.equals(deviceInfo.getDeviceId()))
                return true;
        }
        return false;
    }
}

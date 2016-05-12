package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.jyyl.guideapp.R;
import com.jyyl.guideapp.bean.DeviceInfo;
import com.jyyl.guideapp.ui.adapter.DeviceManageAdapter;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.utils.LogUtils;
import com.jyyl.guideapp.utils.T;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Fuction: 设备管理
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class DeviceManageActivity extends BaseActivity implements RefreshToolbarListener{
    private Toolbar toolbar;
    private ImageView mToolbarRightIv;
    private CheckBox mToolbarCheckBox;
    private TextView mToolbarRightTv;
    private String mTitle = "设备管理";
    private Context mContext;

    private View mBindingView;
    private EditText mDeviceIdEt;
    private ImageButton mScanBtn;
    private Button mBinddingBtn;

    private ListView mListView;
    private DeviceManageAdapter mAdapter;

    private ArrayList<DeviceInfo> mDatas = new ArrayList<DeviceInfo>();
    private boolean flage = false; //是否删除界面
    private boolean isChecked = false; //toolbar的checkbox选择状态
    private boolean isDelFinish = false; //false:取消 true:完成
    private int number = 1;
    private String deviceId = null;

    public static final int REQUEST_CODE = 0;

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
        mAdapter = new DeviceManageAdapter(mContext,mDatas,R.layout.item_device_listview);
        mAdapter.setFlag(flage);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(findViewById(R.id.device_empty_view));
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(mTitle);
        toolbar.setTitleTextColor(Color.WHITE);
        initToolbarAddview();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化toolbar中的其他控件
     */
    private void initToolbarAddview() {
        mToolbarRightIv = (ImageView) findViewById(R.id.toolbar_right_iv);
        mToolbarCheckBox = (CheckBox) findViewById(R.id.toolbar_checkbox);
        mToolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
        mToolbarRightIv.setOnClickListener(this);
        mToolbarCheckBox.setOnClickListener(this);
        mToolbarRightTv.setOnClickListener(this);
        mToolbarRightIv.setImageResource(R.drawable.delete);
        if (mDatas.size() > 0){
            mToolbarRightIv.setVisibility(View.VISIBLE);
        }else {
            mToolbarRightIv.setVisibility(View.GONE);
        }
        mToolbarCheckBox.setVisibility(View.GONE);
        mToolbarRightTv.setVisibility(View.GONE);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mBindingView = findViewById(R.id.view_binding_device);
        mDeviceIdEt = (EditText) findViewById(R.id.et_device_id);
        mScanBtn = (ImageButton) findViewById(R.id.btn_scan);
        mBinddingBtn = (Button) findViewById(R.id.btn_binding_device);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mDeviceIdEt.setOnClickListener(this);
        mScanBtn.setOnClickListener(this);
        mBinddingBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()){
            case R.id.btn_scan:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_binding_device:
                deviceId = mDeviceIdEt.getText().toString().trim();
                addDevice(deviceId);
                break;
            case R.id.toolbar_right_iv://进入批量删除模式
                flage = true;
                mAdapter.setFlag(true);
                toolbar.setTitle(null);
                toolbar.setNavigationIcon(null);
                mBindingView.setVisibility(View.GONE);
                mToolbarRightIv.setVisibility(View.GONE);
                mToolbarCheckBox.setVisibility(View.VISIBLE);
                mToolbarRightTv.setVisibility(View.VISIBLE);
                refresh();
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.toolbar_checkbox:
                if (isChecked) {
                    //全不选
                    for (DeviceInfo deviceInfo : mDatas) {
                        deviceInfo.setIsCheck(false);
                    }
                    mToolbarCheckBox.setChecked(false);
                    isChecked = false;
                } else {
                    //全选
                    for (DeviceInfo deviceInfo : mDatas) {
                        deviceInfo.setIsCheck(true);
                    }
                    mToolbarCheckBox.setChecked(true);
                    isChecked = true;
                }
                refresh();
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.toolbar_right_tv: //完成（确认删除）
                if (isDelFinish) {
                    itemRemove(mDatas);
                    for (DeviceInfo DeviceInfo : mDatas) {
                        DeviceInfo.setIsCheck(false);
                    }
                }
                flage = false;
                mAdapter.setFlag(false);
                toolbar.setTitle(mTitle);
                toolbar.setNavigationIcon(R.drawable.ic_back);
                mBindingView.setVisibility(View.VISIBLE);
                mToolbarCheckBox.setVisibility(View.GONE);
                mToolbarRightTv.setVisibility(View.GONE);
                if (mDatas.size() > 0){
                    mToolbarRightIv.setVisibility(View.VISIBLE);
                }else {
                    mToolbarRightIv.setVisibility(View.GONE);
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void itemRemove(ArrayList<DeviceInfo> list) {
        Iterator<DeviceInfo> it = list.iterator();
        while (it.hasNext()) {
            DeviceInfo deviceInfo = it.next();
            if (deviceInfo.isCheck()) {
                it.remove();
            }
        }
    }

    /**
     * 刷新 批量删除模式 toolbar
     */
    @Override
    public void refreshToolbar() {
        refresh();
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        int mCheckedNum = 0;
        for (DeviceInfo info : mDatas) {
            if (info.isCheck()) {
                mCheckedNum++;
            }
        }
        mToolbarCheckBox.setText(mCheckedNum + " 已选中");

        if (flage && mCheckedNum > 0) {
            isDelFinish = true;
            mToolbarRightTv.setText("完成");
        } else {
            isDelFinish = false;
            mToolbarRightTv.setText("取消");
        }

        if (mCheckedNum == mDatas.size()) {
            mToolbarCheckBox.setChecked(true);
            isChecked = true;
        } else {
            mToolbarCheckBox.setChecked(false);
            isChecked = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){ //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            deviceId = bundle.getString("result");
            LogUtils.d(deviceId);
            addDevice(deviceId);
        }
    }

    /**
     * 添加设备
     * @param deviceId id
     */
    private void addDevice(String deviceId) {
        if (TextUtils.isEmpty(deviceId)){
            T.showShortToast(this, "请输入设备编号");
        }else if (containsDeciveId(deviceId)){
            T.showShortToast(this,"设备已存在");
        }else {
            mDatas.add(new DeviceInfo(number,deviceId));
            number++;
            if (mDatas.size() > 0){
                mToolbarRightIv.setVisibility(View.VISIBLE);
            }else {
                mToolbarRightIv.setVisibility(View.GONE);
            }
            mAdapter.notifyDataSetChanged();
            T.showShortToast(this,"绑定成功");
        }
    }

    /**
     * 检查设备是否已存在
     * @param deviceId
     * @return
     */
    private boolean containsDeciveId(String deviceId){
        for (DeviceInfo deviceInfo : mDatas){
            if (deviceId.equals(deviceInfo.getDeviceId()))
                return true;
        }
        return false;
    }
}

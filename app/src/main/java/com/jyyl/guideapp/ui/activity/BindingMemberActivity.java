package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.bean.DeviceInfo;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.base.BaseAdapterHelper;
import com.jyyl.guideapp.ui.base.ViewHolder;

import java.util.ArrayList;

/**
 * @Fuction: 绑定游客和设备
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class BindingMemberActivity extends BaseActivity {
    private Toolbar toolbar;
    private Context mContext;

    private ListView mListView;
    private BaseAdapterHelper mAdapter;

    private ArrayList<DeviceInfo> mDatas = new ArrayList<DeviceInfo>();
    private int number = 1;
    private String deviceId = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_binding_member);
        initToolBar();
        initData();
        initListview();
    }

    private void initListview() {
        mListView = (ListView) findViewById(R.id.binding_member_listview);
        mAdapter = new BaseAdapterHelper<DeviceInfo>(mContext,
                mDatas, R.layout.item_device_listview) {
            @Override
            public void convert(ViewHolder holder, DeviceInfo deviceInfo) {
                holder.setText(R.id.tv_number, String.valueOf(deviceInfo.getNumber()));
                holder.setText(R.id.tv_device_id, deviceInfo.getDeviceId());
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("deviceId", mDatas.get(position).getDeviceId());
                openActivity(mContext, MemberInfoActivity.class, bundle);
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("绑定游客");
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
        Button mBindingEnd = (Button) findViewById(R.id.btn_bindding_end);
        mBindingEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        for (number = 1; number < 10; number++) {
            deviceId = "10000" + number;
            mDatas.add(new DeviceInfo(number, deviceId));
        }
    }
}

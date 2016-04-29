package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.bean.NoticeInfo;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.base.BaseAdapterHelper;
import com.jyyl.guideapp.ui.base.ViewHolder;
import com.jyyl.guideapp.utils.T;

import java.util.ArrayList;

/**
 * @Fuction: 设置界面
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class NoticesActivity extends BaseActivity {
    private Toolbar toolbar;
    private Context mContext;

    private ListView mListView;
    private ArrayList<NoticeInfo> mDatas = new ArrayList<NoticeInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);
        mContext = this;
        initToolBar();
        initDatas();
        initListview();
    }

    private void initListview() {
        mListView = (ListView) findViewById(R.id.notices_listview);
        mListView.setAdapter(new BaseAdapterHelper<NoticeInfo>(mContext,
                mDatas,R.layout.item_notices_listview) {
            @Override
            public void convert(ViewHolder holder, NoticeInfo noticeInfo) {
                holder.setText(R.id.notice_msg,noticeInfo.getMessage());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T.showShortToast(mContext,mDatas.get(position).getMessage());
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("我的消息");
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

    /**
     * 测试数据
     */
    private void initDatas() {
        NoticeInfo msg = null;
        for (int i = 1; i < 10; i++) {
            msg = new NoticeInfo("测试信息" + i);
            mDatas.add(msg);
        }

    }

}

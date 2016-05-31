package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.NoticeInfo;
import com.jyyl.jinyou.jpush.NoticeCache;
import com.jyyl.jinyou.ui.adapter.NoticeAdapter;
import com.jyyl.jinyou.ui.base.BaseActivity;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @Fuction: 我的消息
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class NoticesActivity extends BaseActivity implements RefreshToolbarListener {
    private Toolbar toolbar;
    private ImageView mToolbarRightIv;
    private CheckBox mToolbarCheckBox;
    private TextView mToolbarRightTv;
    private Context mContext;

    private ListView mListView;
    private NoticeAdapter mAdapter;
    private boolean flage = false; //是否删除界面
    private boolean isChecked = false; //toolbar的checkbox选择状态
    private boolean isDelFinish = false; //false:取消 true:完成
    private LinkedList<NoticeInfo> mDatas = new LinkedList<>();

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
        mAdapter = new NoticeAdapter(mContext, mDatas, R.layout.item_notices_listview);
        mAdapter.setFlag(flage);
        mListView.setAdapter(mAdapter);
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("我的消息");
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
        mToolbarRightIv.setVisibility(View.VISIBLE);
        mToolbarCheckBox.setVisibility(View.GONE);
        mToolbarRightTv.setVisibility(View.GONE);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.toolbar_right_iv://进入批量删除模式
                flage = true;
                mAdapter.setFlag(true);
                toolbar.setTitle(null);
                toolbar.setNavigationIcon(null);
                mToolbarRightIv.setVisibility(View.GONE);
                mToolbarCheckBox.setVisibility(View.VISIBLE);
                mToolbarRightTv.setVisibility(View.VISIBLE);
                refresh();
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.toolbar_checkbox:
                if (isChecked) {
                    //全不选
                    for (NoticeInfo noticeInfo : mDatas) {
                        noticeInfo.setCheck(false);
                    }
                    mToolbarCheckBox.setChecked(false);
                    isChecked = false;
                } else {
                    //全选
                    for (NoticeInfo noticeInfo : mDatas) {
                        noticeInfo.setCheck(true);
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
                    for (NoticeInfo noticeInfo : mDatas) {
                        noticeInfo.setCheck(false);
                    }
                    NoticeCache.getInstance().saveNotcieCache(mDatas);
                }
                flage = false;
                mAdapter.setFlag(false);
                toolbar.setTitle("我的消息");
                toolbar.setNavigationIcon(R.drawable.ic_back);
                mToolbarRightIv.setVisibility(View.VISIBLE);
                mToolbarCheckBox.setVisibility(View.GONE);
                mToolbarRightTv.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void itemRemove(LinkedList<NoticeInfo> list) {
        Iterator<NoticeInfo> it = list.iterator();
        while (it.hasNext()) {
            NoticeInfo noticeInfo = it.next();
            if (noticeInfo.isCheck()) {
                it.remove();
            }
        }
    }

    /**
     * 测试数据
     */
    private void initDatas() {
        mDatas = NoticeCache.getInstance().getNoticeCache();
    }


    /**
     * 刷新toolbar
     */
    @Override
    public void refreshToolbar() {
        refresh();
        mAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        int mCheckedNum = 0;
        for (NoticeInfo info : mDatas) {
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
}

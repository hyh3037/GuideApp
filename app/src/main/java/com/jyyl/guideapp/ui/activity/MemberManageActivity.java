package com.jyyl.guideapp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.bean.MemberInfo;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.base.BaseAdapterHelper;
import com.jyyl.guideapp.ui.base.ViewHolder;
import com.jyyl.guideapp.utils.T;

import java.util.ArrayList;

/**
 * @Fuction: 游客管理（队员管理）
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class MemberManageActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView mDisbandThem;
    private Context mContext;

    private View mMemberView;
    private View mEmptyView;
    private Button mAddMemberBtn;
    private Button mBuildTeambtn;

    private ListView mListView;
    private BaseAdapterHelper mAdapter;
    private ArrayList<MemberInfo> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_management);
        mContext = this;
        initToolBar();
        initListview();
    }

    private void initListview() {
        mListView = (ListView) findViewById(R.id.member_listview);
        mAdapter = new BaseAdapterHelper<MemberInfo>(mContext,mDatas,R.layout.item_member_listview) {

            @Override
            public void convert(ViewHolder holder, MemberInfo memberInfo) {
                holder.setText(R.id.member_name,memberInfo.getName());
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("name", mDatas.get(position).getName());
                openActivity(mContext, HealthDataActivity.class, bundle);
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("游客管理");
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
        mMemberView = findViewById(R.id.member_view);
        mEmptyView = findViewById(R.id.empty_view);
        mAddMemberBtn = (Button) findViewById(R.id.btn_add_member);
        mBuildTeambtn = (Button) findViewById(R.id.btn_build_team);

        mDisbandThem = (TextView) findViewById(R.id.toolbar_right_btn);
        mDisbandThem.setText("解散团队");

        if (!mDatas.isEmpty()){
            showMemberView();
        }else {
            showEmptyView();
        }
    }

    //无旅行团
    private void showEmptyView() {
        mMemberView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mDisbandThem.setVisibility(View.GONE);
    }

    //有旅行团
    private void showMemberView() {
        mMemberView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mDisbandThem.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAddMemberBtn.setOnClickListener(this);
        mBuildTeambtn.setOnClickListener(this);
        mDisbandThem.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()){
            case R.id.toolbar_right_btn:
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                showEmptyView();
                break;

            case R.id.btn_add_member:
                T.showShortToast(this,"添加游客");
                break;

            case R.id.btn_build_team:
                initDatas();
                mAdapter.notifyDataSetChanged();
                showMemberView();
                T.showShortToast(this,"创建团队");
                break;
        }
    }

    /**
     * 测试数据
     */
    private void initDatas() {
        MemberInfo msg = null;
        for (int i = 1; i < 10; i++) {
            msg = new MemberInfo("游客" + i);
            mDatas.add(msg);
        }

    }
}

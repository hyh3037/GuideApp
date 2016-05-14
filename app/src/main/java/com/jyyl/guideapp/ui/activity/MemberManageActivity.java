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
import com.jyyl.guideapp.constans.BaseConstans;
import com.jyyl.guideapp.constans.Sp;
import com.jyyl.guideapp.entity.MemberInfo;
import com.jyyl.guideapp.ui.base.BaseActivity;
import com.jyyl.guideapp.ui.base.BaseAdapterHelper;
import com.jyyl.guideapp.ui.base.ViewHolder;
import com.jyyl.guideapp.ui.dialog.BuildTeamDialog;
import com.jyyl.guideapp.ui.dialog.MemberLongClickDialog;
import com.jyyl.guideapp.utils.LogUtils;
import com.jyyl.guideapp.utils.SPUtils;
import com.jyyl.guideapp.utils.T;

import java.util.ArrayList;

/**
 * @Fuction: 游客管理（队员管理）
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class MemberManageActivity extends BaseActivity
        implements BuildTeamDialog.OnBuildTeamListener, MemberLongClickDialog
        .OnMemberLongClickListener {
    private Toolbar toolbar;
    private TextView mDisbandThem;
    private Context mContext;

    private View mMemberView;
    private View mEmptyView;
    private Button mAddMemberBtn;
    private Button mBuildTeambtn;
    private String mTeamName;

    private ListView mListView;
    private BaseAdapterHelper mAdapter;
    private ArrayList<MemberInfo> mDatas = new ArrayList<>();
    private int longClickIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_member_management);
        initToolBar();
        initListview();
    }

    @Override
    protected void initViews() {
        super.initViews();
        mMemberView = findViewById(R.id.member_view);
        mEmptyView = findViewById(R.id.empty_view);
        mAddMemberBtn = (Button) findViewById(R.id.btn_add_member);
        mBuildTeambtn = (Button) findViewById(R.id.btn_build_team);
        mDisbandThem = (TextView) findViewById(R.id.toolbar_right_tv);
        mDisbandThem.setText("解散团队");
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
        switch (v.getId()) {
            case R.id.toolbar_right_tv: //解散团队
                SPUtils.put(mContext, Sp.SP_KEY_TEAM_NAME, BaseConstans.TEAM_NAME);
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
                toolbar.setTitle("游客管理");
                showEmptyView();
                break;

            case R.id.btn_add_member:
                Bundle bundle = new Bundle();
                bundle.putString("memberId", String.valueOf(mDatas.size()));
                openActivity(mContext, MemberBindingActivity.class);
                break;

            case R.id.btn_build_team:
                BuildTeamDialog buildTeamDialog = new BuildTeamDialog();
                buildTeamDialog.show(getFragmentManager(), "BuildTeam");
                break;
        }
    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitleTextColor(Color.WHITE);
        mTeamName = (String) SPUtils.get(mContext, Sp.SP_KEY_TEAM_NAME, BaseConstans.TEAM_NAME);
        if (mTeamName != null) {
            if (mTeamName.equals(BaseConstans.TEAM_NAME)) {
                toolbar.setTitle("游客管理");
                showEmptyView();
            } else {
                toolbar.setTitle(mTeamName);
                initDatas();
                showMemberView();
            }
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void initListview() {
        mListView = (ListView) findViewById(R.id.member_listview);
        mAdapter = new BaseAdapterHelper<MemberInfo>(mContext, mDatas, R.layout
                .item_member_listview) {

            @Override
            public void convert(ViewHolder holder, MemberInfo memberInfo) {
                holder.setText(R.id.member_name, memberInfo.getName());
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

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, final int position, long id) {
                MemberLongClickDialog longClickDialog = new MemberLongClickDialog();
                longClickDialog.setMemberName(mDatas.get(position).getName());
                longClickDialog.show(getFragmentManager(), "MemberLongClick");
                longClickIndex = position;
                return true;
            }
        });
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

    @Override
    public void setTeamInfo(String teamName) {
        LogUtils.d(teamName);
        if (teamName.isEmpty()) {
            T.showShortToast(mContext, "团队名称不能为空");
        } else {
            SPUtils.put(mContext, Sp.SP_KEY_TEAM_NAME, teamName);
            toolbar.setTitle(teamName);
            initDatas();
            mAdapter.notifyDataSetChanged();
            showMemberView();
            T.showShortToast(this, "团队创建成功");
        }
    }

    @Override
    public void onDeleteBinding() {
        mDatas.remove(longClickIndex);
        mAdapter.notifyDataSetChanged();
    }
}

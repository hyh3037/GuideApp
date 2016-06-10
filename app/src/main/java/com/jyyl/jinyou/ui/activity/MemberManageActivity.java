package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.MemberInfo;
import com.jyyl.jinyou.entity.MemberInfoResult;
import com.jyyl.jinyou.entity.TeamInfo;
import com.jyyl.jinyou.http.ApiException;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.http.HttpResult;
import com.jyyl.jinyou.http.ResultStatus;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.ui.base.BaseAdapterHelper;
import com.jyyl.jinyou.ui.base.ViewHolder;
import com.jyyl.jinyou.ui.dialog.BuildTeamDialog;
import com.jyyl.jinyou.ui.dialog.DeleteTeamDialog;
import com.jyyl.jinyou.ui.dialog.MemberLongClickDialog;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * @Fuction: 游客管理（队员管理）
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class MemberManageActivity extends BaseActivity
        implements BuildTeamDialog.OnBuildTeamListener, MemberLongClickDialog
        .OnMemberLongClickListener, DeleteTeamDialog.OnDeleteTeamListener {
    private Toolbar toolbar;
    private TextView mDisbandThem;
    private Context mContext;

    private View mMemberView;
    private View mEmptyView;
    private Button mAddMemberBtn;
    private Button mBuildTeambtn;
    private String mTeamName, mTeamId;

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
        initTeam();
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
                DeleteTeamDialog deleteTeamDialog = new DeleteTeamDialog();
                deleteTeamDialog.show(getFragmentManager(), "DeleteTeam");
                break;

            case R.id.btn_add_member:
                Bundle bundle = new Bundle();
                bundle.putString("mTeamId", mTeamId);
                LogUtils.d("mTeamId ==>>>" + mTeamId);
                openActivity(mContext, MemberBindingActivity.class, bundle);
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
     * 初始化旅游团队信息
     */
    private void initTeam() {
        HttpMethods.getInstance().getTeamInfo()
                .subscribe(new BaseSubscriber<List<TeamInfo>>(mContext) {
                    @Override
                    public void onNext(List<TeamInfo> teamInfos) {

                        if (teamInfos.size() > 0 ){
                            TeamInfo teamInfo = teamInfos.get(0);
                            mTeamName = teamInfo.getTeamName();
                            mTeamId = teamInfo.getTeamId();
                            toolbar.setTitle(mTeamName);
                            initDatas();
                            showMemberView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException ){
                            toolbar.setTitle("游客管理");
                            showEmptyView();
                            return;
                        }
                        super.onError(e);
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
                openActivity(mContext, MemberInfoActivity.class, bundle);
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
     * 获取游客信息
     */
    private void initDatas() {
        HttpMethods.getInstance().getMemberInfo(mTeamId)
                .subscribe(new BaseSubscriber<List<MemberInfoResult>>(mContext) {
                    @Override
                    public void onNext(List<MemberInfoResult> memberInfoResults) {
                        for (MemberInfoResult memberInfo : memberInfoResults){

                            LogUtils.d(memberInfo.toString());
                        }
                    }
                });
        MemberInfo msg = null;
        for (int i = 1; i < 10; i++) {
            msg = new MemberInfo("游客" + i);
            mDatas.add(msg);
        }

    }

    @Override
    public void setTeamInfo(final String teamName) {
        LogUtils.d(teamName);
        if (teamName.isEmpty()) {
            T.showShortToast(mContext, "团队名称不能为空");
        } else {

            HttpMethods.getInstance().createTeam(teamName)
                    .subscribe(new BaseSubscriber<HttpResult>(mContext) {

                        @Override
                        public void onNext(HttpResult httpResult) {
                            if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                                mTeamId = (String) httpResult.getValues().get(0);
                                toolbar.setTitle(teamName);
                                mDatas.clear();
                                mAdapter.notifyDataSetChanged();
                                showMemberView();
                                T.showShortToast(mContext, "团队创建成功");
                            } else {
                                throw new ApiException(httpResult.getDescritpion());
                            }

                        }
                    });
        }
    }

    @Override
    public void deleteTeme() {

        HttpMethods.getInstance().deleteTeam(mTeamId)
                .subscribe(new BaseSubscriber<HttpResult>(mContext) {

                    @Override
                    public void onNext(HttpResult httpResult) {
                        if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                            mDatas.clear();
                            mAdapter.notifyDataSetChanged();
                            toolbar.setTitle("游客管理");
                            T.showShortToast(mContext, "团队已解散");
                            showEmptyView();
                        } else {
                            throw new ApiException(httpResult.getDescritpion());
                        }

                    }
                });
    }

    @Override
    public void onDeleteBinding() {
        mDatas.remove(longClickIndex);
        mAdapter.notifyDataSetChanged();
    }
}

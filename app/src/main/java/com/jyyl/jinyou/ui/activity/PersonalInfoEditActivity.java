package com.jyyl.jinyou.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyyl.jinyou.R;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.entity.GuideInfoResult;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.http.HttpResult;
import com.jyyl.jinyou.ui.base.BaseActivity;
import com.jyyl.jinyou.utils.SPUtils;
import com.jyyl.jinyou.utils.T;
import com.jyyl.jinyou.widget.CleanEditText;

/**
 * @Fuction: 个人信息编辑
 * @Author: Shang
 * @Date: 2016/5/10  15:31
 */
public class PersonalInfoEditActivity extends BaseActivity{
    private Toolbar toolbar;
    private TextView mSaveBtn;
    private Context mContext;

    private CleanEditText mNameTv;
    private CleanEditText mGuideCardIdTv;
    private CleanEditText mCompanyTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_personal_info_edit);
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle("个人信息编辑");
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
        mSaveBtn = (TextView) findViewById(R.id.toolbar_right_tv);
        mSaveBtn.setText("保存");
        mSaveBtn.setVisibility(View.VISIBLE);

        mNameTv = (CleanEditText) findViewById(R.id.et_personal_name);
        mGuideCardIdTv = (CleanEditText) findViewById(R.id.et_personal_guide_card);
        mCompanyTv = (CleanEditText) findViewById(R.id.et_personal_company);

        String guideInfoString = (String) SPUtils.get(mContext, Sp.SP_KEY_USER_OBJECT, "-1");
        if (!"-1".equals(guideInfoString)){
            GuideInfoResult guideInfoResult = new Gson()
                    .fromJson(guideInfoString, GuideInfoResult.class);

            mNameTv.setText(guideInfoResult.getMemberName());
            mGuideCardIdTv.setText(guideInfoResult.getGuideCard());
            mCompanyTv.setText(guideInfoResult.getCompanyName());
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.toolbar_right_tv:
                String name = mNameTv.getText().toString();
                String cardId = mGuideCardIdTv.getText().toString();
                String company = mCompanyTv.getText().toString();
                HttpMethods.getInstance().uploadGuideInfo(name, cardId, company)
                        .subscribe(new BaseSubscriber<HttpResult>() {
                            @Override
                            public void onNext(HttpResult httpResult) {

                            }
                        });
                T.showShortToast(mContext, "保存成功");
                finish();
                break;
        }
    }
}

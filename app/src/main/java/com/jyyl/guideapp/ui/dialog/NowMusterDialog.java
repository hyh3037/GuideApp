package com.jyyl.guideapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.entity.MemberInfo;
import com.jyyl.guideapp.ui.adapter.SelectMemvberAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Fuction: 立即集合Dialog
 * @Author: Shang
 * @Date: 2016/4/29  11:47
 */
public class NowMusterDialog extends DialogFragment {
    private TextView mSelectMemberTv;
    private GridView mGridView;
    private List<MemberInfo> tvMemberList = new ArrayList<>();
    private List<MemberInfo> tvSelectedList = new ArrayList<>();
    private EditText mMusterMsg;

    private boolean flag = false;

    //向activity传递数据的接口
    public interface SendMusterMsgListener {
        void sendMsg(String msg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_now_muster, null);
        mSelectMemberTv = (TextView) view.findViewById(R.id.select_member);
        mGridView = (GridView) view.findViewById(R.id.gridviw_member);
        mMusterMsg = (EditText) view.findViewById(R.id.et_nowmuster_msg);

        mSelectMemberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag){
                    flag = false;
                    mGridView.setVisibility(View.GONE);
                }else {
                    flag = true;
                    mGridView.setVisibility(View.VISIBLE);
                }
            }
        });

        initDatas();
        SelectMemvberAdapter adapter = new SelectMemvberAdapter(tvMemberList, getActivity(),
                onItemClickClass);
        mGridView.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendMusterMsgListener listener = (SendMusterMsgListener) getActivity();
                        listener.sendMsg(mMusterMsg.getText().toString());
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    /**
     * 实现接口，点击选中或者取消选中，并获取其被选中的集合
     */
    SelectMemvberAdapter.OnItemClickClass onItemClickClass = new SelectMemvberAdapter
            .OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int position, CheckBox checkBox, TextView textView) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                textView.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                textView.setTextColor(getActivity().getResources().getColor(R.color.black));
                for (int i = 0; i < tvSelectedList.size(); i++) {
                    if (textView.getText().toString().equals(tvSelectedList.get(i).getName())) {
                        tvSelectedList.remove(i);
                    }
                }
            } else {
                checkBox.setChecked(true);
                textView.setBackgroundColor(getActivity().getResources().getColor(R.color.skyblue));
                textView.setTextColor(getActivity().getResources().getColor(R.color.white));
                tvSelectedList.add(tvMemberList.get(position));
            }

            if (tvSelectedList.size() == 0 || tvSelectedList.size() == tvMemberList.size()){
                mSelectMemberTv.setText("发送给所有人");
            }else {
                mSelectMemberTv.setText("发送给"+tvSelectedList.get(0).getName()
                        +"等"+tvSelectedList.size()+"位游客");
            }
        }
    };

    /**
     * 测试数据
     */
    private void initDatas() {
        MemberInfo msg = null;
        for (int i = 1; i < 12; i++) {
            msg = new MemberInfo("游客" + i);
            tvMemberList.add(msg);
        }
    }

}

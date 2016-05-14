package com.jyyl.guideapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jyyl.guideapp.R;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/5/11  8:32
 */
public class MemberLongClickDialog extends DialogFragment {

    private TextView mDeleteMember;
    private String memberName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_member_longclick, null);
        mDeleteMember = (TextView) view.findViewById(R.id.tv_delete_member_msg);
        mDeleteMember.setText(getString(R.string.delete_membet_msg,memberName));
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OnMemberLongClickListener listener = (OnMemberLongClickListener) getActivity();
                        listener.onDeleteBinding();
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    public interface OnMemberLongClickListener{
        void onDeleteBinding();
    }

    public void setMemberName(String name){
        memberName = name;
    }

}

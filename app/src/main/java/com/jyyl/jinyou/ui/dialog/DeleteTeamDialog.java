package com.jyyl.jinyou.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jyyl.jinyou.R;

/**
 * @Fuction: 立即集合Dialog
 * @Author: Shang
 * @Date: 2016/4/29  11:47
 */
public class DeleteTeamDialog extends DialogFragment{
    private EditText mTeamName;

    //向activity传递数据的接口
    public interface OnDeleteTeamListener{
        void deleteTeme();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_team, null);
        mTeamName = (EditText) view.findViewById(R.id.et_team_name);
        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OnDeleteTeamListener listener = (OnDeleteTeamListener) getActivity();
                        listener.deleteTeme();
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }
}

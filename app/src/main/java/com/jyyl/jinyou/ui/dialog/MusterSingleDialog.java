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
public class MusterSingleDialog extends DialogFragment {
    private EditText mMusterMsg;

    //向activity传递数据的接口
    public interface MusterSingleListener {
        void sendSingleMsg(String msg);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_muster_single, null);
        mMusterMsg = (EditText) view.findViewById(R.id.et_nowmuster_msg);

        builder.setView(view)
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusterSingleListener listener = (MusterSingleListener) getActivity();
                        listener.sendSingleMsg(mMusterMsg.getText().toString());
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

}

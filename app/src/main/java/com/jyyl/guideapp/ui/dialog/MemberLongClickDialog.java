package com.jyyl.guideapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jyyl.guideapp.R;

/**
 * @Fuction:
 * @Author: Shang
 * @Date: 2016/5/11  8:32
 */
public class MemberLongClickDialog extends DialogFragment {

    private Button mMemberInfoBtn;
    private Button mDeleteBindingBtn;

    public interface OnMemberLongClickListener{
        void onMemberInfo();
        void onDeleteBinding();
    }

    OnMemberLongClickListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_member_longclick, null);
        mMemberInfoBtn = (Button) view.findViewById(R.id.btn_member_info);
        mDeleteBindingBtn = (Button) view.findViewById(R.id.btn_delete_binding);
        listener = (OnMemberLongClickListener) getActivity();
        mMemberInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMemberInfo();
            }
        });

        mDeleteBindingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteBinding();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}

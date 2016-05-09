package com.jyyl.guideapp.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jyyl.guideapp.R;
import com.jyyl.guideapp.widget.pickerview.TimePicker;

import java.util.Calendar;

/**
 * @Fuction: 立即集合Dialog
 * @Author: Shang
 * @Date: 2016/4/29  11:47
 */
public class TimeMusterDialog extends DialogFragment {
    private Calendar calendar;
    private TimePicker mTimePicker;
    //选择时间与当前时间，用于判断用户选择的是否是以前的时间
    private int currentHour, currentMinute, selectHour, selectMinute;
    private EditText mMusterMsg;

    //向activity传递数据的接口
    public interface TimeMusterListener {
        void saveMessage(String msg, int hour, int minute);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_time_muster, null);
        mMusterMsg = (EditText) view.findViewById(R.id.et_timemuster_msg);
        mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);
        mTimePicker.setOnChangeListener(mOnChangeListener);

        //选择时间与当前时间的初始化
        calendar = Calendar.getInstance();
        selectMinute = currentMinute = calendar.get(Calendar.MINUTE);
        selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        builder.setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimeMusterListener listener = (TimeMusterListener) getActivity();
                        listener.saveMessage(mMusterMsg.getText().toString(), selectHour,selectMinute);
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    TimePicker.OnChangeListener mOnChangeListener = new TimePicker.OnChangeListener() {
        @Override
        public void onChange(int hour, int minute) {
            selectHour = hour;
            selectMinute = minute;
        }
    };
}

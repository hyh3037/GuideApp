package com.jyyl.jinyou.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.DeviceInfo;
import com.jyyl.jinyou.entity.DeviceResult;
import com.jyyl.jinyou.http.BaseSubscriber;
import com.jyyl.jinyou.http.HttpMethods;
import com.jyyl.jinyou.ui.base.BaseAdapterHelper;
import com.jyyl.jinyou.ui.base.ViewHolder;
import com.jyyl.jinyou.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Fuction: 绑定游客和设备
 * @Author: Shang
 * @Date: 2016/4/22  17:17
 */
public class SelectDeviceDialog extends DialogFragment {

    private ListView mListView;
    private BaseAdapterHelper mAdapter;

    private ArrayList<DeviceInfo> mDatas = new ArrayList<DeviceInfo>();
    private int number = 1;
    private String deviceId = null;


    //向activity传递数据的接口
    public interface OnSelectDeviceListener{
        void onDeviceInfo(DeviceInfo deviceInfo);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_device, null);
        mListView = (ListView) view.findViewById(R.id.binding_member_listview);
        initData();
        mAdapter = new BaseAdapterHelper<DeviceInfo>(getActivity(),
                mDatas, R.layout.item_device_listview) {
            @Override
            public void convert(ViewHolder holder, DeviceInfo deviceInfo) {
                holder.setText(R.id.tv_number, String.valueOf(deviceInfo.getNumber()));
                holder.setText(R.id.tv_device_id, deviceInfo.getDeviceResult().getDeviceIMEI());
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnSelectDeviceListener listener = (OnSelectDeviceListener) getActivity();
                listener.onDeviceInfo(mDatas.get(position));
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }





    private void initData() {

        HttpMethods.getInstance().getUserNotBoundDevices()
                .subscribe(new BaseSubscriber<List<DeviceResult>>(this.getActivity()) {
                    @Override
                    public void onNext(List<DeviceResult> deviceResults) {
                        LogUtils.d("deviceResults" + deviceResults.toString());
                        mDatas.clear();
                        int number = 1;
                        for (DeviceResult deviceResult : deviceResults){
                            mDatas.add(new DeviceInfo(number, deviceResult));
                            number++;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }
}

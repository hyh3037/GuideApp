package com.jyyl.jinyou.voice;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyyl.jinyou.R;
import com.nickming.view.Recorder;

import java.util.List;

public class RecorderAdapter extends ArrayAdapter<Recorder> {

    private Context mContext;
    private String photoUri;

    private LayoutInflater inflater;

    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;

    public RecorderAdapter(Context context, List<Recorder> dataList, String photoUrl) {
        super(context, -1, dataList);
        mContext = context;
        this.photoUri = photoUrl;

        inflater = LayoutInflater.from(context);

        // 获取系统宽度
        WindowManager wManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.item_icon);
            viewHolder.seconds = (TextView) convertView.findViewById(R.id.recorder_time);
            viewHolder.length = convertView.findViewById(R.id.recorder_length);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(photoUri)
                .error(R.drawable.default_photo)
                .into(viewHolder.photo);

        String seconds = String.valueOf(Math.round(getItem(position).getTime())) + "\"";
        viewHolder.seconds.setText(seconds);
        ViewGroup.LayoutParams lParams = viewHolder.length.getLayoutParams();
        lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f * getItem(position).getTime());
        viewHolder.length.setLayoutParams(lParams);

        return convertView;
    }

    class ViewHolder {
        ImageView photo; //头像
        TextView seconds;// 时间
        View length;// 对话框长度
    }

}

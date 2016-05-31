package com.jyyl.jinyou.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jyyl.jinyou.R;
import com.jyyl.jinyou.entity.MemberInfo;

import java.util.List;

/**
 * @Fuction: 多选 gridview 适配器
 * @Author: Shang
 * @Date: 2016/5/17  14:54
 */
public class SelectMemvberAdapter extends BaseAdapter {

    private List<MemberInfo> tagList;
    private Context context;
    private TagHolder mHolder = null;
    private int clickTemp = -1;
    OnItemClickClass onItemClickClass;

    public SelectMemvberAdapter(List<MemberInfo> tagList, Context context, OnItemClickClass onItemClickClass) {
        this.tagList = tagList;
        this.context = context;
        this.onItemClickClass = onItemClickClass;
    }

    public void setSelection(int choiceCount) {
        this.clickTemp = choiceCount;
    }

    @Override
    public int getCount() {
        return tagList.size();
    }

    @Override
    public Object getItem(int position) {
        return tagList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member_gridview, null);
            mHolder = new TagHolder();
            mHolder.textView = (TextView) convertView.findViewById(R.id.item_member_name);
            mHolder.checkBox = (CheckBox) convertView.findViewById(R.id.item_checkbox);
            convertView.setTag(mHolder);
        } else {
            mHolder = (TagHolder) convertView.getTag();
        }
        mHolder.textView.setText(tagList.get(position).getName());
        mHolder.checkBox.setVisibility(View.GONE);

        //设置被选中和取消选中条目的状态
        if (clickTemp == position) {
            mHolder.textView.setSelected(true);
        } else {
            mHolder.textView.setSelected(false);
        }
        convertView.setOnClickListener(new OnTextClick(position, mHolder.checkBox, mHolder.textView));
        return convertView;
    }

    static class TagHolder {
        TextView textView;
        CheckBox checkBox;
    }

    /** 点击多选的接口 */
    public interface OnItemClickClass {
        public void OnItemClick(View v, int Position, CheckBox checkBox, TextView textView);
    }

    /** 多选的接口实现类 */
    class OnTextClick implements View.OnClickListener {

        int position;
        CheckBox checkBox;
        TextView tvSelected = null;
        TextView textView = null;

        public OnTextClick(int position, CheckBox checkBox) {
            this.position = position;
            this.checkBox = checkBox;
        }

        public OnTextClick(int position, CheckBox checkBox, TextView textView) {
            this.position = position;
            this.checkBox = checkBox;
            this.textView = textView;
        }

        @Override
        public void onClick(View v) {
            if (tagList != null && onItemClickClass != null) {
                onItemClickClass.OnItemClick(v, position, checkBox, textView);
            }
        }
    }
}

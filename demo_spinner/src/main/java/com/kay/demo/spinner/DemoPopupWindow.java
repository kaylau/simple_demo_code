package com.kay.demo.spinner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kaylau on 2017/6/2.
 */

public class DemoPopupWindow extends PopupWindow {


    private ListView listView;
    private Context mContext;
    private ArrayList<String> data;

    public DemoPopupWindow(Context mContext, int width, ArrayList<String> data, OnSpinnerListener listener) {
        this.mContext = mContext;
        this.data = data;
        this.listener = listener;

        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner, null);
        setContentView(view);
        // 设置弹出窗体的宽和高
        this.setWidth(width);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.style_pop_animation);
//        setFocusable(true);
//        setOutsideTouchable(false);
//        setTouchable(true);

        init(view);
    }

    private void init(View view) {

        listView = view.findViewById(R.id.listView);
        listView.setVerticalScrollBarEnabled(false);
        listView.setFastScrollEnabled(false);
        MyAdapter adapter = new MyAdapter();
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("tag", "-------position: " + position);
                if (listener != null && data != null && data.size() > 0) {
                    listener.onCallback(data.get(position));
                    dismiss();
                }
            }
        });


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.e("tag", "---getView------->");
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_item, null);
                viewHolder.textView = convertView.findViewById(R.id.tv_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(data.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
    }

    private OnSpinnerListener listener;

    interface OnSpinnerListener {

        void onCallback(String phoneNum);
    }

}

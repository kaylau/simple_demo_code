package com.kay.demo.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2019/4/18 下午3:13
 * Author: kay lau
 * Description:
 */
public class FragmentHistory extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.layout_coupon, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View view) {
        ListView listView = view.findViewById(R.id.listView);

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add("这是第" + i + "个条目");
        }

        ValidAdapter validAdapter = new ValidAdapter(list);

        listView.setAdapter(validAdapter);

    }

    private class ValidAdapter extends BaseAdapter {

        List<String> list;

        public ValidAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null && list.size() > 0) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return getCount() > 0 ? list.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_coupon_history, null);
                holder = new ViewHolder();
                holder.tv = convertView.findViewById(R.id.tv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv.setText(list.get(position) + " ----- " + position);
            return convertView;
        }
    }

    private class ViewHolder {
        TextView tv;
    }

}

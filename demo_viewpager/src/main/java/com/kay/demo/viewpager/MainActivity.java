package com.kay.demo.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private View line_valid;
    private View line_history;
    private ViewPager viewPager;
    private CouponPager couponPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    private void initView() {
        findViewById(R.id.tv_valid).setOnClickListener(this);
        findViewById(R.id.tv_history).setOnClickListener(this);
        line_valid = findViewById(R.id.line_valid);
        line_history = findViewById(R.id.line_history);
        showLineValid();

        viewPager = findViewById(R.id.viewPager);

    }

    private void initData() {
        FragmentValid fragmentValid = new FragmentValid();
        FragmentHistory fragmentHistory = new FragmentHistory();
        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(fragmentValid);
        listFragments.add(fragmentHistory);

        couponPager = new CouponPager(getSupportFragmentManager());
        couponPager.setList(listFragments);

        viewPager.setAdapter(couponPager);

        viewPager.addOnPageChangeListener(new MyViewPager());
    }

    private void showLineValid() {
        if (line_valid != null) {
            line_valid.setVisibility(View.VISIBLE);
        }
        if (line_history != null) {
            line_history.setVisibility(View.INVISIBLE);
        }
    }

    private void showLineHistory() {
        if (line_valid != null) {
            line_valid.setVisibility(View.INVISIBLE);
        }
        if (line_history != null) {
            line_history.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.tv_valid:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_history:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    private class MyViewPager implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    showLineValid();
                    break;
                case 1:
                    showLineHistory();
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}

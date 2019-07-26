package com.kay.demo.kotlin.weigt;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.kay.demo.kotlin.R;


/**
 * 下拉刷新以及上啦加载更多
 */

public class MyRefreshRelativeLayout extends RelativeLayout {

    /**
     * 常量
     */
    //pullDownType下拉类型的值；1-正常可以下拉刷新  2-不可以下拉刷新(有阻尼效果 )
    public static final int PULL_DOWN_TYPE_REFRESH = 1;
    public static final int PULL_DOWN_TYPE_NOT_PULL = 2;
    //pullUpType上拉类型的值；1-可以上拉加载 2-到了底部自动加载更多  3-不可以上拉刷新(有阻尼效果)
    public static final int PULL_UP_TYPE_LOAD_PULL = 1;
    public static final int PULL_UP_TYPE_LOAD_AUTO = 2;
    public static final int PULL_UP_TYPE_NOT_PULL = 3;
    //pullDownState下拉状态的值；1-下拉  2-松开刷新  3-正在刷新  4-刷新完成
    public static final int PULL_DOWN_STATE_1 = 1;
    public static final int PULL_DOWN_STATE_2 = 2;
    public static final int PULL_DOWN_STATE_3 = 3;
    public static final int PULL_DOWN_STATE_4 = 4;
    //pullDownState上拉状态的值；1-上拉  2-松开加载  3-正在加载  4-加载完成
    public static final int PULL_UP_STATE_1 = 1;
    public static final int PULL_UP_STATE_2 = 2;
    public static final int PULL_UP_STATE_3 = 3;
    public static final int PULL_UP_STATE_4 = 4;

    /**
     * UI
     */
    private RecyclerView recyclerView;
    private RelativeLayout rlyHead;
    private RelativeLayout rlyFoot;


    private RelativeLayout rlyHeadState1;
    private RelativeLayout rlyHeadState2;
    private RelativeLayout rlyHeadState3;
    private RelativeLayout rlyHeadState4;
    private RelativeLayout rlyFootState1;
    private RelativeLayout rlyFootState2;
    private RelativeLayout rlyFootState3;
    private RelativeLayout rlyFootState4;

    /**
     * 变量
     */
    private int pullDownType = PULL_DOWN_TYPE_REFRESH;//下拉类型；
    private int pullUpType = PULL_UP_TYPE_LOAD_PULL;//上拉类型；
    private int pullDownState = PULL_DOWN_STATE_1;//下拉状态
    private int pullUpState = PULL_UP_STATE_1;//上拉状态
    private boolean isRefreshing = false;//正在刷新
    private boolean isLoading = false;//正在加载更多
    private int afterRefreshDelayTime = 200;//刷新完成之后，延迟收起头部视图的时间
    private boolean isTouching = false;//是否正在按压着屏幕

    // y方向上当前触摸点的前一次记录位置
    private int previousY = 0;
    // y方向上的触摸点的起始记录位置
    private int startY = 0;
    // y方向上的触摸点当前记录位置
    private int currentY = 0;
    // y方向上两次移动间移动的相对距离
    private int deltaY = 0;

    // 用于记录childView的初始位置
    private Rect rectRlv = new Rect();//recyclerView的初始位置
    private Rect rectRlyHead = new Rect();//头部的初始位置
    private Rect rectRlyFoot = new Rect();//尾部的初始位置

    //水平移动的距离
    private float moveHeight;

    /**
     * 接口
     */
    private OnRefreshListener onRefreshListener;//刷新回调
    private OnLoadListener onLoadListener;//加载回调
    private OnTouchHeightListener onTouchHeightListener;//拖动的高度监听，正数是下拉，负数是上啦（不会为0）


    public MyRefreshRelativeLayout(Context context) {
        this(context, null);
    }

    public MyRefreshRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRefreshRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), getLayoutId(), this);
        initView();
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rlv);
        rlyHead = getHeadLayout((RelativeLayout) findViewById(R.id.rly_refresh_head));
        rlyFoot = getFootLayout((RelativeLayout) findViewById(R.id.rly_refresh_foot));

        rlyHeadState1 = getHeadState1Layout((RelativeLayout) findViewById(R.id.rly_head_state_1));
        rlyHeadState2 = getHeadState2Layout((RelativeLayout) findViewById(R.id.rly_head_state_2));
        rlyHeadState3 = getHeadState3Layout((RelativeLayout) findViewById(R.id.rly_head_state_3));
        rlyHeadState4 = getHeadState4Layout((RelativeLayout) findViewById(R.id.rly_head_state_4));

        rlyFootState1 = getFootState1Layout((RelativeLayout) findViewById(R.id.rly_foot_state_1));
        rlyFootState2 = getFootState2Layout((RelativeLayout) findViewById(R.id.rly_foot_state_2));
        rlyFootState3 = getFootState3Layout((RelativeLayout) findViewById(R.id.rly_foot_state_3));
        rlyFootState4 = getFootState4Layout((RelativeLayout) findViewById(R.id.rly_foot_state_4));

        //初始化一下三大件的位置
        initLayoutRect();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //四个判定条件，1-不是正在加载 2-处于自动加载更多模式 3-列表已经到底部了 4-列表没有在顶部（3-4一起的意思是列表到底部了，且列表的数据必须大于一页）
                    if (!isLoading && pullUpType == PULL_UP_TYPE_LOAD_AUTO && !recyclerView.canScrollVertically(1) && recyclerView.canScrollVertically(-1)) {
                        setPullUpState(PULL_UP_STATE_3);//到底部自动加载
                    }
                }
            });
        } else {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //四个判定条件，1-不是正在加载 2-处于自动加载更多模式 3-列表已经到底部了 4-列表没有在顶部（3-4一起的意思是列表到底部了，且列表的数据必须大于一页）
                    if (!isLoading && pullUpType == PULL_UP_TYPE_LOAD_AUTO && !recyclerView.canScrollVertically(1) && recyclerView.canScrollVertically(-1)) {
                        setPullUpState(PULL_UP_STATE_3);//到底部自动加载
                    }
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//recyclerView默认是ListView一样的列表
    }

    //供外部调用的功能性方法↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分隔线↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓


    /**
     * 返回RecyclerView，作为数据操作使用
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 设置下拉类型 1-正常可以下拉刷新（默认）  2-不可以下拉刷新(有阻尼效果 )
     * （ 最好是在刚初始化的时候就调用）
     *
     * @param type
     */
    public void setPullDownType(int type) {
        this.pullDownType = type;
        if (type == PULL_DOWN_TYPE_NOT_PULL) {
            rlyHead.setVisibility(GONE);
        } else {
            rlyHead.setVisibility(VISIBLE);
        }
        initLayoutRect();
    }

    /**
     * 设置上啦类型 1-可以下拉刷新（默认） 2-到了底部自动加载更多  3-不可以上拉刷新(有阻尼效果)
     * （ 最好是在刚初始化的时候就调用）
     *
     * @param type
     */
    public void setPullUpType(int type) {
        this.pullUpType = type;
        if (type == PULL_UP_TYPE_NOT_PULL) {
            rlyFoot.setVisibility(GONE);
        } else {
            rlyFoot.setVisibility(VISIBLE);
        }
        initLayoutRect();
    }

    /**
     * 设置正在刷新或者刷新完成
     *
     * @param bln
     */
    public void setRefreshing(final boolean bln) {
        if (bln) {
            setPullDownState(PULL_DOWN_STATE_3);
        } else {
            setPullDownState(PULL_DOWN_STATE_4);
        }
    }

    /**
     * 设置正在刷新或者刷新完成
     *
     * @param bln
     */
    public void setLoading(final boolean bln) {
        if (bln) {
            setPullUpState(PULL_UP_STATE_3);
        } else {
            setPullUpState(PULL_UP_STATE_4);
        }
    }

    /**
     * 监听下拉刷新状态
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    /**
     * 监听上啦或者自动加载状态
     *
     * @param listener
     */
    public void setOnLoadListener(OnLoadListener listener) {
        this.onLoadListener = listener;
    }

    /**
     * 拖动的高度监听，正数是下拉，负数是上啦（不会为0,且只监听拖动事件，按下和抬起均不回调）
     *
     * @param listener true-正在加载  false-加载完成
     */
    public void setOnLoadListener(OnTouchHeightListener listener) {
        this.onTouchHeightListener = listener;
    }

    /**
     * 设置刷新完成之后收起视图的时间，毫秒单位
     *
     * @param time
     */
    public void setAfterRefreshDelayTime(int time) {
        this.afterRefreshDelayTime = time;
    }
    //供外部调用的功能性方法↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分隔线↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    //如需自定义样式，可以继承后重写下方的方法↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓分隔线↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 使用的布局
     *
     * @return
     */
    public int getLayoutId() {
        return R.layout.refresh_relative_layout;
    }

    /**
     * 头部容器（如果需要特殊定制，可以做些修改，但建议不要改动头、尾的容器）
     *
     * @param rlyHead
     * @return
     */
    public RelativeLayout getHeadLayout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    /**
     * 下拉刷新的4种状态的视图
     *
     * @param rlyHead
     * @return
     */
    public RelativeLayout getHeadState1Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    public RelativeLayout getHeadState2Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    public RelativeLayout getHeadState3Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    public RelativeLayout getHeadState4Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    /**
     * 上拉刷新的4种状态的视图
     *
     * @param rlyHead
     * @return
     */
    public RelativeLayout getFootState1Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    public RelativeLayout getFootState2Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    public RelativeLayout getFootState3Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    public RelativeLayout getFootState4Layout(RelativeLayout rlyHead) {
        return rlyHead;
    }

    /**
     * 尾部容器（如果需要特殊定制，可以做些修改，但建议不要改动头、尾的容器）
     *
     * @param rlyFoot
     * @return
     */
    public RelativeLayout getFootLayout(RelativeLayout rlyFoot) {
        return rlyFoot;
    }

    /**
     * 回调动画的动画时长，可以根据需求调节快一点慢一点
     *
     * @return
     */
    public int getAnimTime() {
        return 760;
    }

    //如需自定义样式，可以继承后重写上方的方法↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑分隔线↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    /**
     * 设置下拉的状态
     *
     * @param state 下拉状态的值；1-下拉  2-松开刷新  3-正在刷新  4-刷新完成
     */
    private void setPullDownState(int state) {
        if (state < 1 || state > 4) {
            Log.e("错误", "下拉状态的值越界");
            return;
        }
        pullDownState = state;
        if (onRefreshListener != null && state == PULL_DOWN_STATE_3) {
            //下拉刷新
            onRefreshListener.onRefresh();
        }
        changePullDownState();
    }

    /**
     * 下拉状态改变
     */
    private void changePullDownState() {
        isRefreshing = false;
        rlyHeadState1.setVisibility(INVISIBLE);
        rlyHeadState2.setVisibility(INVISIBLE);
        rlyHeadState3.setVisibility(INVISIBLE);
        rlyHeadState4.setVisibility(INVISIBLE);
        switch (pullDownState) {
            case PULL_DOWN_STATE_1:
                //下拉刷新
                if (pullDownType != PULL_DOWN_TYPE_NOT_PULL) {
                    rlyHeadState1.setVisibility(VISIBLE);
                }
                break;
            case PULL_DOWN_STATE_2:
                //松开刷新
                if (pullDownType != PULL_DOWN_TYPE_NOT_PULL) {
                    rlyHeadState2.setVisibility(VISIBLE);
                }
                break;
            case PULL_DOWN_STATE_3:
                //正在刷新
                isRefreshing = true;//除了正在刷新之外，其他状态都不在刷新
                if (pullDownType != PULL_DOWN_TYPE_NOT_PULL) {
                    rlyHeadState3.setVisibility(VISIBLE);
                }
                //无论当前再什么位置，都跳到正在刷新的地方
                moveViewAnimation(rlyHead, 0);
                moveViewAnimation(recyclerView, rlyHead.getHeight());
                break;
            case PULL_DOWN_STATE_4:
                //刷新成功
                if (pullDownType != PULL_DOWN_TYPE_NOT_PULL) {
                    rlyHeadState4.setVisibility(VISIBLE);
                }
                //无论当前再什么位置，都要跳到初始的位置
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveViewAnimation(rlyHead, -1 * rlyHead.getHeight());
                        moveViewAnimation(recyclerView, 0);
                    }
                }, afterRefreshDelayTime);
                break;
        }
    }


    /**
     * 设置下拉的状态
     *
     * @param state 下拉状态的值；1-下拉  2-松开刷新  3-正在刷新  4-刷新完成
     */
    private void setPullUpState(int state) {
        if (state < 1 || state > 4) {
            Log.e("错误", "上拉状态的值越界");
            return;
        }
        pullUpState = state;
        if (onLoadListener != null && state == PULL_UP_STATE_3) {
            //下拉刷新
            onLoadListener.onLoad();
        }
        changePullUpState();
    }

    /**
     * 下拉状态改变
     */
    private void changePullUpState() {
        isLoading = false;
        rlyFootState1.setVisibility(INVISIBLE);
        rlyFootState2.setVisibility(INVISIBLE);
        rlyFootState3.setVisibility(INVISIBLE);
        rlyFootState4.setVisibility(INVISIBLE);
        switch (pullUpState) {
            case PULL_UP_STATE_1:
                //下拉刷新（自动加载更多的模式下禁止显示）
                if (pullUpType != PULL_UP_TYPE_LOAD_AUTO && pullUpType != PULL_UP_TYPE_NOT_PULL) {
                    rlyFootState1.setVisibility(VISIBLE);
                }
                break;
            case PULL_UP_STATE_2:
                //松开刷新(自动加载模式下禁止显示)
                if (pullUpType != PULL_UP_TYPE_LOAD_AUTO && pullUpType != PULL_UP_TYPE_NOT_PULL) {
                    rlyFootState2.setVisibility(VISIBLE);
                }
                break;
            case PULL_UP_STATE_3:
                //正在刷新
                isLoading = true;//除了正在刷新之外，其他状态都不在刷新
                if (pullUpType != PULL_UP_TYPE_NOT_PULL) {
                    rlyFootState3.setVisibility(VISIBLE);
                }
                //无论当前再什么位置，都跳到正在刷新的地方
                moveViewAnimation(rlyFoot, getHeight() - rlyFoot.getHeight());
                moveViewAnimation(recyclerView, rlyFoot.getHeight() * -1);
                break;
            case PULL_UP_STATE_4:
                //刷新成功
                if (pullUpType != PULL_UP_TYPE_NOT_PULL) {
                    rlyFootState4.setVisibility(VISIBLE);
                }
                //无论当前再什么位置，都要跳到初始的位置
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        moveViewAnimation(rlyFoot, getHeight());
                        moveViewAnimation(recyclerView, 0);
                    }
                }, afterRefreshDelayTime);
                break;
        }
    }

    /**
     * 记录三大控件的初始位置
     */
    private void recordLayoutRect() {
        // 记录三大控件的初始位置
        rectRlv.set(recyclerView.getLeft(), 0, recyclerView.getRight(), getHeight());
        rectRlyHead.set(rlyHead.getLeft(), -1 * rlyHead.getHeight(), rlyHead.getRight(), 0);
        rectRlyFoot.set(rlyFoot.getLeft(), getHeight(), rlyFoot.getRight(), getHeight() + rlyFoot.getHeight());
    }

    /**
     * 初始化三大控件的位置
     */
    private void initLayoutRect() {
        post(new Runnable() {
            @Override
            public void run() {
                //初始化位置
                recyclerView.layout(recyclerView.getLeft(), 0, recyclerView.getRight(), getHeight());
                rlyHead.layout(rlyHead.getLeft(), -1 * rlyHead.getHeight(), rlyHead.getRight(), 0);
                rlyFoot.layout(rlyFoot.getLeft(), getHeight(), rlyFoot.getRight(), getHeight() + rlyFoot.getHeight());

                //绑定一下位置，以免页面重绘的时候位置发生错乱
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
                params.topMargin = 0;
                recyclerView.setLayoutParams(params);
                recyclerView.requestLayout();
                params =
                        (RelativeLayout.LayoutParams) rlyHead.getLayoutParams();
                params.topMargin = -1 * rlyHead.getHeight();
                rlyHead.setLayoutParams(params);
                rlyHead.requestLayout();
                params =
                        (RelativeLayout.LayoutParams) rlyFoot.getLayoutParams();
                params.topMargin = getHeight();
                params.bottomMargin = getHeight() + rlyFoot.getHeight();
                rlyFoot.setLayoutParams(params);
                rlyFoot.requestLayout();

                //初始化状态
                setPullDownState(PULL_DOWN_STATE_1);
                setPullUpState(PULL_UP_STATE_1);
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean isPullRecyclerView = false;//如果把头拉下来了然后再推回上去的时候，必须把recyclerView的触摸事件屏蔽掉，以免列表上移
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                startY = (int) event.getY();
                previousY = startY;
                moveHeight = 0;
                recordLayoutRect();
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = (int) event.getY();
                deltaY = currentY - previousY;
                previousY = currentY;
                //判定是否在顶部或者滑到了底部;第二类判定是，正在刷新什么的，禁止拖动列表
                if ((!recyclerView.canScrollVertically(-1) && (currentY - startY) > 0) ||
                        (!recyclerView.canScrollVertically(1) && (currentY - startY) < 0) ||
                        (rlyHead.getTop() > rectRlyHead.top && (currentY - startY) > 0) ||
                        (rlyFoot.getTop() < rectRlyFoot.top && (currentY - startY) < 0)) {
                    isPullRecyclerView = true;//下拉或者上啦的时候把recyclerView的触摸事件屏蔽掉
                    //计算阻尼
                    float distance = currentY - startY;
                    if (distance < 0) {
                        distance *= -1;
                    }
                    float damping = 0.5f;//阻尼值
                    float height = getHeight();
                    if (height != 0) {
                        if (distance > height) {
                            damping = 0;
                        } else {
                            damping = (height - distance) / height;
                        }
                    }
                    if (currentY - startY < 0) {
                        damping = 1 - damping;
                    }

                    //阻力值限制再0.3-0.5之间，平滑过度
                    damping *= 0.25;
                    damping += 0.25;

                    moveHeight = moveHeight + (deltaY * damping);
                    recyclerView.layout(rectRlv.left, (int) (rectRlv.top + moveHeight), rectRlv.right,
                            (int) (rectRlv.bottom + moveHeight));
                    rlyHead.layout(rectRlyHead.left, (int) (rectRlyHead.top + moveHeight), rectRlyHead.right,
                            (int) (rectRlyHead.bottom + moveHeight));
                    rlyFoot.layout(rectRlyFoot.left, (int) (rectRlyFoot.top + moveHeight), rectRlyFoot.right,
                            (int) (rectRlyFoot.bottom + moveHeight));

                    //判定是否是下拉动作并处于可以下拉刷新状态
                    if (pullDownType == PULL_DOWN_TYPE_REFRESH && moveHeight > 0) {
                        if (moveHeight > rlyHead.getHeight()) {
                            setPullDownState(PULL_DOWN_STATE_2);
                        } else {
                            setPullDownState(PULL_DOWN_STATE_1);
                        }
                    }

                    //判定是否是上拉动作并且处于上啦刷新状态
                    if (pullUpType == PULL_UP_TYPE_LOAD_PULL && moveHeight < 0) {
                        if (moveHeight * -1 > rlyFoot.getHeight()) {
                            setPullUpState(PULL_UP_STATE_2);
                        } else {
                            setPullUpState(PULL_UP_STATE_1);
                        }
                    }

                    //将当前拖动的高度回调
                    if (onTouchHeightListener != null) {
                        //需要列表处于到顶的状态
                        if (!recyclerView.canScrollVertically(-1) && moveHeight >= 1) {
                            onTouchHeightListener.onTouchHeight((int) moveHeight);
                        }
                        //需要列表处于到底的状态
                        if (!recyclerView.canScrollVertically(1) && moveHeight <= -1) {
                            onTouchHeightListener.onTouchHeight((int) moveHeight);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                //判定是否是下拉动作并处于可以下拉刷新状态
                if (pullDownType == PULL_DOWN_TYPE_REFRESH && moveHeight > rlyHead.getHeight()) {
                    //达到了松开刷新的条件
                    setPullDownState(PULL_DOWN_STATE_3);
                } else if (pullUpType == PULL_UP_TYPE_LOAD_PULL && moveHeight * -1 > rlyFoot.getHeight()) {
                    //达到了松开加载的条件
                    setPullUpState(PULL_UP_STATE_3);
                } else {
                    //开始回移动画
                    moveViewAnimation(recyclerView, rectRlv.top);
                    moveViewAnimation(rlyHead, rlyHead.getTop() + rlyHead.getHeight(), 0, rectRlyHead);
                    moveViewAnimation(rlyFoot, rlyFoot.getTop() - rectRlyFoot.top, 0, rectRlyFoot);
                }
                //重置一些参数
                startY = 0;
                currentY = 0;
                break;
        }
        if (isPullRecyclerView) {
            return isPullRecyclerView;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 控件移动动画效果,从当前位置，移动到目标位置
     *
     * @param view  待移动的控件
     * @param stopY 移动到的目标位置
     */
    private void moveViewAnimation(View view, int stopY) {
        moveViewAnimation(view, view.getTop() - stopY, 0, new Rect(view.getLeft(), stopY, view.getRight(), stopY + view.getHeight()));
    }

    /**
     * 控件移动动画效果
     *
     * @param view   待移动的控件
     * @param startY 移动开始的位置（相对控件本身）
     * @param stopY  移动结束的位置（相对控件本身）
     * @param top    动画结束后的位置，即设置控件本身Top的位置
     * @param bottom 动画结束后的位置，即设置控件本身bottom的位置
     */
    private void moveViewAnimation(View view, int startY, int stopY, int top, int bottom) {
        moveViewAnimation(view, startY, stopY, new Rect(view.getLeft(), top, view.getRight(), bottom));
    }

    /**
     * 控件移动动画效果
     *
     * @param view   待移动的控件
     * @param startY 移动开始的位置（相对控件本身）
     * @param stopY  移动结束的位置（相对控件本身）
     * @param rect   动画结束后的位置，即设置控件本身的位置
     */
    private void moveViewAnimation(final View view, int startY, int stopY, Rect rect) {
        if (isTouching) {
            return;//如果还按压着屏幕，则不播放任何动画
        }
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                startY, stopY);
        animation.setDuration(getAnimTime());//动画的持续时间
        animation.setFillAfter(true);
        //设置阻尼动画效果
        animation.setInterpolator(new DampInterpolator());
        view.setAnimation(animation);
        view.layout(rect.left, rect.top, rect.right, rect.bottom);
        recordLayoutRect();//记录位置，用于重绘
    }

    public class DampInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            //没看过源码，猜测是input是时间（0-1）,返回值应该是进度（0-1）
            //先快后慢，为了更快更慢的效果，多乘了几次，现在这个效果比较满意
            return 1 - (1 - input) * (1 - input) * (1 - input) * (1 - input);
        }
    }

    public void requestLayout() {
        if (isRefreshing || isLoading) {
            //刷新recyclerView里面的数据的时候，会调用requestLayout()方法刷新控件，会导刷新刷新完数据之后没有三大部件显示动画就复位了，所以这里屏蔽掉requestLayout方法换成invalidate()刷新页面
            invalidate();
        } else {
            super.requestLayout();
        }
    }


    /**
     * 接口
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public interface OnTouchHeightListener {
        void onTouchHeight(int num);
    }

}
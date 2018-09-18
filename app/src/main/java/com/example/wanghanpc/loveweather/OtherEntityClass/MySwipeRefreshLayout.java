package com.example.wanghanpc.loveweather.OtherEntityClass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private float startY;
    private float startX;

    //记录viewPager是否拖拽
    private boolean isMyDrag;
    private final int touchSlop;

    public MySwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                //记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                //初始化标记
                isMyDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                //如果viewPager正在拖拽中，那么不拦截它的事件，直接return false
                if (isMyDrag){
                    return false;
                }

                //获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceY = Math.abs(endY - startY);
                float distanceX = Math.abs(endX - startX);
                //如果X轴位移大于Y轴，那么将事件交给viewPager处理
                if (distanceX > touchSlop && distanceX > distanceY){
                    isMyDrag = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //初始化标记
                isMyDrag = false;
                break;
        }
        //如果是Y轴位移大于X轴，事件交给SwipeRefreshLayout处理
        return super.onInterceptTouchEvent(ev);
    }
}

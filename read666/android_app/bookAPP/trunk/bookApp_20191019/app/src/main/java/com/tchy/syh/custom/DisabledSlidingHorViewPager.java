package com.tchy.syh.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by stone on 16/6/17.
 */
public class DisabledSlidingHorViewPager extends ViewPager {

    public DisabledSlidingHorViewPager(Context context) {
        super(context);
    }
    public DisabledSlidingHorViewPager(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    private boolean isCanScroll = false;
    public void setScanScroll(boolean isCanScroll){
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(isCanScroll){
            return super.onInterceptTouchEvent(arg0);
        }else{
            //false  不能左右滑动
            return false;
            //true 可以左右滑动
            // return true;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }

    }

}
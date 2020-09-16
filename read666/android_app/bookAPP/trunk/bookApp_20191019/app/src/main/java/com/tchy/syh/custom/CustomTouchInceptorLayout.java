package com.tchy.syh.custom;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class CustomTouchInceptorLayout extends RelativeLayout{
    private final static float DISTANCE = 10;
    private float downX;
    private float downY;
    float MIN_SCALE=1f;
    float MAX_SCALE=1.25f;

    ViewPager pager;
    public CustomTouchInceptorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        pager=(ViewPager) this.getChildAt(0);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        ViewPager pager = null;
//        int count = getChildCount();
//        for(int i=0; i<count; i++) {
//            if(getChildAt(i) instanceof CustomClipedViewpager) {
//                pager = (CustomClipedViewpager)getChildAt(i);
//                break;
//            }
//        }
//        if(pager != null) {
//            return pager.dispatchTouchEvent(event);
//        }
//        return super.onTouchEvent(event);
//    }

}

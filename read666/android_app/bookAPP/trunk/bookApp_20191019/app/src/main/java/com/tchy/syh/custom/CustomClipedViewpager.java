package com.tchy.syh.custom;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomClipedViewpager extends ViewPager{
    public int targetDipWidth=90;
    public int pageMargin=20;
    private final static float DISTANCE = 10;
    private float downX;
    private float downY;

    public CustomClipedViewpager(@NonNull Context context) {
        super(context);
    }

    public CustomClipedViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOffscreenPageLimit(3);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        float pageMarginPx=DensityUtil.dp2px(this.getContext(),pageMargin);
        float targetWidthPx=DensityUtil.dp2px(this.getContext(),targetDipWidth);

        int padding =(int)((screenWidth-(pageMarginPx*2+targetWidthPx))/2);

            this.setPadding(padding,0,padding,0);

        this.setPageMargin(ConvertUtils.dp2px(pageMargin));
        this.setPageTransformer(true,new CustomPagerTransformer(padding/(pageMarginPx*2+targetWidthPx)));


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev.getAction() == MotionEvent.ACTION_DOWN){
//            downX = ev.getX();
//            downY = ev.getY();
//        }else if (ev.getAction() == MotionEvent.ACTION_UP) {
//
//            float upX = ev.getX();
//            float upY = ev.getY();
//
//            if(Math.abs(upX - downX) > DISTANCE || Math.abs(upY - downY) > DISTANCE){
//                return super.dispatchTouchEvent(ev);
//            }
//
//            View view = viewOfClickOnScreen(ev);
//            if (view != null) {
//                int index = (Integer) ((CommonViewModel)view.getTag()).index;
//                if (getCurrentItem() != index) {
//                    setCurrentItem(index);
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//
//    private View viewOfClickOnScreen(MotionEvent ev) {
//        int childCount = getChildCount();
//        int currentIndex =getCurrentItem();
//        int[] location = new int[2];
//        for (int i = 0; i < childCount; i++) {
//            View v = getChildAt(i);
//            int position = ((CommonViewModel)v.getTag()).index;
//            v.getLocationOnScreen(location);
//            int minX = location[0];
//            int minY = location[1];
//
//            int maxX = location[0] + v.getWidth();
//            int maxY = location[1] + v.getHeight();
//            if(position < currentIndex){
//                maxX -= v.getWidth() * (1 - MIN_SCALE) * 0.5 + v.getWidth() * (Math.abs(1 - MAX_SCALE)) * 0.5;
//                minX -= v.getWidth() * (1 - MIN_SCALE) * 0.5 + v.getWidth() * (Math.abs(1 - MAX_SCALE)) * 0.5;
//            }else if(position == currentIndex){
//                minX += v.getWidth() * (Math.abs(1 - MAX_SCALE));
//            }else if(position > currentIndex){
//                maxX -= v.getWidth() * (Math.abs(1 - MAX_SCALE)) * 0.5;
//                minX -= v.getWidth() * (Math.abs(1 -MAX_SCALE)) * 0.5;
//            }
//            float x = ev.getRawX();
//            float y = ev.getRawY();
//
//            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
//                return v;
//            }
//        }
//        return null;
//    }
}

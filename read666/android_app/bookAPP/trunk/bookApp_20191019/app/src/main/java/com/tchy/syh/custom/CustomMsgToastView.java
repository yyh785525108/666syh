package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomMsgToastView extends LinearLayoutCompat {
    int paddingHor=10;
    int paddingVer=5;
    int imageid;
    int strid;
    Spannable topSpan;
    public CustomMsgToastView(Context context) {
        super(context);
        init();
    }
    public CustomMsgToastView(Context context, AttributeSet set) {
        super(context,set);


    }

    public CustomMsgToastView(Context context, int topResourceid, Spannable topSpan, int strid) {
        this(context);
        this.imageid=topResourceid;
        this.topSpan=topSpan;
        this.strid=strid;
        init();

    }
    public void init(){

        setOrientation(VERTICAL);
        setBackground(getResources().getDrawable(R.drawable.toast_filled_round_corner_bg));
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LayoutParams(-2,-2));
        ImageView topImage=new ImageView(this.getContext());
        TextView bottomText=new TextView(this.getContext());




        if(imageid!=0){
            topImage.setImageResource(imageid);
            topImage.setLayoutParams(new LayoutParams(ConvertUtils.dp2px(40),ConvertUtils.dp2px(40)));
            addView(topImage);
        }
        if(topSpan!=null){
            TextView topT=new TextView(getContext());

            topT.setLayoutParams(new ViewGroup.LayoutParams(-2,-2));
            topT.setText(topSpan);
            topT.setTextColor(Color.WHITE);
            topT.setGravity(Gravity.CENTER);
            topT.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            addView(topT);
        }
        if(strid!=0){
            bottomText.setLayoutParams(new ViewGroup.LayoutParams(-2,-2));
            bottomText.setText(getResources().getString(strid));
            bottomText.setTextColor(Color.WHITE);
            addView(bottomText);
        }






    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int childMeasureWidth = 0;
        int childMeasureHeight = 0;

        int top =ConvertUtils.dp2px(paddingVer);
        for ( int i = 0; i < count; i++) {
            View child = getChildAt(i);
            // 注意此处不能使用getWidth和getHeight，这两个方法必须在onLayout执行完，才能正确获取宽高
            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();
            int left= (getWidth()-childMeasureWidth)/2;
            child.layout(left, top, left+childMeasureWidth, top+childMeasureHeight);
            top+=childMeasureHeight;
            top+=ConvertUtils.dp2px(paddingVer);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec. getMode(widthMeasureSpec);
        int heightMode = MeasureSpec. getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec. getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec. getSize(heightMeasureSpec);
        int layoutWidth = 0;
        int layoutHeight = 0;
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int cWidth = 0;
        int cHeight = 0;
        int count = getChildCount();


        if(widthMode == MeasureSpec. EXACTLY){
            layoutWidth = sizeWidth;
        } else{
            for ( int i = 0; i < count; i++)  {
                View child = getChildAt(i);
                cWidth = child.getMeasuredWidth();
                layoutWidth += cWidth ;
            }
        }
        if(heightMode == MeasureSpec. EXACTLY){
            layoutHeight = sizeHeight;
        } else{
            for ( int i = 0; i < count; i++)  {
                View child = getChildAt(i);
                cHeight = child.getMeasuredHeight();
                layoutHeight +=cHeight;
            }
        }

        setMeasuredDimension( layoutWidth+ConvertUtils.dp2px(paddingHor), layoutHeight+ConvertUtils.dp2px(paddingVer*(count+1)));
        Log.d("sort", "onMeasure: ");
    }


}

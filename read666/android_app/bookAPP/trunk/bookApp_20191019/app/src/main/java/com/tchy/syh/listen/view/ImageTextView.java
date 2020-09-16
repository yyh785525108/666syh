package com.tchy.syh.listen.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tchy.syh.R;


public class ImageTextView extends TextView {

    public final static int POSITION_LEFT = 0;
    public final static int POSITION_TOP = 1;
    public final static int POSITION_RIGHT = 2;
    public final static int POSITION_BOTTOM = 3;

    int leftDrawableWidth = 10;
    int leftDrawableHeight = 10;
    int topDrawableWidth = 10;
    int topDrawableHeight = 10;
    int rightDrawableWidth = 10;
    int rightDrawableHeight = 10;
    int bottomDrawableWidth = 10;
    int bottomDrawableHeight = 10;

    Drawable left;
    Drawable top;
    Drawable right;
    Drawable bottom;

    public ImageTextView(Context context) {
        this(context, null, 0);
    }

    public ImageTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getAttributes(context, attrs, defStyleAttr);
    }


    public void getAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ImageTextView_drawableWidth_left:
                    leftDrawableWidth = a.getDimensionPixelSize(attr, 10);
                    break;
                case R.styleable.ImageTextView_drawableHeight_left:
                    leftDrawableHeight = a.getDimensionPixelSize(attr, 10);
                    break;

                case R.styleable.ImageTextView_drawableWidth_top:
                    topDrawableWidth = a.getDimensionPixelSize(attr, 10);
                    break;
                case R.styleable.ImageTextView_drawableHeight_top:
                    topDrawableHeight = a.getDimensionPixelSize(attr, 10);
                    break;

                case R.styleable.ImageTextView_drawableWidth_right:
                    rightDrawableWidth = a.getDimensionPixelSize(attr, 10);
                    break;
                case R.styleable.ImageTextView_drawableHeight_right:
                    rightDrawableHeight = a.getDimensionPixelSize(attr, 10);
                    break;
                case R.styleable.ImageTextView_drawableWidth_bottom:
                    bottomDrawableWidth = a.getDimensionPixelSize(attr, 10);
                    break;
                case R.styleable.ImageTextView_drawableHeight_bottom:
                    bottomDrawableHeight = a.getDimensionPixelSize(attr, 10);
                    break;
            }
        }
        a.recycle();


        setCompoundDrawablesWithIntrinsicBounds(
                left, top, right, bottom);


    }



    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable left,
                                                        @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;



        if (left != null) {
            left.setBounds(0, 0, leftDrawableWidth, leftDrawableHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, rightDrawableWidth, rightDrawableHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, topDrawableWidth, topDrawableHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottomDrawableWidth, bottomDrawableHeight);
        }

        setCompoundDrawables(left, top, right, bottom);
    }

    /*
     * 代码中动态设置drawable的宽高度
     * */
    public void setDrawableSize(int width, int height, int position) {
        if (position == this.POSITION_LEFT) {
            leftDrawableWidth = width;
            leftDrawableHeight = height;
        }
        if (position == this.POSITION_TOP) {
            topDrawableWidth = width;
            topDrawableHeight = height;
        }
        if (position == this.POSITION_RIGHT) {
            rightDrawableWidth = width;
            rightDrawableHeight = height;
        }
        if (position == this.POSITION_BOTTOM) {
            bottomDrawableWidth = width;
            bottomDrawableHeight = height;
        }

        setCompoundDrawablesWithIntrinsicBounds(
                left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background for this view
        super.onDraw(canvas);


    }
}
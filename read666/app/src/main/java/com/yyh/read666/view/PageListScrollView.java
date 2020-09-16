package com.yyh.read666.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PageListScrollView extends ScrollView {
    private OnScrollToBottomListener mOnScrollToBottomListener;


    public PageListScrollView(Context context) {
        super(context);
    }

    public PageListScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageListScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //滚动到底部时，clampedY变为true，此时将回调将状态传出去
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY != 0 && mOnScrollToBottomListener != null) {
            mOnScrollToBottomListener.onScrollBottomListener(clampedY);
        }
    }


    public void setOnScrollToBottomListener(OnScrollToBottomListener listener) {
        mOnScrollToBottomListener = listener;
    }

    public interface OnScrollToBottomListener {
        void onScrollBottomListener(boolean isBottom);
    }

}
package com.tchy.syh.custom.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.DividerLine;

public class CommonDivider extends RecyclerView.ItemDecoration {
    private static final String TAG = DividerLine.class.getCanonicalName();
    private static final int DEFAULT_DIVIDER_SIZE = 1;
    private static final int[] ATTRS = new int[]{16843284};
    private Drawable dividerDrawable;
    private Context mContext;
    private int dividerSize;
    private DividerLine.LineDrawMode mMode;

    public CommonDivider(Context context) {
        this.mMode = null;
        this.mContext = context;
        TypedArray attrArray = context.obtainStyledAttributes(ATTRS);
        this.dividerDrawable = attrArray.getDrawable(0);
        attrArray.recycle();
    }

    public CommonDivider(Context context, DividerLine.LineDrawMode mode) {
        this(context);
        this.mMode = mode;
    }

    public CommonDivider(Context context, int dividerSize, DividerLine.LineDrawMode mode) {
        this(context, mode);
        this.dividerSize = dividerSize;
    }

    public int getDividerSize() {
        return this.dividerSize;
    }

    public void setDividerSize(int dividerSize) {
        this.dividerSize = dividerSize;
    }

    public DividerLine.LineDrawMode getMode() {
        return this.mMode;
    }

    public void setMode(DividerLine.LineDrawMode mode) {
        this.mMode = mode;
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (this.getMode() == null) {
            throw new IllegalStateException("assign LineDrawMode,please!");
        } else {
            switch (this.getMode()) {
                case VERTICAL:
                    this.drawVertical(c, parent, state);
                    break;
                case HORIZONTAL:
                    this.drawHorizontal(c, parent, state);
                    break;
                case BOTH:
                    this.drawHorizontal(c, parent, state);
                    this.drawVertical(c, parent, state);
            }

        }
    }

    private void drawVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            int left = child.getRight() + params.rightMargin;
            int right = this.getDividerSize() == 0 ? left + dip2px(this.mContext, 1.0F) : left + this.getDividerSize();
            this.dividerDrawable.setBounds(left, top, right, bottom);
            this.dividerDrawable.draw(c);
        }

    }

    private void drawHorizontal(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount-1; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int top = child.getBottom() + params.topMargin;
            int right = child.getRight() - params.rightMargin;
            int bottom = this.getDividerSize() == 0 ? top + dip2px(this.mContext, 0.5F) : top + this.getDividerSize();
            this.dividerDrawable.setBounds(left, top, right, bottom);
            this.dividerDrawable.draw(c);
        }

    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = this.getDividerSize() == 0 ? dip2px(this.mContext, 1.0F) : this.getDividerSize();
        outRect.right = this.getDividerSize() == 0 ? dip2px(this.mContext, 1.0F) : this.getDividerSize();
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    public static enum LineDrawMode {
        HORIZONTAL,
        VERTICAL,
        BOTH;

        private LineDrawMode() {
        }
    }
}
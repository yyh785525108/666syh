package com.tchy.syh.custom;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tchy.syh.R;

import java.util.concurrent.TimeUnit;

import me.goldze.mvvmhabit.utils.ConvertUtils;

import static android.widget.RelativeLayout.ALIGN_PARENT_END;
import static android.widget.RelativeLayout.CENTER_VERTICAL;

public class ExpandableLinearLayout extends LinearLayoutCompat {
    public ExpandableLinearLayout(Context context) {
        super(context);
    }

    public ExpandableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    Animation animation;
    boolean isExpanded=false;
    int contentHeight;
    @SuppressLint("RestrictedApi")
    public void init(AttributeSet attrs) {
//        animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF,
//                0, Animation.RELATIVE_TO_SELF, 0);
//        animation.setDuration(200);
//        animation.setRepeatCount(0);
//        animation.setFillAfter(true);

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.ExpandableLinearLayout, 0, 0);
            String title = a.getString(R.styleable.ExpandableLinearLayout_title);
            String content = a.getString(R.styleable.ExpandableLinearLayout_answersContent);
            TextView tvContent=new TextView(getContext());
            tvContent.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
            tvContent.setText(content);
            tvContent.setTextSize(14);
            tvContent.setBackgroundResource(R.drawable.line_top_grey_bg);
            tvContent.setVisibility(INVISIBLE);
            tvContent.setPadding(0, ConvertUtils.dp2px(10), 0, ConvertUtils.dp2px(10));
            tvContent.post(()->{
                contentHeight = tvContent.getHeight();
                tvContent.setVisibility(View.GONE);
            });
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));

            relativeLayout.setPadding(0, ConvertUtils.dp2px(15), 0, ConvertUtils.dp2px(15));
            TextView textView = new TextView(getContext());
            textView.setText(title);
            textView.setTextSize(15);
            textView.setPadding(0, 0, ConvertUtils.dp2px(40), 0);
            textView.setTextColor(Color.parseColor("#333333"));
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.down_arrow);
            RxView.clicks(relativeLayout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(v -> {
                View view = getChildAt(1);
                if (!isExpanded) {
                    isExpanded=true;
                    tvContent.setHeight(0);
                    tvContent.setVisibility(VISIBLE);
                    ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f).setDuration(200).start();
                    ViewWrapper viewWrapper = new ViewWrapper(view);
                    Log.d("sort", "init: "+contentHeight);
//                    ObjectAnimator.ofInt(viewWrapper, "height", contentHeight).setDuration(200).start();
                    tvContent.setHeight(contentHeight);
                }else{

                    isExpanded=false;
                    Log.d("sort", "initghhhh: "+tvContent.getHeight());
                    ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0).setDuration(200).start();
                    ViewWrapper viewWrapper = new ViewWrapper(view);
                    ObjectAnimator.ofInt(viewWrapper, "height", 0).setDuration(200).start();
                    tvContent.setVisibility(GONE);

                }

            });
            RelativeLayout.LayoutParams tlp = new RelativeLayout.LayoutParams(-2, -2);
            tlp.addRule(CENTER_VERTICAL, -1);
            RelativeLayout.LayoutParams ilp = new RelativeLayout.LayoutParams(-2, -2);
            ilp.addRule(CENTER_VERTICAL, -1);
            ilp.addRule(ALIGN_PARENT_END, -1);
            relativeLayout.addView(textView, tlp);
            relativeLayout.addView(imageView, ilp);
            addView(relativeLayout);

            addView(tvContent);
        }

    }
    private class ViewWrapper {

        private View rView;

        public ViewWrapper(View target) {
            rView = target;
        }

        public int getWidth() {
            return rView.getLayoutParams().width;
        }

        public void setWidth(int width) {
            Log.d("sort", "setWidth: "+width);
            rView.getLayoutParams().width = width;
            rView.requestLayout();
        }

        public int getHeight() {
            return rView.getLayoutParams().height;
        }

        public void setHeight(int height) {
            Log.d("sort", "setHeight: "+height);
            rView.getLayoutParams().height = height;
            rView.requestLayout();
        }
    }


}

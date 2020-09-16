package com.tchy.syh.custom;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

public class CustomPagerTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.8f;
    float translation;

    public CustomPagerTransformer(float translation) {

        super();
        this.translation = translation;
    }

    final float SCALE_MAX = 0.8F;
    final float ALPHA_MAX = 0.5F;


    public void transformPage(View page, float position) {
        position = position - translation*MIN_SCALE;
//        float scale = position < 0F ? 1.25f:1f;

        float scale=MIN_SCALE;
//        float alpha = position < 1F ? 0.5F * position + 1.0F : -0.5F * position + 1.0F;
        if (position < -0.95F) {

        } else if (position > 0.95F) {
        } else {
            scale=position>0?0.95f:scale;

        }

        page.setScaleX(scale);
        page.setScaleY(scale);
//        page.setAlpha(Math.abs(alpha));
    }
}
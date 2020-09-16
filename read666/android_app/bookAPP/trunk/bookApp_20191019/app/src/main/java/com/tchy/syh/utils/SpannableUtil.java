package com.tchy.syh.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class SpannableUtil {
    public static Spannable getColoredTextSpannable(String str){
        Spannable spannable =new SpannableString(str);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FCC630")), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        return spannable;
    }

    public static Spannable getRedVipSpannable(String str){
        Spannable spannable =new SpannableString(str);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FCC630")), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
        return spannable;
    }
}

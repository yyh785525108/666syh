package com.tchy.syh.utils;

import android.content.Context;
import android.text.Spannable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tchy.syh.R;
import com.tchy.syh.custom.CustomMsgToastView;
import com.tchy.syh.custom.CustomToastView;

import me.goldze.mvvmhabit.utils.ToastUtils;
import me.goldze.mvvmhabit.utils.Utils;

public class ToastUtil {
    public static void toastCenter(Context context){
        toastCenter(context,0,0);

    }
    public static void toastCenter(Context context,int resid,int strid){
        CustomToastView view=new CustomToastView(context,resid,null, strid);
        ToastUtils.setView(view);
        ToastUtils.setGravity(Gravity.CENTER, 0,0 );
        ToastUtils.showCustomLong();

    }
    public static void toastCenter(Context context, Spannable spannable,int strid){
        CustomToastView view=new CustomToastView(context,0,spannable, strid);
        ToastUtils.setView(view);
        ToastUtils.setGravity(Gravity.CENTER, 0,0 );
        ToastUtils.showCustomLong();

    }
    public static void toastBottom(String text){
//        CustomMsgToastView view=new CustomMsgToastView(context,0,spannable, strid);
        Toast.makeText(Utils.getContext(), text, Toast.LENGTH_SHORT).show();
//        ToastUtils.setView(null);
//        ToastUtils.setGravity(Gravity.BOTTOM, 0,200 );
//        ToastUtils.showLong(text);


    }
}

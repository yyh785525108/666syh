package com.tchy.syh.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.goldze.mvvmhabit.utils.StringUtils;

public class NumberFormatUtil {
    public static String format(String s,float per){
        return s;
//        if(StringUtils.isEmpty(s)||!TextUtils.isDigitsOnly(s)){
//            return "0";
//        }
//        return format(Integer.valueOf(s),per);
    }
    public static String format(String s,float per,String symbol){
        return s;
//        if(StringUtils.isEmpty(s)||!TextUtils.isDigitsOnly(s)){
//            return "0";
//        }
//        return format(Integer.valueOf(s),per,symbol);
    }
    public static String format(int s,float per){
        return s+"";
//        return format(s,per,"");

    }
    public static String format(int s,float per,String symbol){
        return s+"";
//        if(s<per){
//            return s+"";
//        }else{
//            float d=s/per;
//            DecimalFormat df=new DecimalFormat("#.#");
//            return df.format(d)+symbol;
//        }

    }
    public static void main(String args[]){

        System.out.println(format(10323,10000,"+"));
    }




}

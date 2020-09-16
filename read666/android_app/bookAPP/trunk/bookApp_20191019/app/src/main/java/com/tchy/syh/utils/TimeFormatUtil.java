package com.tchy.syh.utils;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.goldze.mvvmhabit.utils.StringUtils;

public class TimeFormatUtil {
    public static long minuteTime=60*1000;
    public static long hourTime=60*minuteTime;
    public static long dayTime=24*hourTime;
    public static long monthTime=30*dayTime;
    public static long yearTime=365*dayTime;

    public static String formatLatest(String s){
        if(StringUtils.isEmpty(s)){
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        long currentTime=calendar.getTimeInMillis();

        calendar.setTimeInMillis(Long.valueOf(s)*1000);

        long historyTime=calendar.getTimeInMillis();

        long computeTime=currentTime-historyTime;

        if(computeTime<minuteTime){
            return "刚刚";
        }else if(computeTime<hourTime){
            return computeTime/minuteTime+"分钟前";
        }else if(computeTime<dayTime){
            return computeTime/hourTime+"小时前";

        }else if(computeTime<monthTime){
            return computeTime/dayTime+"天前";

        }else if(computeTime<yearTime){
            return computeTime/monthTime+"个月前";

        }else {
            return computeTime/yearTime+"年前";
        }


    }
    public static String format(String s){
        if(StringUtils.isEmpty(s)){
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(s)*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(calendar.getTime());
    }
    public static String format(int s){
        if(StringUtils.isEmpty(s+"")){
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(s)*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(calendar.getTime());
    }
    public static String formatFullTime(String s){
        if(StringUtils.isEmpty(s)){
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(s)*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }
    public static String formatFullTime(int s){
        if(StringUtils.isEmpty(s+"")){
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(s)*1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }
    public static String formatPlayerCounter(int second){
        StringBuilder builder=new StringBuilder();
        int min =second/60;
        int sec=second%60;

        if(min<10){
            builder.append("0");
        }
        builder.append(min);
        builder.append(":");
        if(sec<10){
            builder.append("0");
        }
        builder.append(sec);


        return builder.toString();
    }
    public static void main(String args[]){

        System.out.println(formatPlayerCounter(0));
    }




}

package com.yyh.read666.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.yyh.read666.vo.User;

import org.json.JSONObject;


public class SharedPreferencesUtil {


    /**
     * app用shareprefence保存上一次登录成功返回的token和环信账号密码之类
     */
    public static String PRE_NAME="PRE_NAME";
    public static String USER="userJson";

    public static void saveLoginUser(Context context, JSONObject userJson){
        SharedPreferences sp=context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(USER, userJson.toString());
        editor.commit();

    }

    public static User getLoginUser(Context context){
        if (context!=null){
            SharedPreferences sp=context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
            String userJson=sp.getString(USER,"");
            User user=new Gson().fromJson(userJson,User.class);
            return user;
        }
        return null;

    }


    public static String getToken(Context context){
            if (context!=null){
                User user=getLoginUser(context);
                if (user==null){
                    return "";
                }
                String str=user.getAccess_token();
                return str;
            }
        return "";


    }




    public static void clearLoginUser(Context context){
        SharedPreferences sp=context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(USER, "");
        editor.commit();

    }


}

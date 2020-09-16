package com.tchy.syh.utils;

import android.util.Log;

import com.google.common.base.Ascii;
import com.tchy.syh.cons.HttpCons;

import java.io.UnsupportedEncodingException;

import android.net.Uri;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

public class HttpSignUtil {
    public static String getSignSrcString(HashMap<String, Object> params) {

        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        StringBuilder sign = new StringBuilder("");
        for (String key : keys) {
            sign.append(key);
            sign.append("=");
            String val = Uri.encode((String.valueOf(params.get(key))));
            val = val.replace("*", "%2A");
            val = val.replace("'", "%27");
//            val = val.replace("\"", "%22");

            Log.d("sort", "getSignSrcString: "+val);
            sign.append(val);

            //` %27  *	%2a
            sign.append("&");
        }
        if (sign.lastIndexOf("&") == sign.length() - 1) {
            sign.deleteCharAt(sign.length() - 1);
        }

        sign.append(HttpCons.APP_SECRET);

        return sign.toString();
    }

    public static String getSignStr(HashMap<String, Object> params) {
        String str = getSignSrcString(params);
        Log.d("sort", "getSignSrcString: " + str);
        return SHA1.encode(str);
    }
}

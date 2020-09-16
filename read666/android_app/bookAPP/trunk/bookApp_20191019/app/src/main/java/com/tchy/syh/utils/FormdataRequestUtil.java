package com.tchy.syh.utils;

import android.util.Log;

import com.tchy.syh.cons.HttpCons;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FormdataRequestUtil {
    public static HashMap<String, RequestBody> getFormDataRequestParams(HashMap<String, Object> params) {
        params.put("appkey", HttpCons.APP_KEY);
        HashMap<String, RequestBody> map = new HashMap<>();
        for (String key : params.keySet()) {
            RequestBody body = null;
            body = RequestBody.create(MediaType.parse("text/plain")
                    , String.valueOf(params.get(key)));

            map.put(key, body);


        }
        RequestBody sig = RequestBody.create(MediaType.parse("text/plain"), HttpSignUtil.getSignStr(params));
        map.put("sig", sig);
        return map;
    }

    public static JSONObject getFormDataRequestParamsJSON(HashMap<String, Object> params) throws JSONException {
        JSONObject object = new JSONObject();
        params.put("appkey", HttpCons.APP_KEY);
        HashMap<String, RequestBody> map = new HashMap<>();
        for (String key : params.keySet()) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(params.get(key)));
            map.put(key, body);
            object.put(key, params.get(key));

        }
        RequestBody sig = RequestBody.create(MediaType.parse("text/plain"), HttpSignUtil.getSignStr(params));
        object.put("sig", HttpSignUtil.getSignStr(params));
        map.put("sig", sig);
        Log.d("sort", "getFormDataRequestParams: " + object.toString());
        return object;
    }
}

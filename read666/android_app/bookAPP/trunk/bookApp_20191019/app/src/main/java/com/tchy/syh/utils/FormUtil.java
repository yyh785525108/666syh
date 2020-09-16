package com.tchy.syh.utils;

import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 表单工具类
 *
 */
public class FormUtil {
    /**
     * 获取控件的text
     * @param view
     * @return
     */
    public static String getInputText(TextView view){
        if (view==null){
            throw new IllegalArgumentException("传入的控件为空");
        }
        return view.getText().toString().trim();
    }

    public static String getInputTextWithTrim(TextView view){
        return getInputText(view);
    }

    /**
     * 生成Sig
     * @return
     */
    public static String genSig(Map<String, String> map){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String sig="";
        Collection<String> key= map.keySet();
        List<String> list = new ArrayList<String>(key);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            params.add(new BasicNameValuePair(list.get(i),  (String) map.get(list.get(i))));
            if (sig.equals("")) {
                sig = sig +list.get(i) + "=" +java.net.URLEncoder.encode((String) map.get(list.get(i)));

            }else{
                sig = sig +"&"+list.get(i) + "=" +java.net.URLEncoder.encode((String) map.get(list.get(i)));
            }
        }
        sig = sig + "f35ecda2362018c2d5a27c4f1031e58b";
        System.out.println("sig:"+sig);
        sig = Encrypt(sig, "SHA-1");
        return sig;
    }

    public static String Encrypt(String strSrc, String encName) {

        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "MD5";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid algorithm.");
            return null;
        }
        return strDes;
    }
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

}

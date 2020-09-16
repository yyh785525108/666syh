package com.yyh.read666.okhttp;

import com.yyh.read666.configs.Configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SigUtil {
    public static String getSig(Map<String, String> map){
        String sig="";
        Collection<String> key= map.keySet();
        List<String> list = new ArrayList<String>(key);

        //对key键值按字典升序排序
        Collections.sort(list);

        for (int i = 0; i < list.size(); i++) {
            String str=java.net.URLEncoder.encode((String) map.get(list.get(i)));
            str=str.replaceAll("\\+", "%20");
            str=str.replaceAll("\\*", "%2A");

            System.out.println("str:"+str);
            if (sig.equals("")) {
                sig = sig +list.get(i) + "=" +str;

            }else{
                sig = sig +"&"+list.get(i) + "=" +str;

            }
            System.out.println(list.get(i)+"："+(String) map.get(list.get(i)));
        }


        sig = sig + Configs.APPSECRECT;
        System.out.println("sig:"+sig);
        sig = TestEncrypt.Encrypt(sig, "SHA-1");
        System.out.println("SHA-sig:"+sig);

        return sig;
    }
}

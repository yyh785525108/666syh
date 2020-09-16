package com.yyh.read666.okhttp;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by fxjzzyo on 2017/7/12.
 */

public class OkHttpUtils {
    public final static int READ_TIMEOUT = 100;
    public final static int CONNECT_TIMEOUT = 60;
    public final static int WRITE_TIMEOUT = 60;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    private OkHttpUtils() {
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//写入超时
        //支持HTTPS请求，跳过证书验证
        ClientBuilder.sslSocketFactory(createSSLSocketFactory());
        ClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mOkHttpClient = ClientBuilder.build();
    }

    /**
     * 单例模式获取NetUtils
     *
     * @return
     */
    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * get请求，同步方式，获取网络数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     *
     * @param url
     * @return
     */
    public Response getData(String url) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * post请求，同步方式，提交数据，是在主线程中执行的，需要新起线程，将其放到子线程中执行
     *
     * @param url
     * @param bodyParams
     * @return
     */
    public Response postData(String url, Map<String, String> bodyParams) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();
        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call，得到response
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }







    /**
     * del请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param myNetCall
     * @return
     */
    public void delDataAsyn(String url, String token, final MyNetCall myNetCall) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        System.out.println("url:"+url);
        System.out.println("token:"+token);

        Request request = builder.url(url).header("Authorization", "Bearer "+token).delete().build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);


            }
        });
    }
    /**
     * get请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param myNetCall
     * @return
     */
    public void getDataAsyn(String url, final MyNetCall myNetCall) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        System.out.println("url:"+url);

        Request request = builder.url(url).get().build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);


            }
        });
    }



    /**
     * get请求，异步方式，获取网络数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param myNetCall
     * @return
     */
    public void getDataAsyn(String url, String token, final MyNetCall myNetCall) {
        //1 构造Request
        Request.Builder builder = new Request.Builder();
        System.out.println("url:"+url);
        System.out.println("token:"+token);

        Request request = builder.url(url).header("Authorization", "Bearer "+token).get().build();
        //2 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //3 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("onFailure");
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);


            }
        });
    }

    /**
     * 异步put请求
     * @param url
     * @param bodyParams
     * @param fileParams
     * @param myNetCall
     */
    public void putDataAsyn(String url, String token, Map<String, String> bodyParams, Map<String, File> fileParams, final MyNetCall myNetCall){



        MultipartBody.Builder builder= new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                System.out.println(key+":"+bodyParams.get(key));
                builder.addFormDataPart(key, bodyParams.get(key));
            }
        }
        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), fileParams.get(key));
                System.out.println(key+":"+fileParams.get(key));
                builder.addFormDataPart(key, key,fileBody);



            }
        }
        RequestBody requestBody =builder.build();




        Request request =  new Request.Builder().header("Authorization", "Bearer "+token).put(requestBody).url(url).build();


        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }

    /**
     * 异步post请求
     * @param url
     * @param bodyParams
     * @param fileParams
     * @param myNetCall
     */
    public void postDataAsyn(String url, Map<String, String> bodyParams, Map<String, File> fileParams, final MyNetCall myNetCall){

        MultipartBody.Builder builder= new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                builder.addFormDataPart(key, bodyParams.get(key));
            }
        }
        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), fileParams.get(key));
                System.out.println("key:"+key);
                builder.addFormDataPart(key, key,fileBody);



            }
        }
        RequestBody requestBody =builder.build();

        Request request =  new Request.Builder().post(requestBody).url(url).build();

        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }

    /**
     * 异步post请求
     * @param url
     * @param bodyParams
     * @param myNetCall
     */
    public void postDataAsyn(String url, String token, Map<String, String> bodyParams, String pic_key, List<File> files, final MyNetCall myNetCall){

        MultipartBody.Builder builder= new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                builder.addFormDataPart(key, bodyParams.get(key));
            }
        }
        if (files != null){
            for (File file : files) {
                builder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
            }
        }
        RequestBody requestBody =builder.build();

        Request request =  new Request.Builder().header("Authorization", "Bearer "+token).post(requestBody).url(url).build();

        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }

    /**
     * 异步put请求
     * @param url
     * @param bodyParams
     * @param fileParams
     * @param myNetCall
     */
    public void putDataAsyn(String url, String token, Map<String, String> bodyParams, Map<String, File> fileParams, String pic_key, List<File> files, final MyNetCall myNetCall){



        MultipartBody.Builder builder= new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                builder.addFormDataPart(key, bodyParams.get(key));
            }
        }
        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), fileParams.get(key));
                System.out.println("key:"+key);
                builder.addFormDataPart(key, key,fileBody);
            }
        }
        if (files != null){
            for (File file : files) {
                builder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
            }
        }
        RequestBody requestBody =builder.build();




        Request request =  new Request.Builder().header("Authorization", "Bearer "+token).put(requestBody).url(url).build();


        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("response:"+response);

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }



    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param myNetCall
     */
    public void postDataAsyn(String url, Map<String, String> bodyParams, final MyNetCall myNetCall) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).build();

        System.out.println("url:"+url);
        //遍历map集合
        for(Map.Entry<String, String> a:bodyParams.entrySet()){
            System.out.println(a.getKey()+" : "+a.getValue());

        }


        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }

    /**
     * post请求，异步方式，提交数据，是在子线程中执行的，需要切换到主线程才能更新UI
     *
     * @param url
     * @param bodyParams
     * @param myNetCall
     */
    public void postDataAsyn(String url, String token , Map<String, String> bodyParams, final MyNetCall myNetCall) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).header("Authorization", token).build();
        System.out.println("Url:"+url);
        System.out.println("token:"+token);

        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }

    public void putDataAsyn(String url, String token , Map<String, String> bodyParams, final MyNetCall myNetCall) {
        //1构造RequestBody
        RequestBody body = setRequestBody(bodyParams);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
//        Request request = requestBuilder.post(body).url(url).build();
        Request request = requestBuilder.put(body).url(url).header("Authorization", token).build();

        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str=response.body().string();
                System.out.println("response:"+str);
                myNetCall.success(call, str);
            }
        });
    }



    /**
     * post的请求参数，构造RequestBody
     *
     * @param BodyParams
     * @return
     */
    private RequestBody setRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
                Log.d("post http", "post_Params===" + key + "====" + BodyParams.get(key));
            }
        }
        body = formEncodingBuilder.build();
        return body;

    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    public SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    public String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public void postJsonAsyn(String url, String json, String token, final MyNetCall myNetCall) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        //2 构造Request
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(body).url(url).header("Authorization", token).build();




        //3 将Request封装为Call
        Call call = mOkHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                myNetCall.failed(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                myNetCall.success(call, str);
            }
        });
    }

    /**
     * 自定义网络回调接口
     */
    public interface MyNetCall {
        //        void success(Call call, Response response) throws IOException;
        void success(Call call, String response) throws IOException;

        void failed(Call call, IOException e);
    }

    /**
     * 用于信任所有证书
     */
    class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}


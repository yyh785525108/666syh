package com.tchy.syh.shopping.address;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.addresspicker.huichao.addresspickerlibrary.InitAreaTask;
import com.addresspicker.huichao.addresspickerlibrary.address.Province;
import com.alibaba.fastjson.JSON;
import com.tchy.syh.shopping.entity.AddressDataBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadAreaTask extends AsyncTask<Integer, Integer, Boolean> {
    protected LoadAreaTask.onLoadingAddressListener onLoadingAddressListener;
    Context mContext;
    List<AddressDataBean> provinces;

    public LoadAreaTask(Context context, List<AddressDataBean> provinces) {
        this.mContext = context;
        this.provinces = provinces;
    }

    public void setOnLoadingAddressListener(LoadAreaTask.onLoadingAddressListener onLoadingAddressListener) {
        this.onLoadingAddressListener = onLoadingAddressListener;
        this.onLoadingAddressListener.onLoading();
    }

    protected void onPreExecute() {
    }

    protected void onPostExecute(Boolean result) {
        if (this.provinces.size() > 0) {
            this.onLoadingAddressListener.onLoadFinished();
        } else {
            Toast.makeText(this.mContext, "数据初始化失败", Toast.LENGTH_LONG).show();
        }

    }

    protected Boolean doInBackground(Integer... params) {
        String address = null;
        InputStream in = null;

        try {
            in = this.mContext.getResources().getAssets().open("area_data.json");
            byte[] arrayOfByte = new byte[in.available()];
            in.read(arrayOfByte);
            address = new String(arrayOfByte, "UTF-8");
            List<AddressDataBean> provinces1 = JSON.parseArray(address, AddressDataBean.class);
            this.provinces.addAll(provinces1);
            Boolean var6 = true;
            return var6;
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {
                    ;
                }
            }

        }

        return false;
    }

    public interface onLoadingAddressListener {
        void onLoadFinished();

        void onLoading();
    }
}
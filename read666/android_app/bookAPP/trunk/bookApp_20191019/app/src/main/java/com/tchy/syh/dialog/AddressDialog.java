package com.tchy.syh.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.addresspicker.huichao.addresspickerlibrary.R.id;
import com.addresspicker.huichao.addresspickerlibrary.R.layout;
import com.addresspicker.huichao.addresspickerlibrary.R.style;
import com.addresspicker.huichao.addresspickerlibrary.wheel.OnWheelChangedListener;
import com.addresspicker.huichao.addresspickerlibrary.wheel.OnWheelClickedListener;
import com.addresspicker.huichao.addresspickerlibrary.wheel.WheelView;
import com.addresspicker.huichao.addresspickerlibrary.wheel.adapter.AbstractWheelTextAdapter;
import com.tchy.syh.shopping.entity.AddressDataBean;

import java.util.ArrayList;
import java.util.List;

public class AddressDialog extends Dialog {
    private static final int DEFAULT_ITEMS = 5;
    private static final int UPDATE_CITY_WHEEL = 11;
    private static final int UPDATE_COUNTY_WHEEL = 12;
    AbstractWheelTextAdapter provinceAdapter;
    AbstractWheelTextAdapter cityAdapter;
    private Activity mContext;
    private ArrayList<AddressDataBean> mProvinces = new ArrayList();
    private ArrayList<AddressDataBean> mCities = new ArrayList();
    private ArrayList<AddressDataBean> mCounties = new ArrayList();
    private AbstractWheelTextAdapter countyAdapter;
    private WheelView provinceWheel;
    private WheelView citiesWheel;
    private WheelView countiesWheel;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (AddressDialog.this.isShowing()) {
                switch(msg.what) {
                    case 11:
                        AddressDialog.this.mCities.clear();
                        AddressDialog.this.mCities.addAll(((AddressDataBean)AddressDialog.this.mProvinces.get(msg.arg1)).getAreas());
                        AddressDialog.this.citiesWheel.invalidateWheel(true);
                        AddressDialog.this.citiesWheel.setCurrentItem(0, false);
                        AddressDialog.this.mCounties.clear();
                        AddressDialog.this.mCounties.addAll(((AddressDataBean)AddressDialog.this.mCities.get(0)).getAreas());
                        AddressDialog.this.countiesWheel.invalidateWheel(true);
                        AddressDialog.this.countiesWheel.setCurrentItem(0, false);
                        break;
                    case 12:
                        AddressDialog.this.mCounties.clear();
                        AddressDialog.this.mCounties.addAll(((AddressDataBean)AddressDialog.this.mCities.get(msg.arg1)).getAreas());
                        AddressDialog.this.countiesWheel.invalidateWheel(true);
                        AddressDialog.this.countiesWheel.setCurrentItem(0, false);
                }

            }
        }
    };

    public AddressDialog(Activity context, List<AddressDataBean> provinces, AddressDataBean defaultProvince, AddressDataBean defaultCity, AddressDataBean defaultCounty, final AddressDialog.onCityPickedListener listener) {
        super(context);
        this.mContext = context;
        this.getWindow().requestFeature(1);
        this.getWindow().setGravity(80);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        this.getWindow().setWindowAnimations(style.AnimBottom);
        View rootView = this.getLayoutInflater().inflate(layout.dialog_city_picker, (ViewGroup)null);
        int screenWidth = this.mContext.getWindowManager().getDefaultDisplay().getWidth();
        LayoutParams params = new LayoutParams(screenWidth, -1);
        super.setContentView(rootView, params);
        this.mProvinces.addAll(provinces);
        this.initViews();
        this.setDefaultArea(defaultProvince, defaultCity, defaultCounty);
        View done = this.findViewById(id.done);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listener != null) {
                    AddressDataBean province = AddressDialog.this.mProvinces.size() > 0 ? (AddressDataBean)AddressDialog.this.mProvinces.get(AddressDialog.this.provinceWheel.getCurrentItem()) : null;
                    AddressDataBean city = AddressDialog.this.mCities.size() > 0 ? (AddressDataBean)AddressDialog.this.mCities.get(AddressDialog.this.citiesWheel.getCurrentItem()) : null;
                    AddressDataBean county = AddressDialog.this.mCounties.size() > 0 ? (AddressDataBean)AddressDialog.this.mCounties.get(AddressDialog.this.countiesWheel.getCurrentItem()) : null;
                    listener.onPicked(province, city, county);
                }

                AddressDialog.this.dismiss();
            }
        });
        View cancel = this.findViewById(id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddressDialog.this.dismiss();
            }
        });
    }

    private void setDefaultArea(AddressDataBean defaultProvince, AddressDataBean defaultCity, AddressDataBean defaultCounty) {
        int provinceItem = 0;
        int cityItem = 0;
        int countyItem = 0;
        int i;
        if (defaultProvince == null) {
            defaultProvince = (AddressDataBean)this.mProvinces.get(0);
            provinceItem = 0;
        } else {
            for(i = 0; i < this.mProvinces.size(); ++i) {
                if (((AddressDataBean)this.mProvinces.get(i)).getId().equals(defaultProvince.getId())) {
                    provinceItem = i;
                    break;
                }
            }
        }

        this.mCities.clear();
        this.mCities.addAll(defaultProvince.getAreas());
        if (this.mCities.size() == 0) {
            this.mCities.add(new AddressDataBean());
            cityItem = 0;
        } else if (defaultCity == null) {
            defaultCity = (AddressDataBean)this.mCities.get(0);
            cityItem = 0;
        } else {
            for(i = 0; i < this.mCities.size(); ++i) {
                if (((AddressDataBean)this.mCities.get(i)).getId().equals(defaultCity.getId())) {
                    cityItem = i;
                    break;
                }
            }
        }

        this.mCounties.clear();
        this.mCounties.addAll(defaultCity.getAreas());
        if (this.mCounties.size() == 0) {
            this.mCounties.add(new AddressDataBean());
            countyItem = 0;
        } else if (defaultCounty == null) {
            defaultCounty = (AddressDataBean)this.mCounties.get(0);
            countyItem = 0;
        } else {
            for(i = 0; i < this.mCounties.size(); ++i) {
                if (((AddressDataBean)this.mCounties.get(i)).getId().equals(defaultCounty.getId())) {
                    countyItem = i;
                    break;
                }
            }
        }

        this.provinceWheel.setCurrentItem(provinceItem, false);
        this.citiesWheel.setCurrentItem(cityItem, false);
        this.countiesWheel.setCurrentItem(countyItem, false);
    }

    private void initViews() {
        this.provinceWheel = (WheelView)this.findViewById(id.provinceWheel);
        this.citiesWheel = (WheelView)this.findViewById(id.citiesWheel);
        this.countiesWheel = (WheelView)this.findViewById(id.countiesWheel);
        this.provinceAdapter = new AbstractWheelTextAdapter(this.mContext, layout.wheel_text) {
            public int getItemsCount() {
                return AddressDialog.this.mProvinces.size();
            }

            protected CharSequence getItemText(int index) {
                return ((AddressDataBean)AddressDialog.this.mProvinces.get(index)).getName();
            }
        };
        this.cityAdapter = new AbstractWheelTextAdapter(this.mContext, layout.wheel_text) {
            public int getItemsCount() {
                return AddressDialog.this.mCities.size();
            }

            protected CharSequence getItemText(int index) {
                return ((AddressDataBean)AddressDialog.this.mCities.get(index)).getName();
            }
        };
        this.countyAdapter = new AbstractWheelTextAdapter(this.mContext, layout.wheel_text) {
            public int getItemsCount() {
                return AddressDialog.this.mCounties.size();
            }

            protected CharSequence getItemText(int index) {
                return ((AddressDataBean)AddressDialog.this.mCounties.get(index)).getName();
            }
        };
        this.provinceWheel.setViewAdapter(this.provinceAdapter);
        this.provinceWheel.setCyclic(false);
        this.provinceWheel.setVisibleItems(5);
        this.citiesWheel.setViewAdapter(this.cityAdapter);
        this.citiesWheel.setCyclic(false);
        this.citiesWheel.setVisibleItems(5);
        this.countiesWheel.setViewAdapter(this.countyAdapter);
        this.countiesWheel.setCyclic(false);
        this.countiesWheel.setVisibleItems(5);
        OnWheelClickedListener clickListener = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                if (itemIndex != wheel.getCurrentItem()) {
                    wheel.setCurrentItem(itemIndex, true, 500);
                }

            }
        };
        this.provinceWheel.addClickingListener(clickListener);
        this.citiesWheel.addClickingListener(clickListener);
        this.countiesWheel.addClickingListener(clickListener);
        this.provinceWheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                AddressDialog.this.mHandler.removeMessages(11);
                Message msg = Message.obtain();
                msg.what = 11;
                msg.arg1 = newValue;
                AddressDialog.this.mHandler.sendMessageDelayed(msg, 50L);
            }
        });
        this.citiesWheel.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                AddressDialog.this.mHandler.removeMessages(12);
                Message msg = Message.obtain();
                msg.what = 12;
                msg.arg1 = newValue;
                AddressDialog.this.mHandler.sendMessageDelayed(msg, 50L);
            }
        });
    }

    public interface onCityPickedListener {
        void onPicked(AddressDataBean var1, AddressDataBean var2, AddressDataBean var3);
    }
}
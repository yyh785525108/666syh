package com.tchy.syh.shopping.address;

import androidx.databinding.ObservableField;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ShopAddressModBinding;
import com.tchy.syh.dialog.AddressDialog;
import com.tchy.syh.shopping.entity.AddressDataBean;
import com.tchy.syh.shopping.entity.AddressResp;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ModFragment extends BaseFragment<ShopAddressModBinding,ModVM>{
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.shop_address_mod;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }



    @Override
    public ModVM initViewModel() {
        String json =getArguments().getString("bean");
        Gson gson=new Gson();

        return new ModVM(this.getContext(),gson.fromJson(json, AddressResp.DataBean.class));
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //没有地址数据，就去初始化
        LoadAreaTask initAreaTask = new LoadAreaTask(getContext(), viewModel.datas);
        initAreaTask.execute(0);
        initAreaTask.setOnLoadingAddressListener(new LoadAreaTask.onLoadingAddressListener() {
            @Override
            public void onLoadFinished() {

                String did=viewModel.address.get().getDistrict();
                String pid= viewModel.address.get().getProvince();
                String cid= viewModel.address.get().getCity();
                boolean flag=false;
                StringBuilder builder=new StringBuilder();
                for (AddressDataBean p : viewModel.datas) {
                    if(p.getId().equals(pid)){
                        builder.append(p.getName());
                        for (AddressDataBean c : p.getAreas()) {
                            if(c.getId().equals(cid)){
                                builder.append(" ");
                                builder.append(c.getName());
                                for (AddressDataBean d : c.getAreas()) {
                                    if(d.getId().equals(did)){
                                        builder.append(" ");
                                        builder.append(d.getName());
                                        flag=true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if(flag){
                    viewModel.result.set(builder.toString());
                }

            }

            @Override
            public void onLoading() {

            }
        });
        binding.selector.setOnClickListener(v->{
            if (viewModel.datas.size() > 0) {
                showAddressDialog();
            }
        });




    }

    private void showAddressDialog() {//初始化地址数据后，显示地址选择框


        new AddressDialog(getActivity(), viewModel.datas, null, null, null,
                new AddressDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(AddressDataBean selectProvince,
                                         AddressDataBean selectCity, AddressDataBean selectCounty) {

                        AddressResp.DataBean bean=new AddressResp.DataBean();
                        bean.setId(viewModel.address.get().getId());


                        bean.setProvince(selectProvince.getId());
                        bean.setCity(selectCity.getId());
                        bean.setDistrict(selectCounty.getId());
                        bean.setProvinceStr(selectProvince.getName());
                        bean.setCityStr(selectCity.getName());
                        bean.setDistrictStr(selectCounty.getName());

                        viewModel.address.set(bean);
                        String str=bean.getProvinceStr()+" "+bean.getCityStr()+" "+bean.getDistrictStr();
                        viewModel.result.set(str);

                    }
                }).show();
    }








}

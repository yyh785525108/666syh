package com.tchy.syh.shopping.submit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tchy.syh.R;
import com.tchy.syh.databinding.ShopOrderSubmitBinding;

import me.goldze.mvvmhabit.BR;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ConvertUtils;

public class SubmitFrag extends BaseFragment<ShopOrderSubmitBinding,SubmitVm> {
    @Override
    public int initContentView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return R.layout.shop_order_submit;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public SubmitVm initViewModel() {
        return new SubmitVm(getContext(),getArguments().getString("id"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.bonusPay.setOnCheckedChangeListener(listener);
        binding.wechatPay.setOnCheckedChangeListener(listener);
        binding.alipay.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(viewModel!=null){
            viewModel.getAddress();
        }

    }

    public CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton v, boolean isChecked) {
            Drawable drawable;
            if(isChecked)
                drawable= v.getContext().getResources().getDrawable(R.drawable.radio_checked);
            else
                drawable= v.getContext().getResources().getDrawable(R.drawable.radio_default);
            int size=ConvertUtils.dp2px(24);
            v.setCompoundDrawablesWithIntrinsicBounds(v.getCompoundDrawables()[0],  v.getCompoundDrawables()[1],drawable,  v.getCompoundDrawables()[3]);

        }
    };
}

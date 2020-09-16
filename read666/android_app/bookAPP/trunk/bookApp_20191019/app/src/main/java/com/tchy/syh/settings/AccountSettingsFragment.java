package com.tchy.syh.settings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.AccountSettingsBinding;

import me.goldze.mvvmhabit.base.BaseFragment;

public class AccountSettingsFragment extends BaseFragment<AccountSettingsBinding,AccountSettingVM> {


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.account_settings;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public AccountSettingVM initViewModel() {
        return new AccountSettingVM(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.user_info();
    }
}

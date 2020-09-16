package com.tchy.syh.userAccount.account_management.mod_info;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ModNickNameBinding;

import me.goldze.mvvmhabit.base.BaseFragment;

public class ModNickNameFragment extends BaseFragment<ModNickNameBinding,ModInfoVm> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.mod_nick_name;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ModInfoVm initViewModel() {
        return new ModInfoVm(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputFilter filter=new InputFilter.LengthFilter(10);
        binding.et.setFilters(new InputFilter[]{filter});

    }
}

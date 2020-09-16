package com.tchy.syh.userAccount.account_management.pwd;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.AccountModPwdFragBinding;
import com.tchy.syh.userAccount.AccountCommonVm;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * Created by goldze on 2017/7/17.
 * 详情界面
 */

public class ModPwdFragment extends BaseFragment<AccountModPwdFragBinding,AccountCommonVm>{



    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.account_mod_pwd_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public AccountCommonVm initViewModel() {
        return new AccountCommonVm(this.getContext());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int state =getArguments().getInt("state");
        if(state==0){
            binding.etAccount.setVisibility(View.GONE);
            viewModel.old_pwd.set(SPUtils.getInstance().getString("password"));
        }
    }


}

package com.tchy.syh.userAccount.forget;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.AccountUserLoginForgetPwdFragBinding;
import com.tchy.syh.userAccount.AccountCommonVm;

import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2017/7/17.
 * 详情界面
 */

public class ForgetFragment extends BaseFragment<AccountUserLoginForgetPwdFragBinding,AccountCommonVm>{



    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.account_user_login_forget_pwd_frag;
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

    }

}

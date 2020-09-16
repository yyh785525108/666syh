package com.tchy.syh.userAccount;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.AccountResultBinding;

import me.goldze.mvvmhabit.base.BaseFragment;

public class ResultFragment extends BaseFragment<AccountResultBinding, com.tchy.syh.userAccount.ResultVM>{
    public int icon;
    public String msg;
    public String title;
    public String btnText;
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.account_result;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public com.tchy.syh.userAccount.ResultVM initViewModel() {
        return new com.tchy.syh.userAccount.ResultVM(getActivity(),icon,msg,title,btnText);
    }

    @Override
    public void initParam() {
        super.initParam();
        Bundle bundle=getArguments();
        icon=bundle.getInt("icon");
        msg=bundle.getString("msg");
        title=bundle.getString("title");
        btnText=bundle.getString("btnText");

    }
}

package com.tchy.syh.userAccount.userLogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2017/7/17.
 * 详情界面
 */

public class LoginAccountFragment extends BaseFragment {


    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();
//        if (mBundle != null) {
//            entity = mBundle.getParcelable("entity");
//        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.account_user_login_account_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public LoginViewModel initViewModel() {
        return new LoginViewModel(this.getContext());
    }

    @Override
    public void initViewObservable() {
        //监听刷新界面

        ((LoginViewModel) this.viewModel).uiControl.subscribeOn(AndroidSchedulers.mainThread()).subscribe(viewModel ->
                {
                    ((LoginActivity)getActivity()).switchFragment();
                }

        );
    }


}

package com.tchy.syh.userAccount.register;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.UserRegisterFragBinding;
import com.tchy.syh.userAccount.userLogin.LoginActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 * 详情界面
 */

public class RegisterFragment extends BaseFragment<UserRegisterFragBinding,RegisterVM>{


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
        return R.layout.user_register_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public RegisterVM initViewModel() {
        return new RegisterVM(this.getContext());
    }
    @Override
    public void initViewObservable() {
        //监听刷新界面

        ((RegisterVM) this.viewModel).uiControl.subscribeOn(AndroidSchedulers.mainThread()).subscribe(viewModel ->
                {
                    ((LoginActivity)getActivity()).switchFragment();
                }

        );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SpannableString ss = new SpannableString("我已阅读并接受《用户协议》");
//
//        ss.setSpan(new ClickSpan("http://www.baidu.com"), 7, ss.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        binding.cb.setText(ss);
//        binding.cb.setMovementMethod(new LinkMovementMethod());


    }
    public class ClickSpan extends URLSpan{
        String url;

        public ClickSpan (String url){
            super(url);
            this.url=url;
        }
        @Override
        public void onClick(View widget) {
            ToastUtils.showLong("url");

        }
    }
}

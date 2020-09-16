package com.tchy.syh.userAccount.forget;

import android.os.Bundle;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.AccountForgetSelectFragBinding;
import com.tchy.syh.userAccount.AccountCommonVm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by goldze on 2017/7/17.
 * 详情界面
 */

public class ForgetSelectFragment extends BaseFragment<AccountForgetSelectFragBinding,AccountCommonVm>{



    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.account_forget_select_frag;
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
        String email=getArguments().getString("email");
        String mobile=getArguments().getString("mobile");


        StringBuilder emailBuilder=new StringBuilder();
        if(!TextUtils.isEmpty(email)){
            int eLen=email.length();
            if(isEmail(email)){
                String startStr=email.substring(0,3);
                emailBuilder.append(startStr);
                String endStr=email.substring(email.indexOf("@"));


                int placeHolderLength =eLen-startStr.length()-endStr.length();
                for(int i=0;i<placeHolderLength;i++){
                    emailBuilder.append("*");
                }
                emailBuilder.append(endStr);
            }
        }
        StringBuilder mobileBuilder=new StringBuilder();
        if(!TextUtils.isEmpty(mobile)){
            int eLen=mobile.length();
            if(eLen>=11){
                String startStr=mobile.substring(0,3);
                mobileBuilder.append(startStr);
                String endStr=mobile.substring(eLen-2);


                int placeHolderLength =eLen-startStr.length()-endStr.length();
                for(int i=0;i<placeHolderLength;i++){
                    mobileBuilder.append("*");
                }
                mobileBuilder.append(endStr);
            }
        }
        Log.d("sort", "onViewCreated: "+emailBuilder.toString());

    }
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

}

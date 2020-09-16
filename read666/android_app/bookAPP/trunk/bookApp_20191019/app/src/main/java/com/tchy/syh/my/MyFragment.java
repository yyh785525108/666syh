package com.tchy.syh.my;

import android.content.Intent;
import androidx.databinding.Observable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.MyFragmentBinding;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.ToastUtil;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;


public class MyFragment extends BaseFragment<MyFragmentBinding, MyVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.my_fragment;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }


    @Override
    public MyVM initViewModel() {
        return new MyVM(this.getContext());
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Messenger.getDefault().register(this.getActivity(), "refresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                viewModel.user_info();
            }
        });
        Messenger.getDefault().register(this.getActivity(), "myRefresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                viewModel.user_info();
            }
        });
        binding.relativeLayout2.setOnClickListener(v->{
            if(viewModel.bean.get()==null){
                Intent intent=new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
                ToastUtil.toastBottom("请先登录");
                return;
            }
            Messenger.getDefault().sendNoMsg("share");
       });


    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.bean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.bean.get()==null){
                    binding.avatar.setImageResource(R.mipmap.book_grey_round_bg);
                    binding.textView25.setText("请登录");
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

            viewModel.user_info();
        }
    }
}

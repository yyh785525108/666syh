package com.tchy.syh.shopping.list;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ShopMoreListBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ListFragment extends BaseFragment<ShopMoreListBinding,PageListViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.shop_more_list;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public PageListViewModel initViewModel() {

        return new PageListViewModel(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Log.d("sort", "onViewCreated111: "+this.getUserVisibleHint());
        binding.vp.setOffscreenPageLimit(4);
//        binding.tabs.setTextColor(getContext().getResources().getColor(R.color.textColorDarkThemeLight));
//        binding.tabs.setUnderlineColor(Color.parseColor("#dddddd"));
//        binding.tabs.setShouldExpand(false);
//        binding.tabs.setUnderlineHeight(1);


//        binding.tabs.notifyDataSetChanged();
    }

    String id = "";

    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();


        if (mBundle != null) {
            id = mBundle.getString("id");
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.isInitFinish.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isInitFinish.get()) {
//                    binding.tabs.setViewPager(binding.vp);
                    binding.vp.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.tabs.setViewPager(binding.vp);
                        }
                    }, 100);

                }
            }
        });
    }


}

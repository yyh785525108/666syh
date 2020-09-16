package com.tchy.syh.fav.collect;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.FavListFragBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ListFragment extends BaseFragment<FavListFragBinding, ListVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fav_list_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ListVM initViewModel() {

        return new ListVM(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("sort", "onViewCreated: ");
        binding.vp.setOffscreenPageLimit(3);
//        binding.tabs.setTextColor(getContext().getResources().getColor(R.color.textColorDarkThemeLight));
//        binding.tabs.setUnderlineColor(Color.parseColor("#dddddd"));
//        binding.tabs.setShouldExpand(false);
//        binding.tabs.setUnderlineHeight(1);
        binding.back.setOnClickListener(v->{
            getActivity().finish();
        });
        binding.vp.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

                binding.tabs.setViewPager(viewPager);
            }
        });

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

    }
}

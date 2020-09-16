package com.tchy.syh.read.home;

import android.content.Context;

import androidx.databinding.Observable;
import androidx.databinding.ObservableList;
import android.graphics.Color;
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
import com.tchy.syh.databinding.ReadListFragBinding;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ConvertUtils;


public class ReadListFragment extends BaseFragment<ReadListFragBinding, ReadPageListViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.read_list_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ReadPageListViewModel initViewModel() {

        return new ReadPageListViewModel(this.getContext());
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
        ViewPagerHelper.bind(binding.magicIndicator, binding.vp);
        binding.vp.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {


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
        viewModel.isInitFinish.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isInitFinish.get()) {
//                    binding.tabs.setViewPager(binding.vp);
                    binding.vp.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            binding.tabs.setViewPager(binding.vp);
                            CommonNavigator commonNavigator = new CommonNavigator(getContext());
                            commonNavigator.setAdapter(new CommonNavigatorAdapter() {

                                @Override
                                public int getCount() {
                                    return viewModel.categoryList == null ? 0 :  viewModel.categoryList .size();
                                }

                                @Override
                                public IPagerTitleView getTitleView(Context context, final int index) {
                                    ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                                    colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
                                    colorTransitionPagerTitleView.setSelectedColor(Color.RED);
                                    colorTransitionPagerTitleView.setTextSize(16);
                                    colorTransitionPagerTitleView.setText( viewModel.categoryList.get(index).getName());
                                    colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            binding.vp.setCurrentItem(index);
                                        }
                                    });
                                    return colorTransitionPagerTitleView;
                                }

                                @Override
                                public IPagerIndicator getIndicator(Context context) {
                                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                                    indicator.setColors(Color.RED);
                                    indicator.setLineWidth(ConvertUtils.dp2px(30));
                                    indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                                    return indicator;
                                }
                            });
                            binding.magicIndicator .setNavigator(commonNavigator);
                        }
                    }, 100);

                }
            }
        });
    }
    public boolean isFirstLoad=true;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("sort", "setUserVisibleHint: "+isVisibleToUser);
        if(isVisibleToUser&&isFirstLoad){
            isFirstLoad=false;
            viewModel.getCategory();
        }
    }

}

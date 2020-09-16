package com.tchy.syh.daily_advance.daily_home;

import android.content.Context;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.databinding.DailyAdvanceFragmentBinding;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ConvertUtils;

/**
 * 日精进模块首页
 */
public class DailyFragment extends BaseFragment<DailyAdvanceFragmentBinding, DailyViewModel> {
    public ObservableBoolean isInitFinish=new ObservableBoolean(false);
    @Override
    public int initContentView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return R.layout.daily_advance_fragment;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public DailyViewModel initViewModel() {
        return new DailyViewModel(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.waveView2.setDuration(5000);
        binding.waveView2.setStyle(Paint.Style.FILL_AND_STROKE);
        binding.waveView2.setColor(getResources().getColor(R.color.colorPrimaryHalf));
        binding.waveView2.setInterpolator(new AnticipateInterpolator());
       // binding.waveView2.setColor(getResources().getColor(R.color.colorAccentHalf));
        binding.waveView2.start();
        binding.vp.setOffscreenPageLimit(1);
//        binding.tabs.setTabPaddingLeftRight(15);
//       binding.vp.setCurrentItem(2);
//        viewModel.pagerIndex.set(2);
        ViewPagerHelper.bind(binding.magicIndicator, binding.vp);

        binding.vp.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
//              binding.tabs.setViewPager(binding.vp);

            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        LearnCate.DataBean bean = new LearnCate.DataBean("1","热榜");

        viewModel.pagerIndex.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

            }
        });
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
                                    return viewModel.cates == null ? 0 :  viewModel.cates .size();
                                }

                                @Override
                                public IPagerTitleView getTitleView(Context context, final int index) {
                                    ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                                    colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
                                    colorTransitionPagerTitleView.setSelectedColor(Color.RED);
                                    colorTransitionPagerTitleView.setTextSize(16);
                                    colorTransitionPagerTitleView.setText( viewModel.cates.get(index).getName());
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
                            binding.magicIndicator.setNavigator(commonNavigator);
                        }
                    }, 100);

                }
            }
        });
    }
}

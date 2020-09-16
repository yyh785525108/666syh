package com.tchy.syh.book.book_list;

import android.content.Context;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
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
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.databinding.BookListFragBinding;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ConvertUtils;


public class BookListFragment extends BaseFragment<BookListFragBinding, BookListViewModel> {
    public ObservableBoolean isInitFinish=new ObservableBoolean(false);

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_list_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookListViewModel initViewModel() {
        BookListViewModel vm=   new BookListViewModel(this.getContext());
        vm.setId(id,cates);
        return vm;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("sort", "onViewCreated: ");
//        binding.vp.setCurrentItem(0);
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
                            binding.magicIndicator .setNavigator(commonNavigator);
                        }
                    }, 100);

                }
            }
        });
    }

    String id="";
    List<LearnCate.DataBean> cates;
    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();


        if (mBundle != null) {
           id=mBundle.getString("id");
            cates=mBundle.getParcelableArrayList("categorys");
        }

    }


}

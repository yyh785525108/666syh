package com.tchy.syh.book.book_home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.book_detail.entity.FreeBooksResp;
import com.tchy.syh.custom.CustomDivider;
import com.tchy.syh.databinding.BookFragmentBinding;

import java.util.Date;

import me.goldze.mvvmhabit.base.BaseFragment;


public class BookFragment extends BaseFragment<BookFragmentBinding, BookViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_fragment;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookViewModel initViewModel() {
        return new BookViewModel(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.pageIndicatorView.setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.rvRecentbook.setNestedScrollingEnabled(false);
//        DividerItemDecoration divider = new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(this.getContext(),R.drawable.divider));
        binding.rvRecentbook.addItemDecoration(new CustomDivider(this.getContext()));

        binding.freeBooks.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                binding.bookPageIndicator.setSelected(position);

                FreeBooksResp.DataBean.ListBean bean= viewModel.freeBookList.get(position).bean;
                viewModel.selectFreeFName.set("「"+bean.getFname()+"」");
                viewModel.selectFreeName.set(bean.getBookname());
                viewModel.selectFreeIntro.set(bean.getIntro());

                Log.d("sort", "onPageSelected: "+ bean.getAdd_time());


                Date date=new Date();

                viewModel.selectLimitTime.set("00:10:22");


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}

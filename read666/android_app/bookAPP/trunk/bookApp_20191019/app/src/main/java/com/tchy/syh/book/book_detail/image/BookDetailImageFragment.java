package com.tchy.syh.book.book_detail.image;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.BookDetailBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class BookDetailImageFragment extends BaseFragment<BookDetailBinding, BookDetailImagePageVm> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_detail_image;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookDetailImagePageVm initViewModel() {
        return new BookDetailImagePageVm(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("sort", "onViewCreated: ");
//        binding.vp.setOffscreenPageLimit(3);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (viewModel == null) {
            return;
        }
        if (isVisibleToUser) {
            viewModel.content.set(viewModel.contentValue);

        } else {
            viewModel.content.set("");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewModel != null && viewModel.content != null)
            viewModel.content.set("");

    }
}

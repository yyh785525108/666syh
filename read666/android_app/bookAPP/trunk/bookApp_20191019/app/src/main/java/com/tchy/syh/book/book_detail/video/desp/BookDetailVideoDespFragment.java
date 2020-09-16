package com.tchy.syh.book.book_detail.video.desp;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.databinding.BookDetailVideoDespBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class BookDetailVideoDespFragment extends BaseFragment<BookDetailVideoDespBinding, BookDetailVideoPageDespVm> {
    String book_id ;

    public void setBook_id(String id) {

        this.book_id = id;
    }
    public void setHeaderDesp(BookDetailResp.DataBean bean){
        viewModel.dataBean.set(bean);
    }
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_detail_video_desp;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookDetailVideoPageDespVm initViewModel() {
        return new BookDetailVideoPageDespVm(this.getContext(),book_id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        this.binding.rv.setNestedScrollingEnabled(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



}

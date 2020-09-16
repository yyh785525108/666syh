package com.tchy.syh.book.book_detail.video.comment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.databinding.BookDetailVideoCommentBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class  BookDetailVideoCommentFragment extends BaseFragment<BookDetailVideoCommentBinding, BookDetailVideoPageCommentVm> {


    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_detail_video_comment;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookDetailVideoPageCommentVm initViewModel() {
        return new BookDetailVideoPageCommentVm(this.getContext(),((BookVideoPlayerActivity)getActivity()).book_id);
    }
    RecyclerView.Adapter  adapter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        this.binding.twinklingRefreshLayout.setNestedScrollingEnabled(false);
//        this.binding.rv.setNestedScrollingEnabled(false);


//        adapter=new RecyclerView.Adapter() {
//            @NonNull
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 0;
//            }
//        };
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.rv.setLayoutManager(layoutManager);
//        binding.rv.setAdapter(mAdapter);
    }



}

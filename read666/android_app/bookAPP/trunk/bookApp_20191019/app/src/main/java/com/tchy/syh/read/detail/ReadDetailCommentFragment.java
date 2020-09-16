package com.tchy.syh.read.detail;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ReadDetailCommentBinding;
import com.tchy.syh.dialog.CommentDialog;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ReadDetailCommentFragment extends BaseFragment<ReadDetailCommentBinding, ReadDetailPageCommentVm> {


    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.read_detail_comment;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ReadDetailPageCommentVm initViewModel() {
        String id =getArguments().getString("id");
        String title =getArguments().getString("title");
        return new ReadDetailPageCommentVm(this.getContext(),id,title);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    CommentDialog commentDialog;
    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.commentDialogShow.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.commentDialogShow.get()){
                    if(commentDialog!=null){

                        commentDialog=null;
                    }
                    commentDialog=new CommentDialog();
                    Bundle bundle=new Bundle();
                    bundle.putString("type","read" );
                    commentDialog.setArguments(bundle);
                    commentDialog.show(getActivity().getSupportFragmentManager(), "comment");
//                    commentDialog.onDismiss(new DialogInterface() {
//                        @Override
//                        public void cancel() {
//
//                        }
//
//                        @Override
//                        public void dismiss() {
//                            viewModel.commentDialogShow.set(false);
//                        }
//                    });
                }else{
                    if(commentDialog!=null){
                        commentDialog.dismiss();
                    }
                }
            }
        });
    }
}

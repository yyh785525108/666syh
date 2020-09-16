package com.tchy.syh.listen;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ListenListFragBinding;

import me.goldze.mvvmhabit.base.BaseFragment;

public class ListenFragment extends BaseFragment<ListenListFragBinding, ListenViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.listen_list_frag;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public ListenViewModel initViewModel() {
        return new ListenViewModel(getActivity());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        init();
    }

    private void init() {
        viewModel.isFinishRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        viewModel.isFinishLoadMore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
    }
}

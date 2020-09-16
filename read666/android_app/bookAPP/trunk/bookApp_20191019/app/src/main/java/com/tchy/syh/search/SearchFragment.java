package com.tchy.syh.search;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.SearchFragBinding;
import com.tchy.syh.search.hot_words.HotWordsFragment;
import com.tchy.syh.search.result_page.ResultsFragment;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.http.NetworkUtil;


public class SearchFragment extends BaseFragment<SearchFragBinding, SearchVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.search_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public SearchVM initViewModel() {
        return new SearchVM(this.getContext());
    }

    ResultsFragment resultsFragment;
    HotWordsFragment hotWordsFragment;

    FragmentTransaction transaction;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultsFragment = new ResultsFragment();
        resultsFragment.setArguments(getArguments());
        hotWordsFragment = new HotWordsFragment();
        hotWordsFragment.setArguments(getArguments());
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, hotWordsFragment, "hot");
        transaction.commit();
        binding.toolbarSearchview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchAction();
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Messenger.getDefault().unregister("search_action");
    }

    public void searchAction() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {


            if (!resultsFragment.isVisible()) {


                Bundle bundle = getArguments();
                bundle.putString("search_key", viewModel.searchWords.get());
                resultsFragment.setArguments(bundle);
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, resultsFragment);
                transaction.commit();
            }

            Messenger.getDefault().send(viewModel.searchWords.get(), "search_action");
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.searchWords.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                searchAction();
            }
        });
    }

}

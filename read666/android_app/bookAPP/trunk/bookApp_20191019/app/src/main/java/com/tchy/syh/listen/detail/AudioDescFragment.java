package com.tchy.syh.listen.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.FragmentAudioDescBinding;
import com.tchy.syh.listen.entity.UpdateAudioDescEvent;


import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.RxBus;

public class AudioDescFragment extends BaseFragment<FragmentAudioDescBinding,AudioDescViewModel> {
    static String TITLE ="TITLLE";
    String mTitle="";
    private DescAdapter adapter;


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_audio_desc;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AudioDescViewModel initViewModel() {
        return new AudioDescViewModel(mTitle);
    }
    public static AudioDescFragment newInstance(String title) {
        AudioDescFragment tabFragment = new AudioDescFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.descRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DescAdapter();
        binding.descRecycler.setAdapter(adapter);
        RxBus.getDefault().toObservable(UpdateAudioDescEvent.class).subscribe(updateAudioDescEvent -> {
            mTitle=updateAudioDescEvent.desc;
            adapter.notifyDataSetChanged();
        });

    }


    @Override
    public void initParam() {
        super.initParam();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = bundle.getString(TITLE);
        }
    }
    private class DescAdapter extends RecyclerView.Adapter<DescAdapter.VH>{


        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_audio_desc, parent, false);
            return new VH(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {

            holder.webView.loadData(mTitle,"text/html; charset=UTF-8",null);
        }

        @Override
        public int getItemCount() {
            return 1;
        }
        class VH extends RecyclerView.ViewHolder{
            public WebView webView;
            public VH(View itemView) {
                super(itemView);
                webView=itemView.findViewById(R.id.webView);
                WebSettings settings = webView.getSettings();
               

            }
        }
    }
}

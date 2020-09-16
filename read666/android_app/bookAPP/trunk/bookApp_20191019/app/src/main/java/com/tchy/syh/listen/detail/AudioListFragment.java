package com.tchy.syh.listen.detail;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.FragmentAudioListBinding;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ListenEntity;

import me.goldze.mvvmhabit.base.BaseFragment;

public class AudioListFragment extends BaseFragment<FragmentAudioListBinding, AudioListViewModel> {



    private static final String ARGUMENT2 = "ARGUMENT2";
    private ListenEntity.ListenItemEntity mArgument;
    AudioDetailEntity audioDetailEntity;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_audio_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AudioListViewModel initViewModel() {
        return new AudioListViewModel(getActivity(), mArgument,audioDetailEntity);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.updateControlText();
    }

    @Override
    public void initParam() {
        super.initParam();
        Bundle bundle = getArguments();
        if (bundle != null) {

            audioDetailEntity = bundle.getParcelable(ARGUMENT2);
        }
    }


    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.rotateloading.start();
        viewModel.loadSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.loadSuccess.get() ){
                    binding.rotateloading.stop();
                }
                binding.stateView.setVisibility(viewModel.loadSuccess.get() ? View.GONE : View.VISIBLE);


            }
        });
        viewModel.mControlText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel != null && viewModel.mControlText != null) {

                    String text = viewModel.mControlText.get();
                    binding.itbControlText.setButtonText(text);
                    if ("暂停播放".equals(text)){
                        binding.itbControlText.setIcon(R.mipmap.pause_2);
                    }else {
                        binding.itbControlText.setIcon(R.mipmap.play_2);
                    }

                } else {
                    Log.d("can not update ui", "onPropertyChanged: ");
                }
            }
        });
        viewModel.updateItemStyle.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        viewModel.isReverse.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (viewModel.isReverse.get()){
                    binding.itbOrder.setButtonText("倒序");
                    binding.itbOrder.setIcon(R.mipmap.desc);
                }else {
                    binding.itbOrder.setButtonText("正序");
                    binding.itbOrder.setIcon(R.mipmap.asc);
                }
            }
        });

    }

    public static AudioListFragment newInstance(AudioDetailEntity detailEntity) {
        Bundle bundle = new Bundle();

        bundle.putParcelable(ARGUMENT2,detailEntity);
        AudioListFragment contentFragment = new AudioListFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }
}



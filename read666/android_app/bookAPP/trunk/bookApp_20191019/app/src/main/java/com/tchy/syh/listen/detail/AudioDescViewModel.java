package com.tchy.syh.listen.detail;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;


import com.tchy.syh.BR;
import com.tchy.syh.R;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class AudioDescViewModel extends BaseViewModel {

    private final String desc;
    public ObservableList<AudioDescItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemView
    public ItemBinding<AudioDescItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_audio_desc);

    public AudioDescViewModel(String mTitle) {
        this.desc=mTitle;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        observableList.add(new AudioDescItemViewModel(desc));
    }
}

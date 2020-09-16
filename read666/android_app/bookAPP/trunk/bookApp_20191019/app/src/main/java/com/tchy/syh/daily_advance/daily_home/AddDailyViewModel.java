package com.tchy.syh.daily_advance.daily_home;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;


import com.lzy.ninegrid.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;

public class AddDailyViewModel extends BaseViewModel {
    //
    public ObservableBoolean isInitFinish=new ObservableBoolean(false);
    final String IMG_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549879555024&di=6c201a99f1633ebf2f688e6c9" +
            "c7972d2&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ffront%2F214%2Fw580h434%2F20180920%2F-cZ4-hhuhism4329679.jpg";

    List<ImageInfo> imageInfo = new ArrayList<>();
    public  ObservableField<String> imageURL = new ObservableField<>();
    public AddDailyViewModel(Context context) {
            super(context);
        imageURL.set(IMG_URL);

    }
//    public BindingCommand addClick = new BindingCommand(new BindingAction() {
//        @Override
//        public void call() {
//            getImageInfo();
//        }
//    });
//    public BindingCommand minsClick = new BindingCommand(new BindingAction() {
//        @Override
//        public void call() {
//            if (imageInfo.size()>0){
//                imageInfo.remove(imageInfo.size()-1);
//            }
//        }
//    });
//    public void getImageInfo() {
//
//        ImageInfo info = new ImageInfo();
//        info.setThumbnailUrl(IMG_URL);
//        info.setBigImageUrl(IMG_URL);
//        imageInfo.add(info);
//        isInitFinish.set(true);
//    }
}

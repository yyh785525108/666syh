package com.tchy.syh.listen.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.base.ContainerActivity;

public class AdaptationViewModel extends BaseViewModel {

    public AdaptationViewModel(Context context) {
        super(context);
    }

    /**
     * 跳转容器页面
     *
     * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
     */
    public void startAdaptationContainerActivity(String canonicalName,String orientation) {
        Intent intent = new Intent(context, AdaptationContainerActivity.class);
        intent.putExtra(AdaptationContainerActivity.FRAGMENT, canonicalName);
        intent.putExtra("orientation",orientation);
        context.startActivity(intent);
    }

    public void startAdaptationContainerActivity(String canonicalName, Bundle bundle,String orientation) {
        Intent intent = new Intent(context, AdaptationContainerActivity.class);
        intent.putExtra(ContainerActivity.FRAGMENT, canonicalName);
        intent.putExtra("orientation",orientation);
        if (bundle != null) {
            intent.putExtra(AdaptationContainerActivity.BUNDLE, bundle);
        }
        context.startActivity(intent);
    }
}

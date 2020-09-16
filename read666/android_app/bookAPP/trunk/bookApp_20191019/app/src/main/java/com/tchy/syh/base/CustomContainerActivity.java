package com.tchy.syh.base;

import android.os.Bundle;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.base.ContainerActivity;

public class CustomContainerActivity extends ContainerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccentThemeLight));
    }
}

package com.tchy.syh.listen.base;

import android.os.Bundle;
import android.util.Log;



import me.goldze.mvvmhabit.base.ContainerActivity;

public class AdaptationContainerActivity extends ContainerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String orientation = getIntent().getStringExtra("orientation");
        Log.d("适配方向", "onCreate: "+orientation);
        Density.setOrientation(this,orientation);
        super.onCreate(savedInstanceState);
    }
}

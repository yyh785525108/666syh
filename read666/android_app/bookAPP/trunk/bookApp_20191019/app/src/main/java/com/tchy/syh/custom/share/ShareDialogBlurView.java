package com.tchy.syh.custom.share;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ShareDialogBinding;

public class ShareDialogBlurView extends LinearLayout {

    private LayoutInflater mInflater;
    private String id;
    public ShareDialogBlurView(Context context) {
        super(context);
        init();
    }



    ShareDialogBinding binding;

    public ShareDialogBlurView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){

        mInflater = LayoutInflater.from(getContext());
        binding= DataBindingUtil.inflate(mInflater, R.layout.share_dialog,this,true);
        this.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        this.setVisibility(GONE);
        this.setElevation(99);
        binding.setVariable(BR.vm, new ShareVM(getContext()));

        binding.close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              setVisibility(GONE);
            }
        });



    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if( binding.rotateloading!=null&&binding.rotateloading.isStart()){
            binding.rotateloading.stop();
        }

        binding.getVm().onDestroy();
//        binding.setVm(null);
    }

    @Override
    public void setVisibility(int visibility) {

        if(visibility==VISIBLE){
            binding.rotateloading.start();
            binding.getVm().getImage();
//            binding.blurLayout.startBlur();
            binding.blurLayout.setVisibility(VISIBLE);
        }else{
            binding.rotateloading.stop();
            binding.blurLayout.setVisibility(GONE);
//            binding.blurLayout.pauseBlur();
        }
        super.setVisibility(visibility);
    }
}

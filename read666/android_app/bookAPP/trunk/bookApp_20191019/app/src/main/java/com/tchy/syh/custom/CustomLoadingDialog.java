package com.tchy.syh.custom;

import android.content.Context;
import android.view.View;

import com.tchy.syh.R;
import com.flyco.dialog.widget.base.BaseDialog;
import com.victor.loading.rotate.RotateLoading;

public class CustomLoadingDialog extends BaseDialog<CustomLoadingDialog> {
    private RotateLoading loading;
    private  Context context;

    public CustomLoadingDialog(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public View onCreateView() {
//        showAnim(new Swing());
//
//         dismissAnim(new ZoomOutExit());
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);

        View inflate = View.inflate(context, R.layout.custom_loading, null);
        loading=inflate.findViewById(R.id.rotateloading);
        this.setOnDismissListener(v->{
            loading.stop();
        });
        this.setOnShowListener(v->{
            loading.start();

        });
        return inflate;
    }

    @Override
    public void setUiBeforShow() {

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        context=null;
    }
}
package com.tchy.syh.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tchy.syh.R;
import com.tchy.syh.custom.CustomCheckedBtn;

import java.util.ArrayList;
import java.util.List;

public class LoadingDialog extends DialogFragment {
    List<CustomCheckedBtn> checkBoxList=new ArrayList<>();
    double currMoney =366.00;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();

        View view = inflater.inflate(R.layout.loading_dialog, ((ViewGroup) window.findViewById(android.R.id.content)), false);

        
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        params.windowAnimations = R.style.bottomSheet_animation;

        getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);


//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

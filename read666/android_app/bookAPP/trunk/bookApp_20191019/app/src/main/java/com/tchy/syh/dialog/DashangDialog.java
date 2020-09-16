package com.tchy.syh.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.tchy.syh.R;
import com.tchy.syh.custom.CustomCheckedBtn;
import com.tchy.syh.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;

public class DashangDialog extends DialogFragment{
    List<CustomCheckedBtn> checkBoxList=new ArrayList<>();
    float currMoney=0 ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();

        View view = inflater.inflate(R.layout.dashang_dialog, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        final EditText other=view.findViewById(R.id.other);

        other.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp=s.toString();
                if(temp.contains(".")){
                    if(temp.length()>temp.indexOf(".")+3){
                        s.delete(s.length()-1,s.length() );
                    }
                }
            }
        });
        final ImageView close=view.findViewById(R.id.close);
        final Button submit=view.findViewById(R.id.button);
        close.setOnClickListener(v->{
            this.dismiss();
        });
        submit.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(other.getText())){
                float temp=Float.valueOf(other.getText().toString());
                if(temp>0){
                    currMoney=Float.valueOf(other.getText().toString());
                }

            }
            if(currMoney>0){
                RxBus.getDefault().post(new DashangBean(currMoney));
                this.dismiss();
            }else{

                ToastUtil.toastBottom("请输入金额");
            }


            //获得目标Fragment,并将数据通过onActivityResult放入到intent中进行传值

        });
        
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        params.windowAnimations = R.style.bottomSheet_animation;

        getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);
        ViewGroup viewGroup=view.findViewById(R.id.root);

//        other.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    for (int i = 0; i <viewGroup.getChildCount() ; i++) {
//                        if(viewGroup.getChildAt(i) instanceof CustomCheckedBtn){
//                         ((CustomCheckedBtn) viewGroup.getChildAt(i)).setChecked(false);
//                        }
//                    }
//            }
//        });
        if(viewGroup.getChildCount()>0){
            for (int i = 0; i <viewGroup.getChildCount() ; i++) {
                if(viewGroup.getChildAt(i) instanceof CustomCheckedBtn){
                    checkBoxList.add((CustomCheckedBtn)viewGroup.getChildAt(i));
                    ((CustomCheckedBtn) viewGroup.getChildAt(i)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                               currMoney=Float.valueOf(buttonView.getTag().toString());
//                                other.setText(currMoney+"" );
                                for( CustomCheckedBtn btn:checkBoxList){
                                    if(btn!=buttonView){
                                        btn.setChecked(false);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }

//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        return view;
    }
    public class DashangBean{
        public float money=0;
        public DashangBean(float money){
            this.money=money;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

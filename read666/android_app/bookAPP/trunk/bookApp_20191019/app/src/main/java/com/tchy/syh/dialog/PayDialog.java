package com.tchy.syh.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tchy.syh.R;
import com.tchy.syh.custom.CustomCheckedBtn;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.bus.RxBus;

public class PayDialog extends DialogFragment{
    List<CustomCheckedBtn> checkBoxList=new ArrayList<>();
    double currMoney =366.00;
    DecimalFormat format=new DecimalFormat("0.00");
    int type =0;//微信
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();

        View view = inflater.inflate(R.layout.pay_dialog, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        final ImageView close=view.findViewById(R.id.close);
        final TextView price=view.findViewById(R.id.textView33);

        final ImageView closeSelect=view.findViewById(R.id.close_select);
        final Button submit=view.findViewById(R.id.button);
        final LinearLayout ll=view.findViewById(R.id.linearLayout2);
        final TextView other=view.findViewById(R.id.other);
        final RadioButton rb=view.findViewById(R.id.alipay);
        rb.setOnCheckedChangeListener(listener);
        final RadioButton rb2=view.findViewById(R.id.wechatPay);
        rb2.setOnCheckedChangeListener(listener);
        RadioGroup group=view.findViewById(R.id.group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.alipay:
                        type=1;
                        other.setText("支付宝支付");
                        other.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.alipay), null, other.getCompoundDrawables()[2],null );
                        break;
                    case R.id.wechatPay:
                        type=0;
                        other.setText("微信支付");
                        other.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.wechatpay), null, other.getCompoundDrawables()[2],null );

                        break;
                }
            }
        });

        final ConstraintLayout selectll=view.findViewById(R.id.root_select);
        final ConstraintLayout root=view.findViewById(R.id.root);
        other.setOnClickListener(v->{
            selectll.setVisibility(View.VISIBLE);
            root.setVisibility(View.INVISIBLE);
        });
        closeSelect.setOnClickListener(v->{
            selectll.setVisibility(View.INVISIBLE);
            root.setVisibility(View.VISIBLE);
        });
        close.setOnClickListener(v->{
            this.dismiss();
        });



        currMoney=getArguments().getDouble("price",currMoney);
        price.setText("¥ "+format.format(currMoney));

        submit.setOnClickListener(v -> {
            RxBus.getDefault().post(new PayBean(currMoney,type));
            this.dismiss();
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


//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        return view;
    }


    public class PayBean{
        public double money=0;
        public int type=0;
        public PayBean(double money,int type){
            this.money=money;
            this.type=type;

        }
    }
    public CompoundButton.OnCheckedChangeListener listener=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                buttonView.setCompoundDrawablesWithIntrinsicBounds(buttonView.getCompoundDrawables()[0], null,getResources().getDrawable(R.drawable.radio_checked ) ,null );
            else
                buttonView.setCompoundDrawablesWithIntrinsicBounds(buttonView.getCompoundDrawables()[0], null,getResources().getDrawable(R.drawable.radio_default ) ,null );
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

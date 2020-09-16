package com.yyh.read666.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yyh.read666.R;


/**
 * 取消或者确认类型的Dialog
 * <p>
 * 作者： 周旭 on 2017/5/27/0027.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class HongbaoDialog extends Dialog implements View.OnClickListener {
    private OnDialogButtonClickListener buttonClickListner;
    private Button btn_1,btn_2,btn_3,btn_4,btn_5;
    private float money;

    public HongbaoDialog(Context context, OnDialogButtonClickListener listener) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        //指定布局
        setContentView(R.layout.dialog_tips);
        //点击外部不可消失
        setCancelable(true);
        this.buttonClickListner = listener;
        btn_1=findViewById(R.id.btn_1);
        btn_2=findViewById(R.id.btn_2);
        btn_3=findViewById(R.id.btn_3);
        btn_4=findViewById(R.id.btn_4);
        btn_5=findViewById(R.id.btn_5);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);





        findViewById(R.id.zhifuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
//                ok();
                if ((int) money!=0){
                    listener.okButtonClick(money);
                    cancel();
                }else {
                    Toast.makeText(context,"请先选择打赏金额",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //确认
    public void ok() {
    }

    @Override
    public void onClick(View v) {
        if (v==btn_1){
            money=2.0f;
            btn_1.setSelected(true);
            btn_2.setSelected(false);
            btn_3.setSelected(false);
            btn_4.setSelected(false);
            btn_5.setSelected(false);

        }else if (v==btn_2){
            money=5.0f;
            btn_1.setSelected(false);
            btn_2.setSelected(true);
            btn_3.setSelected(false);
            btn_4.setSelected(false);
            btn_5.setSelected(false);
        }else if (v==btn_3){
            money=20.0f;
            btn_1.setSelected(false);
            btn_2.setSelected(false);
            btn_3.setSelected(true);
            btn_4.setSelected(false);
            btn_5.setSelected(false);
        }else if (v==btn_4){
            money=50.0f;
            btn_1.setSelected(false);
            btn_2.setSelected(false);
            btn_3.setSelected(false);
            btn_4.setSelected(true);
            btn_5.setSelected(false);
        }else if (v==btn_5){
            money=100.0f;
            btn_1.setSelected(false);
            btn_2.setSelected(false);
            btn_3.setSelected(false);
            btn_4.setSelected(false);
            btn_5.setSelected(true);
        }
    }

    //实现回调功能
    public interface OnDialogButtonClickListener {
        public void okButtonClick(float money);

    }

}

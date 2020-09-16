package com.yyh.read666.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yyh.read666.R;

import org.json.JSONArray;


/**
 * 取消或者确认类型的Dialog
 * <p>
 * 作者： 周旭 on 2017/5/27/0027.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class YingpingListDialog extends Dialog implements View.OnClickListener {
    private OnDialogButtonClickListener buttonClickListner;
    private JSONArray list;

    public YingpingListDialog(Context context, JSONArray list,OnDialogButtonClickListener listener) {
        //使用自定义Dialog样式
        super(context, R.style.custom_dialog);
        //指定布局
        setContentView(R.layout.dialog_yinping_list);
        //点击外部不可消失
        setCancelable(true);
        this.buttonClickListner = listener;




//
//        findViewById(R.id.zhifuBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View paramView) {
////                ok();
//                if ((int) money!=0){
//                    listener.okButtonClick(money);
//                    cancel();
//                }else {
//                    Toast.makeText(context,"请先选择打赏金额",Toast.LENGTH_SHORT).show();
//                }

//            }
//        });
    }

    //确认
    public void ok() {
    }

    @Override
    public void onClick(View v) {

    }

    //实现回调功能
    public interface OnDialogButtonClickListener {
        public void okButtonClick(float money);

    }

}

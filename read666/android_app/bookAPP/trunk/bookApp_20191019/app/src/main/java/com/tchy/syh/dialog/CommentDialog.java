package com.tchy.syh.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tchy.syh.R;
import com.tchy.syh.utils.ToastUtil;

import me.goldze.mvvmhabit.bus.RxBus;

public class CommentDialog extends DialogFragment {
//    List<CustomCheckedBtn> checkBoxList=new ArrayList<>();
    String comment="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getDialog().getWindow();

        View view = inflater.inflate(R.layout.comment_dialog, ((ViewGroup) window.findViewById(android.R.id.content)), false);
        final EditText other=view.findViewById(R.id.other);
        final ImageView close=view.findViewById(R.id.close);
        final Button submit=view.findViewById(R.id.button);
        InputFilter[] filters = {new InputFilter.LengthFilter(255)};
        other.setFilters(filters);
        close.setOnClickListener(v->{
            this.dismiss();
        });
        submit.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(other.getText())){
                comment=other.getText().toString();
                if(getArguments().getString("type").equals("book")){
                    RxBus.getDefault().post(new CommentBookBean(comment));
                }
                if(getArguments().getString("type").equals("audio")){
                    RxBus.getDefault().post(new CommentAudioBean(comment));
                }
                if(getArguments().getString("type").equals("read")){
                    RxBus.getDefault().post(new CommentReadBean(comment));
                }
                if(getArguments().getString("type").equals("reply")){
                    RxBus.getDefault().post(new CommentReplyBean(comment));
                }
                this.dismiss();
            }else{
                ToastUtil.toastBottom("请输入评论内容");
            }



            //获得目标Fragment,并将数据通过onActivityResult放入到intent中进行传值

        });
        
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        window.setLayout(-1, -1);//这2行,和上面的一样,注意顺序就行;
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.windowAnimations = R.style.bottomSheet_animation;

        getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);


//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        return view;
    }
    public class CommentBookBean {
        public String comment="";
        public CommentBookBean(String comment){
            this.comment=comment;
        }
    }
    public class CommentAudioBean {
        public String comment="";
        public CommentAudioBean(String comment){
            this.comment=comment;
        }
    }
    public class CommentReadBean {
        public String comment="";
        public CommentReadBean(String comment){
            this.comment=comment;
        }
    }
    public class CommentReplyBean {
        public String comment="";
        public CommentReplyBean(String comment){
            this.comment=comment;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

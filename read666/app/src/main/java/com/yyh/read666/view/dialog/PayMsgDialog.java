package com.yyh.read666.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;

import java.util.regex.Pattern;

public class PayMsgDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText phoneEt;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private Button confirmBtn;
    private ImageView headImg;
    private TextView priceTv;

    private int flag;




    public interface OnTextSendListener {

        void onTextSend(String msg);
    }


    private OnTextSendListener mOnTextSendListener;


    public PayMsgDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        init();
        setLayout();
    }



    /**
     * 设置输入提示文字
     */
    public void setHint(String text) {
        phoneEt.setHint(text);
    }
    public void setText(String text) {
        phoneEt.setText(text);
    }

    public void setImgAndPrice(String img, String price){
        Glide.with(mContext).load(img).into(headImg);
        priceTv.setText("价格:￥"+price+"元");
    }

    private void init() {
        setContentView(R.layout.dialog_pay);
        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        confirmBtn=findViewById(R.id.confirmBtn);
        headImg=findViewById(R.id.headImg);
        priceTv=findViewById(R.id.priceTv);



        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        phoneEt=findViewById(R.id.phoneEt);


        phoneEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {

                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String msg=phoneEt.getText().toString();

                if (!TextUtils.isEmpty(msg)) {
                    boolean isPhone=isPhoneNumber(msg);
                    if (isPhone){
                        mOnTextSendListener.onTextSend(msg);
                        imm.showSoftInput(phoneEt, InputMethodManager.SHOW_FORCED);
                        imm.hideSoftInputFromWindow(phoneEt.getWindowToken(), 0);
                        phoneEt.setText("");
                        dismiss();
                    }else {
                        Toast.makeText(mContext, "请输入正确手机号码", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_LONG).show();
                }

            }
        });



        rlDlg = findViewById(R.id.rl_outside_view);
        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view)
                    dismiss();
            }
        });

        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    dismiss();
                }
                mLastDiff = heightDifference;
            }
        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(phoneEt.getWindowToken(), 0);
                dismiss();
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0)
                    dismiss();
                return false;
            }
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }

    @Override
    public void show() {
        super.show();
    }

    public  boolean isPhoneNumber(String input) {// 判断手机号码是否规则
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        Pattern p = Pattern.compile(regex);
        return p.matches(regex, input);//如果不是号码，则返回false，是号码则返回true

    }

}
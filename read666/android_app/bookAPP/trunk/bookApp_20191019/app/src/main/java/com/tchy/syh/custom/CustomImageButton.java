package com.tchy.syh.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tchy.syh.R;

@SuppressLint("RestrictedApi")
public class CustomImageButton extends LinearLayout {


    private LayoutInflater mInflater;

    private ImageView icon;
    private TextView text;
    private boolean isSelected;


    public CustomImageButton(Context context) {
        this(context, null);
    }

    public CustomImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        initView();
        //设置toolbar的边距

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CustomImageButton, defStyleAttr, 0);


            final Drawable icon = a.getDrawable(R.styleable.CustomImageButton_btnIcon);
            //一定要在这里进行条件判断
            if (icon != null) {
                Drawable drawableUp= DrawableCompat.wrap(icon);
                DrawableCompat.setTint(drawableUp, ContextCompat.getColor(context,R.color.btnTextColorThemeLight));
                this.icon.setImageDrawable(drawableUp);

            }

            CharSequence label = a.getText(R.styleable.CustomImageButton_btnText);
            if (label != null) {
                this.text.setTextColor(getContext().getResources().getColor(R.color.btnTextColorThemeLight));
                this.text.setText(label);
            }

            a.recycle();
        }

    }
    public void setSelected(boolean flag){
        this.isSelected=flag;
        Drawable drawableUp= DrawableCompat.wrap(this.icon.getDrawable());

        if(flag){
            DrawableCompat.setTint(drawableUp, ContextCompat.getColor(this.getContext(),R.color.colorAccentThemeLight));
            this.icon.setImageDrawable(drawableUp);
            this.text.setTextColor(ContextCompat.getColor(this.getContext(),R.color.colorAccentThemeLight));
        }else{
            DrawableCompat.setTint(drawableUp, ContextCompat.getColor(this.getContext(),R.color.btnTextColorThemeLight));
            this.icon.setImageDrawable(drawableUp);
            this.text.setTextColor(ContextCompat.getColor(this.getContext(),R.color.btnTextColorThemeLight));
        }
    }

    private void initView() {



            text=new TextView(this.getContext());
            icon=new ImageView(this.getContext());
            int size=(int)getResources().getDimension(R.dimen.bottom_icon_size);
            icon.setLayoutParams(new ViewGroup.LayoutParams(size,size));
            text.setGravity(Gravity.CENTER);
            text.setPadding(0,10,0,0);
            this.setOrientation(LinearLayout.VERTICAL);
            this.setGravity(Gravity.CENTER);
            this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            this.addView(icon);
            this.addView(text);
//            mInflater = LayoutInflater.from(getContext());
//            mInflater.inflate(R.layout.custom_tool_bar, this,true);
//            text = (TextView) findViewById(R.id.icon);
//            icon=(ImageView)findViewById(R.id.label);
//            //把Toolbar里面的控件组合起来
//            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.toolbar_height), Gravity.CENTER_HORIZONTAL);


    }




}

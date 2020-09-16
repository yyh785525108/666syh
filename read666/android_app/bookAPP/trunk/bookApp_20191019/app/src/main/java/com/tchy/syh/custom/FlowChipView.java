package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class FlowChipView extends AppCompatTextView{
    public int position =-1;
    public int rtvRadius=5;
    public FlowChipView(Context context,int i) {
        super(context);
        this.position=i;
        init();
    }

    public FlowChipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(){
        int padding =ConvertUtils.dp2px(8);
        this.setPadding(padding, padding, padding, padding);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        GradientDrawable gd = new GradientDrawable();
        if(position>=0&&position<3){
            setTextColor(getContext().getResources().getColor(R.color.colorPrimaryThemeLight ));
            gd.setColor( getContext().getResources().getColor(R.color.colorAccentLightThemeLight ));
         }else{
            setTextColor(getContext().getResources().getColor(R.color.textColorDarkThemeLight ));
            gd.setColor( getContext().getResources().getColor(R.color.dividerColorThemeLight ));
        }

        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(ConvertUtils.dp2px(rtvRadius));


        this.setBackground(gd);
    }


}

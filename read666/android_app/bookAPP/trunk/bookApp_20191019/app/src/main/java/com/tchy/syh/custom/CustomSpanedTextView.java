package com.tchy.syh.custom;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import com.tchy.syh.R;

public class CustomSpanedTextView extends AppCompatTextView {
    public CustomSpanedTextView(Context context) {
        super(context);
    }

    public CustomSpanedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }
    public void setText(String text){

        String[] tags = text.split(":");
        for(int i =0;i<tags.length;i++){
            tags[i]=" "+tags[i]+" ";
        }

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(tags[0]);
        CustomRoundCornerBgSpan spanHour= new CustomRoundCornerBgSpan(ContextCompat.getColor(getContext(),R.color.colorAccentThemeLight), ContextCompat.getColor(getContext(),R.color.white));
        stringBuilder.setSpan(spanHour, stringBuilder.length() - tags[0].length(), stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(" : ");

        stringBuilder.append(tags[1]);
        CustomRoundCornerBgSpan spanMin= new CustomRoundCornerBgSpan(ContextCompat.getColor(getContext(),R.color.black), ContextCompat.getColor(getContext(),R.color.white));
        stringBuilder.setSpan(spanMin, stringBuilder.length() - tags[1].length(), stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(" : ");

        stringBuilder.append(tags[2]);
        CustomRoundCornerBgSpan spanSec= new CustomRoundCornerBgSpan(ContextCompat.getColor(getContext(),R.color.black), ContextCompat.getColor(getContext(),R.color.white));
        stringBuilder.setSpan(spanSec, stringBuilder.length() - tags[2].length(), stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.setText(stringBuilder);

    }

}

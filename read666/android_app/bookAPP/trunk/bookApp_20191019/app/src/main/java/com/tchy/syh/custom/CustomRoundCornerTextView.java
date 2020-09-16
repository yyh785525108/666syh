package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomRoundCornerTextView extends AppCompatTextView {

    public CustomRoundCornerTextView(Context context) {
        super(context);

    }

    public CustomRoundCornerTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void setBackgroundColor(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
//        gd.setCornerRadius(ConvertUtils.dp2px(8));
        super.setBackground(gd);
    }

    Drawable d;
    int bgColor=0xFFFFFFFF;
    int drawType=0;
    public void setDrawable(Drawable d){
        this.d=d;
        drawType=0;
        invalidate();
    }
    public void setDrawable(int  color){
       bgColor=color;
        drawType=1;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=ConvertUtils.dp2px(75);
        int height=ConvertUtils.dp2px(75);
        setMeasuredDimension(width,height );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(d==null&&drawType==0){
            return;
        }

        int width=ConvertUtils.dp2px(75);
        int height=ConvertUtils.dp2px(75);
        Bitmap bitmap;

        if(drawType==0){
            bitmap=((BitmapDrawable)d).getBitmap();
        }else{
            bitmap = Bitmap.createBitmap(width,height,
                    Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(getResources().getColor(R.color.colorAccentThemeLight ));

        }
        Rect rectSrc=new Rect(0,0, bitmap.getWidth(), bitmap.getHeight());
        Rect rectDst=new Rect(0,0,width,height);

        Paint paint=new Paint();
        final float roundPx = ConvertUtils.dp2px(5);
        paint.setAntiAlias(true);
        paint.setColor(bgColor);
        int canvasWidth = width;
        int canvasHeight = height;
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(new RectF(rectDst), roundPx, roundPx , paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectSrc, rectDst, paint);
//        canvas.drawBitmap(bitmapColor, rectSrc, rectDst, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);
        Paint textPaint=new Paint();
        textPaint.setTextSize(ConvertUtils.sp2px(15));
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics metrics=textPaint.getFontMetrics();
        float fontHeight=metrics.descent-metrics.ascent;

        canvas.drawText(getText().toString(), width/2, (height+fontHeight)/2, textPaint);


    }
}

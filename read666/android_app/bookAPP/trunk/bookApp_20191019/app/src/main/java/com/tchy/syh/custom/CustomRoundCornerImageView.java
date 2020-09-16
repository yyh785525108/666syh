package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomRoundCornerImageView extends AppCompatTextView {

    public CustomRoundCornerImageView(Context context) {
        super(context);

    }

    public CustomRoundCornerImageView(Context paramContext, AttributeSet paramAttributeSet) {
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
    public void setDrawable(Drawable d){
        this.d=d;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(d==null){
            return;
        }

        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        Bitmap bitmap;

        bitmap=((BitmapDrawable)d).getBitmap();
        Rect rectSrc=new Rect(0,0, bitmap.getWidth(), bitmap.getHeight());
        Rect rectDst=new Rect(0,0,width,height);

        Paint paint=new Paint();
        final float roundPx = ConvertUtils.dp2px(5);
        paint.setAntiAlias(true);
        paint.setColor(bgColor);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int layerId = canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(new RectF(rectDst), roundPx, roundPx , paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectSrc, rectDst, paint);
//        canvas.drawBitmap(bitmapColor, rectSrc, rectDst, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);


    }
}

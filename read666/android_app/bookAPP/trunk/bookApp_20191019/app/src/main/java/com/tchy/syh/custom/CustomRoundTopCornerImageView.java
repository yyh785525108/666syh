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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.bumptech.glide.load.resource.gif.GifDrawable;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomRoundTopCornerImageView extends AppCompatImageView {

    public CustomRoundTopCornerImageView(Context context) {
        super(context);

    }

    public CustomRoundTopCornerImageView(Context paramContext, AttributeSet paramAttributeSet) {
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
    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        this.d=drawable;
    }
//    private Paint alphaPaint = new Paint();
//    private int currentAlpha = 0;
//    private static final int FADE_MILLISECONDS = 1000; // 3 second fade effect
//    private static final int FADE_STEP = 50;          // 120ms refresh
//    private static final int ALPHA_STEP = 255 / (FADE_MILLISECONDS / FADE_STEP);
    @Override
    protected void onDraw(Canvas canvas) {
        if(d==null){
            return;
        }

        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        Bitmap bitmap;
        if(d instanceof GifDrawable){
            bitmap=  ((GifDrawable)  d).getFirstFrame();
        }else if(d instanceof ColorDrawable){
            bitmap = Bitmap.createBitmap(width,height,
                    Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(((ColorDrawable) d).getColor());
        } else {
            bitmap=((BitmapDrawable)  d).getBitmap();
        }

        if(bitmap==null){
            bitmap = Bitmap.createBitmap(width,height,
                    Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.parseColor("#eeeeee"));
        }
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
        canvas.drawRect(new RectF(0,height/2,width,height), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rectSrc, rectDst, paint);


//        canvas.drawBitmap(bitmapColor, rectSrc, rectDst, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);


    }
}

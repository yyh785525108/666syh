package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import android.util.AttributeSet;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomSignedBg extends AppCompatTextView{
    public CustomSignedBg(@NonNull Context context) {
        super(context);
    }

    public CustomSignedBg(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width=getMeasuredWidth();
        int height=width;
        float count =16f;
        float unitAngle=360f/count;
        float sweepAngle=15f;
        float spaceAngle=unitAngle-sweepAngle;
        int centerImgSize=ConvertUtils.dp2px(30);
        float startAngle=270-sweepAngle/2;
        float textSize=ConvertUtils.sp2px(20);
        final Paint paint = new Paint();

//        Paint testPaint=new Paint();
//        testPaint.setColor(Color.RED);
//        canvas.drawRect(rect, testPaint);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#FEE993"));
        Shader shader=new RadialGradient(width/2,height/2,width*2/5,new int[]{Color.parseColor("#ffFEE993"),Color.parseColor("#00ffffff")},new float[]{0.3f,1f},Shader.TileMode.CLAMP);
        paint.setShader(shader);



        for(int i=0;i<count;i++){
            canvas.drawArc(0, 0, width,height , startAngle, sweepAngle, true, paint);
            startAngle=startAngle+sweepAngle+spaceAngle;
        }
        Bitmap bitmap=((BitmapDrawable)(getResources().getDrawable(R.mipmap.vip))).getBitmap();

        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(new Rect(width/2-centerImgSize, height/2-centerImgSize, width/2+centerImgSize,
                height/2+centerImgSize));
        canvas.drawBitmap(bitmap, rect, rectF,paint);


        Paint textPaint=new Paint();

        textPaint.setTextAlign(Paint.Align.CENTER);


        String str="签到成功";
        textPaint.setTextSize(textSize);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint.setAntiAlias(true);
//        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(Color.parseColor("#FB9E27"));

        Paint stkPaint = new Paint();
        stkPaint.setStyle(Paint.Style.STROKE);
        stkPaint.setStrokeWidth(15);
        stkPaint.setAntiAlias(true);
        stkPaint.setTextSize(textSize);
//        stkPaint.setTypeface(Typeface.DEFAULT_BOLD);
        stkPaint.setTextAlign(Paint.Align.CENTER);
        stkPaint.setColor(Color.WHITE);
        Paint.FontMetricsInt metrics=textPaint.getFontMetricsInt();
        Paint.FontMetricsInt metrics1=stkPaint.getFontMetricsInt();
        canvas.drawText(str, width/2, height/2+centerImgSize-metrics1.bottom-metrics1.top+20, stkPaint);

//        textPaint.setShadowLayer(10, 10, 10, Color.WHITE);

        canvas.drawText(str, width/2, height/2+centerImgSize-metrics.bottom-metrics.top+20, textPaint);

        String str1=getText().toString();


        canvas.drawText(str1, width/2, height/2-centerImgSize-metrics1.bottom-metrics1.top-10, stkPaint);
        canvas.drawText(str1, width/2, height/2-centerImgSize-metrics.bottom-metrics.top-10, textPaint);
    }
}



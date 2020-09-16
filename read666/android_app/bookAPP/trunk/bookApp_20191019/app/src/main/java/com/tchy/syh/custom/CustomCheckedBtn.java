package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.Gravity;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomCheckedBtn extends AppCompatCheckBox {
    public CustomCheckedBtn(Context context) {
        super(context);
    }
    Disposable d;
    public CustomCheckedBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(Color.RED);
        setGravity(Gravity.CENTER);
        setButtonDrawable(null);
//        d=RxBus.getDefault().toObservable(CustomCheckedBean.class).subscribe(v->{
//            if(v.id!=this.getId()){
//                setChecked(false);
//            }
//        });
    }


//    public int getColor(){
//       return isChecked()?Color.GRAY:Color.parseColor("#FC645A");
//    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        RectF rect = new RectF(2f, 2f, width-2, height-2);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(ConvertUtils.dp2px(1f));

        if (isChecked()) {
            paint.setColor(Color.RED);
            Paint rigthTop = new Paint();
            rigthTop.setColor(Color.RED);
            rigthTop.setAntiAlias(true);
            rigthTop.setStrokeJoin(Paint.Join.ROUND);
            rigthTop.setStrokeCap(Paint.Cap.ROUND);
            int threeAngleStartx = width - ConvertUtils.dp2px(30);
            int threeAngleStarty = ConvertUtils.dp2px(20);
            Path mpath = new Path();

            mpath.moveTo(threeAngleStartx, 2f);
            mpath.lineTo(width-2f, threeAngleStarty);

//            mpath.lineTo(width-5f, 5f);
            mpath.quadTo(width+ConvertUtils.dp2px(8),-+ConvertUtils.dp2px(8) ,threeAngleStartx , 2f);
            mpath.close();

            canvas.drawRoundRect(rect, 12, 12, paint);
            canvas.drawPath(mpath, rigthTop);

            Paint textPaint = new Paint();
            textPaint.setTextAlign(Paint.Align.CENTER);
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setAntiAlias(true);
            textPaint.setDither(true);
            textPaint.setStrokeWidth(ConvertUtils.dp2px(1.5f));
            textPaint.setStrokeCap(Paint.Cap.ROUND);
            textPaint.setStrokeJoin(Paint.Join.ROUND);
            Path symbolPath = new Path();
            int symbolPathCenterX = width - ConvertUtils.dp2px(9);
            int symbolPathCenterY = ConvertUtils.dp2px(9);
            symbolPath.moveTo(symbolPathCenterX - ConvertUtils.dp2px(3.5f), ConvertUtils.dp2px(6.5f));
            symbolPath.lineTo(symbolPathCenterX, symbolPathCenterY);
            symbolPath.lineTo(symbolPathCenterX + ConvertUtils.dp2px(5.5f), ConvertUtils.dp2px(3.5f));
            canvas.drawPath(symbolPath, textPaint);




        }else{
            paint.setColor(Color.parseColor("#DEDEDE"));
            canvas.drawRoundRect(rect, 12, 12, paint);
        }

    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
//        if(checked){
//            RxBus.getDefault().post(new CustomCheckedBean(this.getId(),true));
//        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
             if (d != null) {                                     d.dispose();                                 }
    }

    public class CustomCheckedBean{
        public int id =0;
        public boolean isChecked=false;
        public CustomCheckedBean(int id,boolean isChecked){
            this.isChecked=isChecked;
            this.id=id;
        }
    }

}

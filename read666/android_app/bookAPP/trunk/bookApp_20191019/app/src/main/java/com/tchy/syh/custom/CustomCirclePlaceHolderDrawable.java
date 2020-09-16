package com.tchy.syh.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;

public class CustomCirclePlaceHolderDrawable extends ColorDrawable {

    @Override
    public void draw(Canvas canvas) {
//        super.draw(canvas);
        int width=getIntrinsicWidth();
        int height=getIntrinsicHeight();
        Paint paint =new Paint();
        paint.setColor(Color.parseColor("#eeeeee"));
        paint.setAntiAlias(true);

        canvas.drawCircle(width/2, height/2, height/2, paint);
    }
}

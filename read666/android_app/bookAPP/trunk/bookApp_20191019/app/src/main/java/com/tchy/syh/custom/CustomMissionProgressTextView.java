package com.tchy.syh.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.utils.ConvertUtils;

public class CustomMissionProgressTextView extends AppCompatTextView {
    int textSizeSp = 12;
    int textColor;


    public int position = 5;
    public int currentBgColor;

    public CustomMissionProgressTextView(Context context, int position, boolean checked) {
        super(context);
        this.position = position;
        init(checked);
    }

    public CustomMissionProgressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(true);
    }

    public void init(boolean checked) {

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,ConvertUtils.dp2px(60),1f);
        setLayoutParams(params);

        textColor = getResources().getColor(R.color.colorPrimaryThemeLight);
        if (checked)
            currentBgColor = getResources().getColor(R.color.colorStepProgressChecked);
        else
            currentBgColor = getResources().getColor(R.color.textColorHintThemeLight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        width = getMeasuredWidth();
        height = ConvertUtils.dp2px(35);
        switch (position) {
            case 0:
                drawStart(canvas);
                break;
            case 6:
                drawEnd(canvas);
                break;
            default:
                drawCenter(canvas);
        }
        Paint paintText = getTextPaint();

        Paint.FontMetricsInt fontMetrics = paintText.getFontMetricsInt();
        int baseline = (height - fontMetrics.descent - fontMetrics.ascent) / 2;
        canvas.drawText(getText().toString(), width / 2, baseline, paintText);



        Paint paintBottomLabel=new Paint();
        paintBottomLabel.setTextSize(ConvertUtils.sp2px(13));
        paintBottomLabel.setColor(Color.parseColor("#bbbbbb"));
        paintBottomLabel.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt bottomMetrics = paintBottomLabel.getFontMetricsInt();
        int bottomBaseLine = (height - bottomMetrics.descent - bottomMetrics.ascent) / 2;

        canvas.drawText(position+1+"天", width / 2, height+bottomBaseLine, paintBottomLabel);
        canvas.save();

    }

    private Paint getRingPaint() {
        Paint paintRing = new Paint();
        paintRing.setColor(currentBgColor);
        paintRing.setAntiAlias(true);
        return paintRing;
    }

    private Paint getLinePaint() {
        Paint paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(currentBgColor);
        paintLine.setStrokeWidth(ConvertUtils.dp2px(5));
        return paintLine;

    }
    int width ;
    int height;
    private Paint getTextPaint() {
        Paint paintText = new Paint();
        paintText.setAntiAlias(true);
        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        paintText.setColor(textColor);
        paintText.setTypeface(font);
        paintText.setTextSize(ConvertUtils.sp2px(textSizeSp));
        paintText.setTextAlign(Paint.Align.CENTER);
        return paintText;
    }

    private void drawCircle(Canvas canvas,Paint paint){
        canvas.drawCircle(width / 2, height / 2, height /2, getRingPaint());
    }
    private void drawLeftLine(Canvas canvas,Paint paint){
        canvas.drawLine(0, height / 2, width/2, height / 2, getLinePaint( ));

    }
    private void drawRightLine(Canvas canvas,Paint paint){
        canvas.drawLine(width/2, height / 2, width, height / 2, getLinePaint());

    }
    private void drawStart(Canvas canvas) {
        drawCircle(canvas,getRingPaint());
        drawRightLine(canvas,getLinePaint());

    }

    private void drawEnd(Canvas canvas) {
        drawCircle(canvas,getRingPaint());
        drawLeftLine(canvas,getLinePaint());
    }

    private void drawCenter(Canvas canvas) {
        drawCircle(canvas,getRingPaint());
        drawLeftLine(canvas,getLinePaint());
        drawRightLine(canvas,getLinePaint() );
    }
}

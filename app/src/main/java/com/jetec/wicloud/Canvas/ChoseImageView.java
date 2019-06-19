package com.jetec.wicloud.Canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Screen;

public class ChoseImageView extends View {

    private Paint valuePaint, emptyPaint, pointerPanit, circlePaint, paintText, valueText;
    private Screen screen;
    private static final String TAG = "ChoseImageView";
    private DisplayMetrics dm;
    private Context context;

    public ChoseImageView(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public ChoseImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ChoseImageView(Context context) {
        super(context);

        this.context = context;

        valuePaint = new Paint();
        int valueColor = ContextCompat.getColor(context, R.color.buttonBackground);
        valuePaint.setColor(valueColor);
        valuePaint.setTextAlign(Paint.Align.CENTER);
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setStrokeWidth(getResources().getDimension(R.dimen.canvasImageSize));

        emptyPaint = new Paint();
        int emptyColor = ContextCompat.getColor(context, R.color.loginBackground);
        emptyPaint.setColor(emptyColor);
        emptyPaint.setTextAlign(Paint.Align.CENTER);
        emptyPaint.setStyle(Paint.Style.STROKE);
        emptyPaint.setStrokeWidth(getResources().getDimension(R.dimen.canvasImageSize));

        pointerPanit = new Paint();
        int pointerColor = ContextCompat.getColor(context, R.color.buttonBackground);
        pointerPanit.setColor(pointerColor);
        pointerPanit.setStyle(Paint.Style.FILL);
        pointerPanit.setStrokeWidth(1);

        circlePaint = new Paint();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStrokeWidth(1);

        paintText = new Paint();

        screen = new Screen();
        dm = screen.getScreen(context);

        paintText.setColor(getResources().getColor(R.color.colorAccent));
        paintText.setTextSize(getResources().getDimension(R.dimen.canvasTextSize));
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setStrokeWidth(1);

        valueText = new Paint();
        valueText.setColor(Color.BLACK);
        valueText.setTextSize(getResources().getDimension(R.dimen.canvasValueSize));
        valueText.setTextAlign(Paint.Align.CENTER);
        valueText.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int xPos = (getWidth() / 2);
        int yPos = (getHeight() / 2);
        float px = getResources().getDimension(R.dimen.canvasTextSize);

        float width = dm.widthPixels, height = dm.heightPixels;
        Log.d(TAG, "Screen : xPos : " + xPos + " yPos : " + yPos);
        Log.d(TAG, "Screen : height : " + width + " width : " + height);

        canvas.drawText(context.getString(R.string.imageGauge), xPos, 2 * px, paintText);
        @SuppressLint("DrawAllocation")
        RectF oval1 = new RectF(xPos - yPos, 4 * px,
                xPos + yPos, getHeight() + 4 * px);
        // x:xPos, y:getHeight() - 4 * px, allHeight:getHeight()
        float ovalbottom = getHeight() + 4 * px;
        Log.d(TAG,"left = " + (xPos - yPos) + "right = " + (xPos + yPos));
        Log.d(TAG,"top = " + 4 * px + "bottom = " + ovalbottom);
        canvas.drawArc(oval1, 180, 180, false, emptyPaint);
        canvas.drawArc(oval1, 180, 60, false, valuePaint);
        canvas.drawText(context.getString(R.string.mintext), xPos - yPos, (int)(ovalbottom - yPos + 1.75 * px), valueText);
        canvas.drawText(context.getString(R.string.maxtext), xPos + yPos, (int)(ovalbottom - yPos + 1.75 * px), valueText);
        canvas.drawCircle(xPos, ovalbottom - yPos, px, pointerPanit);
        double x, y, Rx1, Ry1, Rx2, Ry2;
        x = xPos + Math.cos(Math.toRadians((180 + 60))) * (yPos * 2 / 3);   //180:startAngle 60:sweepAngle
        y = ovalbottom - yPos + Math.sin(Math.toRadians((180 + 60))) * (yPos * 2 / 3);   //width:(yPos * 2 / 3)
        Rx1 = xPos + Math.cos(Math.toRadians(((180 + 60) + 90))) * px;
        Rx2 = xPos + Math.cos(Math.toRadians(((180 + 60) - 90))) * px;
        Ry1 = ovalbottom - yPos + Math.sin(Math.toRadians(((180 + 60) + 90))) * px;
        Ry2 = ovalbottom - yPos + Math.sin(Math.toRadians(((180 + 60) - 90))) * px;
        @SuppressLint("DrawAllocation")
        Path path = new Path();
        path.moveTo((int) x, (int) y);
        path.lineTo((int) Rx1, (int) Ry1);
        path.lineTo((int) Rx2, (int) Ry2);
        path.close();
        canvas.drawPath(path, pointerPanit);
        canvas.drawCircle(xPos, ovalbottom - yPos, px / 2, circlePaint);
        Log.d(TAG,"Circle = " + (ovalbottom - yPos));
        canvas.drawText(context.getString(R.string.GaugeDescription), xPos, getHeight() - px, paintText);
    }
}

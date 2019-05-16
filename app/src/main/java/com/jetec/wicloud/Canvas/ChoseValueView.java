package com.jetec.wicloud.Canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import com.jetec.wicloud.R;
import com.jetec.wicloud.Screen;

public class ChoseValueView extends View {

    private static final String defult = "00.00";
    private Paint paint, paintText;
    private static final String TAG = "ChoseValueView";
    private Context context;
    private DisplayMetrics dm;

    public ChoseValueView(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public ChoseValueView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ChoseValueView(Context context) {
        super(context);

        this.context = context;
        paint = new Paint();
        paintText = new Paint();

        paint.setColor(Color.BLACK);
        paint.setTextSize(getResources().getDimension(R.dimen.canvasValueText));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(1);

        paintText.setColor(getResources().getColor(R.color.colorAccent));
        paintText.setTextSize(getResources().getDimension(R.dimen.canvasTextSize));
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setStrokeWidth(1);

        Screen screen = new Screen();
        dm = screen.getScreen(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int xPos = (getWidth() / 2);
        int yPos = (getHeight() / 2);
        float px = getResources().getDimension(R.dimen.canvasTextSize);

        float width = dm.widthPixels, height = dm.heightPixels;
        Log.d(TAG, "Screen : xPos : " + xPos + " yPos : " + yPos);
        Log.d(TAG, "Screen : height : " + width + " width : " + height);

        canvas.drawText(context.getString(R.string.valueText), xPos, 2 * px, paintText);
        canvas.drawText(defult,xPos,yPos + px, paint);
        canvas.drawText(context.getString(R.string.TextDescription), xPos, getHeight() - px, paintText);

        Log.d(TAG, "Screen : dimen : " + px);
    }
}

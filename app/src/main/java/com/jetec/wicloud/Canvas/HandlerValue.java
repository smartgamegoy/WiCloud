package com.jetec.wicloud.Canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.jetec.wicloud.GetUnit;
import com.jetec.wicloud.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;

public class HandlerValue extends View {

    private String TAG = "HandlerValue";
    private int flag;
    private Context context;
    private Paint paint, paintText, valuePaint, emptyPaint, pointerPanit, circlePaint, valueText;
    private String name, model, sensor, max, min;
    private JSONObject jsonObject;
    private double value;
    private GetUnit getUnit = new GetUnit();
    private ImageView imageView;

    public HandlerValue(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public HandlerValue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HandlerValue(Context context) {
        super(context);
    }

    public void set_context(Context context) {

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

        valueText = new Paint();
        valueText.setColor(Color.BLACK);
        valueText.setTextSize(getResources().getDimension(R.dimen.canvasValueSize));
        valueText.setTextAlign(Paint.Align.CENTER);
        valueText.setStrokeWidth(1);
    }

    public void setValue(JSONArray jsonArray, JSONObject jsonObject, ImageView imageView) {
        this.imageView = imageView;
        try {
            this.jsonObject = jsonObject;
            if (jsonArray.get(0).toString().matches("Value")) {
                flag = 0;
                name = jsonArray.get(1).toString();
                model = jsonArray.get(2).toString();
                sensor = jsonArray.get(3).toString();
            } else if (jsonArray.get(0).toString().matches("Image")) {
                flag = 1;
                name = jsonArray.get(1).toString();
                model = jsonArray.get(2).toString();
                sensor = jsonArray.get(3).toString();
                max = jsonArray.get(4).toString();
                min = jsonArray.get(5).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        try {
            if (jsonObject != null) {
                if (jsonObject.get("originDeviceId").toString().matches(model)) {
                    if (flag == 0) {
                        if (sensor.contains(context.getString(R.string.sensor_temperature))) {
                            imageView.setBackgroundResource(R.drawable.temperature);
                            sensor = "temperature" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_humidity))) {
                            imageView.setBackgroundResource(R.drawable.humidity);
                            sensor = "humidity" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_wind_speed))) {
                            imageView.setBackgroundResource(R.drawable.windspeed);
                            sensor = "wind_speed" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_wind_direction))) {
                            imageView.setBackgroundResource(R.drawable.winddirection);
                            sensor = "wind_direction" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_precipitation))) {
                            imageView.setBackgroundResource(R.drawable.rain);
                            sensor = "precipitation" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_value))) {
                            sensor = "value" + ":" + sensor.substring(sensor.length() - 1);
                        } else {
                            sensor = "unknown";
                        }

                        @SuppressLint("DrawAllocation")
                        JSONObject eventData = new JSONObject(jsonObject.get("eventData").toString());
                        @SuppressLint("DrawAllocation")
                        JSONArray timeSeriesData = new JSONArray(eventData.get("timeSeriesData").toString());
                        for (int i = 0; i < timeSeriesData.length(); i++) {
                            @SuppressLint("DrawAllocation")
                            JSONObject obj = new JSONObject(timeSeriesData.get(i).toString());
                            if (obj.get("seriesId").toString().contains(sensor)) {
                                value = Double.valueOf(obj.get("value").toString());
                                @SuppressLint("DrawAllocation")
                                BigDecimal num = new BigDecimal(value);
                                value = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            }
                        }
                        int xPos = (getWidth() / 2);
                        int yPos = (getHeight() / 2);
                        float px = getResources().getDimension(R.dimen.canvasTextSize);

                        canvas.drawText(name, xPos, 2 * px, paintText);
                        canvas.drawText(String.valueOf(value) + getUnit.unit(sensor), xPos, yPos + px, paint);
                    }
                    if (flag == 1) {
                        if (sensor.contains(context.getString(R.string.sensor_temperature))) {
                            imageView.setBackgroundResource(R.drawable.temperature);
                            sensor = "temperature" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_humidity))) {
                            imageView.setBackgroundResource(R.drawable.humidity);
                            sensor = "humidity" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_wind_speed))) {
                            imageView.setBackgroundResource(R.drawable.windspeed);
                            sensor = "wind_speed" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_wind_direction))) {
                            imageView.setBackgroundResource(R.drawable.winddirection);
                            sensor = "wind_direction" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_precipitation))) {
                            imageView.setBackgroundResource(R.drawable.rain);
                            sensor = "precipitation" + ":" + sensor.substring(sensor.length() - 1);
                        } else if (sensor.contains(context.getString(R.string.sensor_value))) {
                            sensor = "value" + ":" + sensor.substring(sensor.length() - 1);
                        } else {
                            sensor = "unknown";
                        }

                        @SuppressLint("DrawAllocation")
                        JSONObject eventData = new JSONObject(jsonObject.get("eventData").toString());
                        @SuppressLint("DrawAllocation")
                        JSONArray timeSeriesData = new JSONArray(eventData.get("timeSeriesData").toString());
                        for (int i = 0; i < timeSeriesData.length(); i++) {
                            @SuppressLint("DrawAllocation")
                            JSONObject obj = new JSONObject(timeSeriesData.get(i).toString());
                            Log.d(TAG, "seriesId = " + obj.get("seriesId").toString());
                            if (obj.get("seriesId").toString().contains(sensor)) {
                                value = Double.valueOf(obj.get("value").toString());
                                @SuppressLint("DrawAllocation")
                                BigDecimal num = new BigDecimal(value);
                                value = num.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            }
                        }

                        int xPos = (getWidth() / 2);
                        int yPos = (getHeight() / 2);
                        float px = getResources().getDimension(R.dimen.canvasTextSize);

                        canvas.drawText(name, xPos, 2 * px, paintText);
                        @SuppressLint("DrawAllocation")
                        RectF oval1 = new RectF(xPos - yPos, 4 * px,
                                xPos + yPos, getHeight() + 4 * px);

                        float ovalbottom = getHeight() + 4 * px;
                        double M = Double.valueOf(max);
                        double m = Double.valueOf(min);
                        double wing;

                        if (value > M) {
                            wing = 180.00;
                        } else if (value < m) {
                            wing = 0.00;
                        } else {
                            wing = ((value - m) / (M - m)) * 180;
                        }

                        canvas.drawArc(oval1, 180, 180, false, emptyPaint);
                        canvas.drawArc(oval1, 180, (float) wing, false, valuePaint);
                        canvas.drawText(min, xPos - yPos, (int) (ovalbottom - yPos + 1.75 * px), valueText);
                        canvas.drawText(max, xPos + yPos, (int) (ovalbottom - yPos + 1.75 * px), valueText);
                        canvas.drawText(String.valueOf(value) + getUnit.unit(sensor), xPos, (int) (ovalbottom - yPos + 2.5 * px), valueText);
                        canvas.drawCircle(xPos, ovalbottom - yPos, px, pointerPanit);

                        double x, y, Rx1, Ry1, Rx2, Ry2;
                        //noinspection IntegerDivisionInFloatingPointContext
                        x = xPos + Math.cos(Math.toRadians((180 + (float) wing))) * (yPos * 2 / 3);   //180:startAngle 60:sweepAngle
                        //noinspection IntegerDivisionInFloatingPointContext
                        y = ovalbottom - yPos + Math.sin(Math.toRadians((180 + (float) wing))) * (yPos * 2 / 3);   //width:(yPos * 2 / 3)
                        Rx1 = xPos + Math.cos(Math.toRadians(((180 + (float) wing) + 90))) * px;
                        Rx2 = xPos + Math.cos(Math.toRadians(((180 + (float) wing) - 90))) * px;
                        Ry1 = ovalbottom - yPos + Math.sin(Math.toRadians(((180 + (float) wing) + 90))) * px;
                        Ry2 = ovalbottom - yPos + Math.sin(Math.toRadians(((180 + (float) wing) - 90))) * px;
                        @SuppressLint("DrawAllocation")
                        Path path = new Path();
                        path.moveTo((int) x, (int) y);
                        path.lineTo((int) Rx1, (int) Ry1);
                        path.lineTo((int) Rx2, (int) Ry2);
                        path.close();
                        canvas.drawPath(path, pointerPanit);
                        canvas.drawCircle(xPos, ovalbottom - yPos, px / 2, circlePaint);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

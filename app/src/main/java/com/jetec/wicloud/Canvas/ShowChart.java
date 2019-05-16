package com.jetec.wicloud.Canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jetec.wicloud.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowChart {

    private static final String TAG = "ShowChart";
    private Context context;
    private LineChart lineChart;
    private DisplayMetrics dm;
    private YAxis leftAxis;
    private LimitLine yLimitLinedown, yLimitLineup;

    public ShowChart(Context context, LineChart lineChart, DisplayMetrics dm){
        this.context = context;
        this.lineChart = lineChart;
        this.dm = dm;
    }

    public void startdraw(String select, ArrayList<String> timeList, ArrayList<String> valueList){
        lineChart.clear();
        lineChart.setExtraOffsets((2 * dm.widthPixels / 100), (4 * dm.heightPixels / 100),
                (3 *  dm.widthPixels / 100),  (dm.heightPixels / 100));
        setDescription(select);
        lineChart.animateXY(800, 800);   //繪製延遲動畫
        setLegend();
        setYAxis(valueList);
        setXAxis(settime(timeList));
        setChartData(select, valueList, timeList);
    }

    private ArrayList<String> settime(ArrayList<String> timeList){
        ArrayList<String> setTime = new ArrayList<>();
        setTime.clear();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat gettime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timeset;

        for(int i = 0; i < timeList.size(); i++){
            try {
                timeset = gettime.parse(timeList.get(i));
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat settime = new SimpleDateFormat("MM-dd HH:mm");
                setTime.add(settime.format(timeset));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return setTime;
    }

    private void setChartData(String select, ArrayList<String> valueList, ArrayList<String> timeList){

        CustomMarkerView mv = new CustomMarkerView(context, R.layout.custom_marker_view_layout,
                dm.widthPixels, dm.heightPixels);
        lineChart.setMarkerView(mv);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(lineDataSet(ChartData(valueList), select));

        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);

        lineChart.setVisibleXRangeMaximum(timeList.size());
        lineChart.setScaleXEnabled(true);
        lineChart.setData(lineData);
    }

    private void setXAxis(ArrayList<String> timeList){
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);  //格線
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(14);
        xAxis.setGranularity(1);    //間隔
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(timeList.size());
        xAxis.setLabelCount(5); //xAxis.setLabelCount(int,true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value == 0) {
                    return "";
                } else {
                    return timeList.get(((int) value) - 1);
                }
            }
        });

        //lineChart.setVisibleXRangeMaximum(4);   //設置可視標籤數，超出的數量需要滑動查看
        lineChart.setXAxisRenderer(
                new CustomXAxisRenderer(
                        lineChart.getViewPortHandler(),
                        lineChart.getXAxis(),
                        lineChart.getTransformer(YAxis.AxisDependency.LEFT)));
        //lineChart.getXAxis().setLabelRotationAngle(-30);
    }

    private void setYAxis(ArrayList<String> valueList) {
        YAxis yAxisLeft = lineChart.getAxisLeft();

        if (leftAxis != null) {
            leftAxis.removeLimitLine(yLimitLinedown);
            leftAxis.removeLimitLine(yLimitLineup);
        }

        float maxIndex = Float.valueOf(valueList.get(0));
        float minIndex = Float.valueOf(valueList.get(0));

        for (int i = 0; i < valueList.size(); i++) {
            if (maxIndex < Float.valueOf(valueList.get(i))) {
                maxIndex = Float.valueOf(valueList.get(i));
            }
            if (minIndex > Float.valueOf(valueList.get(i))) {
                minIndex = Float.valueOf(valueList.get(i));
            }
        }

        maxIndex = maxIndex + 10;
        minIndex = minIndex - 10;

        yAxisLeft.setAxisMaximum(maxIndex);
        yAxisLeft.setAxisMinimum(minIndex);
        yAxisLeft.setGranularity(1);
        yAxisLeft.setTextSize(14);
        yAxisLeft.setTextColor(Color.BLACK);
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });
        lineChart.getAxisRight().setEnabled(false);
    }

    private void setLegend() {
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(14);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setTextColor(Color.BLACK);
    }

    private void setDescription(String descriptionStr) {
        Description description = new Description();
        description.setText(descriptionStr);
        Paint paint = new Paint();
        paint.setTextSize(20);
        float x = 0;
        float y = (float) (2 * dm.heightPixels / 100);
        description.setPosition(x, y);
        lineChart.setDescription(description);
    }

    private LineDataSet lineDataSet(List<Entry> ChartData, String getname) {
        LineDataSet lineDataSet = new LineDataSet(ChartData, getname);
        //lineDataSet.setDrawCircleHole(true);   //空心圓點
        lineDataSet.setColor( Color.BLUE); //線的顏色green
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setCubicIntensity(1);  //强度
        //lineDataSet.setCircleColor( Color.BLUE);    //圓點顏色
        lineDataSet.setLineWidth(1);

        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(value);
            }
        });
        return lineDataSet;
    }

    private List<Entry> ChartData(List<String> list) {
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            float getdata = (Float.valueOf(list.get(i)));
            data.add(new Entry((i + 1), getdata));
        }
        return data;
    }
}

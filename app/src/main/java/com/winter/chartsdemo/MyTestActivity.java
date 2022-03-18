package com.winter.chartsdemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author lei.gao@microtechmd.com
 * @date 2022/3/18
 */
public class MyTestActivity extends AppCompatActivity {

    private LineChartView chart;
    private LineChartData data;

    private TextView tv_time_line_left;
    private TextView tv_time_line_right;

    private static int pointSpace = 30 * 1000;

    private long zero;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        chart = (LineChartView) findViewById(R.id.chart);
        tv_time_line_left = (TextView) findViewById(R.id.tv_time_line_left);
        tv_time_line_right = (TextView) findViewById(R.id.tv_time_line_right);
        generateValues();


        chart.setInteractive(true);
        chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chart.setZoomEnabled(false);
        chart.setMaxZoom(Float.MAX_VALUE);
        chart.setViewportChangeListener(new ViewportChangeListener() {
            @Override
            public void onViewportChanged(Viewport viewport) {
                long timeLineLeft = ((long) viewport.left) * pointSpace + zero;
                long timeLineRight = ((long) viewport.right) * pointSpace + zero;
                SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.getDefault());
                String left = format.format(new Date(timeLineLeft));
                String right = format.format(new Date(timeLineRight));
                tv_time_line_left.setText(left);
                tv_time_line_right.setText(right);
                if (TextUtils.equals(left, right)) {
                    tv_time_line_right.setVisibility(View.GONE);
                } else {
                    tv_time_line_right.setVisibility(View.VISIBLE);
                }
            }
        });
        generateData ();

    }
    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    private void generateData () {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状
            line.setCubic(false);//曲线是否平滑，即是曲线还是折线
            line.setFilled(true);//是否填充曲线的面积
            line.setHasLabels(false);//曲线的数据坐标是否加上备注
            line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据
            line.setHasLines(true);//是否用线显示
            line.setHasPoints(true);//是否显示圆点
            line.setBaseArea(50);

//                line.setHasGradientToTransparent(hasGradientToTransparent);
//                if (pointsHaveDifferentColor){
//                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//                }
            lines.add(line);
        }
        data = new LineChartData(lines);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);   //x 轴分割线
        axisX.setName("Axis X");
        axisY.setName("Axis Y");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
}

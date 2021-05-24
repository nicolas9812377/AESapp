package com.example.pruebacpp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;


public class Graficos extends AppCompatActivity {

    final String[] quarters = new String[]{"128","192","256"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_graficos);
        BarChart chart = findViewById(R.id.chart);
        BarChart chart1 = findViewById(R.id.chart1);
        BarChart chart2 = findViewById(R.id.chart2);
        BarChart chart3 = findViewById(R.id.chart3);

        // id: 0 texto 1 archivos
        // col: 4 tiempo 5 memoria

        grafico(chart,0,4 );
        grafico(chart1,0,5 );

        grafico(chart2,1,4 );
        grafico(chart3,1,5 );

    }
    void grafico(BarChart chart, int id, int col){
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(8f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(3f);
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if (value < quarters.length && value != -1)
                    return quarters[(int)value];
                else
                    return String.valueOf((int)value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        Description description = chart.getDescription();
        description.setEnabled(false);

        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries1 = new ArrayList<>();
        entries.add(new BarEntry(0, Metricas.leer(id,col,128,0)));
        entries.add(new BarEntry(1, Metricas.leer(id,col,192,0)));
        entries.add(new BarEntry(2, Metricas.leer(id,col,256,0)));

        entries1.add(new BarEntry(0, Metricas.leer(id,col,128,1)));
        entries1.add(new BarEntry(1, Metricas.leer(id,col,192,1)));
        entries1.add(new BarEntry(2, Metricas.leer(id,col,256,1)));


        BarDataSet dataSet = new BarDataSet(entries, "Encriptar"); // add entries to dataset
        //dataSet.resetColors();
        dataSet.setColor(getResources().getColor(R.color.green));
        BarDataSet dataSet1 = new BarDataSet(entries1, "Desencriptar"); // add entries1 to dataset
        //dataSet1.resetColors();
        dataSet1.setColor(getResources().getColor(R.color.red));

        BarData data = new BarData(dataSet,dataSet1);
        data.setBarWidth(0.4f);
        chart.setData(data);
        chart.notifyDataSetChanged();

        chart.groupBars(0f, 0.1f, 0.05f);
        chart.animateY(3000);
        chart.invalidate();


    }
    void imprimir(String msg){ Toast.makeText(this," "+msg,Toast.LENGTH_LONG).show(); }
}
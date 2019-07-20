package com.adolfo.techmusic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class Statistics2 extends Fragment {
    LineChart mpLineChart;

    int colorArray[] = {R.color.color0,R.color.color1,R.color.color2,R.color.color3,
            R.color.color4,R.color.color5};
    int[] colorClassArray = new int[]{Color.CYAN,Color.RED,Color.GREEN,Color.YELLOW};
    String[] legendName = {"Banda","Reggaeton","Rock","Hip-Hop"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics,container,false);

        mpLineChart = rootView.findViewById(R.id.line_chart);

        LineDataSet lineDataSet1 = new LineDataSet(dataValues1(),"Data Set 1");

        //LineDataSet lineDataSet2 = new LineDataSet(dataValues2(),"Data set 2");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        //dataSets.add(lineDataSet2);

        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();
        Description description = new Description();
        description.setText("Descripcion de la grafica");
        description.setTextColor(Color.BLUE);
        description.setTextSize(20);
        mpLineChart.setDescription(description);
        /*End description*/

        lineDataSet1.setLineWidth(10);
        //lineDataSet2.setLineWidth(5);
        lineDataSet1.setColor(Color.RED);
        //lineDataSet2.setColor(Color.GREEN);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setCircleColor(Color.GREEN);
        lineDataSet1.setCircleColorHole(Color.BLACK);
        lineDataSet1.setCircleRadius(10);
        lineDataSet1.setCircleHoleRadius(5);
        lineDataSet1.setValueTextSize(15);
        lineDataSet1.setValueTextColor(Color.BLUE);
//        lineDataSet1.enableDashedLine(5,10,0);
        lineDataSet1.setColors(colorArray,getContext());

        Legend legend = mpLineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.RED);
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(10);
        legend.setXEntrySpace(15);//Space between legends
        legend.setFormToTextSpace(10);//Space between form and text

        /*Optional*/
        LegendEntry[] legendEntries = new LegendEntry[4];
        for (int i = 0; i < legendEntries.length; i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor = colorClassArray[i];
            entry.label = String.valueOf(legendName[i]);
            legendEntries[i] = entry;
        }
        legend.setCustom(legendEntries);

        return  rootView;
    }

    private ArrayList<Entry> dataValues1(){
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0,20));
        dataVals.add(new Entry(1,24));
        dataVals.add(new Entry(2,2));
        dataVals.add(new Entry(3,10));
        dataVals.add(new Entry(4,28));

        return dataVals;
    }

    /*Creating a second dataSet line*/
    private ArrayList<Entry> dataValues2(){
        ArrayList<Entry> dataVals = new ArrayList<>();
        dataVals.add(new Entry(0,12));
        dataVals.add(new Entry(2,16));
        dataVals.add(new Entry(3,23));
        dataVals.add(new Entry(5,1));
        dataVals.add(new Entry(7,18));
        return dataVals;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Generos");
    }


}


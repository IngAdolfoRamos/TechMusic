package com.adolfo.techmusic;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;


public class Estadisticas extends Fragment {

    int[] colorClassArray = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
    TextView totalSongsTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_estadisticas,container,false);

        totalSongsTV = rootView.findViewById(R.id.totalSongsTV);
        BarChart barChart = rootView.findViewById(R.id.bar_chart);
        PieChart pieChart = rootView.findViewById(R.id.pie_chart);

        /*Bar*/
        BarDataSet dataSet = new BarDataSet(getDuration(),"Canciones con mayor duracion");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(4);
        data.setBarWidth(0.15f);

        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(10);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        barChart.invalidate();
        /*End bar chart*/

        /*Pie chart*/
        PieDataSet pieDataSet = new PieDataSet(getSongs(),"Generos con mas canciones");
        pieDataSet.setColors(colorClassArray);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        /*End Pie Chart*/

        //getDuration();

        return rootView;
    }

    private ArrayList<BarEntry> getDuration(){
        ArrayList<BarEntry> duration = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);

        if (songCursor != null && songCursor.moveToFirst()){

            int id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int count = 0;

            do {

                count++;

                MediaMetadataRetriever mr = new MediaMetadataRetriever();
                Uri trackUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songCursor.getLong(id));
                mr.setDataSource(getActivity(), trackUri);

                String dura = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                int durationInSeconds = Integer.parseInt(dura);

                totalSongsTV.append(durationInSeconds + "\n");

                numbers.add(durationInSeconds);
                mr.release();

            }while (songCursor.moveToNext());

            Collections.sort(numbers, Collections.<Integer>reverseOrder());
            for (int i = 0; i < 10; i++){
                duration.add(new BarEntry(i,numbers.get(i)));
            }

            return duration;
        }

        return duration;
    }

    private ArrayList<PieEntry> getSongs(){
        ArrayList<PieEntry> generos = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);

        if (songCursor != null && songCursor.moveToFirst()){

            int id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);

            String bandaS = "", hipHopS = "", salsaS = "", cumbiaS = "", bachataS = "";
            int count = 0, banda = 0, hipHop = 0, salsa = 0, cumbia = 0, bachata = 0;

            do {

                count++;

                MediaMetadataRetriever mr = new MediaMetadataRetriever();
                Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songCursor.getLong(id));
                mr.setDataSource(getActivity(), trackUri);

                try{
                    String songGenre = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);

                    if(songGenre.equals("banda") || songGenre.equals("Banda")) {

                        banda++;
                        bandaS = songGenre;

                    }else if(songGenre.equals("www.MzHipHop.com")) {

                        hipHop++;
                        hipHopS = songGenre;

                    }else if(songGenre.equals("Salsa") || songGenre.equals("salsa") || songGenre.equals("SALSA")) {
                        salsa++;
                        salsaS = songGenre;
                    }else if(songGenre.equals("Cumbia") || songGenre.equals("cumbia") || songGenre.equals("CUMBIA")) {
                        cumbia++;
                        cumbiaS = songGenre;
                    }else if(songGenre.equals("Bachata")) {
                        bachata++;
                        bachataS = songGenre;
                    }
                }catch(Exception e){

                }

            }while (songCursor.moveToNext());

            generos.add(new PieEntry(banda, bandaS));
            generos.add(new PieEntry(hipHop, hipHopS));
            generos.add(new PieEntry(salsa, salsaS));
            generos.add(new PieEntry(cumbia, cumbiaS));

/*            totalSongsTV.append(count + " canciones en total y ");
            totalSongsTV.append("hay " + banda +" de " + genre);*/
            return generos;
        }

        return generos;
    }

/*    public void sorting(){
        Integer[] numbers = {2,6,9,2,5,3,5,8};
        Arrays.sort(numbers, Collections.reverseOrder());
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }

    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Gráficas");
    }

    /*Methods Pie chart*/
    private ArrayList<PieEntry> mostGenres(){
        ArrayList<PieEntry> genres =  new ArrayList<>();
        genres.add(new PieEntry(2, "Rock"));
        genres.add(new PieEntry(5, "Salsa"));
        genres.add(new PieEntry(3, "Cumbia"));
        genres.add(new PieEntry(7, "Clasica"));
        genres.add(new PieEntry(1, "Bachata"));
        return genres;
    }
    /*End Methods Pie Chart*/

    /*Methods Bar Chart*/
    private ArrayList<BarEntry> mostDuration(){
        ArrayList<BarEntry> duration = new ArrayList<>();
        duration.add(new BarEntry(1,10));
        duration.add(new BarEntry(2,9));
        duration.add(new BarEntry(3,8));
        duration.add(new BarEntry(4,7));
        duration.add(new BarEntry(5,6));
        duration.add(new BarEntry(6,5));
        duration.add(new BarEntry(7,4));
        duration.add(new BarEntry(8,3));
        duration.add(new BarEntry(9,2));
        duration.add(new BarEntry(10,1));
        return duration;
    }
    /*End Bar chart*/

}

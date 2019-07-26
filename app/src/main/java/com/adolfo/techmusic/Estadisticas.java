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
import android.provider.Settings;
import android.util.SparseArray;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Console;
import java.lang.reflect.Array;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;


public class Estadisticas extends Fragment {

    int[] colorClassArray = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA,Color.BLACK};
    //TextView totalSongsTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_estadisticas, container, false);

        //totalSongsTV = rootView.findViewById(R.id.totalSongsTV);
        BarChart barChart = rootView.findViewById(R.id.bar_chart);
        PieChart pieChart = rootView.findViewById(R.id.pie_chart);

        /*Bar*/
        BarDataSet dataSet = new BarDataSet(getDuration(), "Canciones con mayor duracion");
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
        PieDataSet pieDataSet = new PieDataSet(getSongs(), "Generos con mas canciones");
        pieDataSet.setColors(colorClassArray);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.invalidate();
        /*End Pie Chart*/

        //getDuration();

        return rootView;
    }

    private ArrayList<PieEntry> getSongs() {
        ArrayList<PieEntry> generos = new ArrayList<>();
        ArrayList<String> generosDesordenados = new ArrayList<String>();
        ArrayList<String> generosOrdenados = new ArrayList<String>();
        ArrayList<Integer> cuentas = new ArrayList<Integer>();
        Map<Integer,String> map = new HashMap<>();
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        int cuenta = 0, s = 0;
        String temporal = "", u ="";
        int count = 0;
        if (songCursor != null && songCursor.moveToFirst()) {
            int id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                count++;
                MediaMetadataRetriever mr = new MediaMetadataRetriever();
                Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songCursor.getLong(id));
                mr.setDataSource(getActivity(), trackUri);
                try {
                    String songGenre = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                    generosDesordenados.add(songGenre);
                } catch (Exception e) {
                }
            } while (songCursor.moveToNext());
        }

        try{
            //System.out.println(genero[0] = generosDesordenados.get(0));
            generosOrdenados.add(0,generosDesordenados.get(0));

            for (int i = 0; i < generosDesordenados.size(); i++){
                //System.out.println(i + "Desordenados: " + generosDesordenados.get(i));
                int cont =0;
                for(int j = 0; j < generosOrdenados.size(); j++){
                    System.out.println(generosOrdenados.size());
                    if(generosDesordenados.get(i).equals(generosOrdenados.get(j))){
                        //cuentas.add(cuentas.get(j),cuentas.get(j)+1);
                        System.out.println("prueba");
                        cont++;
                    }else{
                    }
                }
                if(cont==0) {
                    generosOrdenados.add(generosDesordenados.get(i));
                }
            }

            //////
            for (int i = 0; i < generosOrdenados.size(); i++){
                int cont =0;
                for(int j = 1; j < generosDesordenados.size(); j++) {
                    if(generosDesordenados.get(j).equals(generosOrdenados.get(i)))
                        cont++;
                }
                cuentas.add(cont);
            }



            for (int a = 0; a < generosDesordenados.size(); a++){
                System.out.println(a + " Desordenados: " + generosDesordenados.get(a));
            }
            //Collections.sort(cuentas, Collections.<Integer>reverseOrder());
            for (int f = 0; f < generosOrdenados.size(); f++){
                System.out.println(f + " Ordenados: " + generosOrdenados.get(f)+", "+cuentas.get(f));
                map.put(cuentas.get(f),generosOrdenados.get(f));
            }
            System.out.println("El tamaño del HashMap es: " + map.size());

            System.out.println("\n=== Iterating over the HashMap's entrySet using simple for-each loop ===");
            for(Map.Entry<Integer,String> entry: map.entrySet()) {
                System.out.println(entry.getKey() + " => " + entry.getValue());
            }

            int l = 0;
            Map<Integer, String> maps = new TreeMap<Integer, String>(Collections.reverseOrder());
            maps.putAll(map);
            System.out.println("After Sorting:");
            Set set2 = maps.entrySet();
            Iterator iterator2 = set2.iterator();
            while(iterator2.hasNext()) {
                Map.Entry me2 = (Map.Entry)iterator2.next();
                System.out.print(me2.getKey() + ": ");
                int key = (int) me2.getKey();
                System.out.println(me2.getValue());
                generos.add(new PieEntry(key,me2.getValue()));
                l++;
                if (l == 4){
                    break;
                }
            }

            //System.out.println("El tamaño es: "+generosOrdenados.size());

        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        return generos;
    }


                /*if (!generosDesordenados.get(i).equals(temporal)){
                    generosOrdenados.add(generosDesordenados.get(i));
                    temporal = generosDesordenados.get(i);
                }*/

    /*                    for (int g = 0; g < generosDeCelular.size(); g++) {
                        u = generosDeCelular.get(g);
                        System.out.println("Los generos son: " + u);
                        if (songGenre.equals(u)){
                            count++;
                            genres.put(count,u);
                        }
                    }*/

    private ArrayList<BarEntry> getDuration() {
        ArrayList<BarEntry> duration = new ArrayList<>();
        ArrayList<Integer> numbers = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {

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

                //totalSongsTV.append(durationInSeconds + "\n");

                numbers.add(durationInSeconds);
                mr.release();

            } while (songCursor.moveToNext());

            Collections.sort(numbers, Collections.<Integer>reverseOrder());
            try {
                for (int i = 0; i < 10; i++) {
                    duration.add(new BarEntry(i, numbers.get(i)));
                }
            } catch (Exception e) {

            }

            return duration;
        }

        return duration;
    }

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

    /*                    for(int i = 0; i < generosDeCelular.size(); i++){
                        System.out.println(generosDeCelular.get(i));
                        if (songGenre.equals(generosDeCelular.get(i))){
                            counter++;
                            generosExternos.add(songGenre);
                        }
                    }*/
    /*generos.add(new PieEntry(counter,songGenre));*/

    /*    public void sorting(){
        Integer[] numbers = {2,6,9,2,5,3,5,8};
        Arrays.sort(numbers, Collections.reverseOrder());
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }

    }*/

}

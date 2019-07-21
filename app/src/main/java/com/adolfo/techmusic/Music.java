package com.adolfo.techmusic;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Music extends Fragment {

    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;
    TextView genreTV, genre1TV;

    int totalSongs = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_music,container,false);

        listView = view.findViewById(R.id.listView);
        genreTV = view.findViewById(R.id.genreTV);
        genre1TV = view.findViewById(R.id.genre1TV);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST);
            }
        }else{
            doStuff();
        }

        return  view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                        /*Do stuff*/
                    }
                } else {
                    Toast.makeText(getActivity(), "No permission granted", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                return;
            }
        }
    }

    public void doStuff(){
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Mi música");
    }

    public void getMusic(){
        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null,null,null,null);

        String genero;

        if (songCursor != null && songCursor.moveToFirst()){

            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            int id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);

            int count = 0, banda = 0, hipHop = 0;

            do {

                count++;

                /*Genre*/
                MediaMetadataRetriever mr = new MediaMetadataRetriever();
                Uri trackUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        songCursor.getLong(id));
                mr.setDataSource(getActivity(), trackUri);

                genero = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);

                /*End genre*/

                String currenTitle = songCursor.getString(songTitle);
                String currenArtist = songCursor.getString(songArtist);
                String currenLocation = songCursor.getString(songLocation);
                arrayList.add("Title: " + currenTitle + "\n" +
                        "Artist: " + currenArtist + "\n" +
                        "Location: " + currenLocation + "\n" +
                        "Genero: " + genero + "\n" +
                        "Total" + count);

                /*String songGenre = metadataRetriever.extractMetadata
                        (MediaMetadataRetriever.METADATA_KEY_GENRE);*/

                /*if(genero.equals("Banda")) {
                    b++;
                    Toast.makeText(getActivity(),"Genero encontrado: " + genero +
                            " con " + b + " canciones", Toast.LENGTH_SHORT).show();
                }*/

                if(genero.equals("Banda")) {
                    banda++;
                    genreTV.setText(String.valueOf(banda));
                }else if(genero.equals("www.MzHipHop.com")) {
                    hipHop++;
                    genre1TV.setText(String.valueOf(hipHop));
                }

            }while (songCursor.moveToNext());



            /*Total songs*/
            /*totalSongs = count;*/
            Toast.makeText(getActivity(), String.valueOf(count), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            /*End total songs*/
        }
    }

}

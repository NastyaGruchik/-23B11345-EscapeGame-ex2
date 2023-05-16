package com.example.ex2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ex2.Interfaces.CallBack_List;
import com.example.ex2.Interfaces.CallBack_Map;
import com.example.ex2.Models.GameDB;
import com.example.ex2.Models.Record;
import com.example.ex2.Utilities.MSP;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class ScoreActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ListFragment listFragment;
    private MapFragment mapFragment;
    private TextView info;
    private GoogleMap map;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);



        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("myDB");
        listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        listFragment.setCallBackList(callBack_List);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_list, listFragment).commit();

        mapFragment = new MapFragment();
        mapFragment.setCallBackMap(callBack_map);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_map, mapFragment).commit();

    }
    CallBack_List callBack_List = new CallBack_List() {

        @Override
        public void rowSelected(int i) {
            String fromJSON = MSP.getInstance(getApplicationContext()).getStringSP("GAME_DB","");
            GameDB myDB = new Gson().fromJson(fromJSON,GameDB.class);
            if(myDB !=null) {
                if (i < myDB.getRecords().size()) {
                    Record record = myDB.getRecords().get(i);
                    callBack_map.locationSelected(record);
                }
            }
        }
    };


    CallBack_Map callBack_map = new CallBack_Map() {

        @Override
        public void locationSelected(Record record) {
            mapFragment.onClicked(record);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
package com.example.ex2;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ex2.Interfaces.CallBack_Map;
import com.example.ex2.Models.Record;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private CallBack_Map callBack_map;
    private GoogleMap map;

    public void setCallBackMap(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.google_map, mapFragment).commit();
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    private void moveCameraByRecord (Record record) {
        LatLng mark = new LatLng(record.getLat(), record.getLon());
        map.addMarker(new MarkerOptions().position(mark).title("I am here"));
        map.moveCamera(CameraUpdateFactory.
                newLatLngZoom(mark,1));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(record.getLat(), record.getLon()), 15.0f));
    }

    public void onClicked(Record record) {
        moveCameraByRecord(record);
    }


}
package com.example.ex2.Interfaces;

import com.example.ex2.Models.Record;

public interface CallBack_Map {
    void mapClicked(double lat, double lon);
    void locationSelected(Record record);
}

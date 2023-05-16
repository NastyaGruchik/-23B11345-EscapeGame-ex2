package com.example.ex2.Models;

import java.util.ArrayList;
import java.util.Collections;

public class GameDB {
    private ArrayList<Record> records = new ArrayList<>();

    public GameDB() {    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public GameDB setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

    public void sortRecords() {
        Collections.sort(records);
    }
}

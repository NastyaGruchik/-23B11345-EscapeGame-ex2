package com.example.ex2;

import android.app.Application;

import com.example.ex2.Utilities.MSP;
import com.example.ex2.Utilities.SignalGenerator;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MSP.getInstance(this);
        SignalGenerator.init(this);
    }
}

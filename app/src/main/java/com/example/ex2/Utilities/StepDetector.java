package com.example.ex2.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.ex2.Interfaces.StepCallback;

public class StepDetector {
    private Sensor sensor;

    private SensorManager sensorManager;

    private StepCallback stepCallback;

    private int stepCounterX = 0;
    private int stepCounterY = 0;
    private long timestamp = 0;

    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        initEventListener();

    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 350) {
            timestamp = System.currentTimeMillis();
            if (x > 3.0) {
                stepCounterX++;
                if (stepCallback != null)
                    stepCallback.stepX();
            } else if (x<-3.0) {
                stepCounterX--;
                if (stepCallback != null)
                    stepCallback.stepX();
            }
            if (y > 3.0) {
                stepCounterY++;
                if (stepCallback != null)
                    stepCallback.stepY();
            } else if (y<-3.0) {
                stepCounterY--;
                if (stepCallback != null)
                    stepCallback.stepY();
            }

        }
    }

    public int getStepsX() {
        return stepCounterX;
    }

    public int getStepsY() {
        return stepCounterY;
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}

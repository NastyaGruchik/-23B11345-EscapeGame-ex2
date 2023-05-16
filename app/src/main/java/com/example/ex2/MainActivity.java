package com.example.ex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Button sv_btn_easyGame;
    Button sv_btn_hardGame;
    Button sv_btn_score;
    Switch sv_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},101);
        }

        sv_btn_easyGame.setOnClickListener(v -> easyGame());
        sv_btn_hardGame.setOnClickListener(v -> hardGame());
        sv_btn_score.setOnClickListener(v -> scores());

    }

    private void scores() {
        Intent intent=new Intent(this,ScoreActivity.class);
        startActivity(intent);

    }

    private void hardGame() {
        Intent intent=new Intent(this,GameActivity.class);
        intent.putExtra("hardGame",1);
        intent.putExtra("sensorMode",sv_switch.isChecked());
        startActivity(intent);


    }

    private void easyGame() {
        Intent intent=new Intent(this,GameActivity.class);
        intent.putExtra("easyGame",0);
        intent.putExtra("sensorMode",sv_switch.isChecked());
        startActivity(intent);


    }


    private void findViews() {
        sv_btn_easyGame=findViewById(R.id.sv_btn_easyGame);
        sv_btn_hardGame=findViewById(R.id.sv_btn_hardGame);
        sv_btn_score=findViewById(R.id.sv_btn_score);
        sv_switch=findViewById(R.id.sv_switch);
    }
}
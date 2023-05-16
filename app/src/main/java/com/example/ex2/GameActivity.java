package com.example.ex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex2.Interfaces.StepCallback;
import com.example.ex2.Models.GameDB;
import com.example.ex2.Models.Record;
import com.example.ex2.Utilities.MSP;
import com.example.ex2.Utilities.SignalGenerator;
import com.example.ex2.Utilities.StepDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private MediaPlayer gameOverSound, crashSound, coinSound;
    private Timer timer1;
    private GameDB gameDB;
    private int stepCounterX = 0;
    private int stepCounterY = 0;
    private StepDetector stepDetector;
    private final int COLONS = 5;
    private final int ROWS = 6;
   // private final int EASY_GAME = 0;
    private final int HARD_GAME = 1;
    private int period=1000;
    private Button main_BTN_submit;
    private EditText main_ET_name;
    private TextView main_TE_gameOver;
    private MaterialTextView score;
    private FloatingActionButton lButton;
    private FloatingActionButton rButton;
    private ShapeableImageView[] hearts;
    private int lives=3;
    protected ShapeableImageView[][] dogs;
    protected ShapeableImageView[][] prices;
    private ShapeableImageView[] cats;
    private long startTime =0;
    int randomNum1;
    int randomNum2;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        startView();

        String fromJSON = MSP.getInstance(this).getStringSP("GAME_DB", "");
        gameDB = new Gson().fromJson(fromJSON, GameDB.class);
        if (gameDB == null)
            gameDB = new GameDB();

        Intent intent=getIntent();
        if (intent.getBooleanExtra("sensorMode",false)){
            if(intent.getIntExtra("hardGame",0)==HARD_GAME)
                period=500;
            else
                period=1500;
            initStepDetector();
            stepDetector.start();
        } else {
            lButton.setVisibility(View.VISIBLE);
            rButton.setVisibility(View.VISIBLE);
            lButton.setOnClickListener(v -> goLeft());
            rButton.setOnClickListener(v -> goRight());
            if(intent.getIntExtra("hardGame",0)==HARD_GAME)
                period=500;
            else
                period=1500;
        }
    }

    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepX() {
                if(stepCounterX > stepDetector.getStepsX()){
                    stepCounterX=stepDetector.getStepsX();
                    goRight();
                }
                else if (stepCounterX < stepDetector.getStepsX()) {
                    stepCounterX=stepDetector.getStepsX();
                    goLeft();
                }
            }

            @Override
            public void stepY() {
                if(stepCounterY < stepDetector.getStepsY()){
                    stepCounterY=stepDetector.getStepsY();
                    period=period+100;
                }
                else if (stepCounterY > stepDetector.getStepsY()) {
                    stepCounterY=stepDetector.getStepsY();
                    period=period-100;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->falling());
            }
        }, 0,period);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer1 != null)
            timer1.cancel();
        timer1 = null;
    }

    private void falling() {
        startTime++;
        score.setText("" + startTime);
        for (int i=0;i<COLONS;i++){
            for(int j=ROWS-1;j>=0;j--){
                if (j==ROWS-1 && dogs[ROWS-1][i].getVisibility()== View.VISIBLE){
                    dogs[ROWS-1][i].setVisibility(View.INVISIBLE);
                    checkInfected(i);
                }
                if (j==ROWS-1 && prices[ROWS-1][i].getVisibility()==View.VISIBLE){
                    prices[ROWS-1][i].setVisibility(View.INVISIBLE);
                    checkPrice(i);
                }
                if(dogs[j][i].getVisibility()==View.VISIBLE){
                    dogs[j+1][i].setVisibility(View.VISIBLE);
                    dogs[j][i].setVisibility(View.INVISIBLE);
                }
                if(prices[j][i].getVisibility()==View.VISIBLE){
                    prices[j+1][i].setVisibility(View.VISIBLE);
                    prices[j][i].setVisibility(View.INVISIBLE);
                }
            }
        }
        if (startTime%2==0){
            randomNum1 = (int) (Math.random() * (COLONS ));
            dogs[0][randomNum1].setVisibility(View.VISIBLE);
        }
        if (startTime%10 ==0){
            do{
                randomNum2 = (int) (Math.random() * (COLONS ));
            }while(randomNum2==randomNum1);
            prices[0][randomNum2].setVisibility(View.VISIBLE);

        }

    }

    private void checkPrice(int i) {
        if(cats[i].getVisibility()==View.VISIBLE){
            startTime+=4;
            SignalGenerator.getInstance().toast("yay",Toast.LENGTH_SHORT);
            SignalGenerator.getInstance().vibrate();
            coinSound.start();
        }
    }

    private void checkInfected(int i) {
        if(cats[i].getVisibility()==View.VISIBLE){
            if(lives!=0){
                SignalGenerator.getInstance().toast("oh no",Toast.LENGTH_SHORT);
                SignalGenerator.getInstance().vibrate();
                crashSound.start();
                lives--;
                hearts[lives].setVisibility(View.INVISIBLE);
                if (lives==0)
                    gameOver();
            }
        }
    }

    private void gameOver() {
        timer1.cancel();
        if (stepDetector!= null)
            stepDetector.stop();
        startView();
        main_TE_gameOver.setVisibility(View.VISIBLE);
        main_BTN_submit.setVisibility(View.VISIBLE);
        main_ET_name.setVisibility(View.VISIBLE);
        gameOverSound.start();
        Record record=new Record();
        main_BTN_submit.setOnClickListener(v -> {
            
            // Ask location permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return;
            }
            // Set location


            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // check if current score should enter to the highscore list
            if (gameDB.getRecords().size() <= 10) {
                record.setName(main_ET_name.getText().toString()).setScore(startTime).setLat(location.getLatitude()).setLon(location.getLongitude());
                gameDB.getRecords().add(record);
            } else if (gameDB.getRecords().get(gameDB.getRecords().size() - 1).getScore() < startTime) {
                record.setName(main_ET_name.getText().toString()).setScore(startTime).setLat(location.getLatitude()).setLon(location.getLongitude());
                gameDB.getRecords().set(gameDB.getRecords().size() - 1, record);
            }
            gameDB.sortRecords();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            this.finish();
            Intent intent = new Intent(this, ScoreActivity.class);
            Bundle bundle = new Bundle();
            String json = new Gson().toJson(gameDB);
            bundle.putString("gameDB", json);
            intent.putExtra("gameDB", bundle);
            MSP.getInstance(this).putStringSP("GAME_DB", json);
            startActivity(intent);
        });

    }
    private void goLeft() {
        for (int i=1;i<cats.length;i++){
            if (cats[i].getVisibility()== View.VISIBLE){
                cats[i].setVisibility(View.INVISIBLE);
                cats[i-1].setVisibility(View.VISIBLE);
            }
        }
    }
    private void goRight() {
        for(int i=cats.length-2;i>=0 ;i--){
            if(cats[i].getVisibility()== View.VISIBLE){
                cats[i].setVisibility(View.INVISIBLE);
                cats[i+1].setVisibility(View.VISIBLE);
            }
        }
    }

    private void startView() {
        for (int i =0; i<COLONS;i++){
            //for dogs & prices
            for(int j=0; j<ROWS;j++){
                dogs[j][i].setVisibility(View.INVISIBLE);
                prices[j][i].setVisibility(View.INVISIBLE);
            }
            // for cats
            if(i==COLONS/2)
                cats[i].setVisibility(View.VISIBLE);
            else
                cats[i].setVisibility(View.INVISIBLE);
        }
        lButton.setVisibility(View.INVISIBLE);
        rButton.setVisibility(View.INVISIBLE);
        main_ET_name.setVisibility(View.INVISIBLE);
        main_BTN_submit.setVisibility(View.INVISIBLE);
        main_TE_gameOver.setVisibility(View.INVISIBLE);
    }
    private void findViews() {
        gameOverSound =MediaPlayer.create(this,R.raw.gameover);
        coinSound  =MediaPlayer.create(this,R.raw.yay);
        crashSound = MediaPlayer.create(this,R.raw.dog);

        main_TE_gameOver = findViewById(R.id.main_TE_gameOver);
        main_ET_name= findViewById(R.id.main_ET_name);
        main_BTN_submit=findViewById(R.id.main_BTN_submit);
        score = findViewById(R.id.main_LBL_score);
        lButton = findViewById(R.id.main_FAB_left);
        rButton = findViewById(R.id.main_FAB_right);
        hearts= new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart3),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart1)
        };
        dogs = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_dog00),findViewById(R.id.main_IMG_dog01),findViewById(R.id.main_IMG_dog02),findViewById(R.id.main_IMG_dog03),findViewById(R.id.main_IMG_dog04)},
                {findViewById(R.id.main_IMG_dog10),findViewById(R.id.main_IMG_dog11),findViewById(R.id.main_IMG_dog12),findViewById(R.id.main_IMG_dog13),findViewById(R.id.main_IMG_dog14)},
                {findViewById(R.id.main_IMG_dog20),findViewById(R.id.main_IMG_dog21),findViewById(R.id.main_IMG_dog22),findViewById(R.id.main_IMG_dog23),findViewById(R.id.main_IMG_dog24)},
                {findViewById(R.id.main_IMG_dog30),findViewById(R.id.main_IMG_dog31),findViewById(R.id.main_IMG_dog32),findViewById(R.id.main_IMG_dog33),findViewById(R.id.main_IMG_dog34)},
                {findViewById(R.id.main_IMG_dog40),findViewById(R.id.main_IMG_dog41),findViewById(R.id.main_IMG_dog42),findViewById(R.id.main_IMG_dog43),findViewById(R.id.main_IMG_dog44)},
                {findViewById(R.id.main_IMG_dog50),findViewById(R.id.main_IMG_dog51),findViewById(R.id.main_IMG_dog52),findViewById(R.id.main_IMG_dog53),findViewById(R.id.main_IMG_dog54)}
        };
        cats = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_cat0),
                findViewById(R.id.main_IMG_cat1),
                findViewById(R.id.main_IMG_cat2),
                findViewById(R.id.main_IMG_cat3),
                findViewById(R.id.main_IMG_cat4)
        };
        prices = new ShapeableImageView[][]{
                {findViewById(R.id.main_IMG_price00),findViewById(R.id.main_IMG_price01),findViewById(R.id.main_IMG_price02),findViewById(R.id.main_IMG_price03),findViewById(R.id.main_IMG_price04)},
                {findViewById(R.id.main_IMG_price10),findViewById(R.id.main_IMG_price11),findViewById(R.id.main_IMG_price12),findViewById(R.id.main_IMG_price13),findViewById(R.id.main_IMG_price14)},
                {findViewById(R.id.main_IMG_price20),findViewById(R.id.main_IMG_price21),findViewById(R.id.main_IMG_price22),findViewById(R.id.main_IMG_price23),findViewById(R.id.main_IMG_price24)},
                {findViewById(R.id.main_IMG_price30),findViewById(R.id.main_IMG_price31),findViewById(R.id.main_IMG_price32),findViewById(R.id.main_IMG_price33),findViewById(R.id.main_IMG_price34)},
                {findViewById(R.id.main_IMG_price40),findViewById(R.id.main_IMG_price41),findViewById(R.id.main_IMG_price42),findViewById(R.id.main_IMG_price43),findViewById(R.id.main_IMG_price44)},
                {findViewById(R.id.main_IMG_price50),findViewById(R.id.main_IMG_price51),findViewById(R.id.main_IMG_price52),findViewById(R.id.main_IMG_price53),findViewById(R.id.main_IMG_price54)}
        };
    }
}
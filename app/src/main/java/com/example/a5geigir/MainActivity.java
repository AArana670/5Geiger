package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean measuring = false;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        displayState();

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "signalDB"
        ).allowMainThreadQueries().build();

        List<Signal> signalList = db.signalDao().getSignals();
        for (Signal s : signalList){
            Log.d("SignalDB", "cId: "+s.cId+", moment: "+s.moment+", ubiLat: "+s.ubiLat+", ubiLong: "+ s.ubiLong+", dBm: "+s.dBm);
        }
    }

    private void displayState() {
        Button btn = (Button) findViewById(R.id.main_btn);

        if (measuring){
            showCurrentMeasure();
            btn.setText(R.string.main_measureStop);
        }else{
            showLastMeasure();
            btn.setText(R.string.main_measureStart);
        }

    }

    public void jumpToSettings(View v){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void jumpToHistory(View v){
        Intent i = new Intent(this, HistoryActivity.class);
        startActivity(i);
    }

    public void switchState(View v){
        try {
            Button btn = (Button) findViewById(R.id.main_btn);

            if (measuring) {
                //stopMeasure();
                showLastMeasure();
                measuring = false;
                btn.setText(R.string.main_measureStart);
            } else {
                startMeasure();
                showCurrentMeasure();
                measuring = true;
                btn.setText(R.string.main_measureStop);
            }
        }catch (Exception e){
            //dialog insisting on permissions
        }
    }

    private void startMeasure() {

        int cId = (int) Math.floor(Math.random()*30);
        String moment = Calendar.getInstance().toString();
        double ubiLat = 0;
        double ubiLong = 0;
        int dBm = (int) ((Math.random()*-50)-20);

        Signal s = new Signal(cId, moment, ubiLat, ubiLong, dBm);

        db.signalDao().insertSignal(s);

        Log.d("SignalDB", "Added new; cId: "+s.cId+", moment: "+s.moment+", ubiLat: "+s.ubiLat+", ubiLong: "+ s.ubiLong+", dBm: "+s.dBm);
    }

    private void showLastMeasure() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);
    }

    private void showCurrentMeasure() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);

    }
}
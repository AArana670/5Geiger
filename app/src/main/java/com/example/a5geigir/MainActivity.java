package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogListener {

    private boolean measuring = false;
    private boolean hasPermissions = false;
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
            showCurrentMeasurement();
            btn.setText(R.string.main_measureStop);
        }else{
            showLastMeasurement();
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
        Button btn = (Button) findViewById(R.id.main_btn);

        if (measuring) {
            stopMeasure();
            showLastMeasurement();
            measuring = false;
            btn.setText(R.string.main_measureStart);
        } else {
            if (!hasPermissions) {  //Insist on the permission request
                DialogFragment dialog = new PermissionDialog();
                dialog.show(getSupportFragmentManager(), "permission_dialog");
            }else {
                startMeasure();
                showCurrentMeasurement();
                measuring = true;
                btn.setText(R.string.main_measureStop);
            }
        }
    }

    private void startMeasure() {

        int cId = (int) Math.floor(Math.random()*30);
        String moment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        double ubiLat = 0;
        double ubiLong = 0;
        int dBm = (int) ((Math.random()*-50)-20);

        Signal s = new Signal(cId, moment, ubiLat, ubiLong, dBm);

        db.signalDao().insertSignal(s);

        Log.d("SignalDB", "Added new; cId: "+s.cId+", moment: "+s.moment+", ubiLat: "+s.ubiLat+", ubiLong: "+ s.ubiLong+", dBm: "+s.dBm);
    }

    public void stopMeasure(){

    }

    private void showLastMeasurement() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);
    }

    private void showCurrentMeasurement() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);

    }

    public void clearDB(View v){  //Method for debugging
        db.signalDao().clearSignals();
        Log.d("SignalDB", "All signals have been deleted");
    }

    @Override
    public void positiveAnswer() {  //PermissionDialog has accepted the permission request
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);

        hasPermissions = true;

        startMeasure();
        showCurrentMeasurement();
        measuring = true;

        Button btn = (Button) findViewById(R.id.main_btn);
        btn.setText(R.string.main_measureStop);
    }

    @Override
    public void negativeAnswer() {  //PermissionDialog has denied the permission request
        Toast.makeText(this, getString(R.string.dialog_permissions_cancelled), Toast.LENGTH_SHORT).show();
    }
}
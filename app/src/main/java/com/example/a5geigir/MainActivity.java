package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogListener, NetworkListener {


    private AppDatabase db;
    private NetworkManager networkManager;
    private TextView measurementTitle;
    private TextView measurementDBm;
    private TextView measurementMoment;
    private ProgressBar measurementBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        networkManager = NetworkManager.getInstance(this);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "signalDB"
        ).allowMainThreadQueries().build();

        List<Signal> signalList = db.signalDao().getSignals();  //For debugging purposes only
        for (Signal s : signalList){
            Log.d("SignalDB", "cId: "+s.cId+", moment: "+s.moment+", ubiLat: "+s.ubiLat+", ubiLong: "+ s.ubiLong+", dBm: "+s.dBm);
        }
    }

    private void displayState() {  //In case the thread was already running, opening the app will restore the display
        Button btn = (Button) findViewById(R.id.main_btn);

        if (networkManager.isRunning()){
            showCurrentMeasurement(/*null*/);
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

        if (networkManager.isRunning()) {  //If it was measuring, stop it
            stopMeasure();
            showLastMeasurement();
            btn.setText(R.string.main_measureStart);
            networkManager.removeListener(this);
            Toast.makeText(this, "Measurement stopped", Toast.LENGTH_SHORT).show();
        } else {  //If it was not measuring, start it
            if (!hasPermissions()) {  //Insist on the permission request
                DialogFragment dialog = new PermissionDialog();
                dialog.show(getSupportFragmentManager(), "permission_dialog");
            }else {  //If already has permissions
                startMeasure();
                showCurrentMeasurement(/*null*/);
                btn.setText(R.string.main_measureStop);
                networkManager.addListener(this);
            }
        }
    }

    private void startMeasure() {
        networkManager.run();
    }

    public void stopMeasure(){
        networkManager.stop();
    }

    private void showLastMeasurement() {
        //currentMeasurementPanel.setVisibility(View.INVISIBLE);
        if (db.signalDao().getSignals().isEmpty()) {
            measurementTitle.setText(getText(R.string.main_getStarted));
            measurementMoment.setText("");
            measurementDBm.setText("");
            measurementBar.setProgress(measurementBar.getMin());
            setProgressColor(measurementBar.getMin());
        }else{
            measurementTitle.setText(getText(R.string.lastMeasurement_title));
            Signal s = db.signalDao().getLastSignal();
            measurementMoment.setText(s.moment);
            measurementDBm.setText(s.dBm + " dBm");
            measurementBar.setProgress(s.dBm);
            setProgressColor(s.dBm);
        }
    }

    private void showCurrentMeasurement(/*Signal signal*/) {
        measurementTitle.setText(getText(R.string.currentMeasurement_cont)+": "+networkManager.getCount());
        Signal s/* = signal*/;
        //if (s == null)  //In case it does not come from onNetworkUpdate
            s = db.signalDao().getLastSignal();
        if (s != null) {
            measurementMoment.setText(s.moment);
            measurementDBm.setText(s.dBm+"");
            measurementBar.setProgress(s.dBm);
            setProgressColor(s.dBm);
        }
    }

    private void setProgressColor(int p){
        if (p < -50) {
            measurementBar.getProgressDrawable().setColorFilter(  //https://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        }else if (p < -30){
            measurementBar.getProgressDrawable().setColorFilter(
                    Color.rgb(255,88,53), android.graphics.PorterDuff.Mode.SRC_IN);
        }else{
            measurementBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public void clearDB(View v){  //Method for debugging purposes only
        db.signalDao().clearSignals();
        Log.d("SignalDB", "All signals have been deleted");
        showLastMeasurement();
    }

    @Override
    public void positiveAnswer() {  //PermissionDialog has accepted the permission request
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void negativeAnswer() {  //PermissionDialog has denied the permission request
        Toast.makeText(this, getString(R.string.dialog_permissions_cancelled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkUpdate(Signal s) {  //A new signal has been created
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showCurrentMeasurement(/*s*/);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        networkManager.removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        measurementTitle = findViewById(R.id.main_measurement_title);
        measurementDBm = findViewById(R.id.main_measurement_dBm);
        measurementMoment = findViewById(R.id.main_measurement_moment);
        measurementBar = findViewById(R.id.main_measurement_bar);

        displayState();

        if (networkManager.isRunning())
            networkManager.addListener(this);
    }

    private boolean hasPermissions(){
        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            return false;
        if (false && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }
}
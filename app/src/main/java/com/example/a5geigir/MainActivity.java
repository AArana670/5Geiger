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

public class MainActivity extends AppCompatActivity implements DialogListener, NetworkListener {


    private boolean hasPermissions = false;
    private AppDatabase db;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        networkManager = NetworkManager.getInstance(this);

        displayState();

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

        if (networkManager.isRunning()) {
            stopMeasure();
            showLastMeasurement();
            btn.setText(R.string.main_measureStart);
            Toast.makeText(this, "Measurement stopped", Toast.LENGTH_SHORT).show();
        } else {
            if (!hasPermissions) {  //Insist on the permission request
                DialogFragment dialog = new PermissionDialog();
                dialog.show(getSupportFragmentManager(), "permission_dialog");
            }else {  //If already has permissions
                startMeasure();
                showCurrentMeasurement();
                btn.setText(R.string.main_measureStop);
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
        CardView card = (CardView) findViewById(R.id.measureDisplay);
        networkManager.removeListener(this);
    }

    private void showCurrentMeasurement() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);
        networkManager.addListener(this);
    }

    public void clearDB(View v){  //Method for debugging purposes only
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
    }

    @Override
    public void negativeAnswer() {  //PermissionDialog has denied the permission request
        Toast.makeText(this, getString(R.string.dialog_permissions_cancelled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkUpdate(Signal s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "New signal: "+s.dBm+" dBm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        networkManager.removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (networkManager.isRunning())
            networkManager.addListener(this);
    }
}
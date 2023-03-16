package com.example.a5geigir;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NetworkManager {

    private static NetworkManager instance = null;
    private Thread reader;
    private ArrayList<NetworkListener> listeners = new ArrayList<NetworkListener>();
    private AppDatabase db;
    private Context context;
    private boolean running = false;
    private int counter = 0;

    private NetworkManager(Context context) {
        this.context = context;
        createReader();

        db = Room.databaseBuilder(
                context,
                AppDatabase.class,
                "signalDB"
        ).allowMainThreadQueries().build();
    }

    private void createReader() {
        reader = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(5000);
                        Signal s = measure();
                        notifyListeners(s);
                    }
                } catch (InterruptedException e) {
                }
            }
        });
    }

    private Signal measure() {
        int cId = (int) Math.floor(Math.random() * 30);
        String moment = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        LocationServices.getFusedLocationProviderClient(context).getLastLocation();
        double ubiLat = 0;
        double ubiLong = 0;

        int dBm = (int) ((Math.random()*-50)-20);
        String type = "5G";
        int freq = (int) ((Math.random()*400)+3400);

        Signal s = new Signal(cId, moment, ubiLat, ubiLong, dBm, type, freq);

        db.signalDao().insertSignal(s);

        counter++;

        Log.d("SignalDB", "Added new; cId: "+s.cId+", moment: "+s.moment+", ubiLat: "+s.ubiLat+", ubiLong: "+ s.ubiLong+", dBm: "+s.dBm);

        return s;
    }

    public static NetworkManager getInstance(Context context){
        if (instance == null){
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public void addListener(NetworkListener l){
        listeners.add(l);
    }

    public void removeListener(NetworkListener l){
        listeners.remove(l);
    }

    private void notifyListeners(Signal s) {
        for (NetworkListener l : listeners){
            l.onNetworkUpdate(s);
        }
    }

    public boolean isRunning(){
        return running;
    }

    public int getCount(){
        return counter;
    }

    public void run(){
        createReader();
        reader.start();
        running = true;
        counter = 0;
    }

    public void stop(){
        reader.interrupt();
        running = false;
    }
}

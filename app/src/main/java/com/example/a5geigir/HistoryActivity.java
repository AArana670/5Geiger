package com.example.a5geigir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView signalRecyler;
    ListAdapter listAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        createList();
    }

    private void createList() {
        signalRecyler = findViewById(R.id.history_list);
        signalRecyler.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "signalDB"
        ).allowMainThreadQueries().build();

        List<Signal> signalList = db.signalDao().getSignals();

        listAdapter = new ListAdapter(signalList,this);

        signalRecyler.setAdapter(listAdapter);
    }

    public void jumpToSettings(View v){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }
}
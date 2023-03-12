package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.a5geigir.db.Signal;

import java.util.List;

public class MeasurementActivity extends AppCompatActivity {

    private List<Signal> signalList;
    private int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
    }

    public void showPrev(View v){
        Toast toast = Toast.makeText(this, "Prev",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showNext(View v){
        Toast toast = Toast.makeText(this, "Next",Toast.LENGTH_SHORT);
        toast.show();
    }
}
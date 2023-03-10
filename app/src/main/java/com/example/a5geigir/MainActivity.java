package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private boolean measuring = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        displayState();
    }

    private void displayState() {
        Button btn = (Button) findViewById(R.id.main_btn);

        if (measuring){
            showLastMeasure();
            btn.setText(R.string.main_measureStart);
        }else{
            showCurrentMeasure();
            btn.setText(R.string.main_measureStop);
        }

    }

    public void jumpToSettings(View v){
        Intent i = new Intent(this, SettingsActivity.class);
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

    }

    private void showLastMeasure() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);
    }

    private void showCurrentMeasure() {
        CardView card = (CardView) findViewById(R.id.measureDisplay);

    }
}
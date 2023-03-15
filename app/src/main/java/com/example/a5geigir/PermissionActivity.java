package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class PermissionActivity extends AppCompatActivity {

    private int step = 1;  //1: Welcome screen,  2: Permission screen
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("com.example.a5Geigir", MODE_PRIVATE);

        /*Locale nuevaloc = new Locale("eu");
        Locale.setDefault(nuevaloc);
        Configuration configuration =
                getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);*/

        /*if (prefs.getBoolean("firstrun", false)) {  //skip this part if it is not the first run
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }*/

        //Remove title bar:  https://www.geeksforgeeks.org/how-to-remove-title-bar-from-the-activity-in-android/
        getSupportActionBar().hide();

        setContentView(R.layout.activity_permission);
    }


    public void jumpToNext(View v){
        if (step == 1){
            //change layout texts to next step (permissions request)
            ((TextView)findViewById(R.id.permission_title)).setText(R.string.permission_title);
            ((TextView)findViewById(R.id.permission_desc)).setText(R.string.permission_msg);
            ((Button)findViewById(R.id.permission_btn)).setText(R.string.permission_btn);

            step ++;
        }else{
            //Request permissions


            //Jump to Main Activity
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

            step = 1;

            //
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }
}
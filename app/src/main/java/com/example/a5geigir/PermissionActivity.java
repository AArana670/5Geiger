package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PermissionActivity extends AppCompatActivity {

    private int step = 1;  //1: Welcome screen,  2: Permission screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        }
    }
}
package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar:  https://www.geeksforgeeks.org/how-to-remove-title-bar-from-the-activity-in-android/
        getSupportActionBar().hide();

        setContentView(R.layout.activity_permission);
    }


    public void jumpToNext(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
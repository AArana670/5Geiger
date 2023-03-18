package com.example.a5geigir.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5geigir.R;
import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;

import org.w3c.dom.Text;

import java.util.List;

public class MeasurementActivity extends AppCompatActivity {

    private final int currentPos = 0;
    private TextView signalDate;
    private TextView signalTime;
    private TextView signalDBm;
    private ProgressBar signalBar;
    private TextView signalCId;
    private TextView signalUbiLat;
    private TextView signalUbiLong;
    private TextView signalFreq;
    private TextView signalType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_measurement);


        Bundle extras = getIntent().getExtras();
        String moment = extras.getString("moment");

        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "signalDB"
        ).allowMainThreadQueries().build();

        Signal s = db.signalDao().getSignalAt(moment);  //Get the data of the selected signal

        signalDate = (TextView) findViewById(R.id.measurement_date);
        signalDate.setText(moment.split(" ")[0]);

        signalTime = (TextView) findViewById(R.id.measurement_time);
        signalTime.setText(moment.split(" ")[1]);

        signalDBm = (TextView) findViewById(R.id.measurement_dBm_value);
        signalDBm.setText(s.dBm+"");

        signalBar = (ProgressBar) findViewById(R.id.measurement_dBm_bar);
        signalBar.setProgress(s.dBm);
        setProgressColor(s.dBm);

        signalCId = (TextView) findViewById(R.id.measurement_cId_value);
        signalCId.setText(s.cId+"");

        signalUbiLat = (TextView) findViewById(R.id.measurement_ubiLat_value);
        signalUbiLat.setText(s.ubiLat+"");

        signalUbiLong = (TextView) findViewById(R.id.measurement_ubiLong_value);
        signalUbiLong.setText(s.ubiLong+"");

        signalFreq = (TextView) findViewById(R.id.measurement_freq_value);
        signalFreq.setText(s.freq+"");

        signalType = (TextView) findViewById(R.id.measurement_type_value);
        signalType.setText(s.type+"");
    }

    public void showPrev(View v){  //Currently unused feature
        Toast toast = Toast.makeText(this, "Prev",Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showNext(View v){  //Currently unused feature
        Toast toast = Toast.makeText(this, "Next",Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setProgressColor(int p){
        if (p < -50) {
            signalBar.getProgressDrawable().setColorFilter(  //https://stackoverflow.com/questions/2020882/how-to-change-progress-bars-progress-color-in-android
                    Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
        }else if (p < -30){
            signalBar.getProgressDrawable().setColorFilter(
                    Color.rgb(255,88,53), android.graphics.PorterDuff.Mode.SRC_IN);
        }else{
            signalBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}
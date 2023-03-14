package com.example.a5geigir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5geigir.db.AppDatabase;
import com.example.a5geigir.db.Signal;

import org.w3c.dom.Text;

import java.util.List;

public class MeasurementActivity extends AppCompatActivity {

    private List<Signal> signalList;
    private int currentPos = 0;
    private TextView signalDate;
    private TextView signalTime;
    private TextView signalDBm;
    private ProgressBar signalBar;
    private TextView signalCId;
    private TextView signalUbiLat;
    private TextView signalUbiLong;

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

        Signal s = db.signalDao().getSignalAt(moment);

        signalDate = (TextView) findViewById(R.id.measurement_date);
        signalDate.setText(moment.split(" ")[0]);

        signalTime = (TextView) findViewById(R.id.measurement_time);
        signalTime.setText(moment.split(" ")[1]);

        signalDBm = (TextView) findViewById(R.id.measurement_dBm_value);
        signalDBm.setText(s.dBm+"");

        signalCId = (TextView) findViewById(R.id.measurement_cId_value);
        signalCId.setText(s.cId+"");

        signalUbiLat = (TextView) findViewById(R.id.measurement_ubiLat_value);
        signalUbiLat.setText(s.ubiLat+"");

        signalUbiLong = (TextView) findViewById(R.id.measurement_ubiLong_value);
        signalUbiLong.setText(s.ubiLong+"");
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
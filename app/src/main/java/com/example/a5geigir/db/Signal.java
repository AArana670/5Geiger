package com.example.a5geigir.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity
public class Signal{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cId")
    public int cId;

    @NonNull
    @ColumnInfo(name = "moment")
    public String moment;

    @ColumnInfo(name = "ubiLat")
    public double ubiLat;

    @ColumnInfo(name = "ubiLong")
    public double ubiLong;

    @ColumnInfo(name = "dBm")
    public int dBm;

    public Signal(@NonNull int cId, @NonNull String moment, double ubiLat, double ubiLong, int dBm) {
        this.cId = cId;
        this.moment = moment;
        this.ubiLat = ubiLat;
        this.ubiLong = ubiLong;
        this.dBm = dBm;
    }

}

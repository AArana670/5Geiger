package com.example.a5geigir.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface SignalDao {

    @Query("SELECT * FROM signal")
    List<Signal> getSignals();

    @Query("SELECT * FROM signal WHERE moment > :moment")
    List<Signal> getSignalsSince(String moment);

    @Insert
    void insertSignal(Signal signal);

    @Query("DELETE FROM signal WHERE moment < :moment")
    void deleteSignalsUntil(String moment);

    @Query("DELETE FROM signal")
    void clearSignals();

}
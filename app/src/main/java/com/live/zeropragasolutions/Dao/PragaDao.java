package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.Praga;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PragaDao {

    @Query("SELECT * FROM Praga")
    public List<Praga> listaPragas();

    @Insert
    public long[] insert(Praga... values);

    @Update
    public int update(Praga... values);

    @Query("SELECT IFNULL(MAX(id),0)+1 FROM Praga")
    public Integer getProximoCodigo();
}

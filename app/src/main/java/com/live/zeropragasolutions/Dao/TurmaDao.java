package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.Turma;

import java.util.List;

@Dao
public interface TurmaDao {

    @Insert
    public long[] insert(Turma... values);

    @Update
    public int update(Turma... values);

    @Query("SELECT * FROM Turma")
    public List<Turma> listaTurmas();

    @Query("SELECT IFNULL(MAX(id),0)+1 FROM Turma")
    public Integer getProximoCodigo();

}

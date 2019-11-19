package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.Estagio;

import java.util.List;

@Dao
public interface EstagioDao {

    @Insert
    public long[] insert(Estagio... values);

    @Update
    public int update(Estagio... values);

    @Query("SELECT * FROM Estagio")
    public List<Estagio> listaEstagios();

    @Query("SELECT IFNULL(MAX(id),0)+1 FROM Estagio")
    public Integer getProximoCodigo();

    @Query("UPDATE Estagio set _status = 'true' WHERE ID = :cod ")
    public int Desativar(Integer cod );

}

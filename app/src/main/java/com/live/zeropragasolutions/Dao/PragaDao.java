package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.Praga;

import java.util.List;

@Dao
public interface PragaDao {

    @Insert
    public long[] insert(Praga... values);

    @Update
    public int update(Praga... values);

    @Query("SELECT * FROM Praga ORDER BY ID")
    public List<Praga> listaPragas();

    @Query("SELECT IFNULL(MAX(id),0)+1 FROM Praga")
    public Integer getProximoCodigo();

    @Query("UPDATE Praga set _status = 'true' WHERE ID = :codPraga ")
    public int Desativar(Integer codPraga );

    @Query("SELECT * FROM Praga WHERE ID = :codPraga ")
    public Praga ObterPorId(Integer codPraga );

}

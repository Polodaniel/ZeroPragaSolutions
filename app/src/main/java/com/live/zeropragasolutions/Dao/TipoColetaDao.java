package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Usuario;

import java.util.List;

@Dao
public interface TipoColetaDao {

    @Insert
    public long[] insert(TipoColeta... values);

    @Update
    public int update(TipoColeta... values);

    @Query("SELECT * FROM TipoColeta ORDER BY ID")
    public List<TipoColeta> listaTipoColeta();

    @Query("SELECT * FROM Praga ORDER BY ID")
    public List<Praga> listaPragaTipoColeta();

    @Query("SELECT IFNULL(MAX(ID),0)+1 FROM TipoColeta")
    public Integer getProximoCodigo();

    @Query("UPDATE TipoColeta set Status = 'true' WHERE ID = :codUser ")
    public int Desativar(Integer codUser );

}

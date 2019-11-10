package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface HomeDao {

    @Query("SELECT COUNT(*) FROM Praga")
    public Integer getContaTodasPrgas();

    @Query("SELECT COUNT(*) FROM Estagio")
    public Integer getContaTodosEstagio();

    @Query("SELECT COUNT(*) FROM TipoColeta")
    public Integer getContaTodosTipoColeta();

    @Query("SELECT COUNT(*) FROM Turma")
    public Integer getContaTodasTurmas();

}

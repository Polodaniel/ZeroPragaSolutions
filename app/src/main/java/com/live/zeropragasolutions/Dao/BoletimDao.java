package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.Boletim;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Turma;

import java.util.List;

@Dao
public interface BoletimDao {

    @Insert
    public long[] insert(Boletim... values);

    @Update
    public int update(Boletim... values);

    @Query("SELECT * FROM Usuario ORDER BY ID")
    public List<Boletim> listaBoletins();

    @Query("SELECT IFNULL(MAX(ID),0)+1 FROM Boletim")
    public Integer getProximoCodigo();

    @Query("UPDATE Boletim set Status = 'true' WHERE ID = :codigo ")
    public int Desativar(Integer codigo );

    @Query("SELECT * FROM Praga ORDER BY ID")
    List<Praga> listaPragas();

    @Query("SELECT * FROM Estagio ORDER BY ID")
    List<Estagio> listaEstagios();

    @Query("SELECT * FROM TipoColeta ORDER BY ID")
    List<TipoColeta> listaTipoCOleta();

    @Query("SELECT * FROM Turma ORDER BY ID")
    List<Turma> listaTurma();

    //
    @Query("SELECT * FROM Praga WHERE _status != 'true' ORDER BY ID")
    List<Praga> listaPragasAtivos();

    @Query("SELECT * FROM Estagio WHERE _status != 'true' ORDER BY ID")
    List<Estagio> listaEstagiosAtivos();

    @Query("SELECT * FROM TipoColeta WHERE Status != 'true' ORDER BY ID")
    List<TipoColeta> listaTipoColetaAtivos();

    @Query("SELECT * FROM Turma WHERE _status != 'true' ORDER BY ID")
    List<Turma> listaTurmaAtivos();

}

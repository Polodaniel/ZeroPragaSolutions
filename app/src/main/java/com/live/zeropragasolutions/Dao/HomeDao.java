package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.live.zeropragasolutions.Model.Boletim;

import java.util.List;

@Dao
public interface HomeDao {

    @Query("SELECT COUNT(*) FROM Praga WHERE _status != 'true' ")
    public Integer getContaTodasPrgas();

    @Query("SELECT COUNT(*) FROM Estagio WHERE _status != 'true' ")
    public Integer getContaTodosEstagio();

    @Query("SELECT COUNT(*) FROM TipoColeta WHERE Status != 'true' ")
    public Integer getContaTodosTipoColeta();

    @Query("SELECT COUNT(*) FROM Turma WHERE _status != 'true' ")
    public Integer getContaTodasTurmas();

    @Query("SELECT COUNT(*) FROM Boletim WHERE Status != 'true' ")
    public Integer getContaTodosBoletins();

    @Query("SELECT * FROM Boletim ORDER BY ID")
    public List<Boletim> listaBoletins();

    @Query("SELECT * FROM Boletim ORDER BY ID")
    public List<Boletim> listaBoletinsADM();

    @Query("SELECT * FROM Boletim WHERE CodigoFiscal = :CodFuncionario ORDER BY ID")
    public List<Boletim> listaBoletinsFiscal(Integer CodFuncionario);

    @Query("UPDATE Boletim set Status = 'true' WHERE ID = :codBoletim ")
    public int Desativar(Integer codBoletim );

}

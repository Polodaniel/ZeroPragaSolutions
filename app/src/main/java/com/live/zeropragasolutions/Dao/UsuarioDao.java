package com.live.zeropragasolutions.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.live.zeropragasolutions.Model.ObjetoLogin;
import com.live.zeropragasolutions.Model.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    public long[] insert(Usuario... values);

    @Update
    public int update(Usuario... values);

    @Query("SELECT * FROM Usuario")
    public List<Usuario> listaUsuarios();

    @Query("SELECT IFNULL(MAX(ID),0)+1 FROM Usuario")
    public Integer getProximoCodigo();

    @Query("SELECT * FROM Usuario WHERE Login = :user AND Senha = :psd")
    public Usuario buscaUsuario(String user ,String psd);

    @Query("UPDATE Usuario set Status = 'true' WHERE ID = :codUser ")
    public int Desativar(Integer codUser );

}

package com.live.zeropragasolutions.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Estagio implements Serializable
{
    @Ignore
    public static final String EXTRA_NAME = "ESTAGIO";

    public Estagio() {}

    public Estagio(Integer Id, String nome, String descricao) {
        ID = Id;
        Nome = nome;
        Descricao = descricao;
        _status = true;
    }

    @PrimaryKey(autoGenerate = true)
    public Integer ID;

    @ColumnInfo
    public String Nome;

    @ColumnInfo
    public String Descricao;

    @ColumnInfo
    public Integer PragaId;

    @ColumnInfo
    private boolean _status;


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Integer getPragaId() { return PragaId; }

    public void setPragaId(Integer pragaId){ PragaId = pragaId; }

    public String getStatus(){

            if(_status == true)
                return "Ativo";
            else
                return "Inativo";

        }

    public boolean is_status() {
        return _status;
    }

    public void set_status(boolean _status) {
        this._status = _status;
    }
}

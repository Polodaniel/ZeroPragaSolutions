package com.live.zeropragasolutions.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "TipoColeta")
public class TipoColeta implements Serializable {

    public static final String EXTRA_NAME = "TIPOCOLETA";

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo
    private String Nome;

    @ColumnInfo
    private String Descricao;

    @ColumnInfo
    private Integer idPraga;

    public String getNomePraga() {
        return NomePraga;
    }

    public void setNomePraga(String nomePraga) {
        NomePraga = nomePraga;
    }

    private String NomePraga;

    public Integer getIdPraga() {
        return idPraga;
    }

    public void setIdPraga(Integer idPraga) {
        this.idPraga = idPraga;
    }

    @ColumnInfo
    private boolean Status;

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TipoColeta() {
    }

    public String getStatus(){

        if(Status == true)
            return "Ativo";
        else
            return "Inativo";

    }
}

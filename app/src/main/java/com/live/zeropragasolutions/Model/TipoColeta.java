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
    private Long idPraga;

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

    public Long getIdPraga() {
        return idPraga;
    }

    public void setIdPraga(Long idPraga) {
        this.idPraga = idPraga;
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

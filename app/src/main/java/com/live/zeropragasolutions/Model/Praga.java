package com.live.zeropragasolutions.Model;

import java.io.Serializable;

public class Praga implements Serializable
{
    public static final String EXTRA_NAME = "PRAGA";

    public Praga() {}

    public Praga(Integer Id, String nome, String descricao) {
        ID = Id;
        Nome = nome;
        Descricao = descricao;
        _status = true;
    }

    public Integer ID;

    public String Nome;

    public String Descricao;

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

    public String getStatus(){

            if(_status == true)
                return "Ativo";
            else
                return "Inativo";

        }

}

package com.live.zeropragasolutions.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Usuario implements Serializable {

    public static final String EXTRA_NAME = "USUARIO";
    @PrimaryKey(autoGenerate = true)
    private Integer ID;

    @ColumnInfo
    private String Nome;

    @ColumnInfo
    private String DataNascimento;

    @ColumnInfo
    private String RG;

    @ColumnInfo
    private String CPF;

    @ColumnInfo
    private String Login;

    @ColumnInfo
    private String Senha;

    @ColumnInfo
    private String Email;

    @ColumnInfo
    private Integer TipoConta;

    @ColumnInfo
    private boolean Status;


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

    public String getDataNascimento() {
        return DataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        DataNascimento = dataNascimento;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getTipoConta() {
        return TipoConta;
    }

    public void setTipoConta(Integer tipoConta) {
        TipoConta = tipoConta;
    }

    public String getStatus(){

        if(Status == true)
            return "Ativo";
        else
            return "Inativo";

    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}

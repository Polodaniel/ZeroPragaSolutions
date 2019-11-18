package com.live.zeropragasolutions.Model;

import android.widget.DatePicker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Boletim implements Serializable {

    public static final String EXTRA_NAME = "BOLETIM";

    @PrimaryKey(autoGenerate = true)
    private Integer ID;

    @ColumnInfo
    private boolean Status;

    @ColumnInfo
    private Double Latitude;

    @ColumnInfo
    private Double Longitude;

    @ColumnInfo
    private Double Altitude;

    @ColumnInfo
    private String Data;

    @ColumnInfo
    private Integer CodigoFiscal;

    @ColumnInfo
    private String Fiscal;

    @ColumnInfo
    private Integer CodigoTurma;

    @ColumnInfo
    private String Turma;

    @ColumnInfo
    private Integer CodigoPraga;

    @ColumnInfo
    private String NomePraga;

    @ColumnInfo
    private Integer Quantidade;

    @ColumnInfo
    private Integer CodigoEstagio;

    @ColumnInfo
    private String Estagio;

    @ColumnInfo
    private Integer CodigoTipoColeta;

    @ColumnInfo
    private String TipoColeta;

    @ColumnInfo
    private byte[] FotoPragaTirada;

    public Boletim(Integer iD, boolean status, Double latitude, Double longitude, Double altitude, String data,
                   Integer codigoFiscal, String fiscal, Integer codigoTurma, String turma, Integer codigoPraga,
                   String nomePraga, Integer quantidade, Integer codigoEstagio, String estagio, Integer codigoTipoColeta,
                   String tipoColeta, byte[] fotoPragaTirada) {

        this.ID = iD;
        Status = status;
        Latitude = latitude;
        Longitude = longitude;
        Altitude = altitude;
        Data = data;
        CodigoFiscal = codigoFiscal;
        Fiscal = fiscal;
        CodigoTurma = codigoTurma;
        Turma = turma;
        CodigoPraga = codigoPraga;
        NomePraga = nomePraga;
        Quantidade = quantidade;
        CodigoEstagio = codigoEstagio;
        Estagio = estagio;
        CodigoTipoColeta = codigoTipoColeta;
        TipoColeta = tipoColeta;
        FotoPragaTirada = fotoPragaTirada;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getAltitude() {
        return Altitude;
    }

    public void setAltitude(Double altitude) {
        Altitude = altitude;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public Integer getCodigoFiscal() {
        return CodigoFiscal;
    }

    public void setCodigoFiscal(Integer codigoFiscal) {
        CodigoFiscal = codigoFiscal;
    }

    public String getFiscal() {
        return Fiscal;
    }

    public void setFiscal(String fiscal) {
        Fiscal = fiscal;
    }

    public Integer getCodigoTurma() {
        return CodigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        CodigoTurma = codigoTurma;
    }

    public String getTurma() {
        return Turma;
    }

    public void setTurma(String turma) {
        Turma = turma;
    }

    public Integer getCodigoPraga() {
        return CodigoPraga;
    }

    public void setCodigoPraga(Integer codigoPraga) {
        CodigoPraga = codigoPraga;
    }

    public String getNomePraga() {
        return NomePraga;
    }

    public void setNomePraga(String nomePraga) {
        NomePraga = nomePraga;
    }

    public Integer getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        Quantidade = quantidade;
    }

    public Integer getCodigoEstagio() {
        return CodigoEstagio;
    }

    public void setCodigoEstagio(Integer codigoEstagio) {
        CodigoEstagio = codigoEstagio;
    }

    public String getEstagio() {
        return Estagio;
    }

    public void setEstagio(String estagio) {
        Estagio = estagio;
    }

    public Integer getCodigoTipoColeta() {
        return CodigoTipoColeta;
    }

    public void setCodigoTipoColeta(Integer codigoTipoColeta) {
        CodigoTipoColeta = codigoTipoColeta;
    }

    public String getTipoColeta() {
        return TipoColeta;
    }

    public void setTipoColeta(String tipoColeta) {
        TipoColeta = tipoColeta;
    }

    public byte[] getFotoPragaTirada() {
        return FotoPragaTirada;
    }

    public void setFotoPragaTirada(byte[] fotoPragaTirada) {
        FotoPragaTirada = fotoPragaTirada;
    }

    public Boletim() {
    }

    public String getStatus(){

        if(Status == true)
            return "Ativo";
        else
            return "Inativo";

    }
}

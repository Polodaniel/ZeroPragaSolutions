package com.live.zeropragasolutions.Auxiliares;

public class Utilidades {

    public String FormataCodigo(int codigo)
    {
        String Codigo = String.valueOf(codigo);

        String CodigoFormatado = "";

        CodigoFormatado = "000000".substring(Codigo.length()) + Codigo;

        return CodigoFormatado;
    }
}

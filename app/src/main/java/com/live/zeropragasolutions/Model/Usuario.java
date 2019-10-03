package com.live.zeropragasolutions.Model;

public class Usuario {

    public Usuario() {}

    public Usuario(Integer codUsuario, String nomeUsuario, String loginUsuario, String senha, Integer tipoConta) {
        _codUsuario = codUsuario;
        _nomeUsuario = nomeUsuario;
        _loginUsuario = loginUsuario;
        _senha = senha;
        _tipoConta = tipoConta;
    }

    private Integer _codUsuario;

    private String _nomeUsuario;

    private String _loginUsuario;

    private String _senha;

    public Integer get_tipoConta() {
        return _tipoConta;
    }

    public void set_tipoConta(Integer _tipoConta) {
        this._tipoConta = _tipoConta;
    }

    private Integer _tipoConta;

    public boolean StatusRetorno;

    public String get_loginUsuario() {
        return _loginUsuario;
    }

    public String get_senha() {
        return _senha;
    }

    public void setStatusRetorno(boolean statusRetorno) {
        StatusRetorno = statusRetorno;
    }

    public boolean getStatusRetorno() {
        return StatusRetorno;
    }
}

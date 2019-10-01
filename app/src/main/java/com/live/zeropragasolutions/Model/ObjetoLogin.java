package com.live.zeropragasolutions.Model;

import java.io.Serializable;

public class ObjetoLogin implements Serializable {

    public static final String EXTRA_NAME = "ObjetoLogin";
    private int _tipoConta;
    private String _stringLogin;


    public ObjetoLogin() {
    }

    public ObjetoLogin(int _tipoConta, String _stringLogin) {
        this._tipoConta = _tipoConta;
        this._stringLogin = _stringLogin;
    }

    public int get_tipoConta() {
        return _tipoConta;
    }
}

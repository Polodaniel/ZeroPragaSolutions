
package com.live.zeropragasolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.live.zeropragasolutions.Model.ObjetoLogin;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ATUALIZACAO_LOGIN = 1;

    private AppCompatEditText txtLogin;
    private AppCompatEditText txtSenha;
    private AppCompatButton   btnLogar;
    private AppCompatButton   btnLimpar;
    private AppCompatTextView lblErroEmail;
    private AppCompatTextView lblErroSenha;

    ObjetoLogin retornoLogin = new ObjetoLogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        inicializaComponenetes();
        inicializaEventos();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                // Busca Login
                // Valida Lgin

                Boolean Valido = true;

                // User
                retornoLogin = new ObjetoLogin(1,"900150983cd24fb0d6963f7d28e17f72");

                // Admin
                //retornoLogin = new ObjetoLogin(0,"900150983cd24fb0d6963f7d28e17f72");

                if(Valido == true)
                {
                    retornoLogin.get_tipoConta();
                    AbreTelaPrincipal();
                }
                else
                {

                }

                break;
            case R.id.btnLimpar:
                limparComponentes();
                break;
        }
    }

    @SuppressLint("WrongViewCast")
    private void inicializaComponenetes() {
        btnLogar = findViewById(R.id.btnLogin);
        btnLimpar = findViewById(R.id.btnLimpar);
        txtLogin = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        lblErroEmail = findViewById(R.id.msgEmailErro);
        lblErroSenha = findViewById(R.id.msgSenhaErro);
    }

    private void inicializaEventos() {
        btnLogar.setOnClickListener(this);
        btnLimpar.setOnClickListener(this);
    }

    private void limparComponentes()
    {
        txtLogin.setText("");
        txtSenha.setText("");
        lblErroEmail.setVisibility(View.INVISIBLE);
        lblErroSenha.setVisibility(View.INVISIBLE);

        txtLogin.requestFocus();
    }


    private void AbreTelaPrincipal()
    {
        Intent telaPrincipal = new Intent(this, HomeActivity.class);
        telaPrincipal.putExtra(ObjetoLogin.EXTRA_NAME, retornoLogin);
        startActivityForResult(telaPrincipal, REQUEST_ATUALIZACAO_LOGIN);
    }

}

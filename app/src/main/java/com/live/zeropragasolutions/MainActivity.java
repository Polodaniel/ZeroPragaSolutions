
package com.live.zeropragasolutions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.live.zeropragasolutions.Activity.UsuarioActivity;
import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.ObjetoLogin;
import com.live.zeropragasolutions.Model.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_ATUALIZACAO_LOGIN = 1;

    private AppCompatEditText txtLogin;
    private AppCompatEditText txtSenha;
    private AppCompatButton   btnLogar;
    private AppCompatButton   btnLimpar;
    private AppCompatTextView lblErroEmail;
    private AppCompatTextView lblErroSenha;
    private CardView cardCriarUsuario;
    private TextView lblCriaUsuario;

    Usuario retornoLogin;

    List<Usuario> user = new ArrayList<Usuario>();

    UsuarioDao contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        inicializaComponenetes();

        validaQtdeUsuario();

        inicializaEventos();

    }

    private void validaQtdeUsuario() {

        List<Usuario> users = contexto.listaUsuarios();

        if(users.size() <= 0)
        {
            cardCriarUsuario.setVisibility(View.VISIBLE);
        }
        else
        {
            cardCriarUsuario.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btnLogin:

                String user = txtLogin.getText().toString();
                String psd = txtSenha.getText().toString();

                retornoLogin = contexto.buscaUsuario(user,psd);

                if(retornoLogin.getStatus() == "Inativo") {
                    Mensagens.mostraMensagem(this, R.string.ContaInativa);

                    break;
                }

                if(retornoLogin != null)
                    AbreTelaPrincipal();
                else
                    Mensagens.mostraMensagem(this, R.string.LoginErro);

                break;
            case R.id.btnLimpar:
                limparComponentes();
                break;

            case R.id.lblCriaNovoUsuario:
                AbreTelaCriarUsuario();
                break;
        }
    }

    @SuppressLint("WrongViewCast")
    private void inicializaComponenetes()
    {
        btnLogar = findViewById(R.id.btnLogin);
        btnLimpar = findViewById(R.id.btnLimpar);
        txtLogin = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        lblErroEmail = findViewById(R.id.msgEmailErro);
        lblErroSenha = findViewById(R.id.msgSenhaErro);
        lblCriaUsuario = findViewById(R.id.lblCriaNovoUsuario);

        cardCriarUsuario = findViewById(R.id.cardCriarUsuario);

        contexto = AppDataBase.getInstance(this).getUsuarioDao();
    }

    private void inicializaEventos()
    {
        btnLogar.setOnClickListener(this);
        btnLimpar.setOnClickListener(this);
        lblCriaUsuario.setOnClickListener(this);

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
        telaPrincipal.putExtra(Usuario.EXTRA_NAME, retornoLogin);
        startActivityForResult(telaPrincipal, REQUEST_ATUALIZACAO_LOGIN);
    }

    private void AbreTelaCriarUsuario()
    {
        Intent tela = new Intent(this, UsuarioActivity.class);
        startActivity(tela);
    }
}


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

import com.live.zeropragasolutions.Auxiliares.Mensagens;
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

    ObjetoLogin retornoLogin = new ObjetoLogin();

    List<Usuario> user = new ArrayList<Usuario>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        inicializaComponenetes();
        inicializaEventos();

        CriaUsuarioTeste();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btnLogin:

                String Login = txtLogin.getText().toString();
                String Senha = txtSenha.getText().toString();

                Usuario Valido = BuscaUsuario(Login,Senha);

                if(Valido != null) {
                    if (Valido.getStatusRetorno() == true) {
                        retornoLogin.set_tipoConta(Valido.get_tipoConta());
                        AbreTelaPrincipal();
                        finish();
                    } else {
                        Mensagens.mostraMensagem(this, R.string.UsuarioErro);
                    }
                }
                else {

                    Mensagens.mostraMensagem(this, R.string.UsuarioErro);
                }

                break;
            case R.id.btnLimpar:
                limparComponentes();
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
    }

    private void inicializaEventos()
    {
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

    private void CriaUsuarioTeste()
    {
        Usuario newUser1 = new Usuario();
        Usuario newUser2 = new Usuario();

        newUser1 = new Usuario(0,"Daniel","user","user",0);
        newUser2 = new Usuario(1,"Jessica","adm","adm",1);

        user.add(newUser1);
        user.add(newUser2);
    }

    private Usuario BuscaUsuario(String Login , String Senha)
    {
        for(Usuario x : user) {

            String l = x.get_loginUsuario();
            String s = x.get_senha();

            if(l.equals(Login) && s.equals(Senha)) {
                x.setStatusRetorno(true);
                return x;
            }
        }

        return null;
    }
}

package com.live.zeropragasolutions.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.Usuario;
import com.live.zeropragasolutions.R;

import java.io.File;
import java.io.Serializable;

public class UsuarioNewActivity extends AppCompatActivity {

    private FloatingActionButton btnSalvar;
    private FloatingActionButton btnSelecionarImagem;
    private FloatingActionButton btnExcluirImagem;
    private FloatingActionButton btnMenu;

    private EditText txtCodigo;
    private EditText txtNome;
    private EditText txtDataNascimento;
    private EditText txtRG;
    private EditText txtCPF;
    private EditText txtLogin;
    private EditText txtSenha;
    private EditText txtEmail;
    private RadioButton ckbTipoContaFiscal;
    private RadioButton ckbTipoContaAdm;

    private int indiceMenu = 0;

    private Usuario usuario;
    private UsuarioDao contexto = AppDataBase.getInstance(this).getUsuarioDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InicializaComponentes();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Salvar();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirFecharMenu();
            }
        });

        ckbTipoContaFiscal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBoxFiscal();
            }

            private void CheckBoxFiscal() {

                if (ckbTipoContaFiscal.isChecked())
                    ckbTipoContaAdm.setChecked(false);
            }
        });

        ckbTipoContaAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBoxAdm();
            }

            private void CheckBoxAdm() {
                if (ckbTipoContaAdm.isChecked())
                    ckbTipoContaFiscal.setChecked(false);
            }
        });

        carregaInformacoesPassadasPorParametro();
    }

    private void InicializaComponentes() {
        btnSalvar = findViewById(R.id.fab);
        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        btnExcluirImagem = findViewById(R.id.btnRemoverImagem);
        btnMenu = findViewById(R.id.btnMenu);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNome = findViewById(R.id.txtNome);
        txtDataNascimento = findViewById(R.id.txtDataNascimento);
        txtRG = findViewById(R.id.txtRG);
        txtCPF = findViewById(R.id.txtCPF);
        txtLogin = findViewById(R.id.txtLogin);
        txtSenha = findViewById(R.id.txtSenha);
        txtEmail = findViewById(R.id.txtEmail);
        ckbTipoContaFiscal = findViewById(R.id.ckbFiscal);
        ckbTipoContaAdm = findViewById(R.id.ckbAdm);
    }

    private void Salvar() {

        boolean resultado = false;

        Usuario meuUsuario;

        // Cria uma Nova Praga
        if (usuario == null) {

            meuUsuario = new Usuario();

            meuUsuario.setNome(txtNome.getText().toString());
            meuUsuario.setNome(txtNome.getText().toString());
            meuUsuario.setDataNascimento(txtDataNascimento.getText().toString());
            meuUsuario.setRG(txtRG.getText().toString());
            meuUsuario.setCPF(txtCPF.getText().toString());
            meuUsuario.setLogin(txtLogin.getText().toString());
            meuUsuario.setSenha(txtSenha.getText().toString());
            meuUsuario.setEmail(txtEmail.getText().toString());

            if (ckbTipoContaFiscal.isChecked())
                meuUsuario.setTipoConta(0);

            if (ckbTipoContaAdm.isChecked())
                meuUsuario.setTipoConta(1);

            meuUsuario.setStatus(true);

            if (ValidaCampos(meuUsuario)) {
                long[] retorno = contexto.insert(meuUsuario);

                // Verficica se Salvou de forma correta
                if (retorno.length > 0) {
                    resultado = true;
                    meuUsuario.setID((int) retorno[0]);
                }
            } else {
                resultado = false;
            }


        } else {
            // Atualizar uma Praga já existente

            usuario.setNome(txtNome.getText().toString());
            usuario.setDataNascimento(txtDataNascimento.getText().toString());
            usuario.setRG(txtRG.getText().toString());
            usuario.setCPF(txtCPF.getText().toString());
            usuario.setLogin(txtLogin.getText().toString());
            usuario.setSenha(txtSenha.getText().toString());
            usuario.setEmail(txtEmail.getText().toString());
            usuario.setStatus(true);

            if (ckbTipoContaFiscal.isChecked())
                usuario.setTipoConta(0);

            if (ckbTipoContaAdm.isChecked())
                usuario.setTipoConta(1);

            meuUsuario = usuario;

            if (ValidaCampos(meuUsuario)) {
                int retorno = contexto.update(usuario);

                // Verficica se Salvou de forma correta
                if (retorno > 0)
                    resultado = true;
            }
        }


        if (resultado) {
            Mensagens.mostraMensagem(this, R.string.SalvarSucesso);

            Intent data = new Intent();
            data.putExtra(Usuario.EXTRA_NAME, meuUsuario);
            //
            setResult(RESULT_OK, data);

            this.finish();
        } else {
            Mensagens.mostraMensagem(this, R.string.SalvarErro);
        }

    }

    private boolean ValidaCampos(Usuario user) {

        boolean retorno = true;

        Integer Zero = 0;
        Integer Um = 1;
        Integer TipoConta = user.getTipoConta().intValue();

        if (user.getNome().isEmpty())
            return false;
        else if (user.getLogin().isEmpty())
            return false;
        else if (user.getSenha().isEmpty())
            return false;
        else if (user.getDataNascimento().isEmpty())
            return false;
        else if (user.getEmail().isEmpty())
            return false;
        else if (TipoConta != Zero && TipoConta != Um)
            return false;

        return retorno;
    }

    @SuppressLint("RestrictedApi")
    private void AbrirFecharMenu() {
        if (indiceMenu == 0) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSalvar.setVisibility(View.VISIBLE);
                }
            }, 50);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnExcluirImagem.setVisibility(View.VISIBLE);
                }
            }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSelecionarImagem.setVisibility(View.VISIBLE);
                }
            }, 150);

            indiceMenu = 1;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSelecionarImagem.setVisibility(View.INVISIBLE);
                }
            }, 50);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnExcluirImagem.setVisibility(View.INVISIBLE);
                }
            }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSalvar.setVisibility(View.INVISIBLE);
                }
            }, 150);

            indiceMenu = 0;
        }
    }

    private void carregaInformacoesPassadasPorParametro() {
        Serializable objetoPassado = getIntent().getSerializableExtra(Usuario.EXTRA_NAME);
        if (objetoPassado != null) {
            usuario = (Usuario) objetoPassado;
            carregaInformacoesParaAtualizacao();
        } else {
            buscaProximoCodigo();
        }
    }

    private void buscaProximoCodigo() {

        txtCodigo.setEnabled(false);

        txtCodigo.setText(contexto.getProximoCodigo().toString());
    }

    private void carregaInformacoesParaAtualizacao() {

        txtCodigo.setEnabled(false);

        txtCodigo.setText(usuario.getID().toString());
        txtNome.setText(usuario.getNome().toString());
        txtDataNascimento.setText(usuario.getDataNascimento().toString());
        txtRG.setText(usuario.getRG().toString());
        txtCPF.setText(usuario.getCPF().toString());
        txtLogin.setText(usuario.getLogin().toString());
        txtSenha.setText(usuario.getSenha().toString());
        txtEmail.setText(usuario.getEmail().toString());

        if (usuario.getTipoConta() == 0)
            ckbTipoContaFiscal.setChecked(true);

        if (usuario.getTipoConta() == 1)
            ckbTipoContaAdm.setChecked(true);

    }

}

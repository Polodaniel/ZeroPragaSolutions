package com.live.zeropragasolutions.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.Auxiliares.Utilidades;
import com.live.zeropragasolutions.Dao.TipoColetaDao;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Turma;
import com.live.zeropragasolutions.Model.Usuario;
import com.live.zeropragasolutions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TipoColetaNewActivity extends AppCompatActivity {

    private FloatingActionButton btnSalvar;
    private FloatingActionButton btnSelecionarImagem;
    private FloatingActionButton btnExcluirImagem;
    private FloatingActionButton btnMenu;

    private Spinner spinnerPraga;

    private EditText txtCodigo;
    private EditText txtNome;
    private EditText txtDescricao;

    private int indiceMenu = 0;

    private TipoColeta tipoColeta;
    private TipoColetaDao contexto = AppDataBase.getInstance(this).getTipoColetaDao();

    private List<Praga> listaPraga = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_coleta_new);
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

        carregaLisraPragas();

        carregaInformacoesPassadasPorParametro();
    }

    private void InicializaComponentes() {
        btnSalvar = findViewById(R.id.fab);
        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        btnExcluirImagem = findViewById(R.id.btnRemoverImagem);
        btnMenu = findViewById(R.id.btnMenu);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNome = findViewById(R.id.txtNome);
        txtDescricao = findViewById(R.id.txtDescricao);
        spinnerPraga = findViewById(R.id.cbbPraga);
    }

    private void Salvar() {

        boolean resultado = false;

        TipoColeta meuTipoColeta;

        // Cria uma Nova Praga
        if (tipoColeta == null) {

            meuTipoColeta = new TipoColeta();

            meuTipoColeta.setNome(txtNome.getText().toString());

            meuTipoColeta.setDescricao(txtDescricao.getText().toString());

            String PragaSelecionada = spinnerPraga.getSelectedItem().toString();

            Integer CodigoPragaInt = Integer.parseInt(PragaSelecionada.substring(0,6));

            meuTipoColeta.setIdPraga(CodigoPragaInt);

            meuTipoColeta.setNomePraga(PragaSelecionada.substring(9));

            meuTipoColeta.setStatus(true);

            if (ValidaCampos(meuTipoColeta)) {
                long[] retorno = contexto.insert(meuTipoColeta);

                // Verficica se Salvou de forma correta
                if (retorno.length > 0) {
                    resultado = true;
                    meuTipoColeta.setId((int) retorno[0]);
                }
            } else {
                resultado = false;
            }


        } else {
            // Atualizar uma Praga já existente

            tipoColeta.setNome(txtNome.getText().toString());
            tipoColeta.setDescricao(txtDescricao.getText().toString());
            tipoColeta.setStatus(true);

            String PragaSelecionada = spinnerPraga.getSelectedItem().toString();

            Integer CodigoPragaInt = Integer.parseInt(PragaSelecionada.substring(0,6));

            tipoColeta.setIdPraga(CodigoPragaInt);

            tipoColeta.setNomePraga(PragaSelecionada.substring(9));

            meuTipoColeta = tipoColeta;

            if (ValidaCampos(meuTipoColeta)) {
                int retorno = contexto.update(tipoColeta);

                // Verficica se Salvou de forma correta
                if (retorno > 0)
                    resultado = true;
            }
        }


        if (resultado) {
            Mensagens.mostraMensagem(this, R.string.SalvarSucesso);

            Intent data = new Intent();
            data.putExtra(TipoColeta.EXTRA_NAME, meuTipoColeta);
            //
            setResult(RESULT_OK, data);

            this.finish();
        } else {
            Mensagens.mostraMensagem(this, R.string.SalvarErro);
        }

    }

    private boolean ValidaCampos(TipoColeta tipoColeta) {

        if (tipoColeta.getNome().isEmpty())
            return false;
        else if (tipoColeta.getDescricao().isEmpty())
            return false;
        else if(tipoColeta.getIdPraga().toString().isEmpty())
            return false;
        else if(tipoColeta.getNomePraga().isEmpty())
            return false;

        return true;
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
        Serializable objetoPassado = getIntent().getSerializableExtra(TipoColeta.EXTRA_NAME);
        if (objetoPassado != null) {
            tipoColeta = (TipoColeta) objetoPassado;
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

        txtCodigo.setText(tipoColeta.getId().toString());
        txtNome.setText(tipoColeta.getNome().toString());
        txtDescricao.setText(tipoColeta.getDescricao().toString());


    }

    private void carregaLisraPragas() {

        listaPraga = contexto.listaPragaTipoColeta();

        List<String> listaPragasComboBox = new ArrayList<String>();

        String Codigo;

        for (Praga praga : listaPraga) {

            Codigo = new Utilidades().FormataCodigo(praga.getID());

            listaPragasComboBox.add(Codigo + " - " + praga.getNome());
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(TipoColetaNewActivity.this, android.R.layout.simple_spinner_dropdown_item, listaPragasComboBox);

        spinnerPraga.setAdapter(adapterSpinner);


    }
}

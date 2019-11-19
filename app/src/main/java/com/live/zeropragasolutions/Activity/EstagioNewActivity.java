package com.live.zeropragasolutions.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.MainActivity;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.R;
import com.live.zeropragasolutions.SplashScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EstagioNewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private List<Praga> listaPragas;

    private FloatingActionButton btnSalvar;
    private FloatingActionButton btnSelecionarImagem;
    private FloatingActionButton btnExcluirImagem;
    private FloatingActionButton btnMenu;
    private ImageView ImagemTirada;
    private EditText txtCodigo;
    private EditText txtNome;
    private EditText txtDescricao;
    private Spinner spPragas;
    private Bitmap img;
    private int indiceMenu = 0;

    private Estagio estagio;
    private Praga pragaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estagio_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InicializaComponentes();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarEstagio();
            }
        });

        btnSelecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirFecharMenu();
            }
        });

        carregaInformacoesPassadasPorParametro();

        carregaInformacoes();
    }

    private void InicializaComponentes() {
        btnSalvar = findViewById(R.id.fab);
        btnSelecionarImagem = findViewById(R.id.btnSelecionarImagem);
        btnExcluirImagem = findViewById(R.id.btnRemoverImagem);
        btnMenu = findViewById(R.id.btnMenu);
        ImagemTirada = (ImageView) findViewById(R.id.imgPraga);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNome = findViewById(R.id.txtNome);
        txtDescricao = findViewById(R.id.txtDescricao);
        spPragas = findViewById(R.id.spPragas);
    }

    private void SalvarEstagio() {

        boolean resultado = false;

        Estagio meuEstagio;

        // Cria uma nodo Estagio
        if (estagio == null) {

            meuEstagio = new Estagio();
            meuEstagio.setNome(txtNome.getText().toString());
            meuEstagio.setDescricao(txtDescricao.getText().toString());
            meuEstagio.setPragaId(pragaSelecionada.ID);
            meuEstagio.set_status(true);

            if (ValidaCampos(meuEstagio)) {
                long[] retorno = AppDataBase.getInstance(this).getEstagioDao().insert(meuEstagio);

                // Verficica se Salvou de forma correta
                if (retorno.length > 0) {
                    resultado = true;
                    meuEstagio.setID((int) retorno[0]);
                }
            }
            else {
                resultado = false;
            }

        } else {
            // Atualizar uma Praga já existente

            estagio.setNome(txtNome.getText().toString());
            estagio.setDescricao(txtDescricao.getText().toString());
            estagio.setPragaId(pragaSelecionada.ID);
            estagio.set_status(true);

            meuEstagio = estagio;

            if (ValidaCampos(meuEstagio)) {
                int retorno = AppDataBase.getInstance(this).getEstagioDao().update(estagio);

                // Verficica se Salvou de forma correta
                if (retorno > 0)
                    resultado = true;
            }
        }


        if (resultado) {
            Mensagens.mostraMensagem(this, R.string.SalvarSucesso);

            Intent data = new Intent();
            data.putExtra(Estagio.EXTRA_NAME, meuEstagio);
            //
            setResult(RESULT_OK, data);

            this.finish();
        }
        else
        {
            Mensagens.mostraMensagem(this, R.string.SalvarErro);
        }

    }

    private boolean ValidaCampos(Estagio meuEstagio) {
        if (meuEstagio.getNome().isEmpty())
            return false;
        else if (meuEstagio.getDescricao().isEmpty())
            return false;

        return true;

    }

    public void tirarFoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        //espera o resultado
        startActivityForResult(intent, 0);
    }

    @SuppressLint("RestrictedApi")
    private void AbrirFecharMenu() {
        if (indiceMenu == 0) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSalvar.setVisibility(View.VISIBLE);
                }
            }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnExcluirImagem.setVisibility(View.VISIBLE);
                }
            }, 300);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSelecionarImagem.setVisibility(View.VISIBLE);
                }
            }, 500);

            indiceMenu = 1;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSelecionarImagem.setVisibility(View.INVISIBLE);
                }
            }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnExcluirImagem.setVisibility(View.INVISIBLE);
                }
            }, 300);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSalvar.setVisibility(View.INVISIBLE);
                }
            }, 500);

            indiceMenu = 0;
        }
    }

    private void carregaInformacoesPassadasPorParametro() {
        Serializable objetoPassado = getIntent().getSerializableExtra(Estagio.EXTRA_NAME);
        if (objetoPassado != null) {
            estagio = (Estagio) objetoPassado;
            carregaInformacoesParaAtualizacao();
        } else {
            buscaProximoCodigo();
        }
    }

    private void carregaInformacoes() {
        listaPragas = AppDataBase.getInstance(this).getPragaDao().listaPragas();
        List<String> opcoes = new ArrayList<String>();
        for (Praga praga: listaPragas){
            opcoes.add(praga.Nome);
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcoes);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPragas.setOnItemSelectedListener(this);
        spPragas.setAdapter(adaptador);
    }

    private void buscaProximoCodigo() {

        txtCodigo.setEnabled(false);

        txtCodigo.setText(AppDataBase.getInstance(this).getEstagioDao().getProximoCodigo().toString());
    }

    private void carregaInformacoesParaAtualizacao() {
        txtCodigo.setEnabled(false);
        txtCodigo.setText(estagio.getID().toString());
        txtNome.setText(estagio.getNome());
        txtDescricao.setText(estagio.getDescricao());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                img = (Bitmap) bundle.get("data");

                ImagemTirada.setImageBitmap(img);

                //chgama o metodo e pega o URI da imagem
                Uri uri = (Uri) getImageUri(getApplicationContext(), img);

                //chama o metodo e pega o URI da imagem
                File file = new File(geRealPath(uri));

                Toast.makeText(this, "CAMINHO: " + file.getPath(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String geRealPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Uri getImageUri(Context context, Bitmap img) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), img, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pragaSelecionada = listaPragas.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (listaPragas.size() > 0)
            pragaSelecionada = listaPragas.get(0);
    }
}

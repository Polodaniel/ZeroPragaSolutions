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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.MainActivity;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.R;
import com.live.zeropragasolutions.SplashScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;

public class EstagioNewActivity extends AppCompatActivity {

    private FloatingActionButton btnSalvar;
    private FloatingActionButton btnSelecionarImagem;
    private FloatingActionButton btnExcluirImagem;
    private FloatingActionButton btnMenu;
    private ImageView ImagemTirada;
    private EditText txtCodigo;
    private EditText txtNome;
    private EditText txtDescricao;
    private Bitmap img;
    private int indiceMenu = 0;

    private Estagio estagio;

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
    }

    private void SalvarEstagio() {

        boolean resultado = false;

        Estagio meuEstagio;

        // Cria uma nodo Estagio
        if (estagio == null) {

            meuEstagio = new Estagio();
            meuEstagio.setNome(txtNome.getText().toString());
            meuEstagio.setDescricao(txtDescricao.getText().toString());
            meuEstagio.set_status(true);

            long[] retorno = AppDataBase.getInstance(this).getEstagioDao().insert(meuEstagio);

            // Verficica se Salvou de forma correta
            if (retorno.length > 0) {
                resultado = true;
                meuEstagio.setID((int) retorno[0]);
            }

        } else {
            // Atualizar uma Praga já existente

            estagio.setNome(txtNome.getText().toString());
            estagio.setDescricao(txtDescricao.getText().toString());

            meuEstagio = estagio;

            int retorno = AppDataBase.getInstance(this).getEstagioDao().update(estagio);

            // Verficica se Salvou de forma correta
            if (retorno > 0)
                resultado = true;
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
}

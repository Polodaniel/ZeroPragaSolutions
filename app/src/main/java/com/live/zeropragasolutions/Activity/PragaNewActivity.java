package com.live.zeropragasolutions.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.R;
import com.live.zeropragasolutions.SplashScreen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

public class PragaNewActivity extends AppCompatActivity {

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

    private Praga praga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praga_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InicializaComponentes();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarPraga();
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

    private void SalvarPraga() {

        boolean resultado = false;

        Praga minhaPraga;

        // Cria uma Nova Praga
        if (praga == null) {

            minhaPraga = new Praga();
            minhaPraga.setNome(txtNome.getText().toString());
            minhaPraga.setDescricao(txtDescricao.getText().toString());
            minhaPraga.set_status(true);

            if (img != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                img.recycle();

                minhaPraga.setFotoPraga(byteArray);
            }

            long[] retorno = AppDataBase.getInstance(this).getPragaDao().insert(minhaPraga);

            // Verficica se Salvou de forma correta
            if (retorno.length > 0) {
                resultado = true;
                minhaPraga.setID((int) retorno[0]);
            }

        } else {
            // Atualizar uma Praga já existente

            praga.setNome(txtNome.getText().toString());
            praga.setDescricao(txtDescricao.getText().toString());
            praga.set_status(true);

            // Atualiza Foto da Praga
            if (img != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                img.recycle();

                praga.setFotoPraga(byteArray);
            }
            //

            minhaPraga = praga;

            int retorno = AppDataBase.getInstance(this).getPragaDao().update(praga);

            // Verficica se Salvou de forma correta
            if (retorno > 0)
                resultado = true;
        }


        if (resultado) {
            Mensagens.mostraMensagem(this, R.string.SalvarSucesso);

            Intent data = new Intent();
            data.putExtra(Praga.EXTRA_NAME, minhaPraga);
            //
            setResult(RESULT_OK, data);

            this.finish();
        } else {
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
        Serializable objetoPassado = getIntent().getSerializableExtra(Praga.EXTRA_NAME);
        if (objetoPassado != null) {
            praga = (Praga) objetoPassado;
            carregaInformacoesParaAtualizacao();
        } else {
            buscaProximoCodigo();
        }
    }

    private void buscaProximoCodigo() {

        txtCodigo.setEnabled(false);

        txtCodigo.setText(AppDataBase.getInstance(this).getPragaDao().getProximoCodigo().toString());
    }

    private void carregaInformacoesParaAtualizacao() {
        txtCodigo.setEnabled(false);
        txtCodigo.setText(praga.getID().toString());
        txtNome.setText(praga.getNome());
        txtDescricao.setText(praga.getDescricao());

        if (praga.getFotoPraga() != null) {
            Bitmap imagem = BitmapFactory.decodeByteArray(praga.getFotoPraga(), 0, praga.getFotoPraga().length);
            ImagemTirada.setImageBitmap(imagem);
        }


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

package com.live.zeropragasolutions.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.live.zeropragasolutions.Dao.PragaDao;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.Usuario;
import com.live.zeropragasolutions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuarioActivity extends AppCompatActivity {

    private static final int REQUEST_CADASTRO_USUARIO = 1;
    private static final int REQUEST_ATUALIZACAO_USUARIO = 2;

    private FloatingActionButton btAdicionar;
    private RecyclerView rvInformacoes;

    private List<Usuario> listaUsuario = new ArrayList<>();

    UsuarioDao contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbreTelaUsuarioNew();
            }
        });

        inicializaComponentes();

        carregaInformacoes();

        inicializaAdapter();


    }

    private void carregaInformacoes() {
        listaUsuario = contexto.listaUsuarios();
    }

    private void AbreTelaUsuarioNew()
    {
        Intent tela = new Intent(UsuarioActivity.this, UsuarioNewActivity.class);
        startActivityForResult(tela, REQUEST_CADASTRO_USUARIO);
    }

    private void inicializaComponentes() {
        contexto = AppDataBase.getInstance(this).getUsuarioDao();
        rvInformacoes = findViewById(R.id.rvInformacoes);
    }

    private void inicializaAdapter()
    {
        UsuarioActivity.MeuAdapter meuAdapter = new UsuarioActivity.MeuAdapter();
        rvInformacoes.setAdapter(meuAdapter);
    }

    private class MeuHolder extends RecyclerView.ViewHolder
    {
        private AppCompatTextView tvCodigo;
        private AppCompatTextView tvNome;
        private AppCompatTextView tvStatus;
        private AppCompatTextView tvTipoConta;

        public MeuHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTipoConta = itemView.findViewById(R.id.tvTipoConta);

            if(itemView.findViewById(R.id.tvStatus).toString() == "true")
                tvStatus.setTextColor(Color.GREEN);
            else if(itemView.findViewById(R.id.tvStatus).toString() == "false")
                tvStatus.setTextColor(Color.RED);
        }
    }

    private class MeuAdapter extends RecyclerView.Adapter<UsuarioActivity.MeuHolder>
    {
        @NonNull
        @Override
        public UsuarioActivity.MeuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            UsuarioActivity.MeuHolder meuHolder
                    = new UsuarioActivity.MeuHolder(getLayoutInflater().inflate(R.layout.item_cadastro_usuario, parent, false));
            return meuHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UsuarioActivity.MeuHolder holder, final int position) {
            final Usuario user = listaUsuario.get(position);
            holder.tvNome.setText(user.getNome().toString());
            holder.tvCodigo.setText(user.getID().toString());
            holder.tvStatus.setText(user.getStatus().toString());

            if(user.getTipoConta() == 0)
                holder.tvTipoConta.setText("Fiscal");
            else if(user.getTipoConta() == 1)
                holder.tvTipoConta.setText("Administrador");

            if(user.getStatus().toString() == "Ativo")
                holder.tvStatus.setTextColor(Color.GREEN);
            else if(user.getStatus().toString() == "Inativo")
                holder.tvStatus.setTextColor(Color.RED);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listaUsuario.remove(position);
                    notifyItemRemoved(position);
                    return true;
                }

            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent telaCadastro = new Intent(UsuarioActivity.this, UsuarioNewActivity.class);
                    telaCadastro.putExtra(Usuario.EXTRA_NAME, (Serializable) user);
                    startActivityForResult(telaCadastro, REQUEST_ATUALIZACAO_USUARIO);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaUsuario.size();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CADASTRO_USUARIO:
                if(resultCode == RESULT_OK)
                {
                    Usuario usuario = (Usuario) data.getSerializableExtra(Usuario.EXTRA_NAME);
                    listaUsuario.add(0, usuario);
                    rvInformacoes.getAdapter().notifyItemInserted(0);
                }
                break;
            case REQUEST_ATUALIZACAO_USUARIO:
                if(resultCode == RESULT_OK)
                {
                    final Usuario usuario = (Usuario) data.getSerializableExtra(Usuario.EXTRA_NAME);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int i =0; i < listaUsuario.size(); i++) {
                                if(listaUsuario.get(i).getID().equals(usuario.getID()))
                                {
                                    listaUsuario.set(i, usuario);
                                    rvInformacoes.getAdapter().notifyItemChanged(i);
                                    break;
                                }
                            }
                        }
                    }, 1000);

                }
                break;
        }
    }

}

package com.live.zeropragasolutions.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EstagioActivity extends AppCompatActivity {

    private static final int REQUEST_CADASTRO_ESTAGIO = 1;
    private static final int REQUEST_ATUALIZACAO_ESTAGIO = 2;
    //
    private FloatingActionButton btAdicionar;
    private RecyclerView rvInformacoes;
    //
    private List<Estagio> listaEstagios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estagio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {AbreTelaEstagioNew();    }
        });

        inicializaComponentes();
        carregaInformacoes();
        inicializaAdapter();
    }

    private void inicializaComponentes() {
        rvInformacoes = findViewById(R.id.rvInformacoes);
    }

    private void carregaInformacoes() {
        listaEstagios = AppDataBase.getInstance(this).getEstagioDao().listaEstagios();
    }

    private void AbreTelaEstagioNew()
    {
        Intent telaEstagioNew = new Intent(this, EstagioNewActivity.class);
        startActivityForResult(telaEstagioNew, REQUEST_CADASTRO_ESTAGIO);
    }

    private void inicializaAdapter()
    {
        MeuAdapter meuAdapter = new MeuAdapter();
        rvInformacoes.setAdapter(meuAdapter);
    }

    private class MeuHolder extends RecyclerView.ViewHolder
    {
        private AppCompatTextView tvCodigo;
        private AppCompatTextView tvNome;
        private AppCompatTextView tvStatus;

        public MeuHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            if(itemView.findViewById(R.id.tvStatus).toString() == "true")
                tvStatus.setTextColor(Color.GREEN);
            else if(itemView.findViewById(R.id.tvStatus).toString() == "false")
                tvStatus.setTextColor(Color.RED);
        }
    }

    private class MeuAdapter extends RecyclerView.Adapter<MeuHolder>
    {
        @NonNull
        @Override
        public MeuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MeuHolder meuHolder
                    = new MeuHolder(getLayoutInflater().inflate(R.layout.item_cadastro, parent, false));
            return meuHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MeuHolder holder,final int position) {
            final Estagio estagio = listaEstagios.get(position);
            holder.tvNome.setText(estagio.getNome().toString());
            holder.tvCodigo.setText(estagio.getID().toString());
            holder.tvStatus.setText(estagio.getStatus().toString());

            if(estagio.getStatus().toString() == "Ativo")
                holder.tvStatus.setTextColor(Color.GREEN);
            else if(estagio.getStatus().toString() == "Inativo")
                holder.tvStatus.setTextColor(Color.RED);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listaEstagios.remove(position);
                    notifyItemRemoved(position);
                    return true;
                }

            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent telaCadastro = new Intent(EstagioActivity.this, EstagioNewActivity.class);
                    telaCadastro.putExtra(Estagio.EXTRA_NAME, (Serializable) estagio);
                    startActivityForResult(telaCadastro, REQUEST_ATUALIZACAO_ESTAGIO);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaEstagios.size();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CADASTRO_ESTAGIO:
                if(resultCode == RESULT_OK)
                {
                    Estagio pragaRetornada = (Estagio) data.getSerializableExtra(Estagio.EXTRA_NAME);
                    listaEstagios.add(0, pragaRetornada);
                    rvInformacoes.getAdapter().notifyItemInserted(0);
                }
                break;
            case REQUEST_ATUALIZACAO_ESTAGIO:
                if(resultCode == RESULT_OK)
                {
                    final Estagio pragaRetornada = (Estagio) data.getSerializableExtra(Estagio.EXTRA_NAME);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int i =0; i < listaEstagios.size(); i++) {
                                if(listaEstagios.get(i).getID().equals(pragaRetornada.getID()))
                                {
                                    listaEstagios.set(i, pragaRetornada);
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

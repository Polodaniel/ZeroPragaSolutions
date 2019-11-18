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

import com.live.zeropragasolutions.Dao.TipoColetaDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TipoColetaActivity extends AppCompatActivity {

    private static final int REQUEST_CADASTRO_TIPOCOLETA = 1;
    private static final int REQUEST_ATUALIZACAO_TIPOCOLETA = 2;

    private FloatingActionButton btAdicionar;
    private RecyclerView rvInformacoes;

    private List<TipoColeta> listaTipoColeta = new ArrayList<>();

    TipoColetaDao contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_coleta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbreTelaTipoColetaNew();
            }
        });

        inicializaComponentes();

        carregaInformacoes();

        inicializaAdapter();

    }

    private void AbreTelaTipoColetaNew() {
        Intent telaTipoColetaNew = new Intent(this, TipoColetaNewActivity.class);
        startActivityForResult(telaTipoColetaNew, REQUEST_CADASTRO_TIPOCOLETA);
    }

    private void inicializaComponentes() {
        contexto = AppDataBase.getInstance(this).getTipoColetaDao();
        rvInformacoes = findViewById(R.id.rvInformacoesColeta);
    }

    private void carregaInformacoes() {
        listaTipoColeta = contexto.listaTipoColeta();
    }

    private void inicializaAdapter(){
        TipoColetaActivity.MeuAdapter meuAdapter = new TipoColetaActivity.MeuAdapter();
        rvInformacoes.setAdapter(meuAdapter);
    }

    private class MeuHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvCodigo;
        private AppCompatTextView tvNome;
        private AppCompatTextView tvStatus;
        private AppCompatTextView tvPraga;

        public MeuHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNome = itemView.findViewById(R.id.tvNome);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvPraga = itemView.findViewById(R.id.tvPraga);

            if (itemView.findViewById(R.id.tvStatus).toString() == "true")
                tvStatus.setTextColor(Color.GREEN);
            else if (itemView.findViewById(R.id.tvStatus).toString() == "false")
                tvStatus.setTextColor(Color.RED);
        }
    }

    private class MeuAdapter extends RecyclerView.Adapter<TipoColetaActivity.MeuHolder> {
        @NonNull
        @Override
        public TipoColetaActivity.MeuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TipoColetaActivity.MeuHolder meuHolder
                    = new TipoColetaActivity.MeuHolder(getLayoutInflater().inflate(R.layout.item_cadastro_tipo_coleta, parent, false));
            return meuHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TipoColetaActivity.MeuHolder holder, final int position) {

            final TipoColeta tp = listaTipoColeta.get(position);

            holder.tvNome.setText(tp.getNome().toString());
            holder.tvCodigo.setText(tp.getId().toString());
            holder.tvStatus.setText(tp.getStatus().toString());
            holder.tvPraga.setText(tp.getNomePraga().toString());

            if (tp.getStatus().toString() == "Ativo")
                holder.tvStatus.setTextColor(Color.GREEN);
            else if (tp.getStatus().toString() == "Inativo")
                holder.tvStatus.setTextColor(Color.RED);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    TipoColeta tp = new TipoColeta();

                    for (TipoColeta item : listaTipoColeta) {
                        if (item.getId() == (position + 1)) {
                            tp.setStatus(true);
                            tp = item;
                        }
                    }

                    int retorno = contexto.Desativar(tp.getId());

                    listaTipoColeta = contexto.listaTipoColeta();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvInformacoes.getAdapter().notifyDataSetChanged();
                        }
                    }, 1000);


                    //notifyItemRemoved(position);
                    return true;
                }

            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent telaCadastro = new Intent(TipoColetaActivity.this, TipoColetaNewActivity.class);
                    telaCadastro.putExtra(TipoColeta.EXTRA_NAME, (Serializable) tp);
                    startActivityForResult(telaCadastro, REQUEST_ATUALIZACAO_TIPOCOLETA);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaTipoColeta.size();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CADASTRO_TIPOCOLETA:
                if (resultCode == RESULT_OK) {

                    TipoColeta tp = (TipoColeta) data.getSerializableExtra(TipoColeta.EXTRA_NAME);

                    listaTipoColeta.add(0, tp);
                    rvInformacoes.getAdapter().notifyItemInserted(0);
                }
                break;
            case REQUEST_ATUALIZACAO_TIPOCOLETA:
                if (resultCode == RESULT_OK) {

                    final TipoColeta tp = (TipoColeta) data.getSerializableExtra(TipoColeta.EXTRA_NAME);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < listaTipoColeta.size(); i++) {
                                if (listaTipoColeta.get(i).getId().equals(tp.getId()))
                                {
                                    listaTipoColeta.set(i, tp);
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

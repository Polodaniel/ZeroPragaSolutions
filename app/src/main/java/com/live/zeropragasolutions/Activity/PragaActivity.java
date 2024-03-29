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

import com.live.zeropragasolutions.Dao.PragaDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PragaActivity extends AppCompatActivity {

    private static final int REQUEST_CADASTRO_PRAGA = 1;
    private static final int REQUEST_ATUALIZACAO_PRAGA = 2;
    //
    private FloatingActionButton btAdicionar;
    private RecyclerView rvInformacoes;
    //
    private List<Praga> listaPragas = new ArrayList<>();

    PragaDao contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praga);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbreTelaPragaNew();
            }
        });

        inicializaComponentes();

        carregaInformacoes();

        inicializaAdapter();

    }

    private void carregaInformacoes() {
        listaPragas = contexto.listaPragas();
    }

    private void AbreTelaPragaNew()
    {
        Intent telaPragaNew = new Intent(PragaActivity.this, PragaNewActivity.class);
        startActivityForResult(telaPragaNew, REQUEST_CADASTRO_PRAGA);
    }

    private void inicializaComponentes() {
        contexto = AppDataBase.getInstance(this).getPragaDao();
        rvInformacoes = findViewById(R.id.rvInformacoes);
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
            final Praga praga = listaPragas.get(position);
            holder.tvNome.setText(praga.getNome().toString());
            holder.tvCodigo.setText(praga.getID().toString());
            holder.tvStatus.setText(praga.getStatus().toString());

            if(praga.getStatus().toString() == "Ativo")
                holder.tvStatus.setTextColor(Color.GREEN);
            else if(praga.getStatus().toString() == "Inativo")
                holder.tvStatus.setTextColor(Color.RED);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    Praga pragas = new Praga();

                    for(Praga item : listaPragas )
                    {
                        if(item.getID() == (position+1))
                        {
                            pragas.set_status(true);
                            pragas = item;
                        }
                    }

                    int retorno = contexto.Desativar(pragas.getID());

                    listaPragas = contexto.listaPragas();

                    rvInformacoes.getAdapter().notifyDataSetChanged();
                    return true;
                }

            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent telaCadastro = new Intent(PragaActivity.this, PragaNewActivity.class);
                    telaCadastro.putExtra(Praga.EXTRA_NAME, (Serializable) praga);
                    startActivityForResult(telaCadastro, REQUEST_ATUALIZACAO_PRAGA);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaPragas.size();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CADASTRO_PRAGA:
                if(resultCode == RESULT_OK)
                {
                    Praga pragaRetornada = (Praga) data.getSerializableExtra(Praga.EXTRA_NAME);
                    listaPragas.add(0, pragaRetornada);
                    rvInformacoes.getAdapter().notifyItemInserted(0);
                }
                break;
            case REQUEST_ATUALIZACAO_PRAGA:
                if(resultCode == RESULT_OK)
                {
                    final Praga pragaRetornada = (Praga) data.getSerializableExtra(Praga.EXTRA_NAME);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int i =0; i < listaPragas.size(); i++) {
                                if(listaPragas.get(i).getID().equals(pragaRetornada.getID()))
                                {
                                    listaPragas.set(i, pragaRetornada);
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

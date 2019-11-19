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
import com.live.zeropragasolutions.Model.Turma;
import com.live.zeropragasolutions.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TurmaActivity extends AppCompatActivity {
    private static final int REQUEST_CADASTRO_TURMA = 1;
    private static final int REQUEST_ATUALIZACAO_TURMA = 2;
    //
    private FloatingActionButton btAdicionar;
    private RecyclerView rvInformacoes;
    //
    private List<Turma> listaTrumas = new ArrayList<>();

    private AppDataBase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turma);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {AbreTelaTurmaNew();    }
        });

        inicializaComponentes();

        carregaInformacoes();

        inicializaAdapter();
    }

    private void carregaInformacoes() {
        mDB = AppDataBase.getInstance(this);
        listaTrumas = AppDataBase.getInstance(this).getTurmaDao().listaTurmas();
    }

    private void AbreTelaTurmaNew()
    {
        Intent telaTurmaNew = new Intent(this, TurmaNewActivity.class);
        startActivityForResult(telaTurmaNew,REQUEST_CADASTRO_TURMA);
    }

    private void inicializaComponentes() {
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
            final Turma praga = listaTrumas.get(position);
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

                    Turma turma = new Turma();

                    for(Turma item : listaTrumas )
                    {
                        if(item.getID() == (position+1))
                        {
                            turma.set_status(true);
                            turma = item;
                        }
                    }

                    int retorno = mDB.getTurmaDao().Desativar(turma.getID());

                    listaTrumas = mDB.getTurmaDao().listaTurmas();

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
                    Intent telaCadastro = new Intent(TurmaActivity.this, TurmaNewActivity.class);
                    telaCadastro.putExtra(Turma.EXTRA_NAME, (Serializable) praga);
                    startActivityForResult(telaCadastro, REQUEST_ATUALIZACAO_TURMA);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaTrumas.size();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CADASTRO_TURMA:
                if(resultCode == RESULT_OK)
                {
                    Turma pragaRetornada = (Turma) data.getSerializableExtra(Turma.EXTRA_NAME);
                    listaTrumas.add(0, pragaRetornada);
                    rvInformacoes.getAdapter().notifyItemInserted(0);
                }
                break;
            case REQUEST_ATUALIZACAO_TURMA:
                if(resultCode == RESULT_OK)
                {
                    final Turma pragaRetornada = (Turma) data.getSerializableExtra(Turma.EXTRA_NAME);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for(int i =0; i < listaTrumas.size(); i++) {
                                if(listaTrumas.get(i).getID().equals(pragaRetornada.getID()))
                                {
                                    listaTrumas.set(i, pragaRetornada);
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

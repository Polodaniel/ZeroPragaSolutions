package com.live.zeropragasolutions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.live.zeropragasolutions.Activity.BoletimNewActivity;
import com.live.zeropragasolutions.Activity.EstagioActivity;
import com.live.zeropragasolutions.Activity.PragaActivity;
import com.live.zeropragasolutions.Activity.TipoColetaActivity;
import com.live.zeropragasolutions.Activity.TipoColetaNewActivity;
import com.live.zeropragasolutions.Activity.TurmaActivity;
import com.live.zeropragasolutions.Activity.UsuarioActivity;
import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.Auxiliares.Utilidades;
import com.live.zeropragasolutions.Dao.HomeDao;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Boletim;
import com.live.zeropragasolutions.Model.ObjetoLogin;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Usuario;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_HOME = 1;
    private static final int REQUEST_CADASTRO_BOLETIM = 2;
    private static final int REQUEST_ATUALIZACAO_BOLETIM = 3;

    // Variaveis
    public static Usuario meuCadastro = new Usuario();

    // Componentes
    private NavigationView navigationView;
    private FloatingActionButton btnNovo;

    private TextView lblQtdeBoletins;
    private TextView lblQtdePragas;
    private TextView lblQtdeEstagio;
    private TextView lblQtdeTipoColeta;
    private TextView lblQtdeTurma;

    private ImageButton btnAtualizarTotalizador;

    private LinearLayout barraTotalizador;
    private CardView cardTotalizador;

    private RecyclerView rvInformacoes;

    private List<Boletim> listaBoletins = new ArrayList<>();

    private HomeDao contexto = AppDataBase.getInstance(this).getHomeDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Recebe da Tela de Login o ObjetoLogin
        carregaInformacoesTela();

        // Inicializa os Botões
        inicializaComponenetes();

        // Recebe a Lista de Boletins
        carregaInformacoes();

        // Monta o Adapter
        inicializaAdapter();

        // Monta Menu User / Admin
        montaMenuUserAdmin();

        // Monta o Totalizador
        montaListaContadores();

        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbreTelaBoletimNovo();
            }
        });

        btnAtualizarTotalizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { montaListaContadores();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.btn_praga) {
            AbreTelaPraga();
        } else if (id == R.id.btn_estagio) {
            AbreTelaEstagio();
        } else if (id == R.id.btn_turma) {
            AbreTelaTurma();
        } else if (id == R.id.btn_tipo_coleta) {
            AbreTelaTipooColeta();
        } else if( id == R.id.btn_usuario){
            AbreTelaUsuario();
        } else if( id == R.id.nav_sair ) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void carregaInformacoesTela() {
        meuCadastro = (Usuario) getIntent().getSerializableExtra(Usuario.EXTRA_NAME);
    }

    private void inicializaComponenetes() {
        btnNovo = (FloatingActionButton) findViewById(R.id.fab);
        lblQtdeBoletins = findViewById(R.id.lblQtdeBoletins);
        lblQtdePragas = findViewById(R.id.lblQtdePraga);
        lblQtdeEstagio = findViewById(R.id.lblQtdeEstagio);
        lblQtdeTipoColeta = findViewById(R.id.lblQtdeTipoColeta);
        lblQtdeTurma = findViewById(R.id.lblQtdeTurma);

        btnAtualizarTotalizador = findViewById(R.id.btnAtualizarTotalizador);

        barraTotalizador = findViewById(R.id.barraTotalizador);
        cardTotalizador = findViewById(R.id.cardTotalizador);

        rvInformacoes = findViewById(R.id.rvInformacoes);

    }

    private void montaListaContadores()
    {
        lblQtdeBoletins.setText(contexto.getContaTodosBoletins().toString());
        lblQtdePragas.setText(contexto.getContaTodasPrgas().toString());
        lblQtdeEstagio.setText(contexto.getContaTodosEstagio().toString());
        lblQtdeTipoColeta.setText(contexto.getContaTodosTipoColeta().toString());
        lblQtdeTurma.setText(contexto.getContaTodasTurmas().toString());
    }

    private void carregaInformacoes(){

        if(meuCadastro.getTipoConta() == 1)
            listaBoletins = contexto.listaBoletins();
        else if (meuCadastro.getTipoConta() == 0)
            listaBoletins = contexto.listaBoletinsFiscal(meuCadastro.getID());

    }

    private void inicializaAdapter(){
        HomeActivity.MeuAdapter meuAdapter = new HomeActivity.MeuAdapter();
        rvInformacoes.setAdapter(meuAdapter);
    }

    @SuppressLint("RestrictedApi")
    private void montaMenuUserAdmin() {
        try {

            if (meuCadastro.getTipoConta() == 0) {
                navigationView.getMenu().findItem(R.id.nav_descricao).setVisible(false);
                barraTotalizador.setVisibility(View.VISIBLE);
                cardTotalizador.setVisibility(View.VISIBLE);
            } else if (meuCadastro.getTipoConta() == 1) {
                navigationView.getMenu().findItem(R.id.nav_descricao).setVisible(true);
                btnNovo.setVisibility(View.INVISIBLE);
                barraTotalizador.setVisibility(View.VISIBLE);
                cardTotalizador.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {

        }
    }

    private void AbreTelaPraga()
    {
        Intent telaPraga = new Intent(this, PragaActivity.class);
        startActivity(telaPraga);
    }

    private void AbreTelaEstagio()
    {
        Intent telaEstagio = new Intent(this, EstagioActivity.class);
        startActivity(telaEstagio);
    }

    private void AbreTelaTipooColeta()
    {
        Intent telaTipoColeta = new Intent(this, TipoColetaActivity.class);
        startActivity(telaTipoColeta);
    }

    private void AbreTelaTurma()
    {
        Intent telaTurma = new Intent(this, TurmaActivity.class);
        startActivity(telaTurma);
    }

    private void AbreTelaUsuario()
    {
        Intent telaUsuario = new Intent(this, UsuarioActivity.class);
        startActivity(telaUsuario);
    }

    private void AbreTelaBoletimNovo()
    {
        Intent telaBoletimNovo = new Intent(this, BoletimNewActivity.class);
        startActivityForResult(telaBoletimNovo, REQUEST_CADASTRO_BOLETIM);
    }

    private class MeuHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView tvCodigo;
        private AppCompatTextView tvData;
        private AppCompatTextView tvPraga;
        private AppCompatTextView tvQuantidade;
        private AppCompatTextView tvEstagio;
        private AppCompatTextView tvTipoColeta;
        private AppCompatTextView tvNomeFiscal;
        private AppCompatTextView tvStatus;

        public MeuHolder(@NonNull View itemView) {
            super(itemView);

            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvData = itemView.findViewById(R.id.tvData);
            tvPraga = itemView.findViewById(R.id.tvPraga);
            tvQuantidade = itemView.findViewById(R.id.tvQuantidade);
            tvEstagio = itemView.findViewById(R.id.tvEstagio);
            tvTipoColeta = itemView.findViewById(R.id.tvTipoColeta);
            tvNomeFiscal = itemView.findViewById(R.id.tvNomeFiscal);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            if (itemView.findViewById(R.id.tvStatus).toString() == "Ativo")
                tvStatus.setTextColor(Color.GREEN);
            else if (itemView.findViewById(R.id.tvStatus).toString() == "Inativo")
                tvStatus.setTextColor(Color.RED);
        }
    }

    private class MeuAdapter extends RecyclerView.Adapter<HomeActivity.MeuHolder> {
        @NonNull
        @Override
        public HomeActivity.MeuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            HomeActivity.MeuHolder meuHolder
                    = new HomeActivity.MeuHolder(getLayoutInflater().inflate(R.layout.item_boletim, parent, false));
            return meuHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeActivity.MeuHolder holder, final int position) {

            final Boletim boletim = listaBoletins.get(position);

            String Codigo = new Utilidades().FormataCodigo(boletim.getID());

            holder.tvCodigo.setText(Codigo);
            holder.tvData.setText(boletim.getData().toString());
            holder.tvPraga.setText(boletim.getNomePraga().toString());
            holder.tvQuantidade.setText(boletim.getQuantidade().toString());
            holder.tvEstagio.setText(boletim.getEstagio().toString());
            holder.tvTipoColeta.setText(boletim.getTipoColeta().toString());
            holder.tvNomeFiscal.setText(boletim.getFiscal().toString());
            holder.tvStatus.setText(boletim.getStatus().toString());


            if (boletim.getStatus().toString() == "Ativo")
                holder.tvStatus.setTextColor(Color.GREEN);
            else if (boletim.getStatus().toString() == "Inativo")
                holder.tvStatus.setTextColor(Color.RED);

            if(meuCadastro.getTipoConta() == 1 ) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Boletim tp = new Boletim();

                        for (Boletim item : listaBoletins) {
                            if (item.getID() == (position + 1)) {
                                tp.setStatus(true);
                                tp = item;
                            }
                        }

                        int retorno = contexto.Desativar(tp.getID());

                        listaBoletins = contexto.listaBoletins();

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
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent telaCadastro = new Intent(HomeActivity.this, BoletimNewActivity.class);
                    telaCadastro.putExtra(Boletim.EXTRA_NAME, (Serializable) boletim);
                    startActivityForResult(telaCadastro, REQUEST_ATUALIZACAO_BOLETIM);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listaBoletins.size();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CADASTRO_BOLETIM:
                if (resultCode == RESULT_OK) {

                    Boletim bol = (Boletim) data.getSerializableExtra(Boletim.EXTRA_NAME);

                    listaBoletins.add(0, bol);
                    rvInformacoes.getAdapter().notifyItemInserted(0);
                }
                break;
            case REQUEST_ATUALIZACAO_BOLETIM:
                if (resultCode == RESULT_OK) {

                    final Boletim bol = (Boletim) data.getSerializableExtra(Boletim.EXTRA_NAME);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < listaBoletins.size(); i++) {
                                if (listaBoletins.get(i).getID().equals(bol.getID()))
                                {
                                    listaBoletins.set(i, bol);
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

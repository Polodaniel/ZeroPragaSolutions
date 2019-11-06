package com.live.zeropragasolutions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.live.zeropragasolutions.Activity.EstagioActivity;
import com.live.zeropragasolutions.Activity.PragaActivity;
import com.live.zeropragasolutions.Activity.TipoColetaActivity;
import com.live.zeropragasolutions.Activity.TurmaActivity;
import com.live.zeropragasolutions.Activity.UsuarioActivity;
import com.live.zeropragasolutions.Dao.HomeDao;
import com.live.zeropragasolutions.Dao.UsuarioDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.ObjetoLogin;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.Usuario;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_HOME = 1;

    // Variaveis
    Usuario meuCadastro = new Usuario();

    // Botões
    private  NavigationView navigationView;
    private FloatingActionButton btnNovo;

    private TextView lblQtdeBoletins;
    private TextView lblTotalBoletins;
    private TextView lblQtdePragas;
    private TextView lblQtdeEstagio;
    private TextView lblQtdeTipoColeta;
    private TextView lblQtdeTurma;

    private ImageButton btnAtualizarTotalizador;

    private LinearLayout barraTotalizador;
    private CardView cardTotalizador;

    private HomeDao contexto = AppDataBase.getInstance(this).getHomeDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

        // Monta Menu User / Admin
        montaMenuUserAdmin();

        // Monta o Totalizador
        montaListaContadores();

        btnAtualizarTotalizador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                montaListaContadores();
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
        lblTotalBoletins = findViewById(R.id.lblTotalBoletins);
        lblQtdePragas = findViewById(R.id.lblQtdePraga);
        lblQtdeEstagio = findViewById(R.id.lblQtdeEstagio);
        lblQtdeTipoColeta = findViewById(R.id.lblQtdeTipoColeta);
        lblQtdeTurma = findViewById(R.id.lblQtdeTurma);

        btnAtualizarTotalizador = findViewById(R.id.btnAtualizarTotalizador);

        barraTotalizador = findViewById(R.id.barraTotalizador);
        cardTotalizador = findViewById(R.id.cardTotalizador);
    }

    private void montaListaContadores()
    {
        lblQtdeBoletins.setText("0");
        lblQtdePragas.setText(contexto.getContaTodasPrgas().toString());
        lblQtdeEstagio.setText(contexto.getContaTodosEstagio().toString());
        lblQtdeTipoColeta.setText(contexto.getContaTodosTipoColeta().toString());
        lblQtdeTurma.setText(contexto.getContaTodasTurmas().toString());

        if(meuCadastro.getTipoConta() == 0) {
            lblTotalBoletins.setText("0");
        }else if(meuCadastro.getTipoConta() == 1){
            lblTotalBoletins.setText("10");
        }
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

}

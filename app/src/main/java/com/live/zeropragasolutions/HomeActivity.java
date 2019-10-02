package com.live.zeropragasolutions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.live.zeropragasolutions.Activity.PragaActivity;
import com.live.zeropragasolutions.Model.ObjetoLogin;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Variaveis
    ObjetoLogin meuCadastro = new ObjetoLogin();

    // Botões
    private  NavigationView navigationView;
    private FloatingActionButton btnNovo;

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

        // Inicializa os Botões
        //inicializaComponenetes();

        // Monta Menu User / Admin
        //montaMenuUserAdmin();

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

        } else if (id == R.id.btn_turma) {

        } else if (id == R.id.btn_tipo_coleta) {

        } else if ( id == R.id.nav_sair )
        {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void carregaInformacoesTela() {
        meuCadastro = (ObjetoLogin) getIntent().getSerializableExtra(ObjetoLogin.EXTRA_NAME);
    }

    private void inicializaComponenetes() {
        btnNovo = (FloatingActionButton) findViewById(R.id.fab);
    }

    @SuppressLint("RestrictedApi")
    private void montaMenuUserAdmin() {
        try {

            if (meuCadastro.get_tipoConta() == 0) {
                navigationView.getMenu().findItem(R.id.nav_descricao).setVisible(false);
            } else if (meuCadastro.get_tipoConta() == 1) {
                navigationView.getMenu().findItem(R.id.nav_descricao).setVisible(true);
                btnNovo.setVisibility(View.INVISIBLE);
            }

        } catch (Exception ex) {

        }
    }

    private void AbreTelaPraga()
    {
        Intent telaPraga = new Intent(this, PragaActivity.class);
        startActivity(telaPraga);
    }
}
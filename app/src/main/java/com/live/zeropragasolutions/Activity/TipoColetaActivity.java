package com.live.zeropragasolutions.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.live.zeropragasolutions.R;

public class TipoColetaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_coleta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {AbreTelaTipoColetaNew();    }
        });
    }
    private void AbreTelaTipoColetaNew()
    {
        Intent telaTipoColetaNew = new Intent(this, TipoColetaNewActivity.class);
        startActivity(telaTipoColetaNew);
    }
}

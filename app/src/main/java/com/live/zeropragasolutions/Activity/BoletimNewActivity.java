package com.live.zeropragasolutions.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.wrappers.PackageManagerWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.live.zeropragasolutions.Auxiliares.Utilidades;
import com.live.zeropragasolutions.Dao.BoletimDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Boletim;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Turma;
import com.live.zeropragasolutions.R;

import java.util.ArrayList;
import java.util.List;

public class BoletimNewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_PERMISSION_LOCATION = 1;
    private GoogleMap MapaBoletim;
    private Marker marker;

    private FusedLocationProviderClient locationClient;

    private ImageButton btnPegarLocalizacao;

    public Double Latitude;
    public Double Longitude;
    public Double Altitude;

    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtAltitude;
    private TextView ID;
    private TextView Data;
    private TextView Fiscal;
    private TextView Quantidade;

    private Spinner Turma;
    private Spinner NomePraga;
    private Spinner Estagio;
    private Spinner TipoColeta;

    private ImageButton btnFotoPraga;
    private ImageButton btnAddPragaLista;
    private Button btnSalvar;

    private Boletim boletim;
    private BoletimDao contexto = AppDataBase.getInstance(this).getBoletimDao();

    private List<Praga> listaPraga = new ArrayList<>();
    private List<Estagio> listaEstagio = new ArrayList<>();
    private List<TipoColeta> listaTipoColeta = new ArrayList<>();
    private List<Turma> listaTurma = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boletim_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        verificaPermissoes();

        InicializaComponentes();

        CarregasSpnnier();

        btnPegarLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inicializaCapturaLocalizacao();

                if(locationClient != null){
                    locationClient.removeLocationUpdates(locationCallback);
                }
            }
        });
    }

    private void adicionarLocalizacaoTextBox() {

        txtAltitude.setText(Altitude.toString());
        txtLatitude.setText(Latitude.toString());
        txtLongitude.setText(Longitude.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Para de pegar a localização
        if(locationClient != null){
            locationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION_LOCATION)
        {
            for(int grant : grantResults)
            {
                if(grant != PackageManager.PERMISSION_GRANTED)
                {
                    this.finish();
                    return;
                }
            }
        }
        inicializaCapturaLocalizacao();
    }

    private void verificaPermissoes()
    {
        boolean ACCESS_COARSE_LOCATION = ActivityCompat
                .checkSelfPermission(this
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        boolean ACCESS_FINE_LOCATION = ActivityCompat
                .checkSelfPermission(this
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if(ACCESS_COARSE_LOCATION && ACCESS_FINE_LOCATION)
        {
            inicializaCapturaLocalizacao();
        }
        else
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
        }


    }

    private void InicializaComponentes() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.MapaBoletim);

        mapFragment.getMapAsync(this);

        btnPegarLocalizacao = findViewById(R.id.btnPegarLocalizacao);

        txtAltitude = findViewById(R.id.txtAltitude);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);

        ID = findViewById(R.id.txtCodigo);
        Data = findViewById(R.id.txtData);
        Fiscal = findViewById(R.id.txtnomeFiscal);
        Turma = findViewById(R.id.txtTurma);
        Quantidade = findViewById(R.id.txtQtde);
        NomePraga = findViewById(R.id.txtNomePraga);
        Estagio = findViewById(R.id.txtEstagio);
        TipoColeta = findViewById(R.id.txtTipoColeta);

        btnFotoPraga = findViewById(R.id.btnFotoPraga);
        btnAddPragaLista = findViewById(R.id.btnAddPragaLista);
        btnSalvar = findViewById(R.id.btnSalvar);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapaBoletim = googleMap;

        //MapaBoletim.setMapType(googleMap.MAP_TYPE_HYBRID);
        MapaBoletim.setMapType(googleMap.MAP_TYPE_HYBRID);

        MapaBoletim.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                adicionaMarcador(latLng);
                //
                CameraUpdate cameraUpdate = CameraUpdateFactory
                        .newCameraPosition(CameraPosition.fromLatLngZoom(latLng, 17));
                MapaBoletim.animateCamera(cameraUpdate);
            }
        });
    }

    private void adicionaMarcador(LatLng localizacao){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(localizacao);
        markerOptions.title("Minha posição atual");
        markerOptions.draggable(true);

        marker = MapaBoletim.addMarker(markerOptions);

        adicionarLocalizacaoTextBox();
    }

    @SuppressLint("MissingPermission")
    private void inicializaCapturaLocalizacao(){

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest configLocalizacao = new LocationRequest();

        configLocalizacao.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        configLocalizacao.setInterval(5000);

        configLocalizacao.setFastestInterval(5000);

        locationClient.requestLocationUpdates(configLocalizacao,locationCallback, Looper.getMainLooper());

    }

    private void CarregasSpnnier() {

        listaPraga = contexto.listaPragas();
        listaEstagio = contexto.listaEstagios();
        listaTipoColeta = contexto.listaTipoCOleta();
        listaTurma = contexto.listaTurma();

        List<String> listaPragasComboBox = new ArrayList<String>();
        List<String> listaPragasEstagioComboBox = new ArrayList<String>();
        List<String> listaTipoColetaComboBox = new ArrayList<String>();
        List<String> listaTurmaComboBox = new ArrayList<String>();

        String Codigo;

        for (Praga praga : listaPraga) {

            Codigo = new Utilidades().FormataCodigo(praga.getID());

            listaPragasComboBox.add(Codigo + " - " + praga.getNome());
        }

        for (Estagio estagio : listaEstagio) {

            Codigo = new Utilidades().FormataCodigo(estagio.getID());

            listaPragasEstagioComboBox.add(Codigo + " - " + estagio.getNome());
        }

        for (TipoColeta tipoColeta : listaTipoColeta) {

            Codigo = new Utilidades().FormataCodigo(tipoColeta.getId());

            listaTipoColetaComboBox.add(Codigo + " - " + tipoColeta.getNome());
        }

        for (Turma turma : listaTurma) {

            Codigo = new Utilidades().FormataCodigo(turma.getID());

            listaTurmaComboBox.add(Codigo + " - " + turma.getNome());
        }

        ArrayAdapter<String> adapterSpinnerPraga = new ArrayAdapter<String>(BoletimNewActivity.this, android.R.layout.simple_spinner_dropdown_item, listaPragasComboBox);
        ArrayAdapter<String> adapterSpinnerEstagio = new ArrayAdapter<String>(BoletimNewActivity.this, android.R.layout.simple_spinner_dropdown_item, listaPragasEstagioComboBox);
        ArrayAdapter<String> adapterSpinnerTipoColeta = new ArrayAdapter<String>(BoletimNewActivity.this, android.R.layout.simple_spinner_dropdown_item, listaTipoColetaComboBox);
        ArrayAdapter<String> adapterSpinnerTurma = new ArrayAdapter<String>(BoletimNewActivity.this, android.R.layout.simple_spinner_dropdown_item, listaTurmaComboBox);

        NomePraga.setAdapter(adapterSpinnerPraga);
        Estagio.setAdapter(adapterSpinnerEstagio);
        TipoColeta.setAdapter(adapterSpinnerTipoColeta);
        Turma.setAdapter(adapterSpinnerTurma);

    }



    //---------------------------------------------------------------------------------------------------------------------------
    private LocationCallback locationCallback = new LocationCallback(){

        @Override
        public void onLocationResult(LocationResult locationResult) {

            Location ultimaLocalizacao = locationResult.getLastLocation();

            Latitude = ultimaLocalizacao.getLatitude();
            Longitude = ultimaLocalizacao.getLongitude();
            Altitude = ultimaLocalizacao.getAltitude();

            adicionaMarcador(new LatLng(ultimaLocalizacao.getLatitude(),ultimaLocalizacao.getLongitude()));

            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(ultimaLocalizacao.getLatitude(),ultimaLocalizacao.getLongitude()), 17));
            MapaBoletim.animateCamera(cameraUpdate);

            if(locationClient != null){
                locationClient.removeLocationUpdates(locationCallback);
            }

        }
    };

}

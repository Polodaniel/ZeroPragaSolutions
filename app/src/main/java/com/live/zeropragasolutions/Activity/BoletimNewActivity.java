package com.live.zeropragasolutions.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateTimePatternGenerator;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DateSorter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.live.zeropragasolutions.Auxiliares.Mensagens;
import com.live.zeropragasolutions.Auxiliares.Utilidades;
import com.live.zeropragasolutions.Dao.BoletimDao;
import com.live.zeropragasolutions.DataBase.AppDataBase;
import com.live.zeropragasolutions.Model.Boletim;
import com.live.zeropragasolutions.Model.Estagio;
import com.live.zeropragasolutions.Model.Praga;
import com.live.zeropragasolutions.Model.TipoColeta;
import com.live.zeropragasolutions.Model.Turma;
import com.live.zeropragasolutions.R;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.live.zeropragasolutions.HomeActivity.meuCadastro;

public class BoletimNewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_PERMISSION_LOCATION = 1;
    private GoogleMap MapaBoletim;
    private Marker marker;

    private FusedLocationProviderClient locationClient;

    private ImageButton btnPegarLocalizacao;

    public Double Latitude;
    public Double Longitude;
    public Double Altitude;

    private Bitmap img;
    private ImageView ImagemTirada;

    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtAltitude;
    private TextView txtID;
    private TextView txtData;
    private TextView txtFiscal;
    private TextView txtQuantidade;

    private Spinner txtTurma;
    private Spinner txtNomePraga;
    private Spinner txtEstagio;
    private Spinner txtTipoColeta;

    private ImageButton btnFotoPraga;
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

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Salvar();
            }
        });

        btnFotoPraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });

        carregaInformacoesPassadasPorParametro();

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
        if (locationClient != null) {
            locationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void cancelaCapturaLocalizacao()
    {
        //Cancela o listener de atualização
        if(locationClient != null)
            locationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    this.finish();
                    return;
                }
            }
        }
        inicializaCapturaLocalizacao();
    }

    private void verificaPermissoes() {
        boolean ACCESS_COARSE_LOCATION = ActivityCompat
                .checkSelfPermission(this
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        boolean ACCESS_FINE_LOCATION = ActivityCompat
                .checkSelfPermission(this
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (ACCESS_COARSE_LOCATION && ACCESS_FINE_LOCATION) {
            inicializaCapturaLocalizacao();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
        }


    }

    private void InicializaComponentes() {

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.MapaBoletim);

        mapFragment.getMapAsync(this);

        txtAltitude = findViewById(R.id.txtAltitude);
        txtLatitude = findViewById(R.id.txtLatitude);
        txtLongitude = findViewById(R.id.txtLongitude);

        txtID = findViewById(R.id.txtCodigo);
        txtData = findViewById(R.id.txtData);
        txtFiscal = findViewById(R.id.txtnomeFiscal);
        txtTurma = findViewById(R.id.txtTurma);
        txtQuantidade = findViewById(R.id.txtQtde);
        txtNomePraga = findViewById(R.id.txtNomePraga);
        txtEstagio = findViewById(R.id.txtEstagio);
        txtTipoColeta = findViewById(R.id.txtTipoColeta);

        btnFotoPraga = findViewById(R.id.btnFotoPraga);
        btnSalvar = findViewById(R.id.btnSalvar);

        ImagemTirada = (ImageView) findViewById(R.id.imgPraga);

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();

        txtData.setText(formataData.format(data).toString());

        txtQuantidade.setText("0");
    }

    public void tirarFoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        //espera o resultado
        startActivityForResult(intent, 0);
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

        if(boletim != null )
        {
            cancelaCapturaLocalizacao();

            LatLng LocalizacaoSalva = new LatLng(boletim.getLatitude(),boletim.getLongitude());

            adicionaMarcadorConsulta(LocalizacaoSalva);

            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(boletim.getLatitude(),boletim.getLongitude()), 19));

            MapaBoletim.animateCamera(cameraUpdate);
        }
    }

    private void adicionaMarcador(LatLng localizacao) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(localizacao);
        markerOptions.title("Praga Localizada nesse local !");
        markerOptions.draggable(true);

        marker = MapaBoletim.addMarker(markerOptions);

        adicionarLocalizacaoTextBox();
    }

    private void adicionaMarcadorConsulta(LatLng localizacao) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(localizacao);
        markerOptions.title("Praga Localizada nesse local !");
        markerOptions.draggable(true);

        marker = MapaBoletim.addMarker(markerOptions);
    }

    @SuppressLint("MissingPermission")
    private void inicializaCapturaLocalizacao() {

        locationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest configLocalizacao = new LocationRequest();

        configLocalizacao.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        configLocalizacao.setInterval(5000);

        configLocalizacao.setFastestInterval(5000);

        locationClient.requestLocationUpdates(configLocalizacao, locationCallback, Looper.getMainLooper());

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

        txtNomePraga.setAdapter(adapterSpinnerPraga);
        txtEstagio.setAdapter(adapterSpinnerEstagio);
        txtTipoColeta.setAdapter(adapterSpinnerTipoColeta);
        txtTurma.setAdapter(adapterSpinnerTurma);

        txtFiscal.setText(meuCadastro.getNome().toString());

    }

    private void buscaProximoCodigo() {

        txtID.setEnabled(false);

        txtData.setEnabled(false);

        txtID.setText(contexto.getProximoCodigo().toString());
    }

    private void carregaInformacoesPassadasPorParametro() {
        Serializable objetoPassado = getIntent().getSerializableExtra(Boletim.EXTRA_NAME);
        if (objetoPassado != null) {
            boletim = (Boletim) objetoPassado;
            carregaInformacoesParaAtualizacao();
        } else {
            buscaProximoCodigo();
        }
    }

    //Esta Faltando
    private void carregaInformacoesParaAtualizacao() {

        txtID.setEnabled(false);
        txtLatitude.setEnabled(false);
        txtLongitude.setEnabled(false);
        txtAltitude.setEnabled(false);
        txtData.setEnabled(false);
        txtFiscal.setEnabled(false);
        txtQuantidade.setEnabled(false);
        txtTurma.setEnabled(false);
        txtNomePraga.setEnabled(false);
        txtEstagio.setEnabled(false);
        txtTipoColeta.setEnabled(false);
        btnFotoPraga.setEnabled(false);

        btnSalvar.setEnabled(true);
        btnSalvar.setText("Ativar Boletim");

        txtID.setText(boletim.getID().toString());

        txtLatitude.setText(boletim.getLatitude().toString());
        txtLongitude.setText(boletim.getLongitude().toString());
        txtAltitude.setText(boletim.getAltitude().toString());
        txtData.setText(boletim.getData().toString());
        txtFiscal.setText(boletim.getFiscal().toString());
        txtQuantidade.setText(boletim.getQuantidade().toString());
        txtTurma.setSelection((Integer.parseInt(boletim.getCodigoTurma().toString()))-1);
        txtNomePraga.setSelection((Integer.parseInt(boletim.getCodigoPraga().toString()))-1);
        txtEstagio.setSelection((Integer.parseInt(boletim.getCodigoEstagio().toString()))-1);
        txtTipoColeta.setSelection((Integer.parseInt(boletim.getCodigoTipoColeta().toString()))-1);

        if (boletim.getFotoPragaTirada() != null) {
            Bitmap imagem = BitmapFactory.decodeByteArray(boletim.getFotoPragaTirada(), 0, boletim.getFotoPragaTirada().length);
            ImagemTirada.setImageBitmap(imagem);
        }

    }

    private void Salvar() {

        boolean resultado = false;

        Boletim meuBoletim;

        if (boletim == null) {

            meuBoletim = new Boletim();

            // Input EditText
            meuBoletim.setQuantidade(Integer.parseInt(txtQuantidade.getText().toString()));
            meuBoletim.setData(txtData.getText().toString());
            meuBoletim.setFiscal(txtFiscal.getText().toString());
            meuBoletim.setLatitude(Double.parseDouble(txtLatitude.getText().toString()));
            meuBoletim.setLongitude(Double.parseDouble(txtLongitude.getText().toString()));
            meuBoletim.setAltitude(Double.parseDouble(txtAltitude.getText().toString()));
            meuBoletim.setStatus(true);
            meuBoletim.setCodigoFiscal(meuCadastro.getID());

            // Praga
            String PragaSelecionada = txtNomePraga.getSelectedItem().toString();
            Integer CodigoPragaInt = Integer.parseInt(PragaSelecionada.substring(0, 6));
            meuBoletim.setCodigoPraga(CodigoPragaInt);
            meuBoletim.setNomePraga(PragaSelecionada.substring(9));

            // Estagio
            String EstagioSelecionada = txtEstagio.getSelectedItem().toString();
            Integer CodigoEstagioInt = Integer.parseInt(EstagioSelecionada.substring(0, 6));
            meuBoletim.setCodigoEstagio(CodigoEstagioInt);
            meuBoletim.setEstagio(EstagioSelecionada.substring(9));

            // TipoColeta
            String TipoColetaSelecionada = txtTipoColeta.getSelectedItem().toString();
            Integer CodigoTipoColetaInt = Integer.parseInt(TipoColetaSelecionada.substring(0, 6));
            meuBoletim.setCodigoTipoColeta(CodigoTipoColetaInt);
            meuBoletim.setTipoColeta(TipoColetaSelecionada.substring(9));

            // Turma
            String TurmaSelecionada = txtTurma.getSelectedItem().toString();
            Integer CodigoTurmaInt = Integer.parseInt(TurmaSelecionada.substring(0, 6));
            meuBoletim.setCodigoTurma(CodigoTurmaInt);
            meuBoletim.setTurma(TurmaSelecionada.substring(9));

            // Imagem
            if (img != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                img.recycle();

                meuBoletim.setFotoPragaTirada(byteArray);
            }

            if (ValidaCampos(meuBoletim)) {
                long[] retorno = contexto.insert(meuBoletim);

                // Verficica se Salvou de forma correta
                if (retorno.length > 0) {
                    resultado = true;
                    meuBoletim.setID((int) retorno[0]);
                }
            } else
                resultado = false;

        } else {

            // AtualizarBoletim();

            boletim.setStatus(true);

            meuBoletim = boletim;

            if (ValidaCampos(meuBoletim)) {
                int retorno = contexto.update(boletim);

                if (retorno > 0)
                    resultado = true;
            }
        }


        if (resultado) {
            Mensagens.mostraMensagem(this, R.string.SalvarSucesso);

            Intent data = new Intent();
            data.putExtra(Boletim.EXTRA_NAME, meuBoletim);

            setResult(RESULT_OK, data);

            this.finish();
        } else {
            Mensagens.mostraMensagem(this, R.string.SalvarErro);
        }

    }

    private void AtualizarBoletim() {
        // Input EditText
        boletim.setQuantidade(Integer.parseInt(txtQuantidade.getText().toString()));
        boletim.setData(txtData.getText().toString());
        //boletim.setFiscal(txtFiscal.getText().toString());
        boletim.setLatitude(Double.parseDouble(txtLatitude.getText().toString()));
        boletim.setLongitude(Double.parseDouble(txtLongitude.getText().toString()));
        boletim.setAltitude(Double.parseDouble(txtAltitude.getText().toString()));
        boletim.setStatus(true);

        // Input Spinner

        // Praga
        String PragaSelecionada = txtNomePraga.getSelectedItem().toString();
        Integer CodigoPragaInt = Integer.parseInt(PragaSelecionada.substring(0, 6));
        boletim.setCodigoPraga(CodigoPragaInt);
        boletim.setNomePraga(PragaSelecionada.substring(9));

        // Estagio
        String EstagioSelecionada = txtEstagio.getSelectedItem().toString();
        Integer CodigoEstagioInt = Integer.parseInt(EstagioSelecionada.substring(0, 6));
        boletim.setCodigoEstagio(CodigoEstagioInt);
        boletim.setEstagio(EstagioSelecionada.substring(9));

        // TipoColeta
        String TipoColetaSelecionada = txtTipoColeta.getSelectedItem().toString();
        Integer CodigoTipoColetaInt = Integer.parseInt(TipoColetaSelecionada.substring(0, 6));
        boletim.setCodigoTipoColeta(CodigoTipoColetaInt);
        boletim.setTipoColeta(TipoColetaSelecionada.substring(9));

        // Turma
        String TurmaSelecionada = txtTurma.getSelectedItem().toString();
        Integer CodigoTurmaInt = Integer.parseInt(TurmaSelecionada.substring(0, 6));
        boletim.setCodigoTurma(CodigoTurmaInt);
        boletim.setTurma(TurmaSelecionada.substring(9));

        if (img != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            img.recycle();

            boletim.setFotoPragaTirada(byteArray);
        }

    }

    private boolean ValidaCampos(Boletim bol) {

        if (bol.getLatitude().toString().isEmpty()) {
            Mensagens.mostraMensagem(this, R.string.LatitudeInvalida);
            return false;
        } else if (bol.getLongitude().toString().isEmpty()) {
            Mensagens.mostraMensagem(this, R.string.LongitudeInvalida);
            return false;
        } else if (bol.getAltitude().toString().isEmpty()) {
            Mensagens.mostraMensagem(this, R.string.AltitudeInvalida);
            return false;
        } else if (bol.getData().isEmpty())
            return false;
        else if (bol.getFiscal().isEmpty())
            return false;
        else if (bol.getTurma().isEmpty())
            return false;
        else if (bol.getNomePraga().isEmpty())
            return false;
        else if (bol.getQuantidade().toString().isEmpty())
            return false;
        else if (bol.getEstagio().isEmpty())
            return false;
        else if (bol.getTipoColeta().isEmpty())
            return false;


        return true;
    }

    //---------------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                img = (Bitmap) bundle.get("data");

                ImagemTirada.setImageBitmap(img);

                //chgama o metodo e pega o URI da imagem
                Uri uri = (Uri) getImageUri(getApplicationContext(), img);

                //chama o metodo e pega o URI da imagem
                File file = new File(geRealPath(uri));

                Toast.makeText(this, "CAMINHO: " + file.getPath(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String geRealPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Uri getImageUri(Context context, Bitmap img) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), img, "Title", null);
        return Uri.parse(path);
    }


    //---------------------------------------------------------------------------------------------------------------------------
    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {

            Location ultimaLocalizacao = locationResult.getLastLocation();

            Latitude = ultimaLocalizacao.getLatitude();
            Longitude = ultimaLocalizacao.getLongitude();
            Altitude = ultimaLocalizacao.getAltitude();

            adicionaMarcador(new LatLng(ultimaLocalizacao.getLatitude(), ultimaLocalizacao.getLongitude()));

            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(ultimaLocalizacao.getLatitude(), ultimaLocalizacao.getLongitude()), 17));
            MapaBoletim.animateCamera(cameraUpdate);

            if (locationClient != null) {
                locationClient.removeLocationUpdates(locationCallback);
            }

        }
    };

}

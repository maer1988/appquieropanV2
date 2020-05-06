package com.example.appquieropan.Cliente;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.example.appquieropan.Adaptadores.Cliente.CustomInfoWindowAdapter;
import com.example.appquieropan.Cliente.CarroDeCompras.ComprobanteReservaCliente;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoItemVoucher;
import com.example.appquieropan.Entidad.Valoracion;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.EvaluacionCliente.EvaluacionServicio;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Cliente.ProductosDelProveedor.Amasanderias;
import com.example.appquieropan.Cliente.ProductosDelProveedor.OtrasMasas;
import com.example.appquieropan.Cliente.ProductosDelProveedor.Panaderias;
import com.example.appquieropan.Cliente.ProductosDelProveedor.Pastelerias;
import com.example.appquieropan.Cliente.SubProductosDelProveedor.SubProductoPanCliente;
import com.example.appquieropan.Entidad.Cliente;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//linea comentada

public class HomeCliente extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    public static final String idCliente = "id";
    private TextView txtCliente;
    Button salirP;
    private Button btnPanC, btnPastelC, btnMasaC, btnOtrasMC,Carro,Pedido;
    private String id = null;
    private GoogleMap map;
    private LocationManager locManager;
    private LocationManager locManagerUpdate;
    private Location loc;
    private LatLng Clientepos;
    boolean doubleClick = false;
    double longitudeGPS, latitudGPS;
    private Marker markert;
    DatabaseReference databaseReference;
    ArrayList<Proveedor> listproveedor = new ArrayList<Proveedor>();
    public static final int REQUEST_ACCESS_FINE;
    public ArrayList<ArrayList<String>> title = new ArrayList();

    static {
        REQUEST_ACCESS_FINE = 0;
    }

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView botonesNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.image_superior);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        salirP = findViewById(R.id.imgSalir_cliente);

        txtCliente = (TextView) findViewById(R.id.txtNombreCliente);
        id = getIntent().getStringExtra("id");
        Log.d("home",""+firebaseAuth.getCurrentUser().getUid());

        btnPanC = findViewById(R.id.btnPanCliente);
        btnPastelC = findViewById(R.id.btnPastelCliente);
        btnMasaC = findViewById(R.id.btnAmasaCliente);
        btnOtrasMC = findViewById(R.id.btnOtrasMasasCliente);


        //subirLatLongFirebase();


        salirP.setOnClickListener(this);

        btnPanC.setOnClickListener(this);
        btnPastelC.setOnClickListener(this);
        btnMasaC.setOnClickListener(this);
        btnOtrasMC.setOnClickListener(this);
        nombrecliente();
        this.ConsultaEvalucion();
        Log.d("LLL","ConsultaEvaluacion");

        botonesNav = findViewById(R.id.menu_inferior_home_cliente);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(HomeCliente.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
               else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(HomeCliente.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }


                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeCliente.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;

            }
        });

    }

    public void onResume() {
        super.onResume();
        this.checkGPS();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE);


        } else {


            this.obtenGPScliente(map);
            this.listarproveedor();
            map.setOnMarkerClickListener(this);

            /*locManager.requestLocationUpdates(locManager.GPS_PROVIDER, 0, 10000, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15.0f));
                    HomeCliente.this.listarproveedor();


                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });*/

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.imgSalir_cliente:
                signOut();
                break;

            case R.id.btnPanCliente:
                Intent listaPan = new Intent(this, Panaderias.class);
                startActivity(listaPan);
                break;
            case R.id.btnPastelCliente:
                Intent listPastel = new Intent(this, Pastelerias.class);
                startActivity(listPastel);
                break;
            case R.id.btnAmasaCliente:
                Intent listaMasa = new Intent(this, Amasanderias.class);
                startActivity(listaMasa);
                break;
            case R.id.btnOtrasMasasCliente:
                Intent listaOtrasMasas = new Intent(this, OtrasMasas.class);
                startActivity(listaOtrasMasas);
                break;
        }
    }

    /*
        //funcion que abre geolocalizacion
        private void subirLatLongFirebase() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(HomeCliente.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return;
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.e("Latitud: ", + location.getLatitude()+ "Longitud: "+ location.getLongitude());


                            }
                        }
                    });
        }

    */
    public void listarproveedor(){


        databaseReference.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot objSnatshot: dataSnapshot.getChildren() ){

                    Proveedor p = objSnatshot.getValue(Proveedor.class);

                    Log.d("prov",""+p.getNom_proveedor());

                    listproveedor.add(p);




                }

                Log.d("LOCATION",""+loc);


                if (loc != null) {

                    HomeCliente.this.proveedores(new LatLng(loc.getLatitude(), loc.getLongitude()), listproveedor);
                }

                else{

                    HomeCliente.this.proveedores(new LatLng(-33.4379352, -70.6503999), listproveedor);

                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Failed to read value.", databaseError.toException());

            }
        });


    }

    private void signOut() {

        firebaseAuth.signOut();
        Intent login = new Intent(getApplication(), loginCliente.class);
        startActivity(login);
        finish();

    }

    public void nombrecliente(){

        databaseReference.child("Cliente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Cliente cliente = snapshot.getValue(Cliente.class);

                    String uid= firebaseAuth.getCurrentUser().getUid();

                    if(uid.equals(cliente.getId_cliente())){
                        String nombre_usuario = cliente.getNom_cliente();
                        Log.d("Nom", "nombre ususario->>"+nombre_usuario);
                        txtCliente.setText("¡Bienvenido " + nombre_usuario + "!");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void alertOneButton() {

        new AlertDialog.Builder(HomeCliente.this)
                .setTitle("GPS desactivado")
                .setMessage("DEBE ACTIVAR sus servicio GPS para continuar")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                    }
                }).show();
    }

    public void checkGPS() {


        try {
            int gpsSignal = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (gpsSignal == 0) {

                this.alertOneButton();

            }

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ACCESS_FINE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permiso dado", Toast.LENGTH_SHORT).show();
                this.onMapReady(map);
            } else
                Toast.makeText(this, "permiso denegado", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    public void obtenGPScliente(GoogleMap googleMap) {

        googleMap.setMyLocationEnabled(true);
        locManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Log.d("loc",""+loc);
        if (loc != null) {
            LatLng PosCliente = new LatLng(loc.getLatitude(), loc.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(PosCliente));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(PosCliente, 11.0f));

        }else{

            LatLng PosCliente = new LatLng(-33.4379352,-70.6503999);
            map.moveCamera(CameraUpdateFactory.newLatLng(PosCliente));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(PosCliente, 11.0f));
        }



    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

    public void proveedores(LatLng pos, ArrayList<Proveedor> proveedor) {


        /**aca obtengo el arreglo de datos con las coordenadas y rut del proveedor*/


        for(int r=0;r< proveedor.size();r++){
            Log.d("TAG", "nom: "+proveedor.get(r).getNom_proveedor() + " / ");
            Log.d("TAG", "rut: "+proveedor.get(r).getRut_proveedor() + " / ");
            Log.d("TAG", "lat: "+proveedor.get(r).getLatitud() + " / ");
            Log.d("TAG", "long: "+proveedor.get(r).getLongitud()+ " / ");
            Log.d("TAG", "long:------------------------- " );

        }


        /**aca obtengo la posicion del cliente*/
        double C_lat = pos.latitude;
        double C_lng = pos.longitude;

        Log.d("pos_cli", ""+C_lat+"  "+C_lng );

        if (proveedor.size() > 0)
            for (int j = 0; j < proveedor.size(); j++) {

                final String rut=proveedor.get(j).getRut_proveedor();
                final String nombre_pro=proveedor.get(j).getNom_proveedor();

                double P_lat = Double.parseDouble(proveedor.get(j).getLatitud());
                double P_lng = Double.parseDouble(proveedor.get(j).getLongitud());

                double distancia = this.distFrom(C_lat, C_lng, P_lat, P_lng);

                // Log.d("distancia", "nom: " + distancia + " / ");

                if (distancia <= 30000.0) {

                    final LatLng pos_provedoor = new LatLng(P_lat, P_lng);
                    // Log.d("pos", "pos_proveedor: " + pos_provedoor + " / ");


                    databaseReference.child("Pan_Proveedor").orderByChild("rut_empresa").equalTo(rut).addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot datapan) {

                            final String[] pan = new String[1];
                            final String[] pastel = new String[1];
                            final String[] masa= new String[1];
                            final String[] otramasa= new String[1];


                            if(datapan.exists() == true){


                                Log.d("pan","entro rut"+rut);
                                pan[0] ="panaderia";


                            }
                            else {
                                pan[0] ="& ";

                            }





                            databaseReference.child("Pastel_Proveedor").orderByChild("rut_empresa").equalTo(rut).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datapastel) {


                                    if(datapastel.exists()==true){

                                        Log.d("pastel","entro rut"+rut);

                                        pastel[0] ="&Pasteleria";


                                    }


                                    else {

                                        pastel[0] ="& ";

                                    }


                                    databaseReference.child("OtrasMasas_Proveedor").orderByChild("rut_empresa").equalTo(rut).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataOtramasa) {


                                            if(dataOtramasa.exists()==true){

                                                otramasa[0]="&Otras masas";
                                                Log.d("otras_masas","entro rut"+rut);
                                            }
                                            else{

                                                otramasa[0]="& ";
                                            }

                                            databaseReference.child("Masa_Proveedor").orderByChild("rut_empresa").equalTo(rut).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot datamasa) {

                                                    if(datamasa.exists()==true){
                                                        masa[0]="&Masas";
                                                        Log.d("masas","entro rut"+rut);
                                                    }
                                                    else{
                                                        masa[0]="& ";
                                                    }



                                                    final double[] valor = {0.0};
                                                    final int[] registro = {0};
                                                    final double[] valoracion = {0};

                                                    databaseReference= FirebaseDatabase.getInstance().getReference("Valoracion");
                                                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot datavaloracion) {

                                                            for(DataSnapshot pvsnapshot: datavaloracion.getChildren()) {

                                                                Valoracion pv = pvsnapshot.getValue(Valoracion.class);


                                                                if (pv.getRut_valorado().equals(rut)) {


                                                                    valor[0] = valor[0] + Double.parseDouble(pv.getValoracion());
                                                                    registro[0] = registro[0] + 1;



                                                                }

                                                                if (registro[0] == 0) {

                                                                    registro[0] = 1;
                                                                }

                                                                valoracion[0] = valor[0] / registro[0];
                                                                Log.d("QQQ", "" + valoracion[0]);


                                                            }

                                                            create_market(nombre_pro,pan[0]+pastel[0]+masa[0]+otramasa[0],pos_provedoor,rut,valoracion[0]);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });



                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }

            }


    }

    @Override
    /*public boolean onMarkerClick(Marker marker) {
        return false;
    }*/

    public boolean onMarkerClick(Marker marker) {


            Intent intent = new Intent(this, SubProductoPanCliente.class);
            intent.putExtra(SubProductoPanCliente.rutE,marker.getTag().toString());
            startActivity(intent);
        return false;
    }

    public void ConsultaEvalucion(){


        databaseReference.child("Voucher").orderByChild("idcliente").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot Voucher_snapshot : dataSnapshot.getChildren()){

                    Voucher v = Voucher_snapshot.getValue(Voucher.class);

                    if(v.getEstado().equals("entregado") && v.getValorado().equals("0")){
                        Log.d("LOG","Entro");
                        Intent Evaluacion = new Intent(HomeCliente.this,EvaluacionServicio.class);
                        Evaluacion.putExtra(EvaluacionServicio.Voucher,v.getIDVoucher());
                        Evaluacion.putExtra(EvaluacionServicio.rutproveedor,v.getRUTproveedor());
                        Evaluacion.putExtra(EvaluacionServicio.nombreproveedor,v.getNombreproveedor());
                        Evaluacion.putExtra(EvaluacionServicio.txtcliente,txtCliente.getText());
                        startActivity(Evaluacion);



                    }

                    else {
                        Log.d("LOG","NO Entro");


                    }

                }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void create_market(String name,String Arrtitulo, LatLng pos_provedoor,String rut,double valoracion){


        String valor= String.valueOf(valoracion);

        String[] info = Arrtitulo.split("&");
        markert=  map.addMarker(new MarkerOptions().position(pos_provedoor));
        markert.setTag(rut);

        Log.d("arreglo",""+Arrtitulo);
        Log.d("split","_0"+info[0]+"_1"+info[1]+"_2"+info[2]+"_3"+info[3]);


        if(info[0].equals("panaderia") && info[1].equals(" ") && info[2].equals(" ") && info[3].equals(" "))
        {
                markert.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sandwich2));

        }

         if (info[0].equals("panaderia") && info[1].equals("Pasteleria") && info[2].equals(" ") && info[3].equals(" ")) {

             markert.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.panaderia_pasteleria));
        }
             if (info[0].equals("panaderia") && info[1].equals("Pasteleria") && info[2].equals("Masas")) {

                markert.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.panaderia_pasteleria_masas));
        }
                 if(info[0].equals(" ") && info[1].equals("Pasteleria") && info[2].equals(" ") && info[3].equals(" ") ) {

                markert.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sandwich4));
        }
                  if (info[0].equals(" ") && info[1].equals(" ") && info[2].equals("Masas") && info[3].equals(" ") )
        {

            markert.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sandwich3));
        }
                     if (info[0].equals("panaderia") && info[1].equals(" ") && info[2].equals(" ") && info[3].equals(" ") )
        {

                     markert.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sandwich2));
        }



        Log.d("MArket_ti",""+markert.getTitle());

    }


    @Override
    public void onBackPressed(){


    }

}

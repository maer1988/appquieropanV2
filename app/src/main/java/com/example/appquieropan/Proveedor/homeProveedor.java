package com.example.appquieropan.Proveedor;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.appquieropan.Proveedor.RegistroProveedor.registroProveedor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.R;
import com.example.appquieropan.Proveedor.Ventas.tipoVenta;
import com.example.appquieropan.opciones;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class homeProveedor extends AppCompatActivity {

    public static final String userProveedor="names";
    public static final String codigoProvve="id";
    TextView txtProveedor;
    ImageView salirP;
    Button facturas, segmentos, ventas;
    private String rutEmpresa = null;
    private String nombreEmpresa = null;

    private String userP = null;
    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_proveedor);
        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.image_superior);

        txtProveedor =(TextView)findViewById(R.id.txtProveedor);
        userP = getIntent().getStringExtra("id");


        salirP = findViewById(R.id.imgSalir);
        facturas = findViewById(R.id.idFactura);
        segmentos = findViewById(R.id.idSegmento);
        ventas = findViewById(R.id.idVenta);

        botonesNav = findViewById(R.id.botones_navegacion);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Toast.makeText(homeProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(homeProveedor.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    //Toast.makeText(homeProveedor.this, "factura", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(homeProveedor.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(homeProveedor.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(homeProveedor.this, tipoVenta.class);
                    intent.putExtra(tipoVenta.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                return false;

            }
        });

        salirP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        facturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMisFacturas();
            }
        });

        segmentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segmentosProveedor();
            }
        });

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ventasProveedor();
            }
        });

        obtieneRutEmpresa(userP);
        ConfirmarRegistro();
    }

    private void obtieneRutEmpresa(final String userP) {

        databaseReference.child("Proveedor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Proveedor proveedor = snapshot.getValue(Proveedor.class);

                    if(userP.equals(proveedor.getUid())){
                        //cargaDatosPantalla(proveedor.getNom_tipoSubProducto(), subProducto.getDesc_tipoSubProducto(),subProducto.getPrecio(),subProducto.getUrlSubproducto());
                        rutEmpresa = proveedor.getRut_proveedor();
                        nombreEmpresa = proveedor.getNom_proveedor();
                        txtProveedor.setText("¡Bienvenido "+ nombreEmpresa +"!");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void ventasProveedor() {
        Intent ventaP = new Intent(getApplication(), tipoVenta.class);
        ventaP.putExtra(tipoVenta.codigoProvve,userP);
        startActivity(ventaP);
        finish();
    }

    private void segmentosProveedor() {
        Intent segmentosP = new Intent(getApplication(), opcionesSegmentoProveedor.class);
        segmentosP.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
        startActivity(segmentosP);
        finish();
    }

    private void verMisFacturas() {

        Intent facturasP = new Intent(getApplication(), facturasProveedor.class);
        facturasP.putExtra(facturasProveedor.codigoProvve,userP);
        startActivity(facturasP);
        finish();

    }

    private void ConfirmarRegistro(){

        databaseReference.child("Proveedor").orderByChild("uid").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() == false){

                    Intent intencion = new Intent(getApplication(), registroProveedor.class);
                    intencion.putExtra(registroProveedor.codigoProvve, firebaseAuth.getCurrentUser().getUid());
                    startActivity(intencion);

                }else {


                    Log.d("EX", "YA EXISTE EL CLIENTE");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void signOut() {

        firebaseAuth.signOut();
        Intent login = new Intent(getApplication(), opciones.class);
        startActivity(login);
        finish();

    }

    @Override
    public void onBackPressed(){


    }

}

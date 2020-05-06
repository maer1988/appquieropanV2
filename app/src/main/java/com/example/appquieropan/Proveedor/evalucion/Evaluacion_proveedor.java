package com.example.appquieropan.Proveedor.evalucion;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Proveedor.*;

import com.example.appquieropan.Entidad.Valoracion;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.Ventas.tipoVenta;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Evaluacion_proveedor extends AppCompatActivity implements View.OnClickListener {

    private TextView cliente,proveedor;
    private Button evaluar;
    private RatingBar ratingbar;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    public  String Idvoucher,rut_cliente,razonsocial,nombre_cliente;
    public static final String Voucher="voucher";
    public static final String rutproveedor="rut_cliente";
    public static final String nombreproveedor="nombre_proveedor";
    public static final String txtcliente="nombre_cliente";
    ArrayList<Voucher> voucher_list= new ArrayList<Voucher>();

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_proveedor);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        ratingbar = findViewById(R.id.rating2);
        evaluar= findViewById(R.id.evaluar2);
        cliente = findViewById(R.id.textcliente2);
        proveedor = findViewById(R.id.textproveedor2);
        Idvoucher= getIntent().getStringExtra("voucher");
        rut_cliente= getIntent().getStringExtra("rut_cliente");
        razonsocial= getIntent().getStringExtra("nombre_proveedor");
        nombre_cliente= getIntent().getStringExtra("nombre_cliente");
        evaluar.setOnClickListener(this);
        final String userP = getIntent().getStringExtra("id");
        cliente.setText("Hola "+razonsocial+" Podrias evaluar experiencia con ");
        proveedor.setText("el cliente: "+nombre_cliente);

         botonesNav = findViewById(R.id.botones_navegaPerfil);

        botonesNav.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Toast.makeText(Evaluacion_proveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Evaluacion_proveedor.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    //Toast.makeText(homeProveedor.this, "factura", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Evaluacion_proveedor.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Evaluacion_proveedor.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Evaluacion_proveedor.this, tipoVenta.class);
                    intent.putExtra(tipoVenta.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                return false;

            }
        });


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.evaluar2:

                Valoracion pe = new Valoracion();
                pe.setIDvalorado(UUID.randomUUID().toString());
                pe.setIdusuario(firebaseAuth.getCurrentUser().getUid());
                pe.setRut_valorado(rut_cliente);
                pe.setValoracion(String.valueOf(ratingbar.getRating()));
                pe.setIDvoucher(Idvoucher);

                evaluar(pe);

                break;


        }

    }


public  void evaluar(Valoracion pv){

    mDatabase.child("Valoracion").child(pv.getIDvalorado()).setValue(pv);

    Toast t = Toast.makeText(Evaluacion_proveedor.this, "Gracias por tu tiempo!!", Toast.LENGTH_LONG);
    t.show();
    Intent Home = new Intent(Evaluacion_proveedor.this,homeProveedor.class);
    Home.putExtra(homeProveedor.codigoProvve,voucher_list.get(0).getIDproveedor());
    startActivity( Home);



}


    @Override
    public void onBackPressed(){


    }

}

package com.example.appquieropan.Cliente.EvaluacionCliente;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Valoracion;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class EvaluacionServicio extends AppCompatActivity implements View.OnClickListener {

    private TextView cliente,proveedor;
    private Button evaluar;
    private RatingBar ratingbar;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    public  String Idvoucher,rut_proveedor,razonsocial,nombre_cliente;
    public static final String Voucher="voucher";
    public static final String rutproveedor="proveedor";
    public static final String nombreproveedor="nombre_proveedor";
    public static final String txtcliente="nombre_cliente";
    ArrayList<Voucher> voucher_list= new ArrayList<Voucher>();

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_servicio);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        ratingbar = findViewById(R.id.rating);
        evaluar= findViewById(R.id.evaluar);
        cliente = findViewById(R.id.textcliente);
        proveedor = findViewById(R.id.textproveedor);
        Idvoucher= getIntent().getStringExtra("voucher");
        rut_proveedor= getIntent().getStringExtra("proveedor");
        razonsocial= getIntent().getStringExtra("nombre_proveedor");
        nombre_cliente= getIntent().getStringExtra("nombre_cliente");
        evaluar.setOnClickListener(this);

        cliente.setText("Hola "+nombre_cliente+" Podrias evaluar tu ultima compra con:");
        proveedor.setText("proveedor: "+razonsocial+" Rut: "+rut_proveedor);

        botonesNav = findViewById(R.id.botones_navegacion_evaluacion);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(EvaluacionServicio.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluacionServicio.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(EvaluacionServicio.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluacionServicio.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluacionServicio.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
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

            case R.id.evaluar:

                Valoracion pe = new Valoracion();
                pe.setIDvalorado(UUID.randomUUID().toString());
                pe.setIdusuario(firebaseAuth.getCurrentUser().getUid());
                pe.setRut_valorado(rut_proveedor);
                pe.setValoracion(String.valueOf(ratingbar.getRating()));
                pe.setIDvoucher(Idvoucher);

                evaluar(pe);

                break;


        }

    }


public  void evaluar(Valoracion pv){

    mDatabase.child("Valoracion").child(pv.getIDvalorado()).setValue(pv);


    mDatabase.child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot Voucher_snapshot : dataSnapshot.getChildren()){

                Voucher v = Voucher_snapshot.getValue(Voucher.class);

                if(v.getIDVoucher().equals(Idvoucher)){

                    voucher_list.add(v);

                }

            }

            voucher_list.get(0).setValorado("1");

            mDatabase.child("Voucher").child(voucher_list.get(0).getIDVoucher()).setValue(voucher_list.get(0));



            Toast t = Toast.makeText(EvaluacionServicio.this, "Gracias por tu tiempo!!", Toast.LENGTH_LONG);
            t.show();
            Intent Home = new Intent(EvaluacionServicio.this, HomeCliente.class);
            startActivity( Home);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


}


    @Override
    public void onBackPressed(){


    }

}

package com.example.appquieropan.Cliente.CarroDeCompras;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.appquieropan.Adaptadores.Cliente.RecyclerVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListadoVoucher extends AppCompatActivity implements View.OnClickListener {

    private RecyclerVoucher recyclerVoucher;
    private DatabaseReference mDatabase;
    ArrayList<Voucher> list_voucher = new ArrayList<Voucher>();
    private FirebaseAuth firebaseAuth;
    public static final String opcion_voucher="ov";
    String flag_voucher;
    private TextView subtitulo,datos_vacios;

    RecyclerView mRecyclerView;


    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_voucher);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);

        flag_voucher = getIntent().getStringExtra("ov");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        subtitulo= findViewById(R.id.subtitulo);
        datos_vacios=findViewById(R.id.datos_vacios);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        CargarRecyclerView(mRecyclerView);
        despliegaSubtitulo(flag_voucher);
        botonesNav = findViewById(R.id.menu_inferior_voucher);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(ListadoVoucher.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoVoucher.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(ListadoVoucher.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoVoucher.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoVoucher.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;

            }
        });


    }


    public void CargarRecyclerView(final RecyclerView recyclerView){




        mDatabase.child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnatshot : dataSnapshot.getChildren()) {


                    Voucher v = objSnatshot.getValue(Voucher.class);

                    if (v.getIDcliente().equals(firebaseAuth.getCurrentUser().getUid()) && v.getEstado().equals(flag_voucher)) {

                        list_voucher.add(v);

                    }

                }


                if (list_voucher.size() > 0) {
                    recyclerVoucher = new RecyclerVoucher(list_voucher);
                    recyclerView.setAdapter(recyclerVoucher);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    datos_vacios.setVisibility(View.GONE);
                }
                    else {

                    mRecyclerView.setVisibility(View.GONE);
                    datos_vacios.setVisibility((View.VISIBLE));

                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void despliegaSubtitulo(String tipo){

switch (tipo){

    case "entregado":
        subtitulo.setText("Listado de comprobantes de ventas entregadas");
        break;
    case "pendiente":
        subtitulo.setText("Listado de comprobantes que estan pendientes de retirar");
        break;

}



    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onBackPressed(){

finish();
    }

}

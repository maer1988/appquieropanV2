package com.example.appquieropan.Cliente.CarroDeCompras;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appquieropan.Adaptadores.Cliente.RecyclerItemCarro;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Producto_Pedido;
import com.example.appquieropan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListadoItemCarro extends AppCompatActivity implements View.OnClickListener {

    private RecyclerItemCarro RecyclerItemCarro;
    private DatabaseReference mDatabase;
    ArrayList<Producto_Pedido> listproducto_pedido = new ArrayList<Producto_Pedido>();
    private FirebaseAuth firebaseAuth;
    Button btnCancelar,btnconfirmar;
    public static final String tipo_pago="tp";
    private TextView total;
    String TipoPago;
    RecyclerView mRecyclerView;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_item_carro);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);
        btnCancelar = findViewById(R.id.cancelar);
        btnconfirmar = findViewById(R.id.confirmar);
        total = findViewById(R.id.total);
        btnconfirmar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        TipoPago= getIntent().getStringExtra("tp");
        Log.d("TIPO_PAGO",""+TipoPago);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        CargarRecyclerView(mRecyclerView);

        botonesNav = findViewById(R.id.botones_navegacion_item_List_carro);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(ListadoItemCarro.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoItemCarro.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(ListadoItemCarro.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoItemCarro.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoItemCarro.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


    }


    public void CargarRecyclerView(final RecyclerView recyclerView){




   mDatabase.child("Producto_pedido").addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           int total_carro=0;
           for(DataSnapshot objSnatshot: dataSnapshot.getChildren() ){


               Producto_Pedido pp = objSnatshot.getValue(Producto_Pedido.class);

               if(pp.getEstado().equals("nuevo") && pp.getIdCliente().equals(firebaseAuth.getCurrentUser().getUid())) {

                   listproducto_pedido.add(pp);
                   total_carro = total_carro + Integer.parseInt(pp.total_precio());
               }

           }

           Log.d("XXX",""+listproducto_pedido.size());
           total.setText(Integer.toString(total_carro));
           RecyclerItemCarro = new RecyclerItemCarro(listproducto_pedido);


           recyclerView.setAdapter(RecyclerItemCarro);

       }

       @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {

       }
   });



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.cancelar:

                Intent Home = new Intent(ListadoItemCarro.this, HomeCliente.class);
                startActivity(Home);
                finish();

                break;

            case R.id.confirmar:




                Intent Pagodelcliente = new Intent(ListadoItemCarro.this, PagoDelCliente.class);
                Pagodelcliente.putExtra(PagoDelCliente.tipo_pago,TipoPago);
                startActivity(Pagodelcliente);



        }

    }

    @Override
    public void onBackPressed(){


    }

}

package com.example.appquieropan.Cliente.CarroDeCompras;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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


        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "ESTOY EN LISTADO CARRO", Toast.LENGTH_SHORT);

        toast1.show();

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
        final int[] total_carro = {0};
        db.collection("Producto_pedido")
                .whereEqualTo("estado", "nuevo")
                .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto_Pedido pp = document.toObject(Producto_Pedido.class);
                                listproducto_pedido.add(pp);
                                total_carro[0] = total_carro[0] + Integer.parseInt(pp.total_precio());
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                            total.setText(Integer.toString(total_carro[0]));
                            RecyclerItemCarro = new RecyclerItemCarro(listproducto_pedido);


                            recyclerView.setAdapter(RecyclerItemCarro);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
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

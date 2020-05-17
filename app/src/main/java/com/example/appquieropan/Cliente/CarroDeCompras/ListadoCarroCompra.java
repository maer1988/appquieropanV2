package com.example.appquieropan.Cliente.CarroDeCompras;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.example.appquieropan.Entidad.Proveedor;
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

public class ListadoCarroCompra extends AppCompatActivity implements View.OnClickListener {
    private com.example.appquieropan.Adaptadores.Cliente.RecyclerItemCarro RecyclerItemCarro;
    private DatabaseReference mDatabase;
    ArrayList<Producto_Pedido> listproducto_pedido = new ArrayList<Producto_Pedido>();
    private FirebaseAuth firebaseAuth;
    Button btnLimpiar, btnPagar;
    private TextView total;
    RecyclerView mRecyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_carro_compra);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);
        btnLimpiar= findViewById(R.id.limpiar);
        btnPagar = findViewById(R.id.pagar);
        total = findViewById(R.id.total);

        btnLimpiar.setOnClickListener(this);
        btnPagar.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        CargarRecyclerView(mRecyclerView);

        botonesNav = findViewById(R.id.menu_inferior_carro_compras);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(ListadoCarroCompra.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoCarroCompra.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(ListadoCarroCompra.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoCarroCompra.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoCarroCompra.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


    }


    public void CargarRecyclerView(final RecyclerView recyclerView){


        db.collection("Producto_pedido")
                .whereEqualTo("estado", "nuevo")
                .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int total_carro=0;


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto_Pedido pp = document.toObject(Producto_Pedido.class);

                                listproducto_pedido.add(pp);
                                Log.d("XXX",""+total_carro);
                                total_carro = total_carro + Integer.parseInt(pp.total_precio());
                                Log.d("zzz",""+total_carro);

                            }

                            Log.d("XXX",""+listproducto_pedido.size());

                            total.setText(Integer.toString(total_carro));

                            RecyclerItemCarro = new RecyclerItemCarro(listproducto_pedido);


                            recyclerView.setAdapter(RecyclerItemCarro);

                        } else {
                            Log.d("TAG" ,"Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.limpiar:


                db.collection("Producto_pedido")
                        .whereEqualTo("estado", "nuevo")
                        .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        db.collection("Producto_pedido").document(document.getId()).delete();

                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });




                Intent Pagodelcliente = new Intent(ListadoCarroCompra.this, HomeCliente.class);
                startActivity(Pagodelcliente);
                Toast t = Toast.makeText(ListadoCarroCompra.this, "Carro de compras limpiado con exito!!", Toast.LENGTH_LONG);
                t.show();


                break;

            case R.id.pagar:


                db.collection("Producto_pedido")
                        .whereEqualTo("estado", "nuevo")
                        .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                final String[] TipoPago = new String[1];

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Producto_Pedido pp = document.toObject(Producto_Pedido.class);
                                        listproducto_pedido.add(pp);
                                    }

                                    if (listproducto_pedido.size() >0){

                                        db.collection("Proveedor")
                                                .whereEqualTo("rut_proveedor", listproducto_pedido.get(0).getRut_proveedor())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                        if (task2.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document2 : task2.getResult()) {

                                                                TipoPago[0] = (String) document2.get("tipo_Pago_Proveedor");

                                                                Intent Pagodelcliente = new Intent(ListadoCarroCompra.this, PagoDelCliente.class);
                                                                Pagodelcliente.putExtra(PagoDelCliente.tipo_pago, TipoPago[0]);
                                                                startActivity(Pagodelcliente);
                                                                finish();


                                                            }
                                                        } else {
                                                            Log.d("TAG", "Error getting documents: ", task2.getException());
                                                        }
                                                    }
                                                });


                                    }

                                    else{

                                        Toast t = Toast.makeText(ListadoCarroCompra.this, "NO TIENES PRODUTOS PARA PAGAR!!", Toast.LENGTH_LONG);
                                        t.show();


                                    }

                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });









        }

    }

    public void onBackPressed(){

finish();

    }

}


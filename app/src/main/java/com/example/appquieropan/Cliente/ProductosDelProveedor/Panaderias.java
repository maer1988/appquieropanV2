package com.example.appquieropan.Cliente.ProductosDelProveedor;

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
import android.widget.Toast;

import com.example.appquieropan.Adaptadores.Cliente.RecyclerProveedorPan;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.Valoracion;
import com.example.appquieropan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Panaderias extends AppCompatActivity implements View.OnClickListener{

    public static final String categoria="categoria";
    private RecyclerProveedorPan recyclerProductosAdapter;
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    boolean valor = true;
    String tipo_categoria;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panaderias);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = findViewById(R.id.myListaProveedoresCliente);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        tipo_categoria= getIntent().getStringExtra("categoria");

        CargarRecyclerView(mRecyclerView,tipo_categoria);

        botonesNav = findViewById(R.id.botones_navegacion_pan);

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        "ACT. PANADERIA", Toast.LENGTH_SHORT);

        toast1.show();


        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(Panaderias.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Panaderias.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(Panaderias.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Panaderias.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Panaderias.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {



    }

    public void CargarRecyclerView(final RecyclerView recyclerView,String tipo){


        final int[] pos = {0};
        final ArrayList<Proveedor> arrayListTipo = new ArrayList<>();




        Query tipoQ = db.collection("proveedor_categoria").whereEqualTo("categoria", tipo);

       tipoQ.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {

                                Toast toast1 =
                                        Toast.makeText(getApplicationContext(),
                                                "No hay registros para mostrar en "+tipo_categoria, Toast.LENGTH_SHORT);

                                toast1.show();

                            }
                            else{

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    db.collection("Proveedor")
                                            .whereEqualTo("rut_proveedor", document.get("rut_proveedor"))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                                    if (task2.isSuccessful()) {

                                                        for (QueryDocumentSnapshot document2 : task2.getResult()) {

                                                            final Proveedor p = document2.toObject(Proveedor.class);

                                                            final double[] valor = {0.0};
                                                            final int[] registro = {0};
                                                            final double[] valoracion = {0};

                                                            db.collection("Valoracion")
                                                                    .whereEqualTo("rut_valorado", p.getRut_proveedor())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task3) {
                                                                            if (task3.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot document3 : task3.getResult()) {


                                                                                    Valoracion pv = document3.toObject(Valoracion.class);

                                                                                    valor[0] = valor[0] + Double.parseDouble(pv.getValoracion());
                                                                                    registro[0] = registro[0] + 1;


                                                                                    Log.d("Valor", "valor: " + valor[0] + " Registro:" + registro[0] + " en la poscicion: " + pos[0]);



                                                                                    if (registro[0] == 0) {

                                                                                        registro[0] = 1;
                                                                                    }

                                                                                    valoracion[0] = valor[0] / registro[0];
                                                                                    Log.d("QQQ", "" + valoracion[0]);


                                                                                    p.setNota_proveeedor(String.valueOf(valoracion[0]));




                                                                                }
                                                                                arrayListTipo.add(p);
                                                                                recyclerProductosAdapter = new RecyclerProveedorPan(getApplicationContext(),R.layout.item_proveedores,arrayListTipo);
                                                                                recyclerView.setAdapter(recyclerProductosAdapter);
                                                                            } else {
                                                                                Log.d("TAG", "Error getting documents: ", task3.getException());
                                                                            }
                                                                        }
                                                                    });


                                                        }



                                                    } else {
                                                        Log.d("TAG", "Error getting documents: ", task2.getException());
                                                    }
                                                }
                                            });

                                }

                            }


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }








}

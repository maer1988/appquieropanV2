package com.example.appquieropan.Cliente.ProductosDelProveedor;

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

import java.util.ArrayList;

public class Panaderias extends AppCompatActivity implements View.OnClickListener{


    private RecyclerProveedorPan recyclerProductosAdapter;
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView;
    boolean valor = true;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panaderias);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = findViewById(R.id.myListaProveedoresCliente);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        CargarRecyclerView(mRecyclerView);

        botonesNav = findViewById(R.id.botones_navegacion_pan);
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

    public void CargarRecyclerView(final RecyclerView recyclerView){


        final int[] pos = {0};


        mDatabase.child("Proveedor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final ArrayList<Proveedor> arrayListTipo = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    final Proveedor proveedor1 = snapshot.getValue(Proveedor.class);

                    // if(rutEmpresa.equals(subProducto.getRut_Empresa())) {
                    String rutEmpresa = proveedor1.getRut_proveedor();

                    mDatabase.child("Pan_Proveedor").orderByChild("rut_empresa").equalTo(rutEmpresa).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            if(dataSnapshot2.exists() == true){


                                final double[] valor = {0.0};
                                final int[] registro = {0};
                                final double[] valoracion = {0};

                                mDatabase= FirebaseDatabase.getInstance().getReference("Valoracion");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot pvsnapshot: dataSnapshot.getChildren()) {

                                            Valoracion pv = pvsnapshot.getValue(Valoracion.class);


                                            if (pv.getRut_valorado().equals(proveedor1.getRut_proveedor())) {


                                                valor[0] = valor[0] + Double.parseDouble(pv.getValoracion());
                                                registro[0] = registro[0] + 1;


                                                Log.d("Valor", "valor: " + valor[0] + " Registro:" + registro[0] + " en la poscicion: " + pos[0]);

                                            }

                                            if (registro[0] == 0) {

                                                registro[0] = 1;
                                            }

                                            valoracion[0] = valor[0] / registro[0];
                                            Log.d("QQQ", "" + valoracion[0]);


                                            proveedor1.setNota_proveeedor(String.valueOf(valoracion[0]));


                                        }
                                        arrayListTipo.add(proveedor1);





                                        recyclerProductosAdapter = new RecyclerProveedorPan(getApplicationContext(),R.layout.item_proveedores,arrayListTipo);
                                        recyclerView.setAdapter(recyclerProductosAdapter);



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }

                            else{

                                Log.d("X","nada");


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                    //}

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }







}

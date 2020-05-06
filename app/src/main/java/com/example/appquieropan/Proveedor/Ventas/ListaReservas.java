package com.example.appquieropan.Proveedor.Ventas;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.appquieropan.opciones;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.appquieropan.Adaptadores.Proveedor.RecyclerVoucherProveedor;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.homeProveedor;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListaReservas extends AppCompatActivity implements View.OnClickListener {

    private RecyclerVoucherProveedor recyclerVoucherProveedor;
    private DatabaseReference mDatabase;
    ArrayList<Voucher> list_voucher = new ArrayList<Voucher>();
    private FirebaseAuth firebaseAuth;

    RecyclerView mRecyclerView;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.recyclerView);

        final String userP = firebaseAuth.getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        CargarRecyclerView(mRecyclerView);

        botonesNav = findViewById(R.id.botones_navegaVenta);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(ListaReservas.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaReservas.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(ListaReservas.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Intent intent = new Intent(ListaReservas.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {

                    Toast.makeText(ListaReservas.this, "venta", Toast.LENGTH_SHORT).show();

                }
                return false;

            }
        });


    }


    public void CargarRecyclerView(final RecyclerView recyclerView){




        mDatabase.child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnatshot: dataSnapshot.getChildren() ){


                    Voucher v = objSnatshot.getValue(Voucher.class);

                    Log.d("id_pro",""+firebaseAuth.getCurrentUser().getUid());
                    Log.d("id_pro_vouc",""+v.getIDproveedor());

                    if(v.getIDproveedor().equals(firebaseAuth.getCurrentUser().getUid()) && (v.getTipoVenta().equals("efectivo") || v.getTipoVenta().equals("ambosPagos") )) {




                        list_voucher.add(v);

                    }

                }



                recyclerVoucherProveedor = new RecyclerVoucherProveedor(list_voucher);


                recyclerView.setAdapter(recyclerVoucherProveedor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onBackPressed(){



            Intent intencion = new Intent(getApplication(), tipoVenta.class);
            startActivity(intencion);
            finish();


    }

}

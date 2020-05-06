package com.example.appquieropan.Cliente.SubProductosDelProveedor;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Adaptadores.Cliente.RecyclerProveedorMasaSel;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SubProductoMasaCliente extends AppCompatActivity {


    public static final String rutE="rut";

    private DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseStorage mStorage;
    RecyclerView mRecyclerView;

    private TextView nombreProveedorPan;

    private RecyclerProveedorMasaSel recyclerProductosAdapter;
    private ImageView imagenProveedoresCliente;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_producto_masa_cliente);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        imagenProveedoresCliente = findViewById(R.id.idImagenPCliente);
        nombreProveedorPan = findViewById(R.id.idNomProveedorCliente);

        String rutEmpresaElegida = getIntent().getStringExtra("rut");

        Toast.makeText(this, rutEmpresaElegida, Toast.LENGTH_SHORT).show();

        cargaDatosProveedorElegido(rutEmpresaElegida);

        mRecyclerView = findViewById(R.id.myMasaProdCliProv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        botonesNav = findViewById(R.id.botones_navegacion_sub_masa);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(SubProductoMasaCliente.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubProductoMasaCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(SubProductoMasaCliente.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubProductoMasaCliente.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubProductoMasaCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }

    private void cargaDatosProveedorElegido(final String idProveedor) {

        mDatabase.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Proveedor proveedor = snapshot.getValue(Proveedor.class);

                    Log.e("Datos","" + idProveedor);

                    if(idProveedor.equals(proveedor.getRut_proveedor())){
                        Log.e("Reales","" + proveedor.getNom_proveedor());
                        cargaImagenProveedor(proveedor.getUrl_proveedor(),idProveedor, proveedor.getNom_proveedor());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void cargaImagenProveedor(String url, String rutProveedor, String nombreP) {

        nombreProveedorPan.setText(nombreP);

        if (url != null) {
            Glide
                    .with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imagenProveedoresCliente.setVisibility(View.VISIBLE);
                            imagenProveedoresCliente.setImageResource(R.drawable.imagen_no_disponible);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imagenProveedoresCliente.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imagenProveedoresCliente);
        }else{
            imagenProveedoresCliente.setImageResource(R.drawable.imagen_no_disponible);
        }
        cargaSubProductosDelProveedor(rutProveedor);
    }

    private void cargaSubProductosDelProveedor(final String rutProveedor) {

        mDatabase.child("SubProductoMasa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<TipoSubProducto> arrayListTipo = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    TipoSubProducto pasteles = snapshot.getValue(TipoSubProducto.class);

                    Log.e("Datos","subpro" + rutProveedor);

                    if(rutProveedor.equals(pasteles.getRut_Empresa())){

                        Log.e("iguales","iguales"+pasteles.getNom_tipoSubProducto());
                        //  Toast.makeText(SubProductoPanCliente.this, "Carga exitosa", Toast.LENGTH_SHORT).show();
                        //cargaImagenProveedor(TipoSubProducto pastel);

                        pasteles.setNom_tipoSubProducto(pasteles.getNom_tipoSubProducto());
                        pasteles.setDesc_tipoSubProducto(pasteles.getDesc_tipoSubProducto());
                        pasteles.setUrlSubproducto(pasteles.getUrlSubproducto());
                        pasteles.setUid(pasteles.getUid());
                        arrayListTipo.add(pasteles);

                    }
                }

                recyclerProductosAdapter = new RecyclerProveedorMasaSel(getApplicationContext(),R.layout.item_subproductos,arrayListTipo);
                mRecyclerView.setAdapter(recyclerProductosAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}


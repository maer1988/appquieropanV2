package com.example.appquieropan.Cliente.DetalleProductoDelProveedor;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Cliente.CarroDeCompras.CarroComprasCliente;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.example.appquieropan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SubProductoElegidoPan extends AppCompatActivity implements View.OnClickListener {

    public static final String idSubproducto="cod";
    public static final String Tabla="tabla";
    public static final String Nombre_tabla="";



    private ImageView imgPrdElegido;
    private TextView txtDescripProducto,rutProveedor;
    private CheckBox chkLocal, chkDeli;
    private Button btnSiguiente;

    private DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseStorage mStorage;
    private String codigoSubProducto;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_producto_elegido);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        imgPrdElegido = findViewById(R.id.imgProdClienteElegido);
        txtDescripProducto = findViewById(R.id.txtDescripcionProdCliente);
        chkLocal = findViewById(R.id.checkLocal);
        chkDeli = findViewById(R.id.checkDelivery);
        btnSiguiente = findViewById(R.id.btnEligeSiguiente);


        codigoSubProducto = getIntent().getStringExtra("cod");

        Toast.makeText(this, codigoSubProducto, Toast.LENGTH_SHORT).show();

        btnSiguiente.setOnClickListener(this);

        cargaDatosProductoElegido(codigoSubProducto);
        rutProveedor=findViewById(R.id.rutProveedor);
      //  Log.d("rut_prov","XXX" +  rutProveedor.getText().toString());

        botonesNav = findViewById(R.id.botones_navegacion_subprd_elegido);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(SubProductoElegidoPan.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubProductoElegidoPan.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(SubProductoElegidoPan.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubProductoElegidoPan.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SubProductoElegidoPan.this, ListadoVoucher.class);
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

        switch (v.getId()){
            case R.id.btnEligeSiguiente:
                Intent carroPan = new Intent(this, CarroComprasCliente.class);
                // envio el paso de parametros
                MandarDatos(codigoSubProducto,carroPan);

                Toast.makeText(this, "al carro por github", Toast.LENGTH_SHORT).show();
        }

    }

    private void cargaDatosProductoElegido(final String idProducto) {

        mDatabase.child(getIntent().getStringExtra("tabla")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    TipoSubProducto tipoSubProducto = snapshot.getValue(TipoSubProducto.class);

                    Log.e("Datos","" + idProducto);
                    Log.e("Tipo","" + tipoSubProducto.getUid());

                    if(idProducto.equals(tipoSubProducto.getUid())){
                        Log.e("Reales","" + tipoSubProducto.getNom_tipoSubProducto());
                        cargaImagenProducto(tipoSubProducto.getUrlSubproducto(),tipoSubProducto.getRut_Empresa(), tipoSubProducto.getDesc_tipoSubProducto());
                        Log.d("rut_prov","XXX" +  tipoSubProducto.getRut_Empresa());

                    }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void MandarDatos(final String idProducto, final Intent carroPan){

        mDatabase.child(getIntent().getStringExtra("tabla")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    TipoSubProducto tipoSubProducto2 = snapshot.getValue(TipoSubProducto.class);



                    if(idProducto.equals(tipoSubProducto2.getUid())){


                        carroPan.putExtra(CarroComprasCliente.idSubproducto,tipoSubProducto2.getUid());
                        carroPan.putExtra(CarroComprasCliente.rut_proveedor,tipoSubProducto2.getRut_Empresa());
                        carroPan.putExtra(CarroComprasCliente.precio,tipoSubProducto2.getPrecio());
                        carroPan.putExtra(CarroComprasCliente.nombre,tipoSubProducto2.getNom_tipoSubProducto());
                        carroPan.putExtra(CarroComprasCliente.unidad,tipoSubProducto2.getTipoVentaProducto());
                        startActivity(carroPan);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void cargaImagenProducto(String url, String rutProveedor, String descrProd) {

        txtDescripProducto.setText(descrProd);

        if (url != null) {
            Glide
                    .with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imgPrdElegido.setVisibility(View.VISIBLE);
                            imgPrdElegido.setImageResource(R.drawable.imagen_no_disponible);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imgPrdElegido.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imgPrdElegido);
        }else{
            imgPrdElegido.setImageResource(R.drawable.imagen_no_disponible);
        }
        //cargaSubProductosDelProveedor(rutProveedor);
    }
}

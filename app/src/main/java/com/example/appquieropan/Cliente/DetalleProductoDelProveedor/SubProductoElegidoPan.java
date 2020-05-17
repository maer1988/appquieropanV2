package com.example.appquieropan.Cliente.DetalleProductoDelProveedor;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appquieropan.Entidad.Producto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SubProductoElegidoPan extends AppCompatActivity implements View.OnClickListener {

    public static final String idSubproducto="cod";




    private ImageView imgPrdElegido;
    private TextView txtDescripProducto,rutProveedor;
    private CheckBox chkLocal, chkDeli;
    private Button btnSiguiente;

    private DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseStorage mStorage;
    private String codigoSubProducto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        Log.d("id_prod", "id_producto"+codigoSubProducto);


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

    private void cargaDatosProductoElegido(String idProducto) {

        db.collection("producto")
                .whereEqualTo("id_producto", idProducto)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {

                                Log.d("QQQ", "nada que mostrar");
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()) {



                                    Producto p = document.toObject(Producto.class);
                                    cargaImagenProducto(p.getUrlSubproducto(), p.getRut_Empresa(), p.getDesc_tipoSubProducto());

                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    public void MandarDatos(final String idProducto, final Intent carroPan){

        db.collection("producto")
                .whereEqualTo("id_producto", idProducto)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Producto p2 = document.toObject(Producto.class);
                                Log.d("ENTRO", "rut prove: "+p2.getRut_Empresa());
                                Log.d("PPP", document.getId() + " => " + document.getData());
                                carroPan.putExtra(CarroComprasCliente.idSubproducto,p2.getId_producto());
                                carroPan.putExtra(CarroComprasCliente.rut_proveedor,p2.getRut_Empresa());
                                carroPan.putExtra(CarroComprasCliente.precio,p2.getPrecio());
                                carroPan.putExtra(CarroComprasCliente.nombre,p2.getNom_tipoSubProducto());
                                carroPan.putExtra(CarroComprasCliente.unidad,p2.getTipoVentaProducto());
                                startActivity(carroPan);
                                finish();

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
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

package com.example.appquieropan.Cliente.SubProductosDelProveedor;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Adaptadores.Cliente.RecyclerProveedorPanSel;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Proveedor;
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

import java.util.ArrayList;

public class SubProductoPanCliente extends AppCompatActivity {

  public static final String rutE="rut";

  private DatabaseReference mDatabase;
  StorageReference storageReference;
  private FirebaseStorage mStorage;
  RecyclerView mRecyclerView;
  FirebaseFirestore db = FirebaseFirestore.getInstance();

  private TextView nombreProveedorPan;

  private RecyclerProveedorPanSel recyclerProductosAdapter;
  private ImageView imagenProveedoresCliente;

  private BottomNavigationView botonesNav;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sub_producto_pan_cliente);

    mDatabase = FirebaseDatabase.getInstance().getReference();
    storageReference = FirebaseStorage.getInstance().getReference();
    mStorage = FirebaseStorage.getInstance();

    imagenProveedoresCliente = findViewById(R.id.idImagenPanCliente);
    nombreProveedorPan = findViewById(R.id.idNomProveedorCliente);

    String rutEmpresaElegida = getIntent().getStringExtra("rut");

    Toast.makeText(this, rutEmpresaElegida, Toast.LENGTH_SHORT).show();

    cargaDatosProveedorElegido(rutEmpresaElegida);

    mRecyclerView = findViewById(R.id.myPanProdCliProv);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(layoutManager);

    Toast toast1 =
            Toast.makeText(getApplicationContext(),
                    "act. SubProductoPanCliente", Toast.LENGTH_SHORT);

    toast1.show();


    botonesNav = findViewById(R.id.botones_navegacion_sub_pan);
    botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId()==R.id.nav_home_cliente){
          Intent intent = new Intent(SubProductoPanCliente.this, HomeCliente.class);
          //intent.putExtra(homeProveedor.codigoProvve,userP);
          startActivity(intent);
          finish();
          //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
        }
        else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
          //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(SubProductoPanCliente.this, ListadoVoucher.class);
          intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
          startActivity(intent);
          finish();
        }
        else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
          Intent intent = new Intent(SubProductoPanCliente.this, PerfilCliente.class);
          startActivity(intent);
          finish();
        }
        else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
          //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(SubProductoPanCliente.this, ListadoCarroCompra.class);
          //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
          startActivity(intent);
          finish();
        }
        else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
          //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(SubProductoPanCliente.this, ListadoVoucher.class);
          intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
          startActivity(intent);
          finish();
        }
        return false;
      }
    });

  }

  private void cargaDatosProveedorElegido(final String idProveedor) {

    db.collection("Proveedor")
            .whereEqualTo("rut_proveedor", idProveedor)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {
                    final Proveedor p = document.toObject(Proveedor.class);
                    cargaImagenProveedor(p.getUrl_proveedor(), idProveedor, p.getNom_proveedor());
                  }
                } else {
                  Log.d("tag", "Error getting documents: ", task.getException());
                }
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


    db.collection("producto")
            .whereEqualTo("rut_Empresa", rutProveedor)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
              @Override
              public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Producto> arrayListTipo = new ArrayList<>();

                if (task.isSuccessful()) {
                  for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("tag", document.getId() + " => " + document.getData());

                    final Producto prod = document.toObject(Producto.class);

                    prod.setNom_tipoSubProducto(prod.getNom_tipoSubProducto());
                    prod.setDesc_tipoSubProducto(prod.getDesc_tipoSubProducto());
                    prod.setUrlSubproducto(prod.getUrlSubproducto());
                    prod.setUid(prod.getUid());
                    arrayListTipo.add(prod);

                  }
                  recyclerProductosAdapter = new RecyclerProveedorPanSel(getApplicationContext(),R.layout.item_subproductos,arrayListTipo);
                  mRecyclerView.setAdapter(recyclerProductosAdapter);
                }



				else {
                  Log.d("tag", "Error getting documents: ", task.getException());
                }
              }
            });

  }
}

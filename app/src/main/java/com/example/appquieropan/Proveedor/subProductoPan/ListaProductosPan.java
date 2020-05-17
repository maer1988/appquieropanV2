package com.example.appquieropan.Proveedor.subProductoPan;

import android.content.Intent;
import androidx.annotation.NonNull;

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
import android.widget.Toast;

import com.example.appquieropan.Adaptadores.Proveedor.RecyclerProductosAdapter;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.Ventas.tipoVenta;
import com.example.appquieropan.Proveedor.homeProveedor;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
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

public class ListaProductosPan extends AppCompatActivity implements View.OnClickListener{

    public static final String codigoProvve="id";
    public static final String categoria="cat";
    private String userP = null;
    private String tipo_cat = null;
    private String rutEmpresa = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private RecyclerProductosAdapter recyclerProductosAdapter;
    RecyclerView mRecyclerView;

    ImageView btnAgregarProd;
    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos_pan);

        firebaseAuth = FirebaseAuth.getInstance();

        btnAgregarProd= findViewById(R.id.imgSinProductosPan);
        btnAgregarProd.setOnClickListener(this);

        userP = getIntent().getStringExtra("id");
        tipo_cat = getIntent().getStringExtra("cat");

        mRecyclerView = findViewById(R.id.myRecyclerP);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        CargarRecyclerView(mRecyclerView,tipo_cat);

        botonesNav = findViewById(R.id.botones_navegaPan);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaProductosPan.this, homeProveedor.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaProductosPan.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    //Toast.makeText(homeProveedor.this, "factura", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaProductosPan.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaProductosPan.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaProductosPan.this, tipoVenta.class);
                    intent.putExtra(tipoVenta.codigoProvve,userP);
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

            case R.id.imgSinProductosPan:
                Intent intentP = new Intent(this, ingresaSubProductoPan.class);
                intentP.putExtra(ingresaSubProductoPan.codigoProvve,userP);
                intentP.putExtra(ingresaSubProductoPan.categoria,tipo_cat);
                startActivity(intentP);
                break;
        }
    }

    public void CargarRecyclerView(RecyclerView recyclerView, final String tipo){


        db.collection("Proveedor")
                .whereEqualTo("id_proveedor", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Proveedor proveedor = document.toObject(Proveedor.class);

                                rutEmpresa = proveedor.getRut_proveedor();
                                // nombreEmpresa = proveedor.getNom_proveedor();
                                //txtProveedor.setText("¡Bienvenido "+ nombreEmpresa +"!");

                            }


                            db.collection("producto")
                                    .whereEqualTo("categoria", tipo)
                                    .whereEqualTo("rut_Empresa", rutEmpresa)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            ArrayList<Producto> arrayListTipo = new ArrayList<>();


                                            if (task.isSuccessful()) {


                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Log.d("ESTO", document.getId() + " => " + document.getData());
                                                    Producto subProducto = document.toObject(Producto.class);
                                                    subProducto.setNom_tipoSubProducto(subProducto.getNom_tipoSubProducto());
                                                    subProducto.setDesc_tipoSubProducto(subProducto.getDesc_tipoSubProducto());
                                                    subProducto.setUrlSubproducto(subProducto.getUrlSubproducto());
                                                    subProducto.setUid(subProducto.getUid());

                                                    arrayListTipo.add(subProducto);


                                                }
                                                recyclerProductosAdapter = new RecyclerProductosAdapter(getApplicationContext(), R.layout.item_ventas, arrayListTipo);
                                                mRecyclerView.setAdapter(recyclerProductosAdapter);

                                            }



                                            else {
                                                Log.d("TAG", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });



                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}

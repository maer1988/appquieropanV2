package com.example.appquieropan.Proveedor.subProductoPan;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaProductosPan extends AppCompatActivity implements View.OnClickListener{

    public static final String codigoProvve="id";
    private String userP = null;
    private String rutEmpresa = null;

    private RecyclerProductosAdapter recyclerProductosAdapter;
    private DatabaseReference mDatabase;
    RecyclerView mRecyclerView;

    ImageView btnAgregarProd;
    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos_pan);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnAgregarProd= findViewById(R.id.imgSinProductosPan);
        btnAgregarProd.setOnClickListener(this);

        userP = getIntent().getStringExtra("id");

        mRecyclerView = findViewById(R.id.myRecyclerP);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        obtieneRutEmpresa(userP);
        CargarRecyclerView(mRecyclerView);

        botonesNav = findViewById(R.id.botones_navegaPan);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaProductosPan.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
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

    private void obtieneRutEmpresa(final String userP) {

        mDatabase.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Proveedor proveedor = snapshot.getValue(Proveedor.class);

                    if(userP.equals(proveedor.getUid())){
                        //cargaDatosPantalla(proveedor.getNom_tipoSubProducto(), subProducto.getDesc_tipoSubProducto(),subProducto.getPrecio(),subProducto.getUrlSubproducto());
                        rutEmpresa = proveedor.getRut_proveedor();
                        Toast.makeText(ListaProductosPan.this, rutEmpresa, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imgSinProductosPan:
                Intent intentP = new Intent(this, ingresaSubProductoPan.class);
                intentP.putExtra(ingresaSubProductoPan.codigoProvve,userP);
                startActivity(intentP);
                break;
        }
    }

    public void CargarRecyclerView(RecyclerView recyclerView){

        mDatabase = FirebaseDatabase.getInstance().getReference("SubProductoPan");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<TipoSubProducto> arrayListTipo = new ArrayList<>();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    TipoSubProducto subProducto = snapshot.getValue(TipoSubProducto.class);

                    if(rutEmpresa.equals(subProducto.getRut_Empresa())) {

                        subProducto.setNom_tipoSubProducto(subProducto.getNom_tipoSubProducto());
                        subProducto.setDesc_tipoSubProducto(subProducto.getDesc_tipoSubProducto());
                        subProducto.setUrlSubproducto(subProducto.getUrlSubproducto());
                        subProducto.setUid(subProducto.getUid());

                        arrayListTipo.add(subProducto);
                    }

                }
                recyclerProductosAdapter = new RecyclerProductosAdapter(getApplicationContext(),R.layout.item_ventas,arrayListTipo);
                mRecyclerView.setAdapter(recyclerProductosAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}

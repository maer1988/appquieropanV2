package com.example.appquieropan.Proveedor.Ventas;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.appquieropan.Adaptadores.Proveedor.ReciclerItemVoucher_Proveedor;
import com.example.appquieropan.Entidad.Producto_Detalle;

import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.homeProveedor;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListadoItemVoucher_Proveedor extends AppCompatActivity {

    private ReciclerItemVoucher_Proveedor reciclerItemVoucher_Proveedor;
    private DatabaseReference mDatabase;
    public  static String value="";
    ArrayList<Producto_Detalle> list_pd = new ArrayList<>();


    RecyclerView mRecyclerView;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_item_voucher__proveedor);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            value = extras.getString("idvoucher");

        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = findViewById(R.id.recyclerView);

        final String userP = getIntent().getStringExtra("id");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        CargarRecyclerView(mRecyclerView);

           botonesNav = findViewById(R.id.botones_navegaVenta);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(ListadoItemVoucher_Proveedor.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListadoItemVoucher_Proveedor.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(ListadoItemVoucher_Proveedor.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Intent intent = new Intent(ListadoItemVoucher_Proveedor.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {

                    Toast.makeText(ListadoItemVoucher_Proveedor.this, "venta", Toast.LENGTH_SHORT).show();

                }
                return false;

            }
        });


    }


    public void CargarRecyclerView(final RecyclerView recyclerView){




        mDatabase.child("Producto_Detalle").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnatshot: dataSnapshot.getChildren() ){

                    Log.d("ValueXXX",""+value);
                    Producto_Detalle pd = objSnatshot.getValue(Producto_Detalle.class);

                    if(pd.getIDvoucher().equals(value)) {

                        list_pd.add(pd);

                        Log.d("PPPPPP",""+list_pd.get(0).getNombre_producto());

                    }

                }


                reciclerItemVoucher_Proveedor = new ReciclerItemVoucher_Proveedor(list_pd);


                recyclerView.setAdapter(reciclerItemVoucher_Proveedor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    public void onBackPressed(){
finish();

    }

}

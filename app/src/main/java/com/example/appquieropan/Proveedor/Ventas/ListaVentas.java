package com.example.appquieropan.Proveedor.Ventas;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ListaVentas extends AppCompatActivity implements View.OnClickListener {

    private RecyclerVoucherProveedor recyclerVoucherProveedor;
    ArrayList<Voucher> list_voucher = new ArrayList<Voucher>();
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView mRecyclerView;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ventas);
        firebaseAuth = FirebaseAuth.getInstance();
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
                    Intent intent = new Intent(ListaVentas.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ListaVentas.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(ListaVentas.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Intent intent = new Intent(ListaVentas.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {

                    Toast.makeText(ListaVentas.this, "venta", Toast.LENGTH_SHORT).show();

                }
                return false;

            }
        });


    }


    public void CargarRecyclerView(final RecyclerView recyclerView){

        db.collection("Voucher")
                .whereEqualTo("tipoVenta", "online")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Voucher v = document.toObject(Voucher.class);
                                list_voucher.add(v);
                            }

                            recyclerVoucherProveedor = new RecyclerVoucherProveedor(list_voucher);
                            recyclerView.setAdapter(recyclerVoucherProveedor);


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
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

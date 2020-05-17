package com.example.appquieropan.Proveedor;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appquieropan.Entidad.Producto;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.Ventas.tipoVenta;
import com.example.appquieropan.Proveedor.subProductoMasa.ListaProductosMasa;
import com.example.appquieropan.Proveedor.subProductoPan.ListaProductosPan;
import com.example.appquieropan.Proveedor.subProductoPastel.ListaProductosPastel;
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


public class opcionesSegmentoProveedor extends AppCompatActivity implements View.OnClickListener {

    public static final String codigoProvve="id";
    private FirebaseAuth firebaseAuth;
    Button btn_pan, btn_masa,btn_pastel, btn_otros;
    private BottomNavigationView botonesNav;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    String userP = null;
    private String rutEmpresa = null;
    private String nombreEmpresa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_segmento_proveedor);
        firebaseAuth = FirebaseAuth.getInstance();

        userP = firebaseAuth.getCurrentUser().getUid();

        btn_pan = findViewById(R.id.btnPan);
        btn_masa = findViewById(R.id.btnAmansanderia);
        btn_pastel = findViewById(R.id.btnPasteleria);
        btn_otros = findViewById(R.id.btnOtros);

        btn_pan.setOnClickListener(this);
        btn_masa.setOnClickListener(this);
        btn_pastel.setOnClickListener(this);
        btn_otros.setOnClickListener(this);

        botonesNav = findViewById(R.id.botones_navegacion);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, homeProveedor.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Toast.makeText(opcionesSegmentoProveedor.this, "segmento", Toast.LENGTH_SHORT).show();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, tipoVenta.class);
                    intent.putExtra(tipoVenta.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                return false;

            }
        });

        obtieneRutEmpresa(userP);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnPan:

                Intent intentP = new Intent(this, ListaProductosPan.class);
                intentP.putExtra(ListaProductosPan.codigoProvve,userP);
                intentP.putExtra(ListaProductosPan.categoria,"panaderia");
                startActivity(intentP);
                break;
            case R.id.btnAmansanderia:

                Intent intenta = new Intent(this, ListaProductosPan.class);
                intenta.putExtra(ListaProductosPan.codigoProvve,userP);
                intenta.putExtra(ListaProductosPan.categoria,"amasanderia");
                startActivity(intenta);
                break;
            case R.id.btnPasteleria:

                Intent intentPs = new Intent(this, ListaProductosPan.class);
                intentPs.putExtra(ListaProductosPan.codigoProvve,userP);
                intentPs.putExtra(ListaProductosPan.categoria,"pasteleria");
                startActivity(intentPs);
                break;
            case R.id.btnOtros:
                Toast.makeText(this, "Sin Opcion", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    private void obtieneRutEmpresa(final String userP) {

        db.collection("Proveedor")
                .whereEqualTo("id_proveedor", userP)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Proveedor proveedor = document.toObject(Proveedor.class);

                                rutEmpresa = proveedor.getRut_proveedor();
                                nombreEmpresa = proveedor.getNom_proveedor();
                                //txtProveedor.setText("¡Bienvenido "+ nombreEmpresa +"!");

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}

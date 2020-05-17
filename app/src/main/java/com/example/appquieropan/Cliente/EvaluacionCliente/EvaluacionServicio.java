package com.example.appquieropan.Cliente.EvaluacionCliente;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Valoracion;
import com.example.appquieropan.Entidad.Voucher;
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
import java.util.UUID;

public class EvaluacionServicio extends AppCompatActivity implements View.OnClickListener {

    private TextView cliente,proveedor;
    private Button evaluar;
    private RatingBar ratingbar;

    private FirebaseAuth firebaseAuth;
    public  String Idvoucher,rut_proveedor,razonsocial,nombre_cliente;
    public static final String Voucher="voucher";
    public static final String rutproveedor="proveedor";
    public static final String nombreproveedor="nombre_proveedor";
    public static final String txtcliente="nombre_cliente";
    ArrayList<Voucher> voucher_list= new ArrayList<Voucher>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluacion_servicio);

        firebaseAuth = FirebaseAuth.getInstance();

        ratingbar = findViewById(R.id.rating);
        evaluar= findViewById(R.id.evaluar);
        cliente = findViewById(R.id.textcliente);
        proveedor = findViewById(R.id.textproveedor);
        Idvoucher= getIntent().getStringExtra("voucher");
        rut_proveedor= getIntent().getStringExtra("proveedor");
        razonsocial= getIntent().getStringExtra("nombre_proveedor");
        nombre_cliente= getIntent().getStringExtra("nombre_cliente");
        evaluar.setOnClickListener(this);

        cliente.setText("Hola "+nombre_cliente+" Podrias evaluar tu ultima compra con:");
        proveedor.setText("proveedor: "+razonsocial+" Rut: "+rut_proveedor);

        botonesNav = findViewById(R.id.botones_navegacion_evaluacion);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(EvaluacionServicio.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluacionServicio.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(EvaluacionServicio.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluacionServicio.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EvaluacionServicio.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.evaluar:

                Valoracion pe = new Valoracion();
                pe.setIDvalorado(UUID.randomUUID().toString());
                pe.setIdusuario(firebaseAuth.getCurrentUser().getUid());
                pe.setRut_valorado(rut_proveedor);
                pe.setValoracion(String.valueOf(ratingbar.getRating()));
                pe.setIDvoucher(Idvoucher);

                evaluar(pe);

                break;


        }

    }


public  void evaluar(Valoracion pv){
    final String[] idDocumento = new String[1];

    db.collection("Valoracion").document(pv.getIDvalorado()).set(pv);


    db.collection("Voucher")
            .whereEqualTo("idvoucher", Idvoucher)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Voucher v = document.toObject(Voucher.class);
                            idDocumento[0] =document.getId();
                            Log.d("VVV", document.getId() + " => " + document.getData());
                            voucher_list.add(v);
                        }

                        voucher_list.get(0).setValorado("1");
                        db.collection("Voucher").document(idDocumento[0]).set(voucher_list.get(0));

                        Toast t = Toast.makeText(EvaluacionServicio.this, "Gracias por tu tiempo!!", Toast.LENGTH_LONG);
                        t.show();
                        Intent Home = new Intent(EvaluacionServicio.this, HomeCliente.class);
                        startActivity( Home);

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });


}


    @Override
    public void onBackPressed(){


    }

}

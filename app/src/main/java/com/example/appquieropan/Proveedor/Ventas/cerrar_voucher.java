package com.example.appquieropan.Proveedor.Ventas;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoItemCarro;
import com.example.appquieropan.Proveedor.evalucion.Evaluacion_proveedor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Entidad.Voucher;
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

public class cerrar_voucher extends AppCompatActivity implements View.OnClickListener {

    public static  String Idvoucher="";
    public static  String fechavoucher="";
    public static  String totalvoucher="";
    private TextView id1,id2,id3,id4,id5;
    private Button cerrar,cancelar;
    private DatabaseReference mDatabase;
    ArrayList<Voucher> voucher_list = new ArrayList<Voucher>();
    private BottomNavigationView botonesNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_voucher);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Idvoucher = extras.getString("idvoucher");
            fechavoucher = extras.getString("fecha");
            totalvoucher = extras.getString("total");

        }

                 id1=findViewById(R.id.id1_cerrar);
                 id2=findViewById(R.id.id4_cerrar);
                 id3=findViewById(R.id.id5_cerrar);

                 cerrar=findViewById(R.id.cerrar);
                 cancelar=findViewById(R.id.cancelar);

        id1.setText(Idvoucher);
        id2.setText(fechavoucher);
        id3.setText(totalvoucher);


        cerrar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

                 final String userP = getIntent().getStringExtra("id");
                 botonesNav = findViewById(R.id.botones_navegaVenta);
                 botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                        if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(cerrar_voucher.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(cerrar_voucher.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(cerrar_voucher.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Intent intent = new Intent(cerrar_voucher.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {

                    Toast.makeText(cerrar_voucher.this, "venta", Toast.LENGTH_SHORT).show();

                }
                return false;

            }
        });




    }





    public void cerrar(String id){




        mDatabase.child("Voucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot Voucher_snapshot : dataSnapshot.getChildren()){

                    Voucher v = Voucher_snapshot.getValue(Voucher.class);

                    if(v.getIDVoucher().equals(Idvoucher)){

                        voucher_list.add(v);

                    }

                }

                voucher_list.get(0).setEstado("entregado");

                mDatabase.child("Voucher").child(voucher_list.get(0).getIDVoucher()).setValue(voucher_list.get(0));



                Toast t = Toast.makeText(cerrar_voucher.this, "La operacion ha sido cerrada exitosamente!!!!", Toast.LENGTH_LONG);
                t.show();
                Intent evaluar = new Intent(cerrar_voucher.this, Evaluacion_proveedor.class);
                evaluar.putExtra(Evaluacion_proveedor.Voucher, Idvoucher);
                evaluar.putExtra(Evaluacion_proveedor.nombreproveedor, voucher_list.get(0).getNombreproveedor());
                evaluar.putExtra(Evaluacion_proveedor.rutproveedor, voucher_list.get(0).getRUTproveedor());
                evaluar.putExtra(Evaluacion_proveedor.txtcliente, voucher_list.get(0).getIDcliente());
                startActivity( evaluar);
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onBackPressed(){


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cerrar:

                cerrar(Idvoucher);

                break;
            case R.id.cancelar:
                Intent Home = new Intent(cerrar_voucher.this, tipoVenta.class);
                startActivity(Home);
                finish();

                break;

        }

    }
}


package com.example.appquieropan.Cliente.CarroDeCompras;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.example.appquieropan.Adaptadores.Cliente.RecyclerProductoLista;
import com.example.appquieropan.Cliente.CarroDeCompras.mailer.SendMailAsynTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Cliente;
import com.example.appquieropan.Adaptadores.Cliente.RecyclerProductoLista;
import com.example.appquieropan.Entidad.Producto_Detalle;
import com.example.appquieropan.Entidad.Proveedor;
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

public class ComprobanteReservaCliente extends AppCompatActivity  implements View.OnClickListener{
    private com.example.appquieropan.Adaptadores.Cliente.RecyclerProductoLista RecyclerProductoLista;
    private TextView NroTransaccion,ClientePago,ValorPago,FechaRetiroPago,NombreProveedorPago,RutClientePago;
    Button enviarcorreo;
    public String HtmlCorreo="";
    public static final String voucher_post="vocher";
    DatabaseReference databaseReference;
    ArrayList<Cliente> cliente_lista = new ArrayList<Cliente>();
    ArrayList<Producto_Detalle> producto_detalle_lista = new ArrayList<Producto_Detalle>();
    ArrayList<Proveedor> proveedor_lista = new ArrayList<Proveedor>();
    ArrayList<Voucher> voucher_lista = new ArrayList<Voucher>();
    String idVoucher;

    private FirebaseAuth firebaseAuth;
    RecyclerView mRecyclerView;

    private BottomNavigationView botonesNav;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobante_reserva_cliente);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

       NroTransaccion=findViewById(R.id.nroTransaccion);
       ClientePago = findViewById(R.id.ClientePago);
       ValorPago=findViewById(R.id.valorPago);
       FechaRetiroPago=findViewById(R.id.fechaRetiroPago);
       NombreProveedorPago=findViewById(R.id.nombreProveedorPago);
       RutClientePago=findViewById(R.id.rutClientePago);
       enviarcorreo=findViewById(R.id.btnEnviarCorreoReserva);
       mRecyclerView = findViewById(R.id.recyclerView);
       enviarcorreo.setOnClickListener(this);
       idVoucher = getIntent().getStringExtra("vocher");



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        CargaListadoReserva();
        CargarRecyclerView(mRecyclerView);

        botonesNav = findViewById(R.id.botones_navegacion_comp_reserva);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(ComprobanteReservaCliente.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComprobanteReservaCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(ComprobanteReservaCliente.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComprobanteReservaCliente.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComprobanteReservaCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


    }

    public void CargaListadoReserva(){

        db.collection("Voucher")
                .whereEqualTo("idvoucher", idVoucher)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Voucher v = document.toObject(Voucher.class);
                                voucher_lista.add(v);
                            }

                            NroTransaccion.setText(Html.fromHtml("<b>ID: </b> "+voucher_lista.get(0).getIDVoucher()));
                            FechaRetiroPago.setText(Html.fromHtml("<b>Fecha Retiro:</b> "+voucher_lista.get(0).getFechaentrega()));

                            db.collection("Cliente")
                                    .whereEqualTo("id_cliente", voucher_lista.get(0).getIDcliente())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                            if (task2.isSuccessful()) {
                                                for (QueryDocumentSnapshot document2 : task2.getResult()) {
                                                    Cliente c = document2.toObject(Cliente.class);
                                                    cliente_lista.add(c);
                                                }

                                                ClientePago.setText(Html.fromHtml("<b>Cliente: </b>"+cliente_lista.get(0).getNom_cliente()));

                                                db.collection("Proveedor")
                                                        .whereEqualTo("id_proveedor", voucher_lista.get(0).getIDproveedor())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task3) {
                                                                if (task3.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document3 : task3.getResult()) {

                                                                        Proveedor p = document3.toObject(Proveedor.class);
                                                                        proveedor_lista.add(p);
                                                                    }

                                                                    NombreProveedorPago.setText(Html.fromHtml("<b>Direccion retiro: </b>"+proveedor_lista.get(0).getDireccion_proveedor()));
                                                                    RutClientePago.setText(Html.fromHtml("<b>Vendedor: </b>"+proveedor_lista.get(0).getNom_proveedor()));

                                                                }

                                                                else {
                                                                    Log.d("TAG", "Error getting documents: ", task3.getException());
                                                                }
                                                            }
                                                        });

                                            } else {
                                                Log.d("TAG", "Error getting documents: ", task2.getException());
                                            }
                                        }
                                    });

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void CargarRecyclerView(final RecyclerView recyclerView){

        db.collection("Producto_Detalle")
                .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                .whereEqualTo("idvoucher", idVoucher)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.d("CRC", "entroo: ", task.getException());


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto_Detalle pd = document.toObject(Producto_Detalle.class);
                                producto_detalle_lista.add(pd);
                                Log.d("TAG1", document.getId() + " => " + document.getData());

                            }

                            RecyclerProductoLista = new RecyclerProductoLista(producto_detalle_lista);
                            recyclerView.setAdapter(RecyclerProductoLista);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



   /* public void enviarCorreo(){




        databaseReference.child("Producto_Detalle").addListenerForSingleValueEvent(new ValueEventListener() {


            String encabezado="<html><h2 style=\"text-align: left;\"><strong style=\"margin-left: 60px;\">Comprobante de Reserva</strong></h2>\n" +
                    "<p style=\"margin-left: 50px;\">con este comprobante puede retirar su producto</p>\n" +
                    "<p style=\"text-align: center;\"><img style=\"float: left;margin-left: 110px;\" src=\"https://firebasestorage.googleapis.com/v0/b/myapplication-a201a.appspot.com/o/correo%2Flogo.jpg?alt=media&amp;token=a7707575-d924-4f83-b392-5d7e1c5993fa\" alt=\"\" width=\"141\" height=\"175\" /></p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                    "<ul>\n" +
                    "<li style=\"text-align: left;\"><strong>id: </strong>"+NroTransaccion.getText()+"</li>\n" +
                    "<li style=\"text-align: left;\"><strong>Cliente: </strong>"+ClientePago.getText()+"</li>\n" +
                    "<li style=\"text-align: left;\"><strong>Fecha de retiro: </strong>"+FechaRetiroPago.getText()+"</li>\n" +
                    "<li style=\"text-align: left;\"><strong>Direccion de retiro: </strong>"+NombreProveedorPago.getText()+"</li>\n" +
                    "<li style=\"text-align: left;\"><strong>Vendedor: </strong>"+RutClientePago.getText()+"</li>" +
                    "\"<li><strong>productos:</strong>\"<ul>";



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnatshot : dataSnapshot.getChildren()) {

                    Log.e("cantidad",""+dataSnapshot.getChildrenCount());
                    Producto_Detalle pd = objSnatshot.getValue(Producto_Detalle.class);


                    if (pd.getIDvoucher().equals(idVoucher) && pd.getIdCliente().equals(firebaseAuth.getCurrentUser().getUid())) {

                        producto_detalle_lista.add(pd);
                        Log.e("pd1",""+pd.getIDvoucher());
                        Log.e("pd2",""+pd.getNombre_producto());

                        encabezado=encabezado+"<li style=\"text-align: left;\">"+pd.getNombre_producto()+"  "+pd.getCantidad()+" "+pd.getTipo_cantidad()+"</li>";

                    }

                }

                    encabezado=encabezado+"</ul>\n" +
                            "</li>\n" +
                            "</ul>\n" +
                            "<p style=\"text-align: center;\"><strong><img style=\"float: left;\" src=\"https://firebasestorage.googleapis.com/v0/b/myapplication-a201a.appspot.com/o/correo%2Fpie.jpg?alt=media&amp;token=95ef9425-3ea5-466d-b19e-c9e11056c323\" alt=\"\" width=\"463\" height=\"71\" /></strong></p></html>";



                new SendMailAsynTask(ComprobanteReservaCliente.this, "espejo.miguel.antonio@gmail.com", "Comprobante de reserva", ""+encabezado).execute();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    public void enviarCorreo(){

          db.collection("Producto_Detalle")
                .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                .whereEqualTo("idvoucher", idVoucher)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String encabezado="con este comprobante puede retirar su producto\n" +
                                NroTransaccion.getText()+"\n" +
                                ClientePago.getText()+"\n" +
                                FechaRetiroPago.getText()+"\n" +
                                NombreProveedorPago.getText()+"\n" +
                                RutClientePago.getText()+"\n";


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Producto_Detalle pd = document.toObject(Producto_Detalle.class);
                                producto_detalle_lista.add(pd);
                                encabezado=encabezado+"Producto: "+pd.getNombre_producto()+"  "+pd.getCantidad()+" "+pd.getTipo_cantidad()+"\n";

                            }

                            new SendMailAsynTask(ComprobanteReservaCliente.this,firebaseAuth.getCurrentUser().getEmail(), "Comprobante de reserva", ""+encabezado).execute();

                            Toast.makeText(ComprobanteReservaCliente.this, "El correo fue enviado exitosamente!", Toast.LENGTH_SHORT).show();


                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.btnEnviarCorreoReserva:

                Intent home = new Intent(this, HomeCliente.class);
                enviarCorreo();
                startActivity(home);
                finish();
                break;


        }


    }

    @Override
    public void onBackPressed(){


    }
}

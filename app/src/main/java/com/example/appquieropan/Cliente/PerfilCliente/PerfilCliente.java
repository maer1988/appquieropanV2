package com.example.appquieropan.Cliente.PerfilCliente;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Entidad.Cliente;
import com.example.appquieropan.Entidad.Cliente_direccion;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoCarroCompra;
import com.example.appquieropan.Cliente.CarroDeCompras.ListadoVoucher;
import com.example.appquieropan.Cliente.HomeCliente;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class PerfilCliente extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView botonesNav;

    private Button guardarPerfil,btnVolver,direcciones;
    private TextView nombre_cliente,direccion_cliente,correo_cliente,telefono_cliente,comuna_cliente,ciudad_cliente;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);

        firebaseAuth = FirebaseAuth.getInstance();


        nombre_cliente=findViewById(R.id.nombre_cliente);
        direccion_cliente=findViewById(R.id.direccion_cliente);
        correo_cliente=findViewById(R.id.correo_cliente);
        telefono_cliente=findViewById(R.id.telefono_cliente);
        comuna_cliente=findViewById(R.id.comuna_cliente);
        ciudad_cliente=findViewById(R.id.ciudad_cliente);

        guardarPerfil = findViewById(R.id.perfilGuarda);
        direcciones = findViewById(R.id.cliente_direcciones);

        btnVolver = findViewById(R.id.perfilVolver);
        Log.d("perf",""+firebaseAuth.getCurrentUser().getUid());
        cargarDatosVistaPerfil(firebaseAuth.getCurrentUser().getUid());



        guardarPerfil.setOnClickListener(this);

        btnVolver.setOnClickListener(this);
        direcciones.setOnClickListener(this);

        botonesNav = findViewById(R.id.menu_inferior_perfil);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(PerfilCliente.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PerfilCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(PerfilCliente.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PerfilCliente.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PerfilCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

    }

    private void validaDatosEntrada() {

        String rutE,nomE,telE;
        nomE = nombre_cliente.getText().toString().trim();
        rutE = correo_cliente.getText().toString().trim();
        telE = telefono_cliente.getText().toString();





        if(!telE.isEmpty() && !nomE.isEmpty() && !rutE.isEmpty()){


                actualizaFirebase(nomE,rutE,telE);
                Toast.makeText(this, "Datos Actualizados", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(this, "Campos en blanco, debe completar toda la informacion", Toast.LENGTH_SHORT).show();
        }


    }

    private void elegirOpcion() {

        final CharSequence[] opciones = {"Tomar Foto", "Elegir de galeria","Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilCliente.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(opciones[which] == "Tomar Foto"){

                    //tomarFoto();

                }else if(opciones[which] == "Elegir de galeria"){

                    //elegirGaleria();

                }
                else if(opciones[which] == "Cancelar"){

                    dialog.dismiss();

                }

            }
        });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.perfilGuarda:

                validaDatosEntrada();
                break;

                case R.id.cliente_direcciones:

                    Intent seguir = new Intent(PerfilCliente.this, direcciones_cliente.class);
                    startActivity(seguir);
                break;



            case R.id.perfilVolver:

                Intent intent = new Intent(PerfilCliente.this, HomeCliente.class);
                intent.putExtra(HomeCliente.idCliente,firebaseAuth.getCurrentUser().getUid());
                startActivity(intent);
                finish();

                break;
        }
    }





    private void actualizaFirebase(String nombre, String correo, String telefono) {



                                Cliente cliente = new Cliente();
                                cliente.setEmail_cliente(correo);
                                cliente.setNom_cliente(nombre);
                                cliente.setTelefono(telefono);
                                cliente.setId_cliente(firebaseAuth.getCurrentUser().getUid());

                             Log.d("current",""+firebaseAuth.getCurrentUser().getUid());


                                db.collection("Cliente").document(firebaseAuth.getCurrentUser().getUid()).set(cliente);

                                  Intent intent = new Intent(PerfilCliente.this, HomeCliente.class);
                                  intent.putExtra(HomeCliente.idCliente,firebaseAuth.getCurrentUser().getUid());
                                  startActivity(intent);
                                  finish();



                                Toast.makeText(PerfilCliente.this,"Datos actualizados",Toast.LENGTH_SHORT).show();



    }




    private void cargarDatosVistaPerfil(final String idcliente) {


        db.collection("Cliente")
                .whereEqualTo("id_cliente", idcliente)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cliente cliente = document.toObject(Cliente.class);
                                cargaDatosPantalla(cliente.getId_cliente(),cliente.getNom_cliente(),cliente.getEmail_cliente(),cliente.getTelefono());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void cargaDatosPantalla(final String idcliente, String nom, String email, String telefono) {

        final String[][] dir = {new String[3]};


        db.collection("Cliente_direccion")
                .whereEqualTo("id_cliente", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Cliente_direccion cliente_dir = document.toObject(Cliente_direccion.class);

                                if (cliente_dir.getDireccion() == "" || cliente_dir.getDireccion().isEmpty() || cliente_dir.getDireccion() == null) {

                                    dir[0][0]="";
                                    dir[0][1]="";
                                    dir[0][2]="";
                                }
                                else{

                                    dir[0][0] = cliente_dir.getDireccion();
                                    dir[0][1] = cliente_dir.getComuna();
                                    dir[0][2] = cliente_dir.getCiudad();

                                }



                                direccion_cliente.setText(dir[0][0]);
                                comuna_cliente.setText(dir[0][1]);
                                ciudad_cliente.setText(dir[0][2]);


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        nombre_cliente.setText(nom);
        correo_cliente.setText(email);
        telefono_cliente.setText(telefono);


    }


    @Override
    public void onBackPressed(){


    }



}

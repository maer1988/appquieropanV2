package com.example.appquieropan.Cliente.CarroDeCompras;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Producto_Pedido;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.TipoSubProducto;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.UUID;

public class CarroComprasCliente extends AppCompatActivity implements View.OnClickListener {

    public static final String idSubproducto="cod";
    public static final String rut_proveedor="rut";
    public static final String nombre="detalle";
    public static final String precio="precio";
    public static final String unidad="unidad";
    public String Redireccionar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private TextView seleccion_unidad, seleccioneCantidad, seleccioneTipoCompra, seleccioneTipoEntrega;
    private ImageView imgPrdCarro;
    private Button btn_pagar;
    private Button btn_guardar;

    String codigoSubProductoCarro,proveedor,precio_unitario,nombre_item,tipo_unidad;
    ArrayList<Producto_Pedido> listproductopedido = new ArrayList<Producto_Pedido>();
    ArrayList<Proveedor> proveedor_list = new ArrayList<Proveedor>();

    private DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage mStorage;

    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro_compras_cliente);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();


        codigoSubProductoCarro = getIntent().getStringExtra("cod");
        precio_unitario = getIntent().getStringExtra("precio");
        proveedor = getIntent().getStringExtra("rut");
        Log.d("RP", "rut: "+proveedor);
        nombre_item = getIntent().getStringExtra("detalle");
        tipo_unidad = getIntent().getStringExtra("unidad");

        cargaDatosProductoElegido(codigoSubProductoCarro);
/*
        Log.d("rut_prov",codigoSubProductoCarro);
        Log.d("precio",precio_unitario);
        Log.d("prove",proveedor);
        Log.d("nombre", nombre_item);*/

        seleccion_unidad = findViewById(R.id.idSelUnidad);
        seleccioneCantidad = findViewById(R.id.idSelCantidad);
        seleccioneTipoCompra = findViewById(R.id.idSelTipoCompra);
        seleccioneTipoEntrega = findViewById(R.id.idSelTipoEntrega);
        imgPrdCarro = findViewById(R.id.imgProdClienteCarro);
        btn_pagar = findViewById(R.id.btncancelar);
        btn_guardar = findViewById(R.id.btnguardar);


        seleccion_unidad.setOnClickListener(this);
        seleccioneCantidad.setOnClickListener(this);
        seleccioneTipoCompra.setOnClickListener(this);
        seleccioneTipoEntrega.setOnClickListener(this);
        btn_pagar.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);

        botonesNav = findViewById(R.id.botones_navegacion_inicio_carro);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(CarroComprasCliente.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CarroComprasCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(CarroComprasCliente.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CarroComprasCliente.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CarroComprasCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
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

            case R.id.idSelUnidad:
                seleccioneOpcionesU(tipo_unidad);
                break;

            case R.id.idSelCantidad:
                seleccioneOpcionesC();
                break;

            case R.id.idSelTipoCompra:
                opcionesproveedor("tipo_compra", proveedor);
                break;

            case R.id.idSelTipoEntrega:
                opcionesproveedor("tipo_entrega", proveedor);
                break;


            case R.id.btncancelar:


                if(seleccion_unidad.getText().equals("")|| seleccioneCantidad.getText().equals("")||seleccioneTipoCompra.getText().equals("")||seleccioneTipoEntrega.getText().equals("")){

                    Toast t = Toast.makeText(CarroComprasCliente.this, "DEBE COMPLETAR TODOS LOS CAMPOS", Toast.LENGTH_LONG);
                    t.show();

                }

                else {


                    String uid = firebaseAuth.getCurrentUser().getUid();
                    Producto_Pedido pp1 = new Producto_Pedido();
                    pp1.setUid(UUID.randomUUID().toString());
                    pp1.setCantidad(seleccioneCantidad.getText().toString());
                    pp1.setIdCliente(uid);
                    pp1.setNombre_producto(nombre_item);
                    pp1.setPrecio(precio_unitario);
                    pp1.setRut_proveedor(proveedor);
                    pp1.setTipo_cantidad(seleccion_unidad.getText().toString());
                    CargarCarroCompra(pp1, "pago");

                }
                break;

            case R.id.btnguardar:

                if(seleccion_unidad.getText().equals("")|| seleccioneCantidad.getText().equals("")||seleccioneTipoCompra.getText().equals("")||seleccioneTipoEntrega.getText().equals("")){

                    Toast t = Toast.makeText(CarroComprasCliente.this, "DEBE COMPLETAR TODOS LOS CAMPOS", Toast.LENGTH_LONG);
                    t.show();

                }

                else {
                    String uid2 = firebaseAuth.getCurrentUser().getUid();
                    Producto_Pedido pp = new Producto_Pedido();

                    pp.setUid(UUID.randomUUID().toString());
                    pp.setCantidad(seleccioneCantidad.getText().toString());
                    pp.setIdCliente(uid2);
                    pp.setNombre_producto(nombre_item);
                    pp.setPrecio(precio_unitario);
                    pp.setRut_proveedor(proveedor);
                    pp.setTipo_cantidad(seleccion_unidad.getText().toString());
                    CargarCarroCompra(pp, "home");
                }
                break;

        }

    }



    private void seleccioneOpcionesU(String tipo_unidad) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarroComprasCliente.this);
        builder.setTitle("Seleccione una Opcion").setIcon(R.drawable.logo_superior);

        final CharSequence[] opciones;

            if (tipo_unidad.equalsIgnoreCase("ambos")){
       opciones = new CharSequence[2];

        opciones[0] = "Unidad";
        opciones[1] = "Kilo";

    }

            else {
                opciones = new CharSequence[1];
                 opciones[0] = tipo_unidad;

                }


                  builder.setItems(opciones, new DialogInterface.OnClickListener() {
                 @Override
                      public void onClick(DialogInterface dialog, int which) {

                       Toast.makeText(CarroComprasCliente.this, "Su elecccion es " + opciones[which], Toast.LENGTH_SHORT).show();
                       seleccion_unidad.setText(opciones[which]);

            }
        });

                     builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
              @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void seleccioneOpcionesC() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarroComprasCliente.this);
        builder.setTitle("Seleccione una Opcion").setIcon(R.drawable.logo_superior);

        final CharSequence[] opciones = new CharSequence[10];

        opciones[0] = "1";
        opciones[1] = "2";
        opciones[2] = "3";
        opciones[3] = "4";
        opciones[4] = "5";
        opciones[5] = "6";
        opciones[6] = "7";
        opciones[7] = "8";
        opciones[8] = "9";
        opciones[9] = "10";


        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(CarroComprasCliente.this, "Su elecccion es " + opciones[which], Toast.LENGTH_SHORT).show();
                seleccioneCantidad.setText(opciones[which]);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void seleccioneOpcionesTC(ArrayList<Proveedor> p) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarroComprasCliente.this);
        builder.setTitle("Seleccione una Opcion").setIcon(R.drawable.logo_superior);

        final CharSequence[] opciones;

        if (p.get(0).getTipo_Pago_Proveedor().equalsIgnoreCase("ambos")){
            opciones = new CharSequence[2];

            opciones[0] = "Efectivo";
            opciones[1] = "Online";

        }

        else {
            opciones = new CharSequence[1];
            opciones[0] = p.get(0).getTipo_Pago_Proveedor();

        }

        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(CarroComprasCliente.this, "Su elecccion es " + opciones[which], Toast.LENGTH_SHORT).show();
                seleccioneTipoCompra.setText(opciones[which]);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void seleccioneOpcionesTE(ArrayList<Proveedor> p) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarroComprasCliente.this);
        builder.setTitle("Seleccione una Opcion").setIcon(R.drawable.logo_superior);

        final CharSequence[] opciones;

        if (p.get(0).getTipo_Pago_Proveedor().equalsIgnoreCase("ambos")){
            opciones = new CharSequence[2];

            opciones[0] = "Local";
            opciones[1] = "Domicilio";

        }

        else {
            opciones = new CharSequence[1];
            opciones[0] = p.get(0).getTipo_Despacho_Proveedor();

        }

        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(CarroComprasCliente.this, "Su elecccion es " + opciones[which], Toast.LENGTH_SHORT).show();
                seleccioneTipoEntrega.setText(opciones[which]);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void cargaDatosProductoElegido(String idProducto) {

        db.collection("producto")
                .whereEqualTo("id_producto", idProducto)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {

                                Log.d("QQQ", "nada que mostrar");
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    Log.d("PPP", document.getId() + " => " + document.getData());

                                    Producto p = document.toObject(Producto.class);
                                    cargaImagenProducto(p.getUrlSubproducto(), p.getRut_Empresa(), p.getDesc_tipoSubProducto());

                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void cargaImagenProducto(String url, String rutProveedor, String descrProd) {

        //txtDescripProducto.setText(descrProd);

        if (url != null) {
            Glide
                    .with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imgPrdCarro.setVisibility(View.VISIBLE);
                            imgPrdCarro.setImageResource(R.drawable.imagen_no_disponible);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imgPrdCarro.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imgPrdCarro);
        }else{
            imgPrdCarro.setImageResource(R.drawable.imagen_no_disponible);
        }
        //cargaSubProductosDelProveedor(rutProveedor);
    }


    private void CargarCarroCompra(final Producto_Pedido pp, final String siguiente){


        db.collection("Producto_pedido")
                .whereEqualTo("estado", "nuevo")
                .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int validar = 0;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Producto_Pedido pp2 = document.toObject(Producto_Pedido.class);
                                listproductopedido.add(pp2);

                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }

                            if (listproductopedido.size() > 0) {


                                if (listproductopedido.get(0).getRut_proveedor().equals(pp.getRut_proveedor())) {

                                    db.collection("Producto_pedido").document().set(pp);
                                    Toast t = Toast.makeText(CarroComprasCliente.this, "Producto agregado al carro con exito!!", Toast.LENGTH_LONG);
                                    t.show();

                                } else {



                                    validar = 1;  //captura si existe una diferencia entre el provedor del producto y el que actualmente existe en el carro


                                }
                            } else {

                                db.collection("Producto_pedido").document().set(pp);
                                Toast t = Toast.makeText(CarroComprasCliente.this, "Producto agregado al carro con exito!!", Toast.LENGTH_LONG);
                                t.show();

                            }


                            if(validar == 1) {

                                Toast t = Toast.makeText(CarroComprasCliente.this, "No puede agregar porductos de distinto provedor", Toast.LENGTH_LONG);
                                t.show();


                            }

                            else{

                                if (siguiente.equals("home")) {

                                    Intent Home = new Intent(CarroComprasCliente.this, HomeCliente.class);
                                    startActivity(Home);
                                } else {


                                    Intent Home = new Intent(CarroComprasCliente.this, ListadoItemCarro.class);
                                    Home.putExtra(ListadoItemCarro.tipo_pago, seleccioneTipoCompra.getText().toString());
                                    startActivity(Home);
                                    finish();


                                }


                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    private void opcionesproveedor(final String opcion, final String proveedor){


        db.collection("Proveedor")
                .whereEqualTo("rut_proveedor", proveedor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Proveedor p = document.toObject(Proveedor.class);
                                proveedor_list.add(p);
                            }
                            switch (opcion){

                                case "tipo_compra":

                                    seleccioneOpcionesTC(proveedor_list);


                                    break;


                                case  "tipo_entrega":

                                    seleccioneOpcionesTE(proveedor_list);

                                    break;

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }




}

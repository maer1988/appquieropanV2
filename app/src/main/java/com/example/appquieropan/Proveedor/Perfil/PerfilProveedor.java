package com.example.appquieropan.Proveedor.Perfil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Ventas.tipoVenta;
import com.example.appquieropan.Proveedor.homeProveedor;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class PerfilProveedor extends AppCompatActivity implements View.OnClickListener{

    public static final String idProveedor="idProveedor";
    private String idProvee = null;

    private Button guardarPerfil, btn_abrirGaleria, btn_sacarFoto,btnVolver;
    private ImageView imagenPerfil;
    private Uri filepath;
    private int FOTO = 1;
    private int check= 1;
    private EditText edtRazonSocial, edtRutEmpresa, edtDireccion,edtTelefono;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private CheckBox opcionO,opcionE, opcionL,opcionD;

    private String urlGuardada, nombrePre, rutPre,correoPRe;

    private BottomNavigationView botonesNav;

    private DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_proveedor);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        if (ContextCompat.checkSelfPermission(PerfilProveedor.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PerfilProveedor.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PerfilProveedor.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        guardarPerfil = findViewById(R.id.perfilGuarda);
        btn_abrirGaleria = findViewById(R.id.btnAbrirGaleria);
        btn_sacarFoto = findViewById(R.id.btnSacarFoto);
        btnVolver = findViewById(R.id.perfilVolver);
        imagenPerfil = findViewById(R.id.imagenPerfil);

        idProvee = getIntent().getStringExtra("idProveedor");
        final String userP = getIntent().getStringExtra("idProveedor");

        edtRazonSocial = findViewById(R.id.edtRazonS);
        edtRutEmpresa = findViewById(R.id.edtRutE);
        edtDireccion = findViewById(R.id.edtDireccion);
        edtTelefono = findViewById(R.id.edtTelefono);

        opcionO = findViewById(R.id.checkOnline);
        opcionE = findViewById(R.id.checkEfectivo);
        opcionL = findViewById(R.id.checkLocal);
        opcionD = findViewById(R.id.checkDomi);

        guardarPerfil.setOnClickListener(this);
        btn_abrirGaleria.setOnClickListener(this);
        btn_sacarFoto.setOnClickListener(this);
        btnVolver.setOnClickListener(this);

        String id = getIntent().getStringExtra("idProveedor");

        cargarDatosVistaPerfil(id);

/*
        opcionO.setOnClickListener(this);
        opcionE.setOnClickListener(this);
        opcionL.setOnClickListener(this);
        opcionD.setOnClickListener(this);
*/

        botonesNav = findViewById(R.id.botones_navegaPerfil);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(PerfilProveedor.this, homeProveedor.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    Toast.makeText(PerfilProveedor.this, "perfil", Toast.LENGTH_SHORT).show();

                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(PerfilProveedor.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PerfilProveedor.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PerfilProveedor.this, tipoVenta.class);
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

            case R.id.perfilGuarda:

                validaDatosEntrada();
                break;

            case R.id.btnSacarFoto:

                Toast.makeText(this, "Elige opcion", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();
                //elegirGaleria();
                //elegirOpcion();
                break;

            case R.id.btnAbrirGaleria:

                Toast.makeText(this, "Elige opcion", Toast.LENGTH_SHORT).show();
                //dispatchTakePictureIntent();
                elegirGaleria();
                break;

            case R.id.perfilVolver:

                Intent intent = new Intent(PerfilProveedor.this, homeProveedor.class);
                intent.putExtra(homeProveedor.codigoProvve,idProvee);
                startActivity(intent);
                finish();

                break;
        }

    }

    private void validaDatosEntrada() {

        String rutE,nomE,dirE,telE,eleccionPago = null, eleccioEntrega = null;
        nomE = edtRazonSocial.getText().toString().trim();
        rutE = edtRutEmpresa.getText().toString().trim();
        dirE = edtDireccion.getText().toString().trim();
        telE = edtTelefono.getText().toString();

        if(opcionO.isChecked() && !opcionE.isChecked()){

            Toast.makeText(this, "online", Toast.LENGTH_SHORT).show();
            check =1;
            eleccionPago = "online";

        }else if(opcionE.isChecked() && !opcionO.isChecked()){

            //Toast.makeText(this, "efecitvo", Toast.LENGTH_SHORT).show();
            check =1;
            eleccionPago = "efectivo";

        }else if(opcionO.isChecked() && opcionE.isChecked()){
            //Toast.makeText(this, "ambosPagos", Toast.LENGTH_SHORT).show();
            check =1;
            eleccionPago = "ambosPagos";
        }else{

            Toast.makeText(this, "Seleccione Tipo de pago", Toast.LENGTH_SHORT).show();
            check = 2;

        }


        if(opcionL.isChecked()&&!opcionD.isChecked()){
            //Toast.makeText(this, "Local", Toast.LENGTH_SHORT).show();
            eleccioEntrega = "Local";
            check =1;
        }else if(opcionD.isChecked()&&!opcionL.isChecked()){
            //Toast.makeText(this, "Domicilio", Toast.LENGTH_SHORT).show();
            eleccioEntrega = "Domicilio";
            check =1;
        }else if(opcionL.isChecked() && opcionD.isChecked()){
            check =1;
            eleccioEntrega = "ambasEntrega";
        }else{

            Toast.makeText(this, "Seleccione Tipo de entrega", Toast.LENGTH_SHORT).show();
            check = 2;

        }



        if(check ==1 && !dirE.isEmpty() && !telE.isEmpty()){
            if(FOTO ==2){

                actualizaFirebase(nomE,rutE,dirE,telE, FOTO, filepath,eleccionPago,eleccioEntrega);
                Toast.makeText(this, "con foto", Toast.LENGTH_SHORT).show();
            }
            else{

                actualizaDatosSinImagen(dirE,telE,eleccionPago,eleccioEntrega);
                Toast.makeText(this, "sin foto", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Campos en blanco", Toast.LENGTH_SHORT).show();
        }





    }

    private void actualizaFirebase(final String nomE, final String rutE, final String dirE, final String telE, int FOTO, Uri filepath, final String eleccionPago, final String eleccioEntrega) {

        if (!TextUtils.isEmpty(dirE)){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo.........");
            progressDialog.show();

            if (FOTO == 1){

                filepath = Uri.parse(urlGuardada);

                //actualizaDatosSinImagen(dirE,telE,eleccionPago,eleccioEntrega);

            }else{

                StorageReference reference = storageReference.child("Proveedor/"+ nomE +"/" + System.currentTimeMillis() +".png");
                reference.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete());
                                Uri url = uri.getResult();
                                String urlGuardada;
                                //objeto de tipo subproducto
                                Proveedor proveedor = new Proveedor();
                                proveedor.setUrl_proveedor(url.toString());
                                //proveedor.setNom_proveedor("SEVPRO SPA");
                                proveedor.setNom_proveedor(nombrePre);
                                //proveedor.setEmail_proveedor("maxi@gmail.com");
                                proveedor.setEmail_proveedor(correoPRe);
                                proveedor.setRut_proveedor(rutPre);
                                //proveedor.setRut_proveedor("768410879");
                                proveedor.setDireccion_proveedor(dirE);
                                proveedor.setFono1_proveedor(telE);
                                proveedor.setTipo_Pago_Proveedor(eleccionPago);
                                proveedor.setTipo_Despacho_Proveedor(eleccioEntrega);
                                //proveedor.setTipoVentaProducto(datoRadio);
                                proveedor.setId_proveedor(getIntent().getStringExtra("idProveedor"));

                                mDatabase.child("Proveedor").child(proveedor.getId_proveedor()).setValue(proveedor);
                                //databaseReference.child(databaseReference.push().getKey()).setValue(tipoSubProducto);

                                Toast.makeText(PerfilProveedor.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();


                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                        progressDialog.setMessage("Cargando..." + (int)progress+ "%");

                    }
                });
            }

        }

        else{

            Toast.makeText(this, "Complete todos campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void elegirOpcion() {

        final CharSequence[] opciones = {"Tomar Foto", "Elegir de galeria","Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilProveedor.this);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void elegirGaleria() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),REQUEST_IMAGE_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            filepath = data.getData();
            Bundle extras;
            extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagenPerfil.setImageBitmap(imageBitmap);

            FOTO = 2;
        }
        else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            filepath = data.getData();

            try {
                Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imagenPerfil.setImageBitmap(bitmapImagen);
                //flagImgen = 2;
                FOTO = 2;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cargarDatosVistaPerfil(final String idProveedor) {

        mDatabase.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Proveedor proveedor = snapshot.getValue(Proveedor.class);

                    Log.e("Datos","" + idProveedor);

                    if(idProveedor.equals(proveedor.getId_proveedor())){
                        Log.e("Reales","" + proveedor.getNom_proveedor());
                        cargaDatosPantalla(proveedor.getNom_proveedor(),proveedor.getRut_proveedor(),proveedor.getUrl_proveedor(),
                                proveedor.getDireccion_proveedor(),proveedor.getFono1_proveedor(),proveedor.getTipo_Despacho_Proveedor(),
                                proveedor.getTipo_Pago_Proveedor());
                        urlGuardada = proveedor.getUrl_proveedor();
                        nombrePre = proveedor.getNom_proveedor();
                        correoPRe = proveedor.getEmail_proveedor();
                        rutPre = proveedor.getRut_proveedor();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void cargaDatosPantalla(String nom, String rut,String url, String dire, String fono, String despacho, String pago) {

        edtRazonSocial.setText(nom);
        edtRutEmpresa.setText(rut);
        edtDireccion.setText(dire);
        edtTelefono.setText(fono);
/*
        if (pago == "ambosPagos"){
            Toast.makeText(this, "ambas", Toast.LENGTH_SHORT).show();
            opcionO.isChecked();
            opcionE.isChecked();
        }else if(pago == "Efectivo"){
            Toast.makeText(this, "ee", Toast.LENGTH_SHORT).show();
            opcionE.isChecked();
        }else if(pago == "Online"){
            Toast.makeText(this, "oo", Toast.LENGTH_SHORT).show();
            opcionO.isChecked();
        }else{
            Toast.makeText(this, pago, Toast.LENGTH_SHORT).show();
        }
*/
        if (despacho == "Local"){
            Toast.makeText(this, "ambas", Toast.LENGTH_SHORT).show();
            opcionL.isChecked();

        }else{
            Toast.makeText(this, despacho, Toast.LENGTH_SHORT).show();
        }
        //txtPrecio.setText(prec);
        //imgProducto.setTextDirection(url);
        //Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();

        if (url != null) {
            Glide
                    .with(this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imagenPerfil.setVisibility(View.VISIBLE);
                            //imgProducto.setImageResource(R.drawable.ic_launcher_background);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imagenPerfil.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imagenPerfil);
        }else{
            imagenPerfil.setImageResource(R.drawable.ic_launcher_background);
        }


    }

    private void actualizaDatosSinImagen(String direccion, String telefono, String opcionPago, String opcionEntrega) {

        Proveedor proveedor = new Proveedor();
        //proveedor.setNom_proveedor("SEVPRO SPA");
        proveedor.setNom_proveedor(nombrePre);
        //proveedor.setEmail_proveedor("maxi@gmail.com");
        proveedor.setEmail_proveedor(correoPRe);
        proveedor.setRut_proveedor(rutPre);
        //proveedor.setRut_proveedor("768410879");
        proveedor.setDireccion_proveedor(direccion);
        proveedor.setFono1_proveedor(telefono);
        proveedor.setTipo_Pago_Proveedor(opcionPago);
        proveedor.setTipo_Despacho_Proveedor(opcionEntrega);
        //proveedor.setTipoVentaProducto(datoRadio);
        proveedor.setUrl_proveedor(urlGuardada);
        proveedor.setId_proveedor(getIntent().getStringExtra("idProveedor"));

        mDatabase.child("Proveedor").child(proveedor.getId_proveedor()).setValue(proveedor);

        Toast.makeText(PerfilProveedor.this,"Pefil actualizado",Toast.LENGTH_SHORT).show();

    }
}

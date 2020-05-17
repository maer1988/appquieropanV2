package com.example.appquieropan.Proveedor.subProductoPan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Entidad.Producto;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.example.appquieropan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class detalleProductoSeleccionado extends AppCompatActivity implements View.OnClickListener {

    public static final String nombreProducto="nombreP";
    public static final String descriProducto="descripcionP";
    public static final String precioProducto="precio-p";
    public static final String idProducto="idP";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String codigoProvve="id";
    private String userP = null;
    private String rutEmpresa = null;

    private EditText txtEdnombre, txtDescripcion, txtPrecio,rut_emp,categoria;
    private ImageView imgProducto;
    private Button btnCambiosG,btnEliminar;
    private Uri filepath;
    private int flagUrl = 1;
    private  int flagImgen = 1;
    private String urlGuardada;

    private String datoRadio= null;
    private RadioGroup radioGroup;
    private RadioButton radio1,radio2;
    private String id;


    StorageReference storageReference;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto_seleccionado);


        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();

        txtEdnombre = findViewById(R.id.EdnomProductoPan);
        txtDescripcion = findViewById(R.id.EddescProdutoPan);
        txtPrecio = findViewById(R.id.EdidPrecioProductoPan);
        categoria = findViewById(R.id.categoria);
        rut_emp = findViewById(R.id.rut_empresa);
        imgProducto = findViewById(R.id.imgEditProductoPan);

        radioGroup = findViewById(R.id.radioGroupPan);
        radio1 = findViewById(R.id.radioKiloPan);
        radio2 = findViewById(R.id.radioUnidadPan);

        btnCambiosG = findViewById(R.id.btnEditarProductoPan);
        btnEliminar = findViewById(R.id.btnEliminarProductoPan);
        id = getIntent().getStringExtra("idP");
        userP = getIntent().getStringExtra("id");

        Log.d("id", "ID =>>"+id);

        cargarDatosVistaDetalle(id);
        //obtieneRutEmpresa(userP);

        btnCambiosG.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        imgProducto.setOnClickListener(this);

    }



    private void guardarCambios(final Uri data, String datoRadio) {
        String nombre,descripcion,precio,rutEmpresa,catego;
        nombre = txtEdnombre.getText().toString().trim();
        descripcion = txtDescripcion.getText().toString().trim();
        precio = txtPrecio.getText().toString().trim();
       rutEmpresa = rut_emp.getText().toString().trim();
       catego = categoria.getText().toString().trim();


        if(!nombre.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            actualizaFirebase(nombre,descripcion,precio, data, datoRadio,rutEmpresa,catego);
            //validarSiHayCambios(nombre,descripcion,precio);


        }else{
            Toast.makeText(this, "Debe registrar al menos un cambio", Toast.LENGTH_SHORT).show();
        }


    }

    private void actualizaFirebase(final String nombre, final String descripcion, final String precio, Uri data, final String datoRadio,final String rut,final String categoria) {

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(descripcion) && !TextUtils.isEmpty(precio)){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo.........");
            progressDialog.show();

            if (flagUrl == 1){

                data = Uri.parse(urlGuardada);

                actualizaDatosSinImagen(nombre,descripcion,precio,data,datoRadio,rut,categoria);

            }else{

                StorageReference reference = storageReference.child("Pan/Subproducto/"+ nombre +"_"+ System.currentTimeMillis() +".png");
                reference.putFile(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete());
                                Uri url = uri.getResult();
                                String urlGuardada;
                                //objeto de tipo subproducto
                                Producto actTipo = new Producto();
                                actTipo.setNom_tipoSubProducto(nombre);
                                actTipo.setDesc_tipoSubProducto(descripcion);
                                actTipo.setPrecio(precio);
                                actTipo.setUrlSubproducto(url.toString());
                                actTipo.setTipoVentaProducto(datoRadio);
                                actTipo.setCategoria(categoria);
                                actTipo.setRut_Empresa(rut);
                                actTipo.setUid(getIntent().getStringExtra("idP"));
                                actTipo.setId_producto(actTipo.getUid());


                                db.collection("producto").document(actTipo.getUid()).set(actTipo);
                                //databaseReference.child(databaseReference.push().getKey()).setValue(tipoSubProducto);

                                Toast.makeText(detalleProductoSeleccionado.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                limpiaCampos();

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

    private void cargarDatosVistaDetalle(final String idProducto) {

        db.collection("producto")
                .whereEqualTo("id_producto", idProducto)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Producto subProducto = document.toObject(Producto.class);
                                cargaDatosPantalla(subProducto.getNom_tipoSubProducto(), subProducto.getDesc_tipoSubProducto(), subProducto.getPrecio(), subProducto.getUrlSubproducto(),subProducto.getRut_Empresa(),subProducto.getCategoria(),subProducto.getTipoVentaProducto());
                                urlGuardada = subProducto.getUrlSubproducto();

                            }

                        }else {
                                Log.d("FF", "Error getting documents: ", task.getException());
                            }
                        }
                    });

                }

    private void cargaDatosPantalla(String sub, String des,String prec, String url,String rut,String ca,String tv ) {

        txtEdnombre.setText(sub);
        txtDescripcion.setText(des);
        txtPrecio.setText(prec);
        rut_emp.setText(rut);
        categoria.setText(ca);
        if(tv.equals("Kilo")){
            radio1.setChecked(true);
        }
        if(tv.equals("Unidad")){

            radio2.setChecked(true);

        }

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
                            imgProducto.setVisibility(View.VISIBLE);
                            //imgProducto.setImageResource(R.drawable.ic_launcher_background);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //productoViewHolder.mProgressBar.setVisibility(View.GONE);
                            imgProducto.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imgProducto);
        }else{
            imgProducto.setImageResource(R.drawable.nuevo);
        }


    }

    private void abrirFotoGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),1);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData()!=null){

            filepath = data.getData();

            try {
                Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imgProducto.setImageBitmap(bitmapImagen);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imgEditProductoPan:
                abrirFotoGaleria();
                break;
/*
            case R.id.btnEditarProducto:
                guardarCambios(filepath);
                break;
*/
            case R.id.btnEditarProductoPan:


                if(radio1.isChecked()){

                    datoRadio = "Kilo";
                    //Toast.makeText(this, datoRadio, Toast.LENGTH_SHORT).show();
                    guardarCambios(filepath,datoRadio);

                }
                else if(radio2.isChecked()){
                    datoRadio = "Unidad";
                    // Toast.makeText(this, datoRadio, Toast.LENGTH_SHORT).show();
                    guardarCambios(filepath,datoRadio);
                }
                else{
                    Toast.makeText(this, "Eliga tipo de venta", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnEliminarProductoPan:
                eliminaProductoPan();
                break;
        }
    }

    private void limpiaCampos() {

        Intent cliente = new Intent(getApplication(), opcionesSegmentoProveedor.class);
        startActivity(cliente);
        finish();

    }

    private void eliminaProductoPan() {

        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage("¿ Seguro que quiere eliminar el producto ?");
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {


                StorageReference imageAlmacenada = mStorage.getReferenceFromUrl(urlGuardada);
                imageAlmacenada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Producto actTipo = new Producto();
                        actTipo.setUid(getIntent().getStringExtra("idP"));

                        db.collection("producto").document(actTipo.getUid()).delete();

                        Toast.makeText(detalleProductoSeleccionado.this,"Producto Eliminado",Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
                        limpiaCampos();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detalleProductoSeleccionado.this, "Error al: "+ e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();



    }

    private void actualizaDatosSinImagen(String nombre, String descripcion, String precio, Uri data, String datoRadio,String rut,String categoria) {

        Producto actTipo = new Producto();
        actTipo.setNom_tipoSubProducto(nombre);
        actTipo.setDesc_tipoSubProducto(descripcion);
        actTipo.setPrecio(precio);
        actTipo.setUrlSubproducto(data.toString());
        actTipo.setTipoVentaProducto(datoRadio);
        actTipo.setRut_Empresa(rut);
        actTipo.setCategoria(categoria);
        actTipo.setUid(getIntent().getStringExtra("idP"));
        actTipo.setId_producto(actTipo.getUid());

        db.collection("producto").document(actTipo.getUid()).set(actTipo);

        Toast.makeText(detalleProductoSeleccionado.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
        limpiaCampos();

    }

}

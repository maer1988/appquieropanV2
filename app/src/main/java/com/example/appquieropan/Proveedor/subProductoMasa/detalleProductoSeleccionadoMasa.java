package com.example.appquieropan.Proveedor.subProductoMasa;

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
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.example.appquieropan.R;
import com.google.android.gms.tasks.OnFailureListener;
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

public class detalleProductoSeleccionadoMasa extends AppCompatActivity implements View.OnClickListener {

    public static final String codigoProvve="id";
    private String userP = null;

    public static final String nombreProducto="nombreP";
    public static final String descriProducto="descripcionP";
    public static final String precioProducto="precio-p";
    public static final String idProducto="idP";

    private EditText txtEdnombre, txtDescripcion, txtPrecio;
    private ImageView imgProducto;
    private Button btnCambiosG,btnEliminar;
    private Uri filepath;
    private String datoRadio= null;
    private int flagUrl = 1;
    private  int flagImgen = 1;
    private String urlGuardada;

    private RadioGroup radioGroup;
    private RadioButton radio1,radio2;

    private DatabaseReference mDatabase;
    StorageReference storageReference;
    private FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto_seleccionado_masa);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        txtEdnombre = findViewById(R.id.EdnomProductoMasa);
        txtDescripcion = findViewById(R.id.EddescProdutoMasa);
        txtPrecio = findViewById(R.id.EdidPrecioProductoMasa);
        imgProducto = findViewById(R.id.imgEditProductoMasa);

        radioGroup = findViewById(R.id.radioGroup5Masa);
        radio1 = findViewById(R.id.radioKiloMasa);
        radio2 = findViewById(R.id.radioUnidadMasa);

        btnCambiosG = findViewById(R.id.btnEditarProductoMasa);
        btnEliminar = findViewById(R.id.btnEliminarProductoMasa);
        String id = getIntent().getStringExtra("idP");

        userP = getIntent().getStringExtra("id");

        cargarDatosVistaDetalle(id);

        btnCambiosG.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        imgProducto.setOnClickListener(this);
    }

    private void guardarCambios(final Uri data, String datoRadio) {
        String nombre,descripcion,precio;
        nombre = txtEdnombre.getText().toString().trim();
        descripcion = txtDescripcion.getText().toString().trim();
        precio = txtPrecio.getText().toString().trim();

        if(!nombre.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

            actualizaFirebase(nombre,descripcion,precio, data, datoRadio);
            //validarSiHayCambios(nombre,descripcion,precio);


        }else{
            Toast.makeText(this, "Debe registrar al menos un cambio", Toast.LENGTH_SHORT).show();
        }


    }

    private void actualizaFirebase(final String nombre, final String descripcion, final String precio, Uri data, final String datoRadio) {

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(descripcion) && !TextUtils.isEmpty(precio)){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo.........");
            progressDialog.show();

            if (flagUrl == 1){

                data = Uri.parse(urlGuardada);

                actualizaDatosSinImagen(nombre,descripcion,precio,data,datoRadio);

            }else{

                StorageReference reference = storageReference.child("Masa/Subproducto/"+ nombre +"_"+ System.currentTimeMillis() +".png");
                reference.putFile(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete());
                                Uri url = uri.getResult();
                                String urlGuardada;
                                //objeto de tipo subproducto
                                TipoSubProducto actTipo = new TipoSubProducto();
                                actTipo.setNom_tipoSubProducto(nombre);
                                actTipo.setDesc_tipoSubProducto(descripcion);
                                actTipo.setPrecio(precio);
                                actTipo.setUrlSubproducto(url.toString());
                                actTipo.setTipoVentaProducto(datoRadio);
                                actTipo.setUid(getIntent().getStringExtra("idP"));

                                mDatabase.child("SubProductoMasa").child(actTipo.getUid()).setValue(actTipo);
                                //databaseReference.child(databaseReference.push().getKey()).setValue(tipoSubProducto);

                                Toast.makeText(detalleProductoSeleccionadoMasa.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
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

        mDatabase.child("SubProductoMasa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    TipoSubProducto subProducto = snapshot.getValue(TipoSubProducto.class);

                    Log.e("Datos","" + idProducto);

                    if(idProducto.equals(subProducto.getUid())){
                        Log.e("Reales","" + subProducto.getNom_tipoSubProducto());
                        cargaDatosPantalla(subProducto.getNom_tipoSubProducto(), subProducto.getDesc_tipoSubProducto(),subProducto.getPrecio(),subProducto.getUrlSubproducto());
                        urlGuardada = subProducto.getUrlSubproducto();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void cargaDatosPantalla(String sub, String des,String prec, String url) {

        txtEdnombre.setText(sub);
        txtDescripcion.setText(des);
        txtPrecio.setText(prec);
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

            case R.id.imgEditProductoMasa:
                abrirFotoGaleria();
                break;
/*
            case R.id.btnEditarProducto:
                guardarCambios(filepath);
                break;
*/
            case R.id.btnEditarProductoMasa:


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
            case R.id.btnEliminarProductoMasa:
                eliminaProductoMasa();
                break;

        }
    }

    private void limpiaCampos() {

        Intent cliente = new Intent(getApplication(), ListaProductosMasa.class);
        cliente.putExtra(ListaProductosMasa.codigoProvve,userP);
        startActivity(cliente);
        finish();

    }

    private void eliminaProductoMasa() {

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

                        TipoSubProducto actTipo = new TipoSubProducto();
                        actTipo.setUid(getIntent().getStringExtra("idP"));
                        mDatabase.child("SubProductoMasa").child(actTipo.getUid()).removeValue();

                        Toast.makeText(detalleProductoSeleccionadoMasa.this,"Producto Eliminado",Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
                        limpiaCampos();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(detalleProductoSeleccionadoMasa.this, "Error al: "+ e.toString(), Toast.LENGTH_SHORT).show();
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

    private void actualizaDatosSinImagen(String nombre, String descripcion, String precio, Uri data, String datoRadio) {

        TipoSubProducto actTipo = new TipoSubProducto();
        actTipo.setNom_tipoSubProducto(nombre);
        actTipo.setDesc_tipoSubProducto(descripcion);
        actTipo.setPrecio(precio);
        actTipo.setUrlSubproducto(data.toString());
        actTipo.setTipoVentaProducto(datoRadio);
        actTipo.setUid(getIntent().getStringExtra("idP"));

        mDatabase.child("SubProductoPastel").child(actTipo.getUid()).setValue(actTipo);

        Toast.makeText(detalleProductoSeleccionadoMasa.this,"Producto actualizado",Toast.LENGTH_SHORT).show();
        limpiaCampos();

    }

}

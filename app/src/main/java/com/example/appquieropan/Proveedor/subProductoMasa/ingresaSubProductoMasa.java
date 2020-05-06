package com.example.appquieropan.Proveedor.subProductoMasa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.TipoSubProducto;
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
import java.util.UUID;

public class ingresaSubProductoMasa extends AppCompatActivity implements View.OnClickListener{

    public static final String codigoProvve="id";
    private String userP = null;

    private ImageView imagenProducto;
    private Uri filepath;
    private final int PIC_FOTO = 1;
    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnAgregarProductoPan;
    private  int flagImgen = 1;
    private String datoRadio= null;
    private String rutEmpresa = null;


    private RadioGroup radioGroup;
    private RadioButton radio1,radio2;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa_sub_producto_masa);

        userP = getIntent().getStringExtra("id");

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        imagenProducto = findViewById(R.id.imgNuevoProductoMasa);
        edtNombre = findViewById(R.id.nomProductoMasa);
        edtDescripcion = findViewById(R.id.descProdutoMasa);
        edtPrecio = findViewById(R.id.idPrecioProductoMasa);
        radioGroup = findViewById(R.id.radioGroupMasa);
        radio1 = findViewById(R.id.radioKiloMasai);
        radio2 = findViewById(R.id.radioUnidadMasai);

        btnAgregarProductoPan = findViewById(R.id.btnGuardarProductoMasa);
        btnAgregarProductoPan.setOnClickListener(this);
        imagenProducto.setOnClickListener(this);

        obtieneRutEmpresa(userP);
    }

    private void obtieneRutEmpresa(final String userP) {

        databaseReference.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Proveedor proveedor = snapshot.getValue(Proveedor.class);

                    if(userP.equals(proveedor.getUid())){
                        //cargaDatosPantalla(proveedor.getNom_tipoSubProducto(), subProducto.getDesc_tipoSubProducto(),subProducto.getPrecio(),subProducto.getUrlSubproducto());
                        rutEmpresa = proveedor.getRut_proveedor();
                        Toast.makeText(ingresaSubProductoMasa.this, rutEmpresa, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void abrirFotoGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        //intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),PIC_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PIC_FOTO && resultCode == RESULT_OK && data!= null && data.getData()!=null){

            filepath = data.getData();



            try {
                Bitmap bitmapImagen = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imagenProducto.setImageBitmap(bitmapImagen);
                flagImgen = 2;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imgNuevoProductoMasa:
                abrirFotoGaleria();
                break;

            case R.id.btnGuardarProductoMasa:


                if(radio1.isChecked()){

                    datoRadio = "Kilo";
                    //Toast.makeText(this, datoRadio, Toast.LENGTH_SHORT).show();
                    subirArchivoFile(filepath,datoRadio);

                }
                else if(radio2.isChecked()){
                    datoRadio = "Unidad";
                    // Toast.makeText(this, datoRadio, Toast.LENGTH_SHORT).show();
                    subirArchivoFile(filepath,datoRadio);
                }
                else{
                    Toast.makeText(this, "Eliga tipo de venta", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void subirArchivoFile(final Uri data, final String datoRadio) {

        final String nombrePan = edtNombre.getText().toString();
        final String descripPan = edtDescripcion.getText().toString();
        final String precioPan = edtPrecio.getText().toString();


        if (!TextUtils.isEmpty(nombrePan) && !TextUtils.isEmpty(descripPan) && !TextUtils.isEmpty(precioPan)){

            if (flagImgen == 2){

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Subiendo.........");
                progressDialog.show();

                StorageReference reference = storageReference.child("Masa/" +rutEmpresa +"/Subproducto/"+ nombrePan +"_"+ System.currentTimeMillis() +".png");

                reference.putFile(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uri.isComplete());
                                Uri url = uri.getResult();
                                //objeto de tipo subproducto
                                TipoSubProducto tipoSubProducto = new TipoSubProducto();

                                tipoSubProducto.setUid(UUID.randomUUID().toString());
                                tipoSubProducto.setNom_tipoSubProducto(nombrePan);
                                tipoSubProducto.setDesc_tipoSubProducto(descripPan);
                                tipoSubProducto.setUrlSubproducto(url.toString());
                                tipoSubProducto.setTipoVentaProducto(datoRadio);
                                tipoSubProducto.setPrecio(precioPan);
                                tipoSubProducto.setRut_Empresa(rutEmpresa);

                                databaseReference.child("SubProductoMasa").child(tipoSubProducto.getUid()).setValue(tipoSubProducto);
                                //databaseReference.child(databaseReference.push().getKey()).setValue(tipoSubProducto);

                                Toast.makeText(ingresaSubProductoMasa.this,"Archivo subido",Toast.LENGTH_SHORT).show();
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

            }else{

                Toast.makeText(this, "El producto nuevo debe contener imagen", Toast.LENGTH_SHORT).show();
            }


        }

        else{

            //Toast.makeText(this, "Complete todos campos", Toast.LENGTH_SHORT).show();
        }



    }

    private void limpiaCampos() {

        edtNombre.setText("");
        edtDescripcion.setText("");
        edtPrecio.setText("");
        Intent cliente = new Intent(getApplication(), ListaProductosMasa.class);
        cliente.putExtra(ListaProductosMasa.codigoProvve,userP);
        startActivity(cliente);
        finish();

    }
}

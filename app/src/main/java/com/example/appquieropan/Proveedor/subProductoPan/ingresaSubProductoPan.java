package com.example.appquieropan.Proveedor.subProductoPan;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.appquieropan.Entidad.Producto;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.example.appquieropan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ingresaSubProductoPan extends AppCompatActivity implements View.OnClickListener  {

    public static final String codigoProvve="id";
    public static final String categoria="cat";
    private String userP = null;
    private String tipo_cat = null;
    private FirebaseAuth firebaseAuth;
    private ImageView imagenProducto;
    private Uri filepath;
    private final int PIC_FOTO = 1;
    private EditText edtNombre, edtDescripcion, edtPrecio;
    private Button btnAgregarProductoPan;
    private  int flagImgen = 1;
    private String datoRadio= null;
    private String rutEmpresa = null;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private RadioGroup radioGroup;
    private RadioButton radio1,radio2;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa_sub_producto_pan);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        imagenProducto = findViewById(R.id.imgNuevoProducto);
        edtNombre = findViewById(R.id.nomProducto);
        edtDescripcion = findViewById(R.id.descProduto);
        edtPrecio = findViewById(R.id.idPrecioProducto);
        radioGroup = findViewById(R.id.radioGroup5);
        radio1 = findViewById(R.id.radioKilo);
        radio2 = findViewById(R.id.radioUnidad);
        tipo_cat = getIntent().getStringExtra("cat");
        userP = getIntent().getStringExtra("id");

        btnAgregarProductoPan = findViewById(R.id.btnGuardarProducto);
        btnAgregarProductoPan.setOnClickListener(this);
        imagenProducto.setOnClickListener(this);

        obtieneRutEmpresa(userP);

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


                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
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

            case R.id.imgNuevoProducto:
                abrirFotoGaleria();
                break;

            case R.id.btnGuardarProducto:


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

                    StorageReference reference = storageReference.child("Pan/"+rutEmpresa+"/Subproducto/"+ nombrePan +"_"+ System.currentTimeMillis() +".png");

                    reference.putFile(data)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uri.isComplete());
                                    Uri url = uri.getResult();
                                    //objeto de tipo subproducto
                                    final Producto tipoSubProducto = new Producto();

                                    tipoSubProducto.setUid(UUID.randomUUID().toString());
                                    tipoSubProducto.setNom_tipoSubProducto(nombrePan);
                                    tipoSubProducto.setDesc_tipoSubProducto(descripPan);
                                    tipoSubProducto.setUrlSubproducto(url.toString());
                                    tipoSubProducto.setTipoVentaProducto(datoRadio);
                                    tipoSubProducto.setPrecio(precioPan);
                                    tipoSubProducto.setRut_Empresa(rutEmpresa);
                                    tipoSubProducto.setCategoria(tipo_cat);
                                    tipoSubProducto.setId_producto(tipoSubProducto.getUid());


                                     db.collection("producto").document(tipoSubProducto.getUid()).set(tipoSubProducto).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {
                                             Log.d("TAG", "producto creado correctamente");
                                         }
                                     })
                                             .addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                     Log.w("TAG", "Error writing document", e);
                                                 }
                                             });



                                    db.collection("proveedor_categoria")
                                            .whereEqualTo("categoria",tipo_cat)
                                            .whereEqualTo("rut_proveedor", rutEmpresa)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    Log.d("TAG", "entro en la consulta");

                                                    if (task.isSuccessful()) {
                                                      if(task.getResult().isEmpty()){
                                                          CollectionReference prove_categoria = db.collection("proveedor_categoria");
                                                          Map<String, Object> data1 = new HashMap<>();
                                                          data1.put("categoria", tipoSubProducto.getCategoria());
                                                          data1.put("rut_proveedor", tipoSubProducto.getRut_Empresa());
                                                          prove_categoria.document().set(data1);
                                                      }
                                                          else
                                                              {

                                                                  Log.d("TAG", "ya existe referencia", task.getException());

                                                      }

                                                        }
                                                     else {
                                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });



                                    Toast.makeText(ingresaSubProductoPan.this,"Archivo subido",Toast.LENGTH_SHORT).show();
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
        Intent cliente = new Intent(getApplication(), opcionesSegmentoProveedor.class);
        cliente.putExtra(ListaProductosPan.codigoProvve,userP);
        startActivity(cliente);
        finish();

    }

}

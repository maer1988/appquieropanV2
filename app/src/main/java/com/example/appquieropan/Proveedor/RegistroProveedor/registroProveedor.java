package com.example.appquieropan.Proveedor.RegistroProveedor;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.homeProveedor;
import com.example.appquieropan.R;
import com.example.appquieropan.opciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registroProveedor extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String TAG ="RegistroProveedor";
    private EditText rSocial,rutEmpresa,rDireccion;
    private Button btnRegistraProveedor;
    public static final String codigoProvve="id";
    private String userP = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_proveedor);
        userP = getIntent().getStringExtra("id");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        rDireccion = findViewById(R.id.direccion);
        rSocial = findViewById(R.id.rRazonSocial);
        rutEmpresa = findViewById(R.id.rRutEmpresa);
        btnRegistraProveedor = findViewById(R.id.btnRegProv);

        btnRegistraProveedor.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnRegProv:
                String razonSocial = rSocial.getText().toString().trim();
                String rEmpresa = rutEmpresa.getText().toString().trim();
                String rdireccion = rDireccion.getText().toString().trim();


                if(!razonSocial.isEmpty() && !rEmpresa.isEmpty() && !rdireccion.isEmpty()){

                        registraNuevoUsuario(rEmpresa,razonSocial,rdireccion);



                }else{
                    Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }



                break;

        }

    }

    public void registraNuevoUsuario(String rut, String razon, String direccion){




                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            Proveedor proveedor = new Proveedor();
                            proveedor.setUid(uid);
                            proveedor.setNom_proveedor(rSocial.getText().toString().trim());
                            proveedor.setRut_proveedor(rutEmpresa.getText().toString().trim());
                            proveedor.setDireccion_proveedor(rDireccion.getText().toString().trim());
                            String id = uid;
                            mDatabase.child("Proveedor").child(uid).setValue(proveedor);

                            new GetCoordinates().execute(direccion.replace(" ","+"));

                            Intent intentP = new Intent(getApplication(), homeProveedor.class);
                            intentP.putExtra(homeProveedor.codigoProvve, id);
                            startActivity(intentP);
                            finish();




                    }




    private class GetCoordinates extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s"+"&key=AIzaSyBp8jp6GZOnh3O1K45KVr4eXbTshTASPDQ",address);
                Log.d("URL",""+url);
                response = http.getHTTPData(url);
                return response;
            }
            catch (Exception ex)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                String error = jsonObject.getString("error_message");
                Log.d("error",""+error);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();


                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();

                Proveedor pro = new Proveedor();
                pro.setUid(uid);
                pro.setLatitud(lat);
                pro.setLongitud(lng);

                mDatabase.child("Proveedor").child(uid).setValue(pro);




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

@Override
    public void onBackPressed(){



    }
}
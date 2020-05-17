package com.example.appquieropan.Cliente.PerfilCliente;

import android.os.Bundle;

import com.example.appquieropan.Entidad.Cliente_direccion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appquieropan.R;
import com.google.firebase.FirebaseApp;
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
import java.util.List;
import java.util.UUID;

public class direcciones_cliente extends AppCompatActivity{

        private List<Cliente_direccion> listPerson = new ArrayList<Cliente_direccion>();
        ArrayAdapter<Cliente_direccion> arrayAdapterPersona;

        EditText direccion,comuna,ciudad,id,uid;
        ListView listV_personas;
        private FirebaseAuth firebaseAuth;
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Cliente_direccion personaSelected;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_direcciones_cliente);

            direccion = findViewById(R.id.txt_direccion);
            comuna = findViewById(R.id.txt_Comuna);
            ciudad = findViewById(R.id.txt_Ciudad);


            firebaseAuth = FirebaseAuth.getInstance();
            listV_personas = findViewById(R.id.lv_datosPersonas);
            listarDatos();

                    listV_personas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    personaSelected = (Cliente_direccion) parent.getItemAtPosition(position);
                    direccion.setText(personaSelected.getDireccion());
                    comuna.setText(personaSelected.getComuna());
                    ciudad.setText(personaSelected.getCiudad());

                }
            });

        }

        private void listarDatos() {

            db.collection("Cliente_direccion")
                    .whereEqualTo("id_cliente", firebaseAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            listPerson.clear();
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Cliente_direccion p = document.toObject(Cliente_direccion.class);
                                    listPerson.add(p);
                                    arrayAdapterPersona = new ArrayAdapter<Cliente_direccion>(direcciones_cliente.this, android.R.layout.simple_list_item_1, listPerson);
                                    listV_personas.setAdapter(arrayAdapterPersona);
                                }
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            String direc = direccion.getText().toString();
            String com = comuna.getText().toString();
            String ciu = ciudad.getText().toString();

            switch (item.getItemId()){
                case R.id.icon_add:{
                    if (direc.equals("")||com.equals("")||ciu.equals("")){
                        validacion();
                    }
                    else {
                        Cliente_direccion p = new Cliente_direccion();
                        p.setId(UUID.randomUUID().toString());
                        p.setDireccion(direc);
                        p.setComuna(com);
                        p.setCiudad(ciu);
                        p.setUid(firebaseAuth.getCurrentUser().getUid());


                        db.collection("Cliente_direccion").document(p.getId()).set(p);
                        Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
                        limpiarCajas();
                    }
                    break;
                }
                case R.id.icon_save:{
                    Cliente_direccion p = new Cliente_direccion();
                    p.setId(personaSelected.getUid());
                    p.setDireccion(direccion.getText().toString().trim());
                    p.setComuna(comuna.getText().toString().trim());
                    p.setCiudad(ciudad.getText().toString().trim());
                    p.setUid(firebaseAuth.getCurrentUser().getUid());
                    db.collection("Cliente_direccion").document().set(p);
                    Toast.makeText(this,"Actualizado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
                case R.id.icon_delete:{
                    Cliente_direccion p = new Cliente_direccion();
                    p.setId(personaSelected.getId());
                    db.collection("Cliente_direccion").document(p.getId()).delete();
                    Toast.makeText(this,"Eliminado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                    break;
                }
                default:break;
            }
            return true;
        }

        private void limpiarCajas() {
            direccion.setText("");
            ciudad.setText("");
            comuna.setText("");

        }

        private void validacion() {
            String dir= direccion.getText().toString();
            String comu = comuna.getText().toString();
            String city = ciudad.getText().toString();

            if (dir.equals("")){
                direccion.setError("Required");
            }
            else if (comu.equals("")){
                comuna.setError("Required");
            }
            else if (city.equals("")){
                ciudad.setError("Required");
            }

        }
}

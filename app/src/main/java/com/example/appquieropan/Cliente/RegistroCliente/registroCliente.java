package com.example.appquieropan.Cliente.RegistroCliente;

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

import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Entidad.Cliente;
import com.example.appquieropan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registroCliente extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String TAG ="RegistroCliente";
    private EditText email,password, rNombreCli,rPassword;
    private Button btnRegistracliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.rMailCliente);
        password = findViewById(R.id.rPassCliente);
        rPassword = findViewById(R.id.rRePassCliente);
        rNombreCli = findViewById(R.id.rNombreCliente);
        btnRegistracliente = findViewById(R.id.btnRegCli);

        btnRegistracliente.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnRegCli:
                String nombreCliente = rNombreCli.getText().toString().trim();
                String rEmail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String Rpass = rPassword.getText().toString().trim();

                if(!nombreCliente.isEmpty() && !rEmail.isEmpty()&& !pass.isEmpty() && !Rpass.isEmpty()){
                    if(pass.equals(Rpass)){
                        registraNuevoUsuario(rEmail,pass,nombreCliente);
                    }else{
                        Toast.makeText(this, "Las password no son iguales", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private void registraNuevoUsuario(final String rEmail, String pass, final String nombreCliente) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Registrando...");
        dialog.setCancelable(false);
        dialog.show();

        mAuth.createUserWithEmailAndPassword(rEmail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            Cliente cliente = new Cliente();
                            cliente.setId_cliente(uid);
                            cliente.setNom_cliente(nombreCliente);
                            cliente.setEmail_cliente(rEmail);

                            //mDatabase.child("Cliente").child(task.getResult().getUser().getUid()).setValue(cliente);
                            mDatabase.child("Cliente").child(uid).setValue(cliente);

                            Intent intentP = new Intent(getApplication(), HomeCliente.class);
                            intentP.putExtra(HomeCliente.idCliente, uid);
                            startActivity(intentP);

                            dialog.dismiss();


                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            dialog.dismiss();

                        }

                        // ...
                    }
                });
    }
}

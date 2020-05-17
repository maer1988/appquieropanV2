package com.example.appquieropan.Cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appquieropan.Entidad.Cliente;
import com.example.appquieropan.R;
import com.example.appquieropan.opciones;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class loginCliente extends AppCompatActivity implements View.OnClickListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
            mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
            signInButton=findViewById(R.id.sign_in_button);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Referenciamos los views




        signInButton.setOnClickListener(this);


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Log.w("reques", ""+requestCode);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("T", "Google sign in failed"+e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("V",   "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final ProgressDialog dialog = new ProgressDialog(loginCliente.this);
                            dialog.setMessage("Registrando...");
                            dialog.setCancelable(false);
                            dialog.show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("T", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            registrarUsuario();
                            Intent intencion = new Intent(getApplication(), HomeCliente.class);
                            intencion.putExtra(HomeCliente.idCliente, user.getUid());
                            startActivity(intencion);
                            finish();//finaliza actividad
                            dialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("F", "signInWithCredential:failure", task.getException());
                            Toast.makeText(loginCliente.this, "Reinicie la aplicacion y vuelva a intentar", Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });


    }

    private void registrarUsuario() {

        db.collection("Cliente")
                .whereEqualTo("id_cliente",firebaseAuth.getCurrentUser().getUid() )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {

                                CollectionReference cliente = db.collection("Cliente");

                                Map<String, Object> ggbData = new HashMap<>();
                                ggbData.put("id_cliente",firebaseAuth.getCurrentUser().getUid());
                                ggbData.put("nom_cliente", firebaseAuth.getCurrentUser().getDisplayName());
                                ggbData.put("email_cliente", firebaseAuth.getCurrentUser().getEmail());
                                cliente.document(firebaseAuth.getCurrentUser().getUid()).set(ggbData);




                            }
                            else
                            {
                                Log.d("MMM1", "YA EXISTE EL CLIENTE");
                            }
                        }
                     else {
                        Log.d("MMMM2", "Error getting documents: ", task.getException());
                    }
                    }

    });
}

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            // Toast.makeText(this, "usuario ya logueado", Toast.LENGTH_LONG).show();
            Intent intencion = new Intent(getApplication(), HomeCliente.class);
            intencion.putExtra(HomeCliente.idCliente,currentUser.getUid());
            startActivity(intencion);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){


                case R.id.sign_in_button:
                    signIn();
                break;

        }

    }

    @Override
    public void onBackPressed(){

        Intent intencion = new Intent(getApplication(), opciones.class);
        startActivity(intencion);
        finish();

    }


}

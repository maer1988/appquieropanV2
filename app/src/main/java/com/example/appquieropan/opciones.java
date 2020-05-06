package com.example.appquieropan;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.appquieropan.Cliente.loginCliente;
import com.example.appquieropan.Proveedor.loginProveedor;

public class opciones extends AppCompatActivity {

    Button botonCliente, botonProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        botonCliente = findViewById(R.id.btnCliente);
        botonProveedor = findViewById(R.id.btnProveedor);

        botonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaSesionC();
            }
        });
        botonProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaSesionP();
            }
        });
    }
    private void cargaSesionP() {

        Intent cliente = new Intent(getApplication(), loginProveedor.class);
        startActivity(cliente);
        finish();
    }


    private void cargaSesionC() {

        Intent cliente = new Intent(getApplication(), loginCliente.class);
        startActivity(cliente);
        finish();
    }

    @Override
    public void onBackPressed(){


    }
}

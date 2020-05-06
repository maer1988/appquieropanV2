package com.example.appquieropan.Proveedor.Ventas;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.homeProveedor;
import com.example.appquieropan.Proveedor.opcionesSegmentoProveedor;
import com.example.appquieropan.R;

public class tipoVenta extends AppCompatActivity implements View.OnClickListener{

    public static final String codigoProvve="id";

    private Button verListaVenta, verListaReserva;
    private BottomNavigationView botonesNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_venta);

        verListaVenta = findViewById(R.id.verListaVenta);
        verListaReserva = findViewById(R.id.verListaReserva);

        final String userP = getIntent().getStringExtra("id");

        verListaVenta.setOnClickListener(this);
        verListaReserva.setOnClickListener(this);

        botonesNav = findViewById(R.id.botones_navegaVenta);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(tipoVenta.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(tipoVenta.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(tipoVenta.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Intent intent = new Intent(tipoVenta.this, opcionesSegmentoProveedor.class);
                    intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {

                    Toast.makeText(tipoVenta.this, "venta", Toast.LENGTH_SHORT).show();

                }
                return false;

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.verListaVenta:
                Intent intentVenta = new Intent(this, ListaVentas.class);
                startActivity(intentVenta);
                finish();
                break;
            case R.id.verListaReserva:
                Intent intentResrva = new Intent(this, ListaReservas.class);
                startActivity(intentResrva);
                finish();
                break;
        }
    }
}

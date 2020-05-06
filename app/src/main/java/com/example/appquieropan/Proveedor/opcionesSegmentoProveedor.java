package com.example.appquieropan.Proveedor;

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

import com.example.appquieropan.Entidad.Producto;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Proveedor.Facturas.facturasProveedor;
import com.example.appquieropan.Proveedor.Perfil.PerfilProveedor;
import com.example.appquieropan.Proveedor.Ventas.tipoVenta;
import com.example.appquieropan.Proveedor.subProductoMasa.ListaProductosMasa;
import com.example.appquieropan.Proveedor.subProductoPan.ListaProductosPan;
import com.example.appquieropan.Proveedor.subProductoPastel.ListaProductosPastel;
import com.example.appquieropan.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class opcionesSegmentoProveedor extends AppCompatActivity implements View.OnClickListener {

    public static final String codigoProvve="id";

    Button btn_pan, btn_masa,btn_pastel, btn_otros;
    private BottomNavigationView botonesNav;

    private DatabaseReference mDatabase;

    String userP = null;
    private String rutEmpresa = null;
    private String nombreEmpresa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_segmento_proveedor);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        userP = getIntent().getStringExtra("id");

        btn_pan = findViewById(R.id.btnPan);
        btn_masa = findViewById(R.id.btnAmansanderia);
        btn_pastel = findViewById(R.id.btnPasteleria);
        btn_otros = findViewById(R.id.btnOtros);

        btn_pan.setOnClickListener(this);
        btn_masa.setOnClickListener(this);
        btn_pastel.setOnClickListener(this);
        btn_otros.setOnClickListener(this);

        botonesNav = findViewById(R.id.botones_navegacion);

        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home){
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, homeProveedor.class);
                    intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, PerfilProveedor.class);
                    intent.putExtra(PerfilProveedor.idProveedor,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_factura) {
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, facturasProveedor.class);
                    intent.putExtra(facturasProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_productos) {

                    Toast.makeText(opcionesSegmentoProveedor.this, "segmento", Toast.LENGTH_SHORT).show();

                }
                else if(menuItem.getItemId()==R.id.nav_ventas) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(opcionesSegmentoProveedor.this, tipoVenta.class);
                    intent.putExtra(tipoVenta.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                return false;

            }
        });

        obtieneRutEmpresa(userP);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnPan:
                crearRelacionPanProveedor();
                Intent intentP = new Intent(this, ListaProductosPan.class);
                intentP.putExtra(ListaProductosPan.codigoProvve,userP);
                startActivity(intentP);
                break;
            case R.id.btnAmansanderia:
                crearRelacionMasaProveedor();
                Intent intenta = new Intent(this, ListaProductosMasa.class);
                intenta.putExtra(ListaProductosMasa.codigoProvve,userP);
                startActivity(intenta);
                break;
            case R.id.btnPasteleria:
                crearRelacionPastelProveedor();
                Intent intentPs = new Intent(this, ListaProductosPastel.class);
                intentPs.putExtra(ListaProductosPastel.codigoProvve,userP);
                startActivity(intentPs);
                break;
            case R.id.btnOtros:
                crearRelacionOtrasMasaProveedor();
                Toast.makeText(this, "Sin Opcion", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void crearRelacionOtrasMasaProveedor() {

        Producto producto = new Producto();
        producto.setId_producto(userP);
        producto.setDesc_producto("OtrasMasas");
        producto.setRut_empresa(rutEmpresa);
        producto.setNom_empresa(nombreEmpresa);
        //proveedor.setEmail_proveedor(email.getText().toString());
        //proveedor.setNom_proveedor(rSocial.getText().toString().trim());
        //proveedor.setRut_proveedor(rutEmpresa.getText().toString().trim());
        mDatabase.child("OtrasMasas_Proveedor").child(producto.getId_producto()).setValue(producto);

    }

    private void crearRelacionPastelProveedor() {

        Producto producto = new Producto();
        producto.setId_producto(userP);
        producto.setDesc_producto("Pastel");
        producto.setRut_empresa(rutEmpresa);
        producto.setNom_empresa(nombreEmpresa);
        //proveedor.setEmail_proveedor(email.getText().toString());
        //proveedor.setNom_proveedor(rSocial.getText().toString().trim());
        //proveedor.setRut_proveedor(rutEmpresa.getText().toString().trim());
        mDatabase.child("Pastel_Proveedor").child(producto.getId_producto()).setValue(producto);

    }

    private void crearRelacionMasaProveedor() {

        Producto producto = new Producto();
        producto.setId_producto(userP);
        producto.setDesc_producto("Masa");
        producto.setRut_empresa(rutEmpresa);
        producto.setNom_empresa(nombreEmpresa);
        //proveedor.setEmail_proveedor(email.getText().toString());
        //proveedor.setNom_proveedor(rSocial.getText().toString().trim());
        //proveedor.setRut_proveedor(rutEmpresa.getText().toString().trim());
        mDatabase.child("Masa_Proveedor").child(producto.getId_producto()).setValue(producto);

    }

    private void crearRelacionPanProveedor() {

        Producto producto = new Producto();
        producto.setId_producto(userP);
        producto.setDesc_producto("Pan");
        producto.setRut_empresa(rutEmpresa);
        producto.setNom_empresa(nombreEmpresa);
        //proveedor.setEmail_proveedor(email.getText().toString());
        //proveedor.setNom_proveedor(rSocial.getText().toString().trim());
        //proveedor.setRut_proveedor(rutEmpresa.getText().toString().trim());
        mDatabase.child("Pan_Proveedor").child(producto.getId_producto()).setValue(producto);

    }

    private void obtieneRutEmpresa(final String userP) {

        mDatabase.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Proveedor proveedor = snapshot.getValue(Proveedor.class);

                    if(userP.equals(proveedor.getUid())){
                        //cargaDatosPantalla(proveedor.getNom_tipoSubProducto(), subProducto.getDesc_tipoSubProducto(),subProducto.getPrecio(),subProducto.getUrlSubproducto());
                        rutEmpresa = proveedor.getRut_proveedor();
                        nombreEmpresa = proveedor.getNom_proveedor();
                        //txtProveedor.setText("¡Bienvenido "+ nombreEmpresa +"!");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}

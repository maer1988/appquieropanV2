package com.example.appquieropan.Cliente.CarroDeCompras;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appquieropan.Cliente.CarroDeCompras.gpay_library.PaymentsUtil;
import com.example.appquieropan.Cliente.HomeCliente;
import com.example.appquieropan.Cliente.PerfilCliente.PerfilCliente;
import com.example.appquieropan.Entidad.Cliente;
import com.example.appquieropan.Entidad.Producto_Pedido;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class PagoDelCliente extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgGpay, imgReserva;
    private TextView Total;
    private FirebaseAuth firebaseAuth;
    public static final String tipo_pago="tp";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String Total_voucher="";
    String TipoPago;
    int total=0;
    int monto=0;
    Button cancelar;
    ArrayList<Cliente> Cliente = new ArrayList<Cliente>();
    ArrayList<Producto_Pedido> producto_pedido = new ArrayList<Producto_Pedido>();
    ArrayList<String> Id_pedido = new ArrayList<String>();
    ArrayList<Proveedor> proveedorlista = new ArrayList<Proveedor>();

    private BottomNavigationView botonesNav;

    private PaymentsClient mPaymentsClient;
    private View mGooglePayButton;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_del_cliente);

        Total=findViewById(R.id.total);
       // Total.setText(set_total());

        firebaseAuth = FirebaseAuth.getInstance();



        imgGpay = findViewById(R.id.imgGpay);
        imgReserva = findViewById(R.id.imgReservaCliente);
        cancelar= findViewById(R.id.btncancelar);


        imgGpay.setOnClickListener(this);
        imgReserva.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        TipoPago= getIntent().getStringExtra("tp");

        mGooglePayButton = findViewById(R.id.imgGpay);
        mPaymentsClient = PaymentsUtil.createPaymentsClient(this);
        possiblyShowGooglePayButton();

        mGooglePayButton.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {
                        requestPayment(view);
                    }
                });

        switch (TipoPago){

            case ("efectivo"):


                imgGpay.setVisibility(View.INVISIBLE);
                imgReserva.setVisibility(View.VISIBLE);

            break;

            case ("online"):


                imgGpay.setVisibility(View.VISIBLE);
                imgReserva.setVisibility(View.INVISIBLE);
                break;

            case ("ambosPagos"):


                imgGpay.setVisibility(View.VISIBLE);
                imgReserva.setVisibility(View.VISIBLE);

                break;

        }

        botonesNav = findViewById(R.id.botones_navegacion_elige_pago);
        botonesNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId()==R.id.nav_home_cliente){
                    Intent intent = new Intent(PagoDelCliente.this, HomeCliente.class);
                    //intent.putExtra(homeProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(facturasProveedor.this, "home", Toast.LENGTH_SHORT).show();
                }
                else if(menuItem.getItemId()==R.id.nav_pedidos_cliente) {
                    //Toast.makeText(homeProveedor.this, "perfil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PagoDelCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"pendiente");
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_perfil_cliente) {
                    Intent intent = new Intent(PagoDelCliente.this, PerfilCliente.class);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_compras_cliente) {
                    //Toast.makeText(homeProveedor.this, "segmento", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PagoDelCliente.this, ListadoCarroCompra.class);
                    //intent.putExtra(opcionesSegmentoProveedor.codigoProvve,userP);
                    startActivity(intent);
                    finish();
                }
                else if(menuItem.getItemId()==R.id.nav_pagos_pendientes) {
                    //Toast.makeText(homeProveedor.this, "venta", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PagoDelCliente.this, ListadoVoucher.class);
                    intent.putExtra(ListadoVoucher.opcion_voucher,"entregado");
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btncancelar:

                Intent Home = new Intent(this, HomeCliente.class);
                startActivity( Home);
                finish();

                break;

            case R.id.imgGpay:
                cargaVentaGpay();
                break;
            case R.id.imgReservaCliente:
                Intent pagoR = new Intent(PagoDelCliente.this, ComprobanteReservaCliente.class);
                reservaProducto(pagoR);


                break;
        }
    }

    private void reservaProducto( final Intent pagoR) {


        db.collection("Producto_pedido")
                .whereEqualTo("estado", "nuevo")
                .whereEqualTo("idCliente", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Id_pedido.add(document.getId());
                                Producto_Pedido pp = document.toObject(Producto_Pedido.class);
                                producto_pedido.add(pp);
                                total= total + Integer.parseInt(pp.total_precio());

                            }


                            db.collection("Proveedor")
                                    .whereEqualTo("rut_proveedor", producto_pedido.get(0).getRut_proveedor())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {


                                                    Proveedor p = document.toObject(Proveedor.class);
                                                    proveedorlista.add(p);
                                                }


                                                Voucher voucher = new Voucher();

                                                voucher.setIDVoucher(UUID.randomUUID().toString());
                                                voucher.setIDcliente(firebaseAuth.getCurrentUser().getUid());
                                                voucher.setRUTproveedor(proveedorlista.get(0).getRut_proveedor());
                                                voucher.setIDproveedor(proveedorlista.get(0).getId_proveedor());
                                                voucher.setNombreproveedor(proveedorlista.get(0).getNom_proveedor());
                                                voucher.setFechaentrega(getFechaActual());
                                                voucher.setTotal(Integer.toString(total));
                                                voucher.setTipoVenta(TipoPago);

													/* Log.e("VOUCHER","id "+voucher.getIDVoucher());
														Log.e("VOUCHER","id cliente "+voucher.getIDcliente());
														Log.e("VOUCHER","id provedore "+voucher.getIDproveedor());
														Log.e("VOUCHER","fecha "+voucher.getFechaentrega());*/



                                                db.collection("Voucher").document().set(voucher);

                                                for (int z = 0; z < producto_pedido.size(); z++) {
                                                    Log.e("tamaño en voucher","tamaño "+producto_pedido.size());
                                                    Producto_Pedido ppu = new Producto_Pedido();

                                                    ppu.setUid(producto_pedido.get(z).getUid());
                                                    ppu.setEstado("terminado");
                                                    ppu.setTipo_cantidad(producto_pedido.get(z).getTipo_cantidad());
                                                    ppu.setRut_proveedor(producto_pedido.get(z).getRut_proveedor());
                                                    ppu.setPrecio(producto_pedido.get(z).getPrecio());
                                                    ppu.setNombre_producto(producto_pedido.get(z).getNombre_producto());
                                                    ppu.setIdCliente(producto_pedido.get(z).getIdCliente());
                                                    ppu.setCantidad(producto_pedido.get(z).getCantidad());
                                                    ppu.setIDvoucher(voucher.getIDVoucher());


                                                    db.collection("Producto_Detalle").document().set(ppu);



                                                }


                                                for(int i=0;i < Id_pedido.size();i++) {
                                                    db.collection("Producto_pedido").document(Id_pedido.get(i))
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("TAG", "Error deleting document", e);
                                                                }
                                                            });
                                                }



                                                pagoR.putExtra(ComprobanteReservaCliente.voucher_post,voucher.getIDVoucher());
                                                startActivity(pagoR);
                                                finish();





                                            }


                                            else {
                                                Log.d("TAG", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });



                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    private void cargaVentaGpay() {

        Intent pagGpay = new Intent(this, ComprobanteDePagoCliente.class);
        //carroPan.putExtra(CarroComprasCliente.idSubproducto,codigoSubProductoCarro);
        startActivity(pagGpay);
    }

    private void cargaVentaWebPay() {

        Intent pagoWebPay = new Intent(this, ComprobanteDePagoCliente.class);
        //carroPan.putExtra(CarroComprasCliente.idSubproducto,codigoSubProductoCarro);
        startActivity(pagoWebPay);
    }

    public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
        return formateador.format(ahora);
    }


// funciones GPAY

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        if (request == null) {
            return;
        }


        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(this,
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            setGooglePayAvailable(task.getResult());
                        } else {
                            Log.w("isReadyToPay failed", task.getException());
                        }
                    }
                });
    }


    private void setGooglePayAvailable(boolean available) {
        if (available) {

            mGooglePayButton.setVisibility(View.VISIBLE);
        } else {
            mGooglePayButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case PagoDelCliente.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case PagoDelCliente.RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                    default:
                        // Do nothing.
                }

                // Re-enables the Google Pay payment button.
                mGooglePayButton.setClickable(true);
                break;
        }
    }


    private void handlePaymentSuccess(PaymentData paymentData) {
        String paymentInformation = paymentData.toJson();

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        if (paymentInformation == null) {
            return;
        }
        JSONObject paymentMethodData;

        try {
            paymentMethodData = new JSONObject(paymentInformation).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type")
                    .equals("PAYMENT_GATEWAY")
                    && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
                    .equals("examplePaymentMethodToken")) {
                AlertDialog alertDialog =
                        new AlertDialog.Builder(this)
                                .setTitle("Warning")
                                .setMessage(
                                        "Gateway name set to \"example\" - please modify "
                                                + "Constants.java and replace it with your own gateway.")
                                .setPositiveButton("OK", null)
                                .create();
                alertDialog.show();
            }

            String billingName =
                    paymentMethodData.getJSONObject("info").getJSONObject("billingAddress").getString("name");
            Log.d("BillingName", billingName);


            // Logging token string.
            Log.d("GooglePaymentToken", paymentMethodData.getJSONObject("tokenizationData").getString("token"));
        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString());
            return;
        }
    }


    private void handleError(int statusCode) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }

    // This method is called when the Pay with Google button is clicked.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        mGooglePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        //String price = Total.getText().toString();
        String price = "1990";

        // TransactionInfo transaction = PaymentsUtil.createTransaction(price);
        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price);
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }


    public void onBackPressed(){


    }

}

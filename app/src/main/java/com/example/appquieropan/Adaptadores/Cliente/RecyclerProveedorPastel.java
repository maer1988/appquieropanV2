package com.example.appquieropan.Adaptadores.Cliente;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Cliente.SubProductosDelProveedor.SubProductoPastelCliente;
import com.example.appquieropan.Entidad.Proveedor;
import com.example.appquieropan.R;

import java.util.ArrayList;

public class RecyclerProveedorPastel  extends RecyclerView.Adapter<RecyclerProveedorPastel.ProveedorViewHolder> {


    private Context mContext;
    private int layoutResource;
    private ArrayList<Proveedor> arrayListProveedor;
    Proveedor proveedor;

    public RecyclerProveedorPastel(Context mContext, int layoutResource, ArrayList<Proveedor> arrayListProveedor) {
        this.mContext = mContext;
        this.layoutResource = layoutResource;
        this.arrayListProveedor = arrayListProveedor;

    }

    @NonNull
    @Override
    public RecyclerProveedorPastel.ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(layoutResource, viewGroup, false);

        return new RecyclerProveedorPastel.ProveedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerProveedorPastel.ProveedorViewHolder proveedorViewHolder, int i) {

        proveedor = arrayListProveedor.get(i);
        proveedorViewHolder.nombreProveedor.setText(proveedor.getNom_proveedor());
        proveedorViewHolder.mRut.setText(proveedor.getRut_proveedor());
        proveedorViewHolder.tipoDespacho.setText("Tipo de Despacho: " + proveedor.getTipo_Despacho_Proveedor());
        proveedorViewHolder.tipoPago.setText("Tipo de Pago: " + proveedor.getTipo_Pago_Proveedor());
        proveedorViewHolder.notaProveedor.setText(proveedor.getNota_proveeedor());
        //proveedorViewHolder.mPrecio.setText("Precio: "+proveedor.getPrecio());

        Glide
                .with(mContext)
                .load(proveedor.getUrl_proveedor())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        proveedorViewHolder.mProgressBar.setVisibility(View.GONE);
                        proveedorViewHolder.imageProveedor.setVisibility(View.VISIBLE);
                        proveedorViewHolder.imageProveedor.setImageResource(R.drawable.imagen_no_disponible);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        proveedorViewHolder.mProgressBar.setVisibility(View.GONE);
                        proveedorViewHolder.imageProveedor.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(proveedorViewHolder.imageProveedor);

    }

    @Override
    public int getItemCount() {

        if(arrayListProveedor.size() > 0 ){
            return arrayListProveedor.size();
        }
        return 0;

    }

    public class ProveedorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nombreProveedor,tipoDespacho,tipoPago, descripDespacho, notaProveedor, mRut;
        ImageView imageProveedor;
        ProgressBar mProgressBar;
        Button btnVerProv;

        public ProveedorViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreProveedor = itemView.findViewById(R.id.txtNombreE);
            tipoDespacho = itemView.findViewById(R.id.txtTipoD);
            tipoPago = itemView.findViewById(R.id.txtTipoP);
            descripDespacho = itemView.findViewById(R.id.txtDespacho);
            notaProveedor = itemView.findViewById(R.id.idNotaProveedor);

            imageProveedor = itemView.findViewById(R.id.imgProveedorCliente);
            mProgressBar = itemView.findViewById(R.id.idProgressCliente);
            mRut = itemView.findViewById(R.id.txtId);
            btnVerProv = itemView.findViewById(R.id.btnEditPanCliente);
            btnVerProv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnEditPanCliente:
                    Intent intent = new Intent(mContext, SubProductoPastelCliente.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(SubProductoPastelCliente.rutE,mRut.getText().toString());
                    mContext.startActivity(intent);
                    break;
            }

        }
    }

}

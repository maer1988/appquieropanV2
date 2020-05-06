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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.appquieropan.Cliente.DetalleProductoDelProveedor.SubProductoElegidoPan;
import com.example.appquieropan.Entidad.TipoSubProducto;
import com.example.appquieropan.Proveedor.subProductoPan.detalleProductoSeleccionado;
import com.example.appquieropan.Proveedor.subProductoPastel.detalleProductoSeleccionadoPastel;
import com.example.appquieropan.R;

import java.util.ArrayList;

public class RecyclerProveedorPastelSel extends RecyclerView.Adapter<RecyclerProveedorPastelSel.ProductoViewHolder>{

    private Context mCcontext;
    private int layoutResource;
    private ArrayList<TipoSubProducto> arrayListProductos;
    TipoSubProducto tipoSubProducto;

    public RecyclerProveedorPastelSel(Context context, int layoutResource, ArrayList<TipoSubProducto> arrayListProductos) {
        this.mCcontext = context;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
    }

    @NonNull
    @Override
    public RecyclerProveedorPastelSel.ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mCcontext).inflate(layoutResource, viewGroup, false);

        return new RecyclerProveedorPastelSel.ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerProveedorPastelSel.ProductoViewHolder productoViewHolder, int i) {

        tipoSubProducto = arrayListProductos.get(i);
        productoViewHolder.mID.setText(tipoSubProducto.getUid());
        productoViewHolder.mNombre.setText(tipoSubProducto.getNom_tipoSubProducto());

        Glide
                .with(mCcontext)
                .load(tipoSubProducto.getUrlSubproducto())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        productoViewHolder.mProgressBar.setVisibility(View.GONE);
                        productoViewHolder.imagenProducto.setVisibility(View.VISIBLE);
                        productoViewHolder.imagenProducto.setImageResource(R.drawable.imagen_no_disponible);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        productoViewHolder.mProgressBar.setVisibility(View.GONE);
                        productoViewHolder.imagenProducto.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(productoViewHolder.imagenProducto);


    }

    @Override
    public int getItemCount() {

        if(arrayListProductos.size() > 0 ){
            return arrayListProductos.size();
        }
        return 0;
    }

    public  class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mPrecio, mNombre, mID;
        ImageView imagenProducto;
        ProgressBar mProgressBar;
        Button btnEditar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            //mPrecio = itemView.findViewById(R.id.txtPrecio);
            mNombre = itemView.findViewById(R.id.txtNombreClie);
            imagenProducto = itemView.findViewById(R.id.imgSubPrdCli);
            mProgressBar = itemView.findViewById(R.id.idProgressCli);
            //btnEditar = itemView.findViewById(R.id.btnEditarPan);
            mID = itemView.findViewById(R.id.idPrdo);
            //btnEditar.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.imgSubPrdCli:
                    Intent intent = new Intent(mCcontext, SubProductoElegidoPan.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //Toast.makeText(mCcontext, "valores : "+imagenProducto, Toast.LENGTH_SHORT).show();
                    intent.putExtra(SubProductoElegidoPan.idSubproducto,mID.getText().toString());
                    intent.putExtra(SubProductoElegidoPan.Tabla,"SubProductoPastel");
                    mCcontext.startActivity(intent);

                    break;
            }
        }
    }

}
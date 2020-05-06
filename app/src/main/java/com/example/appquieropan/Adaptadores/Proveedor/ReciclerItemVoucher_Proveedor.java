package com.example.appquieropan.Adaptadores.Proveedor;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appquieropan.Entidad.Producto_Detalle;
import com.example.appquieropan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReciclerItemVoucher_Proveedor extends RecyclerView.Adapter<ReciclerItemVoucher_Proveedor.MyViewHolder>{
    public ArrayList<Producto_Detalle> mDataset;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        Context context;
        public TextView nombrep;
        public TextView cantidadp;
        public TextView preciop;
        public TextView totalp;
        //Button btnDelete;
        private DatabaseReference mDatabase;


        public MyViewHolder(View v) {
            super(v);
            mDatabase = FirebaseDatabase.getInstance().getReference();

            context=v.getContext();
            nombrep= (TextView) v.findViewById(R.id.descripcion_prove);
            cantidadp=(TextView) v.findViewById(R.id.cantidadp);
            preciop=(TextView) v.findViewById(R.id.preciop);
            totalp=(TextView) v.findViewById(R.id.total_pro_item);
          


        }


        void setOnClickListeners(){

            
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReciclerItemVoucher_Proveedor(ArrayList<Producto_Detalle> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReciclerItemVoucher_Proveedor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recicler_item_voucher__proveedor, parent, false);


        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombrep.setText(mDataset.get(position).getNombre_producto());
        holder.cantidadp.setText(mDataset.get(position).getCantidad());
        holder.preciop.setText(mDataset.get(position).getPrecio());
        holder.totalp.setText(mDataset.get(position).total_precio());

       // holder.setOnClickListeners();

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

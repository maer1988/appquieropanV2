package com.example.appquieropan.Adaptadores.Cliente;


import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appquieropan.Entidad.Producto_Pedido;
import com.example.appquieropan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerItemCarro extends RecyclerView.Adapter<RecyclerItemCarro.MyViewHolder>{
    public ArrayList<Producto_Pedido> mDataset;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
  Context context;
        public TextView nombre;
        public TextView cantidad;
        public TextView precio;
        public TextView total;
        //Button btnDelete;
        private DatabaseReference mDatabase;


        public MyViewHolder(View v) {
            super(v);
            mDatabase = FirebaseDatabase.getInstance().getReference();

context=v.getContext();
            nombre= (TextView) v.findViewById(R.id.nombre);
            cantidad=(TextView) v.findViewById(R.id.cantidad);
            precio=(TextView) v.findViewById(R.id.precio);
            total=(TextView) v.findViewById(R.id.total);
          //  btnDelete = (Button) v.findViewById(R.id.delete);



        }


void setOnClickListeners(){

           // btnDelete.setOnClickListener(this);

}


        @Override
        public void onClick(View view) {

//            int i = getAdapterPosition();
//            switch (view.getId()){
//
//
//                case R.id.delete:
//
//                    Producto_Pedido pp = new Producto_Pedido();
//                    pp.setUid(mDataset.get(i).getUid());
//
//                    mDatabase.child("Producto_pedido").child(pp.getUid()).removeValue();
//                    RecyclerItemCarro.this.notifyItemRemoved(i);
//                    RecyclerItemCarro.this.notifyDataSetChanged();
//
//
//
//                    break;
//
//
//            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerItemCarro(ArrayList<Producto_Pedido> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerItemCarro.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carro, parent, false);


        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre.setText("Descripcion: "+mDataset.get(position).getNombre_producto());
        holder.cantidad.setText("Cantidad: "+mDataset.get(position).getCantidad());
        holder.precio.setText("Precio: $"+mDataset.get(position).getPrecio());
        holder.total.setText("Total item: $"+mDataset.get(position).total_precio());

       holder.setOnClickListeners();

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

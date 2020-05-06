package com.example.appquieropan.Adaptadores.Cliente;


import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.appquieropan.Cliente.CarroDeCompras.ListadoItemVoucher;
import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerVoucher extends RecyclerView.Adapter<RecyclerVoucher.MyViewHolder>{
    public ArrayList<Voucher> mDataset;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        Context context;
        public TextView nombre;
        public TextView rut;
        public TextView razon;
        public TextView fecha;
        public TextView total;
        public TextView estado;
        Button btnConsulta;
        private DatabaseReference mDatabase;


        public MyViewHolder(View v) {
            super(v);
            mDatabase = FirebaseDatabase.getInstance().getReference();

            context=v.getContext();
            nombre= v.findViewById(R.id.nombre);
            rut= v.findViewById(R.id.rut);
            razon= v.findViewById(R.id.razon);
            fecha= v.findViewById(R.id.fecha);
            total= v.findViewById(R.id.total);
            estado=v.findViewById(R.id.estado);
            btnConsulta = v.findViewById(R.id.consulta);



        }


        void setOnClickListeners(){

             btnConsulta.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {


          switch (view.getId()){


              case R.id.consulta:


                Intent intent = new Intent(context, ListadoItemVoucher.class);
                intent.putExtra("idvoucher",nombre.getText());
                context.startActivity(intent);

                 break;


           }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerVoucher(ArrayList<Voucher> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerVoucher.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recycler_voucher, parent, false);


        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombre.setText(mDataset.get(position).getIDVoucher());
        holder.rut.setText(mDataset.get(position).getRUTproveedor());
        holder.razon.setText(mDataset.get(position).getNombreproveedor());
        holder.fecha.setText(mDataset.get(position).getFechaentrega());
        holder.total.setText(mDataset.get(position).getTotal());
        holder.estado.setText(mDataset.get(position).getEstado());

        holder.setOnClickListeners();

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

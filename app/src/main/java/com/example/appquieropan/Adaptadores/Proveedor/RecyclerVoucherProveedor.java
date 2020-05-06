package com.example.appquieropan.Adaptadores.Proveedor;


import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.appquieropan.Entidad.Voucher;
import com.example.appquieropan.Proveedor.Ventas.ListadoItemVoucher_Proveedor;
import com.example.appquieropan.Proveedor.Ventas.cerrar_voucher;
import com.example.appquieropan.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerVoucherProveedor extends RecyclerView.Adapter<RecyclerVoucherProveedor.MyViewHolder>{
    public ArrayList<Voucher> mDataset;




    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        Context context;
        public TextView nombrep;
        public TextView rutp;
        public TextView razonp;
        public TextView fechap;
        public TextView totalp;
        public TextView estadop;
        Button btnConsulta,btncerrar;
        private DatabaseReference mDatabase;


        public MyViewHolder(View v) {
            super(v);
            mDatabase = FirebaseDatabase.getInstance().getReference();

            context=v.getContext();
            nombrep= v.findViewById(R.id.idcomprobante);
            rutp= v.findViewById(R.id.rut_prove);
            razonp= v.findViewById(R.id.nombre_prove);
            fechap= v.findViewById(R.id.fecha_venta);
            totalp= v.findViewById(R.id.total_pro);
            estadop=v.findViewById(R.id.estadop);
            btnConsulta = v.findViewById(R.id.consultap);
            btncerrar = v.findViewById(R.id.cerrar);



        }


        void setOnClickListeners(){

            btnConsulta.setOnClickListener(this);
            btncerrar.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {


            switch (view.getId()){

                case R.id.consultap:
                    Intent intent = new Intent(context, ListadoItemVoucher_Proveedor.class);
                    intent.putExtra("idvoucher",nombrep.getText());
                    context.startActivity(intent);

                    break;

                case R.id.cerrar:
                    Intent intent2 = new Intent(context, cerrar_voucher.class);
                    intent2.putExtra("idvoucher",nombrep.getText());
                    intent2.putExtra("fecha",fechap.getText());
                    intent2.putExtra("total",totalp.getText());

                    context.startActivity(intent2);

                    break;

            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerVoucherProveedor(ArrayList<Voucher> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerVoucherProveedor.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recycler_voucher_proveedor, parent, false);


        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.nombrep.setText(mDataset.get(position).getIDVoucher());
        holder.rutp.setText(mDataset.get(position).getRUTproveedor());
        holder.razonp.setText(mDataset.get(position).getNombreproveedor());
        holder.fechap.setText(mDataset.get(position).getFechaentrega());
        holder.totalp.setText(mDataset.get(position).getTotal());
        holder.estadop.setText(mDataset.get(position).getEstado());

      if (mDataset.get(position).getEstado().equals("pendiente")){
          holder.btncerrar.setVisibility(View.VISIBLE);
      }else{
          holder.btncerrar.setVisibility(View.INVISIBLE);
      }

        holder.setOnClickListeners();

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }




}

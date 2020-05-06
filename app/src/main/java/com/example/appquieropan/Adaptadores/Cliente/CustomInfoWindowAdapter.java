package com.example.appquieropan.Adaptadores.Cliente;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.appquieropan.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.infowindow_layout, null);
        String[] info = m.getTitle().split("&");
        String url = m.getSnippet();
        ((TextView)v.findViewById(R.id.info_window_nombre)).setText(info[0]);
        ((TextView)v.findViewById(R.id.info_window_placas)).setText(info[1]);
        ((TextView)v.findViewById(R.id.info_window_estado)).setText(info[2]);
        ((TextView)v.findViewById(R.id.info_window_estado2)).setText(info[3]);
        ((TextView)v.findViewById(R.id.info_window_estado3)).setText(info[4]);
        ((TextView)v.findViewById(R.id.info_window_estado4)).setText("Valoracion: "+info[5]);

        return v;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}
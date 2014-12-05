package com.example.serj.inmobiliaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends ArrayAdapter<Inmueble>{

    private Context contexto;
    private ArrayList<Inmueble> inmuebles;
    private int recurso;
    private static LayoutInflater i;

    public static class ViewHolder {
        public TextView tvLocalidad, tvPrecio;
    }

    public Adaptador(Context context, int resource, ArrayList<Inmueble> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.recurso = resource;
        this.inmuebles = objects;
        this.i = (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh =  null;

        if(convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvLocalidad = (TextView)convertView.findViewById(R.id.tvLocalidad);
            vh.tvPrecio = (TextView)convertView.findViewById(R.id.tvPrecio);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tvLocalidad.setText(inmuebles.get(position).getLocalidad());
        vh.tvPrecio.setText(String.valueOf(inmuebles.get(position).getPrecio()));

        return convertView;
    }
}

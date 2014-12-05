package com.example.serj.inmobiliaria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Detalle extends Fragment implements View.OnClickListener{

    /**********************************************************************************************/
    /**************************************VARIABLES***********************************************/
    /**********************************************************************************************/

    private View v;
    private ArrayList<Bitmap> fotos;
    private ImageView iv;
    private Button btAnt, btSig;
    private int pos;
    private String id;

    public Detalle() {}

    /**********************************************************************************************/
    /**************************************ON...***************************************************/
    /**********************************************************************************************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate el layout para este fragmento
        v = inflater.inflate(R.layout.fragment_detalle, container, false);
        iv = (ImageView)v.findViewById(R.id.galeria);
        btAnt = (Button)v.findViewById(R.id.btAnterior);
        btSig = (Button)v.findViewById(R.id.btSiguiente);
        btAnt.setOnClickListener(this);
        btSig.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        // Controla la acción que se realiza según el botón que se pulse
        switch (v.getId()) {
            case R.id.btAnterior:
                anterior();
                break;
            case R.id.btSiguiente:
                siguiente();
                break;
        }
    }

    /**********************************************************************************************/
    /***********************************MÉTODOS AUXILIARES*****************************************/
    /**********************************************************************************************/

    public void setDetalle(String id){
        // Recoge y muestra todas las imagenes de un Inmueble
        this.id = id;
        fotos = new ArrayList<Bitmap>();
        guardarImagenes();
    }

    public void guardarImagenes(){
        // Recoge todas las fotos de la memoria y se queda con las que corresponden al ID
        // Si hay fotos las muestra
        // (Esto debería de ir en una hebra aparte)
        File[] allPhotos = getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM).listFiles();
        for(int i=0; i<allPhotos.length; i++){
            String photoId = allPhotos[i].getPath();
            if(photoId.contains("inmueble_"+ this.id + "_")){
                Bitmap foto = BitmapFactory.decodeFile(allPhotos[i].getPath());
                fotos.add(foto);
            }
        }
        if(!fotos.isEmpty()) {
            pos = 0;
            iv.setImageBitmap(fotos.get(pos));
        } else {
            iv.setImageDrawable(getResources().getDrawable(R.drawable.nodisponible));
            Toast.makeText(getActivity(), getString(R.string.no_fotos), Toast.LENGTH_SHORT).show();
        }
    }

    public void anterior(){
        // Retrocede una imagen en la galería
        if( fotos != null && pos-1 >= 0){
            pos--;
            iv.setImageBitmap(fotos.get(pos));
        }
    }

    public void siguiente(){
        // Avanza una imagen en la galería
        if(fotos != null && pos + 1 < fotos.size()){
            pos++;
            iv.setImageBitmap(fotos.get(pos));
        }
    }
}

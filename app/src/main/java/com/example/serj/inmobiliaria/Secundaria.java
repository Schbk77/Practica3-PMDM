package com.example.serj.inmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Secundaria extends Activity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundaria);
        // Muestra el detalle en el fragmento
        id = getIntent().getStringExtra(getString(R.string.tag_id));
        final Detalle fragmento_detalle = (Detalle)getFragmentManager().findFragmentById(R.id.fragment3);
        if(fragmento_detalle != null && fragmento_detalle.isInLayout()){
            fragmento_detalle.setDetalle(id);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Cuando se gire la pantalla mostrara el detalle del inmueble seleccionado
        super.onSaveInstanceState(outState);
        Intent i = new Intent();
        i.putExtra(getString(R.string.tag_id), id);
        setResult(RESULT_OK, i);
        finish();
    }
}

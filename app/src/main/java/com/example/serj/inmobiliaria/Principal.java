package com.example.serj.inmobiliaria;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Principal extends Activity {

    private ArrayList<Inmueble> inmuebles;
    private Adaptador ad;
    private ListView lv;
    private final int ACTIVDAD_SECUNDARIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void leerXML (){
        //Lee desde el archivo .xml guardado en la memoria externa privada
        //Guarda en el ArrayList los Discos almacenados en el archivo
        Inmueble i = new Inmueble();
        String etiqueta;
        XmlPullParser lectorxml = Xml.newPullParser();
        try {
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"archivo.xml")),"utf-8");
            int evento = lectorxml.getEventType();

            while (evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("inmueble")==0){
                        i = new Inmueble();
                    }
                    if(etiqueta.compareTo("_id")==0){
                        i.set_id(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("localidad")==0){
                        i.setLocalidad(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("direccion")==0){
                        i.setDireccion(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("tipo")==0){
                        i.setTipo(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo("precio")==0){
                        i.setPrecio(Double.parseDouble(lectorxml.nextText()));

                    }
                    if(etiqueta.compareTo("foto")==0){
                        i.setFoto(lectorxml.nextText());
                    }
                }else if(evento == XmlPullParser.END_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("inmueble")==0){
                        inmuebles.add(i);
                    }
                }
                evento = lectorxml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        //Inicializa el ArrayList con los datos del archivo .xml
        //Añade el Adaptador al ListView
        inmuebles = new ArrayList<Inmueble>();
        crearXMLysetDefaultCDs();
        leerXML();
        lv = (ListView)findViewById(R.id.lvLista);
        ad = new Adaptador(this, R.layout.detallelista, inmuebles);
        lv.setAdapter(ad);
        final Detalle fragmento_detalle = (Detalle)getFragmentManager().findFragmentById(R.id.fragment2);
        final boolean horizontal = fragmento_detalle != null && fragmento_detalle.isInLayout();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(horizontal) {
                    // Mostrar detalle lista
                    Toast.makeText(getApplicationContext(), "Item at position: "+i, Toast.LENGTH_SHORT).show();
                } else {
                    // Lanzar actividad secundaria
                    Intent intent = new Intent(Principal.this, Secundaria.class);
                    // Meter datos
                    startActivityForResult(intent, ACTIVDAD_SECUNDARIA); // Para que cuando vuelva y este en horizontal lo muestre
                }
            }
        });
        registerForContextMenu(lv);
    }

    public void crearXMLysetDefaultCDs() {
        try {
            FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "inmuebles");

            docxml.startTag(null, "inmueble");
                docxml.startTag(null, "_id");
                    docxml.text("1");
                docxml.endTag(null, "_id");
                docxml.startTag(null, "localidad");
                    docxml.text("Granada");
                docxml.endTag(null, "localidad");
                docxml.startTag(null, "direccion");
                    docxml.text("Calle Falsa, 123");
                docxml.endTag(null, "direccion");
                docxml.startTag(null, "tipo");
                    docxml.text("Piso");
                docxml.endTag(null, "tipo");
                docxml.startTag(null, "precio");
                    docxml.text("65000");
                docxml.endTag(null, "precio");
                docxml.startTag(null, "foto");
                    docxml.text("drawable://" + R.drawable.casa);
                docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.startTag(null, "inmueble");
            docxml.startTag(null, "_id");
            docxml.text("1");
            docxml.endTag(null, "_id");
            docxml.startTag(null, "localidad");
            docxml.text("Granada");
            docxml.endTag(null, "localidad");
            docxml.startTag(null, "direccion");
            docxml.text("Calle Falsa, 123");
            docxml.endTag(null, "direccion");
            docxml.startTag(null, "tipo");
            docxml.text("Piso");
            docxml.endTag(null, "tipo");
            docxml.startTag(null, "precio");
            docxml.text("65000");
            docxml.endTag(null, "precio");
            docxml.startTag(null, "foto");
            docxml.text("drawable://" + R.drawable.casa);
            docxml.endTag(null, "foto");
            docxml.endTag(null, "inmueble");

            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
package com.example.serj.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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

    /**********************************************************************************************/
    /**************************************VARIABLES***********************************************/
    /**********************************************************************************************/

    private ArrayList<Inmueble> inmuebles;
    private Adaptador ad;
    private ListView lv;
    private Detalle fragmento_detalle;
    private boolean horizontal;
    private final int ACTIVDAD_SECUNDARIA = 1;
    private final int EDITAR_INMUEBLE = 2;

    /**********************************************************************************************/
    /**************************************ON...***************************************************/
    /**********************************************************************************************/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            //Segun el resultado de otra actividad, realiza una acción determinada
            switch (requestCode){
                case ACTIVDAD_SECUNDARIA:{
                    // Muestra el detalle del Inmueble que se estaba visualizando antes de girar
                    if(horizontal){
                        String id = data.getStringExtra(getString(R.string.tag_id));
                        fragmento_detalle.setDetalle(id);
                    }
                    break;
                }
                case EDITAR_INMUEBLE: {
                    // Controla registros únicos y modifica los datos del Inmueble
                    int id = data.getIntExtra(getString(R.string.tag_id), 0);
                    int pos = data.getIntExtra(getString(R.string.tag_posicion), 0);
                    String localidad = data.getStringExtra(getString(R.string.tag_localidad));
                    String direccion = data.getStringExtra(getString(R.string.tag_direccion));
                    String tipo = data.getStringExtra(getString(R.string.tag_tipo));
                    double precio = data.getDoubleExtra(getString(R.string.tag_precio), 0);
                    Inmueble inm = new Inmueble(id, localidad, direccion, tipo, precio);
                    inmuebles.set(pos, inm);
                    guardarXML();
                    ad.notifyDataSetChanged();
                    Toast.makeText(this,getString(R.string.inm_mod), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Inicializa los componentes del layout principal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        initComponents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menu de la ActionBar
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Realiza la acción que se elija del menú de la ActionBar
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_anadir) {
            return anadir();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //Infla el menu que se visualiza al hacer longClick en un elemento del ListView
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuopciones, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Realiza la acción que se elija del menu contextual
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = info.position;
        if(id == R.id.opEditar){
            return editar(posicion);
        }else if(id == R.id.opBorrar) {
            return borrar(posicion);
        }
        return super.onContextItemSelected(item);
    }

    /**********************************************************************************************/
    /***************************************EDICIÓN************************************************/
    /**********************************************************************************************/

    private boolean anadir(){
        //Método que crea un AlertDialog con un layout personalizado y añade un inmueble nuevo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View entradaTexto = inflater.inflate(R.layout.dialog_anadir, null);
        builder.setTitle(getString(R.string.nuevoinmueble));
        builder.setView(entradaTexto);
        final EditText et1 = (EditText)entradaTexto.findViewById(R.id.etLocalidad);
        final EditText et2 = (EditText)entradaTexto.findViewById(R.id.etDireccion);
        final EditText et3 = (EditText)entradaTexto.findViewById(R.id.etTipo);
        final EditText et4 = (EditText)entradaTexto.findViewById(R.id.etPrecio);
        builder.setPositiveButton(getString(R.string.btanadir), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Controla que los campos del EditText no estén vacios
                if (!et1.getText().toString().isEmpty() &&
                        !et2.getText().toString().isEmpty() &&
                        !et3.getText().toString().isEmpty() &&
                        !et4.getText().toString().isEmpty()) {
                    Inmueble nuevoInmueble = new Inmueble();
                    nuevoInmueble.set_id(get_id());
                    nuevoInmueble.setLocalidad(et1.getText().toString());
                    nuevoInmueble.setDireccion(et2.getText().toString());
                    nuevoInmueble.setTipo(et3.getText().toString());
                    nuevoInmueble.setPrecio(Double.parseDouble(et4.getText().toString()));
                    //Controla registros únicos y añade un Inmueble nuevo
                    if(!inmuebles.contains(nuevoInmueble)){
                        inmuebles.add(nuevoInmueble);
                        guardarXML();
                        ad.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getApplicationContext(), getString(R.string.inm_ex), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.btcancelar),null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public boolean borrar(final int pos){
        //Método que crea un AlertDialog que nos permite borrar un Inmueble y sus fotos
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.dialog_title);
        alert.setMessage(R.string.dialog_message);
        alert.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                borrarFotosMemoria(pos);
                inmuebles.remove(pos);
                guardarXML();
                ad.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), getString(R.string.inm_del), Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton(R.string.no,null);
        AlertDialog dialog = alert.create();
        dialog.show();
        return true;
    }

    public boolean editar(final int pos){
        //Método que lanza una actividad para obtener un resultado
        Intent nuevoIntent = new Intent(this, Editar.class);
        Bundle b = new Bundle();
        b.putString(getString(R.string.tag_localidad), inmuebles.get(pos).getLocalidad());
        b.putString(getString(R.string.tag_direccion), inmuebles.get(pos).getDireccion());
        b.putString(getString(R.string.tag_tipo), inmuebles.get(pos).getTipo());
        b.putDouble(getString(R.string.tag_precio), inmuebles.get(pos).getPrecio());
        b.putInt(getString(R.string.tag_id), inmuebles.get(pos).get_id());
        b.putInt(getString(R.string.tag_posicion), pos);
        nuevoIntent.putExtras(b);
        startActivityForResult(nuevoIntent, EDITAR_INMUEBLE);
        return true;
    }

    /**********************************************************************************************/
    /***********************************MÉTODOS AUXILIARES*****************************************/
    /**********************************************************************************************/

    private void initComponents() {
        // Inicializa el ArrayList con los datos del archivo .xml
        // Añade el Adaptador al ListView
        // Controla que acción realizar al pulsar un item de la lista según la orientación
        inmuebles = new ArrayList<Inmueble>();
        leerXML();
        lv = (ListView)findViewById(R.id.lvLista);
        ad = new Adaptador(this, R.layout.detallelista, inmuebles);
        lv.setAdapter(ad);
        fragmento_detalle = (Detalle)getFragmentManager().findFragmentById(R.id.fragment2);
        horizontal = fragmento_detalle != null && fragmento_detalle.isInLayout();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(horizontal) {
                    // Mostrar detalle lista
                    String id = String.valueOf(inmuebles.get(i).get_id());
                    fragmento_detalle.setDetalle(id);
                } else {
                    // Lanzar actividad secundaria
                    Intent intent = new Intent(Principal.this, Secundaria.class);
                    // Meter datos
                    String id = String.valueOf(inmuebles.get(i).get_id());
                    intent.putExtra(getString(R.string.tag_id), id);
                    startActivityForResult(intent, ACTIVDAD_SECUNDARIA); // Para que cuando vuelva y este en horizontal lo muestre
                }
            }
        });
        registerForContextMenu(lv);
    }

    public void borrarFotosMemoria(int pos){
        // Borra todas las fotos almacenadas segun el id del elemento a borrar
        String id = String.valueOf(inmuebles.get(pos).get_id());
        File[] allPhotos = getExternalFilesDir(Environment.DIRECTORY_DCIM).listFiles();
        for(int i=0; i<allPhotos.length; i++){
            String photoId = allPhotos[i].getPath();
            if(photoId.contains(getString(R.string.inm)+ id + "_")){
                allPhotos[i].delete();
            }
        }
    }

    public int get_id() {
        // En caso de no haber ningun Inmueble devuelve el ID -> 0
        // Si hay inmuebles, devuelve el ID siguiente al último existente
        if (inmuebles.isEmpty()) {
            return 0;
        } else {
            return (inmuebles.get(inmuebles.size()-1).get_id())+1;
        }
    }

    /**********************************************************************************************/
    /*****************************************XML**************************************************/
    /**********************************************************************************************/

    private void guardarXML (){
        //Actualiza el archivo .xml guardado en la memoria externa privada
        //Recoge los datos almacenados en el ArrayList para actualizar el .xml
        File file = new File(getExternalFilesDir(null), getString(R.string.nombre_xml));
        FileOutputStream fosxml = null;
        try {
            fosxml = new FileOutputStream(file);
        }catch (FileNotFoundException e){
            Toast.makeText(this, getString(R.string.no_ex), Toast.LENGTH_SHORT).show();
        }
        XmlSerializer docxml = Xml.newSerializer();
        try {
            docxml.setOutput(fosxml, getString(R.string.codificacion));
            docxml.startDocument(null, true);
            docxml.setFeature(getString(R.string.indent), true);
            docxml.startTag(null, getString(R.string.root));

            for(int i = 0; i<inmuebles.size(); i++){
                docxml.startTag(null, getString(R.string.etiqueta_objeto));
                docxml.startTag(null, getString(R.string.etiqueta_elemento1));
                docxml.text(String.valueOf(inmuebles.get(i).get_id()));
                docxml.endTag(null, getString(R.string.etiqueta_elemento1));
                docxml.startTag(null, getString(R.string.etiqueta_elemento2));
                docxml.text(inmuebles.get(i).getLocalidad());
                docxml.endTag(null, getString(R.string.etiqueta_elemento2));
                docxml.startTag(null, getString(R.string.etiqueta_elemento3));
                docxml.text(inmuebles.get(i).getDireccion());
                docxml.endTag(null, getString(R.string.etiqueta_elemento3));
                docxml.startTag(null, getString(R.string.etiqueta_elemento4));
                docxml.text(inmuebles.get(i).getTipo());
                docxml.endTag(null, getString(R.string.etiqueta_elemento4));
                docxml.startTag(null, getString(R.string.etiqueta_elemento5));
                docxml.text(String.valueOf(inmuebles.get(i).getPrecio()));
                docxml.endTag(null, getString(R.string.etiqueta_elemento5));
                docxml.endTag(null, getString(R.string.etiqueta_objeto));
            }
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        }catch (IOException e){

        }
    }

    private void leerXML (){
        //Lee desde el archivo .xml guardado en la memoria externa privada
        //Guarda en el ArrayList los Inmuebles almacenados en el archivo
        Inmueble i = new Inmueble();
        String etiqueta;
        XmlPullParser lectorxml = Xml.newPullParser();
        try {
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),getString(R.string.nombre_xml))),getString(R.string.codificacion));
            int evento = lectorxml.getEventType();

            while (evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo(getString(R.string.etiqueta_objeto))==0){
                        i = new Inmueble();
                    }
                    if(etiqueta.compareTo(getString(R.string.etiqueta_elemento1))==0){
                        i.set_id(Integer.parseInt(lectorxml.nextText()));

                    }
                    if(etiqueta.compareTo(getString(R.string.etiqueta_elemento2))==0){
                        i.setLocalidad(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo(getString(R.string.etiqueta_elemento3))==0){
                        i.setDireccion(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo(getString(R.string.etiqueta_elemento4))==0){
                        i.setTipo(lectorxml.nextText());

                    }
                    if(etiqueta.compareTo(getString(R.string.etiqueta_elemento5))==0){
                        i.setPrecio(Double.parseDouble(lectorxml.nextText()));

                    }
                }else if(evento == XmlPullParser.END_TAG){
                    etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo(getString(R.string.etiqueta_objeto))==0){
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
}
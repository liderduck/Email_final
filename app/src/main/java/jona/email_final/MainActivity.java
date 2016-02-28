package jona.email_final;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    HashSet<String>dicc = new HashSet<String>();
    HashMap<String,Integer> dicError = new HashMap<>();
    private EditText t3;
    private int nColor,nLetra;
    private int ultLetra=Typeface.BOLD;
    private int ultColor=Color.BLACK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t3=(EditText)findViewById(R.id.eT3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  crearDic();
        try {
            cargarDic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_comprobar) {
            comprobar();
            return true;
        }else if (id == R.id.action_corregir){
            corregir();
            return true;
        }else if (id==R.id.action_color){
            verColorear().show();
            return true;
        }else if(id==R.id.action_estilo){
            verEstilo().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void comprobar(){
        String[] palabra;
        int inicio,fin;
        int correct=0;

        String texto= t3.getText().toString();
        palabra=texto.split(" ");

        for(int i=0;i<palabra.length;i++) {

            if (!dicc.contains(palabra[i])) {
                inicio=correct;
                nLetra=Typeface.BOLD;
                nColor=Color.RED;
                marcar(palabra[i],nLetra,nColor,inicio);
                dicError.put(palabra[i], inicio);

            }
            correct=palabra[i].length()+1+correct;
        }
    }

    public void marcar(String palabra,Integer nLetra,Integer nColor,Integer inicio){

        if(nLetra==null){
            nLetra=ultLetra;
        }else{
            ultLetra=nLetra;
        }
        if(nColor==null){
            nColor=ultColor;
        }else{
            ultColor=nColor;
        }

        Editable stringconestilo = Editable.Factory.getInstance().newEditable(palabra);
        stringconestilo.setSpan(new StyleSpan(nLetra),0,palabra.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringconestilo.setSpan(new ForegroundColorSpan(nColor), 0, palabra.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t3.setText(t3.getText().replace(inicio, inicio + palabra.length(), stringconestilo));

    }

    public AlertDialog verEstilo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Elige el color");
        final CharSequence[] opciones ={"BOLD","ITALIC","NORMAL"};
        builder.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), opciones[which], Toast.LENGTH_SHORT).show();
                Log.i("dialogopulsado", "opcion" + opciones[which]);
                estilo(opciones[which]);
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public void estilo(CharSequence opcion){
        if (opcion=="BOLD"){
            nLetra=Typeface.BOLD;
        }else if(opcion=="ITALIC"){
            nLetra=Typeface.ITALIC;
        }else if (opcion=="NORMAL"){
            nLetra=Typeface.NORMAL;
        }

        Iterator miIt2 = dicError.keySet().iterator();
        while (miIt2.hasNext()){
            String j=(String) miIt2.next();
            Integer k = (Integer) dicError.get(j);
            marcar(j,nLetra,null,k);
        }
    }

    public AlertDialog verColorear(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Elige el color");
        final CharSequence[] opciones ={"ROJO","AZUL","VERDE"};
        builder.setSingleChoiceItems(opciones, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), opciones[which], Toast.LENGTH_SHORT).show();
                Log.i("dialogopulsado", "opcion" + opciones[which]);
                colorear(opciones[which]);
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public void colorear(CharSequence opcion){
            if (opcion=="ROJO"){
                nColor=Color.RED;
            }else if(opcion=="AZUL"){
                nColor=Color.BLUE;
            }else if (opcion=="VERDE"){
                nColor=Color.GREEN;
            }
        Iterator miIt2 = dicError.keySet().iterator();
        while (miIt2.hasNext()){
            String j=(String) miIt2.next();
            Integer k = (Integer) dicError.get(j);
            marcar(j,null,nColor,k);
        }
    }

    public void corregir(){
        Iterator miIt = dicError.keySet().iterator();
        while (miIt.hasNext()){
            String j=(String) miIt.next();
            Integer k = (Integer) dicError.get(j);
            disInfo(j,k).show();
        }

    }

    public AlertDialog disInfo(final String j,final Integer k){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setMessage(j)
                    .setTitle("incluir al diccionario?")
                    .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String c = String.valueOf(j);
                            dicc.add(c);
                            dicError.remove(c);
                            grabarDic();
                            nLetra=Typeface.NORMAL;
                            nColor=Color.BLACK;
                            marcar(c,nLetra,nColor,k);

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        return builder.create();
    }

    public void cargarDic() throws IOException {

        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("diccionario.txt")));

            String texto ;
            while ( (texto = fin.readLine())!=null){
                dicc.add(texto);
            }
            for (String s:dicc) {
                Log.i("mola",s);
            }
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }

    public void grabarDic(){
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("diccionario.txt", Context.MODE_PRIVATE));
            for (String s:dicc) {
                fout.write(s);
                fout.write("\n");
            }
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }
}

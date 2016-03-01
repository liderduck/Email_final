package jona.email_final;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;


public class Main2Activity extends AppCompatActivity {
    ListView lalista;
    ArrayAdapter<String> adaptador;
    ArrayList<String> palabras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lalista = (ListView) findViewById(R.id.listView);
         adaptador = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView,parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
            Intent intent = getIntent();
            final HashSet<String> dicc = (HashSet<String>) intent.getSerializableExtra("hash");

        rellenarlista(dicc);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
            verBorrar(position,dicc).show();
            }
        });
    }
    public AlertDialog verBorrar(final Integer posicion,final HashSet<String> dicc){
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

        builder.setMessage("hola")
                .setTitle("Borrar del diccionario")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String palabraAct=palabras.get(posicion);
                        Log.i("esta",palabraAct);
                        dicc.remove(palabraAct);
                        Intent data = new Intent();
                        data.putExtra("tabla", dicc);
                        setResult(RESULT_OK, data);
                        adaptador.clear();
                        rellenarlista(dicc);
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
        public void rellenarlista(HashSet<String> dicc){
            palabras=new ArrayList<>();
            for(String s:dicc){
                adaptador.add(s);
                palabras.add(s);
            }
            lalista.setAdapter(adaptador);
        }

}

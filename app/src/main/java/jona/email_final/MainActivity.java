package jona.email_final;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    HashSet<String>dicc = new HashSet<String>();
    private EditText t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t3=(EditText)findViewById(R.id.eT3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                fin= inicio+palabra[i].length();
                Editable act =marcar(palabra[i]);
                Log.i("num", String.valueOf(inicio));
                Log.i("num", String.valueOf(fin));
                t3.setText(t3.getText().replace(inicio, fin, act));

            }
            correct=palabra[i].length()+1+correct;
        }
    }

    public Editable marcar(String palabra){

        Editable stringconestilo = Editable.Factory.getInstance().newEditable(palabra);
        stringconestilo.setSpan(new StyleSpan(Typeface.BOLD),0,palabra.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringconestilo.setSpan(new ForegroundColorSpan(Color.RED), 0, palabra.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringconestilo;
    }

    public void cargarDic() throws IOException {

        Scanner s = new Scanner(getResources().openRawResource(R.raw.dic));
        String linea;
        try{
            while (s.hasNext()){
                linea = s.nextLine();
                dicc.add(linea);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}

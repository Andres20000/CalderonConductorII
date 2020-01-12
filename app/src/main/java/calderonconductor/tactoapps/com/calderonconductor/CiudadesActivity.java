package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

public class CiudadesActivity extends Activity {

    String idServicio;
    String ciudad;
    Modelo modelo  = Modelo.getInstance();

    // List view
    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;


    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ciudades);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        ciudad = bundle.getString("ciudad");


        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        //llamamos el metodo que carga el txt
    modelo.cargarPueblos(getApplicationContext());


        // Adding items to listview
        // en modelo.pueblos esta el array list donde se guardo la informacion del txt
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, modelo.pueblos);
        lv.setAdapter(adapter);


        //search
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                CiudadesActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        //selecionar ciudad
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String text = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                ciudad = text;
                Intent i  = new Intent(getApplicationContext(), NuevoDestino.class);
                i.putExtra("id", idServicio);
                i.putExtra("ciudad",text);
                startActivity(i);
                finish();


            }
        });


    }


    public void atras(View v) {
        Intent i = new Intent(getApplicationContext(), NuevoDestino.class);
        i.putExtra("id", "" + idServicio);
        i.putExtra("ciudad", "" + ciudad);
        startActivity(i);
        finish();
    }



    //bhtn_atras_hadware
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar","cerrar");
        }
        return false;
    }*/
}

package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;


public class RegistroConductor extends Activity {

    EditText txt_propietario,txt_empresa, txt_nit_cc, txt_direccion, txt_telefono;
    EditText txt_nom_empresa;
    CheckedTextView check_propietario, check_empresa;
    LinearLayout layout_nom_empresa;
    Modelo modelo = Modelo.getInstance();
    String tipo = "";
    LinearLayout layout_nom_apelido;
    EditText txt_nombre_propietario,txt_apellido_propietario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_conductor);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        txt_propietario = (EditText)findViewById(R.id.txt_propietario);
        txt_empresa = (EditText)findViewById(R.id.txt_empresa);
        txt_nit_cc = (EditText)findViewById(R.id.txt_nit_cc);
        txt_direccion = (EditText)findViewById(R.id.txt_direccion);
        txt_telefono = (EditText)findViewById(R.id.txt_telefono);
        check_propietario = (CheckedTextView)findViewById(R.id.check_propietario);
        check_empresa = (CheckedTextView)findViewById(R.id.check_empresa);
        layout_nom_empresa = (LinearLayout)findViewById(R.id.layout_nom_empresa);
        txt_nom_empresa = (EditText)findViewById(R.id.txt_nom_empresa);
        layout_nom_apelido = (LinearLayout)findViewById(R.id.layout_nom_apelido);
        txt_nombre_propietario = (EditText)findViewById(R.id.txt_nombre_propietario);
        txt_apellido_propietario = (EditText)findViewById(R.id.txt_apellido_propietario);


        //metodo que me valida si la lista tiene informacion y mostrarlos en los objetos

        loadDatos();

        //check
        check_propietario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_propietario.isChecked()==false){
                    check_propietario.setChecked(true);
                    check_empresa.setChecked(false);
                    layout_nom_empresa.setVisibility(View.GONE);
                    layout_nom_apelido.setVisibility(View.VISIBLE);
                    tipo = "Propietario";
                    modelo.propietarioVehiculo.setNombre("");
                }
            }
        });


        check_empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_empresa.isChecked()==false){
                    check_empresa.setChecked(true);
                    check_propietario.setChecked(false);
                    layout_nom_empresa.setVisibility(View.VISIBLE);
                    layout_nom_apelido.setVisibility(View.GONE);
                    tipo = "Empresa";
                    modelo.propietarioVehiculo.setNombrePropietario("");
                    modelo.propietarioVehiculo.setApelidoPropietario("");

                }

            }
        });



    }

    private void loadDatos() {

        if(!modelo.propietarioVehiculo.getNombre().equals("")){
            check_empresa.setChecked(true);
            check_propietario.setChecked(false);
            layout_nom_empresa.setVisibility(View.VISIBLE);
            layout_nom_apelido.setVisibility(View.GONE);
            tipo = "Empresa";
            modelo.propietarioVehiculo.setNombrePropietario("");
            modelo.propietarioVehiculo.setApelidoPropietario("");
        }
        else{
            check_propietario.setChecked(true);
            check_empresa.setChecked(false);
            layout_nom_empresa.setVisibility(View.GONE);
            layout_nom_apelido.setVisibility(View.VISIBLE);
            tipo = "Propietario";
            modelo.propietarioVehiculo.setNombre("");
        }
        txt_nom_empresa.setText(modelo.propietarioVehiculo.getNombre());
        txt_nombre_propietario.setText(modelo.propietarioVehiculo.getNombrePropietario());
        txt_apellido_propietario.setText(modelo.propietarioVehiculo.getApelidoPropietario());
        txt_nit_cc.setText(modelo.propietarioVehiculo.getNit_cc());
        txt_direccion.setText(modelo.propietarioVehiculo.getDireccion());
        txt_telefono.setText(modelo.propietarioVehiculo.getTelefono());



    }


    public void atras(View v){
        atras2();
    }

    public void atras2(){
        limpiarDatos1();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }

    private void limpiarDatos1() {

        modelo.propietarioVehiculo.setNombre("");
        modelo.propietarioVehiculo.setNombrePropietario("");
        modelo.propietarioVehiculo.setApelidoPropietario("");
        modelo.propietarioVehiculo.setDireccion("");
        modelo.propietarioVehiculo.setNit_cc("");
        modelo.propietarioVehiculo.setTelefono("");
        modelo.propietarioVehiculo.setTipo("");


    }


    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            atras2();
        }
        return true;
    }


    public void continuar(View v){

        if(tipo.equals("")){
            Toast.makeText(getApplicationContext(),"Todos los campos son obligatorios",Toast.LENGTH_SHORT).show();
        }
       else if(layout_nom_empresa.getVisibility() == View.VISIBLE &&  txt_nom_empresa.getText().toString().length() < 3){
            txt_nom_empresa.setError("Información muy corta");
        }

        else if(layout_nom_apelido.getVisibility() == View.VISIBLE &&  txt_nombre_propietario.getText().toString().length() < 3){
            txt_nom_empresa.setError("Información muy corta");
        }

        else if(layout_nom_apelido.getVisibility() == View.VISIBLE &&  txt_apellido_propietario.getText().toString().length() < 3){
            txt_nom_empresa.setError("Información muy corta");
        }

        else if(txt_nit_cc.getText().toString().length() < 3){
            txt_nit_cc.setError("Información muy corta");
            return;
        }

        else if(txt_direccion.getText().toString().length() < 3){
            txt_direccion.setError("Información muy corta");
            return;
        }

        else if(txt_telefono.getText().toString().length() < 10){
            txt_telefono.setError("Información muy corta");
            return;
        }
        else{

           if(layout_nom_empresa.getVisibility() == View.VISIBLE){
               modelo.propietarioVehiculo.setNombre(txt_nom_empresa.getText().toString());
           }
           if(layout_nom_apelido.getVisibility() == View.VISIBLE){
               modelo.propietarioVehiculo.setNombrePropietario(txt_nombre_propietario.getText().toString());
               modelo.propietarioVehiculo.setApelidoPropietario(txt_apellido_propietario.getText().toString());
           }

           modelo.propietarioVehiculo.setDireccion(txt_direccion.getText().toString());
           modelo.propietarioVehiculo.setNit_cc(txt_nit_cc.getText().toString());
           modelo.propietarioVehiculo.setTelefono(txt_telefono.getText().toString());
           modelo.propietarioVehiculo.setTipo(tipo);

            Intent i  = new Intent(getApplicationContext(), RegistroConductor2.class);
            startActivity(i);
            finish();
        }

    }
}

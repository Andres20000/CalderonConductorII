package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

public class EditarPerfil extends Activity {

    private static final String TAG ="EmailPassword";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Modelo modelo = Modelo.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    EditText nombre;
    EditText apellido;
    EditText celular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_editar_perfil);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        nombre = (EditText)findViewById(R.id.nombre);
        apellido = (EditText)findViewById(R.id.apellido);
        celular = (EditText)findViewById(R.id.celular);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent i = new Intent(EditarPerfil.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    finish();
                    Toast.makeText(getApplication(), "...." + user + "no logueado", Toast.LENGTH_SHORT).show();

                }

            }
        };

        nombre.setText(""+ modelo.conductor.getNombre());
        apellido.setText(""+ modelo.conductor.getApellido());
        celular.setText(""+ modelo.conductor.getCelular());

    }


    public void atras(View v){
        /*Intent i = new Intent(EditarPerfil.this, Pgina_Principa.class);
        i.putExtra("vistaPosicion","uno");
        startActivity(i);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        finish();*/
        onBackPressed();
    }





    //actualizar datos perfil de
    public void actualizarDatos(View v){
        if(nombre.getText().toString().equals("") || apellido.getText().toString().equals("") || celular.getText().toString().equals("")){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Campos Vacios");
            dialogo1.setMessage("Todos los camopos son obligatorios");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }else{
            crearActualizarConductor();
        }

    }

    //actualizar datos conductor
    public void crearActualizarConductor(){
        DatabaseReference ref;//
        DatabaseReference ref2;
        DatabaseReference ref3;

        if(modelo.tipoConductor.equals("conductor")){

            ref = database.getReference("empresa/conductores/" + modelo.uid+"/nombre/");//ruta path
            ref.setValue(nombre.getText().toString());

            ref2 = database.getReference("empresa/conductores/" + modelo.uid+"/apellido/");//ruta path
            ref2.setValue(apellido.getText().toString());

            ref3 = database.getReference("empresa/conductores/" + modelo.uid+"/celular/");//ruta path
            ref3.setValue(celular.getText().toString());

             modelo.conductor.setNombre(nombre.getText().toString());
             modelo.conductor.setApellido(apellido.getText().toString());
             modelo.conductor.setCelular(celular.getText().toString());

        }else{

            ref = database.getReference("empresa/conductoresTerceros/" + modelo.uid+"/nombre/");//ruta path
            ref.setValue(apellido.getText().toString());

            ref2 = database.getReference("empresa/conductoresTerceros/" + modelo.uid+"/apellido/");//ruta path
            ref2.setValue(nombre.getText().toString());

            ref3 = database.getReference("empresa/conductoresTerceros/" + modelo.uid+"/celular/");//ruta path
            ref3.setValue(celular.getText().toString());

            modelo.conductor.setNombre(nombre.getText().toString());
            modelo.conductor.setApellido(apellido.getText().toString());
            modelo.conductor.setCelular(celular.getText().toString());
        }


        Toast.makeText(getApplicationContext(), "Datos actualizados con exito.", Toast.LENGTH_LONG).show();
    }


    //bhtn_atras_hadware
   /* public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar","cerrar");
        }
        return false;
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

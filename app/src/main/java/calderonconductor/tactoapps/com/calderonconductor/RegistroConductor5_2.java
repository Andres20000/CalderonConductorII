package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductorDocumentosTerceros;

public class RegistroConductor5_2 extends Activity implements ComandoConductorDocumentosTerceros.OnComandoConductorDocumentosTercerosChangeListener{

    int uno =0;
    int dos =0;
    int tres =0;
    int cuatro =0;
    int cinco =0;

    boolean delanteraizq  = false;
    boolean delanterader  = false;
    boolean traseraizq = false;
    boolean traserader  = false;
    boolean repuesto  = false;


    Button check_delantera_izq,check_delantera_izq_x;
    Button check_delantera_der,check_delantera_der_x;
    Button check_trasera_izq,check_trasera_izq_x;
    Button check_trasera_der,check_trasera_der_x;
    Button check_repuesto,check_repuesto_x;

    TextView terminos_condiciones;


    Modelo modelo = Modelo.getInstance();
    ComandoConductorDocumentosTerceros comandoConductorDocumentosTerceros;
    final Context context = this;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user = mAuth.getCurrentUser();
    private static final String TAG ="Android";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_conductor5_2);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        check_delantera_izq = (Button) findViewById(R.id.check_delantera_izq);
        check_delantera_izq_x = (Button) findViewById(R.id.check_delantera_izq_x);
        check_delantera_der = (Button) findViewById(R.id.check_delantera_der);
        check_delantera_der_x = (Button) findViewById(R.id.check_delantera_der_x);
        check_trasera_izq = (Button) findViewById(R.id.check_trasera_izq);
        check_trasera_izq_x = (Button) findViewById(R.id.check_trasera_izq_x);
        check_trasera_der = (Button) findViewById(R.id.check_trasera_der);
        check_trasera_der_x = (Button) findViewById(R.id.check_trasera_der_x);
        check_repuesto = (Button) findViewById(R.id.check_repuesto);
        check_repuesto_x = (Button) findViewById(R.id.check_repuesto_x);
        terminos_condiciones = (TextView) findViewById(R.id.terminos_condiciones);


        terminos_condiciones.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        terminos_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.calderonapp.com/terminos";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        progressDialog = new ProgressDialog(this);
        comandoConductorDocumentosTerceros = new ComandoConductorDocumentosTerceros(this);
    }

    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("atras","atras");
            atras2();
        }
        return false;
    }

    public void atras2(){
        Intent i = new Intent(getApplicationContext(), RegistroConductor5.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }


    public void delanteraizq(View view) {
        uno =1;
        switch (view.getId()) {
            case R.id.check_delantera_izq:
                check_delantera_izq.setBackgroundResource(R.drawable.radio_chulo);
                check_delantera_izq_x.setBackgroundResource(R.drawable.radio);
                delanteraizq  = true;
                break;

            case R.id.check_delantera_izq_x:
                check_delantera_izq_x.setBackgroundResource(R.drawable.radio_x);
                check_delantera_izq.setBackgroundResource(R.drawable.radio);
                delanteraizq  = false;
                break;
        }
    }

    public void delanterader(View view) {
        dos =2;
        switch (view.getId()) {
            case R.id.check_delantera_der:
                check_delantera_der.setBackgroundResource(R.drawable.radio_chulo);
                check_delantera_der_x.setBackgroundResource(R.drawable.radio);
                delanterader  = true;
                break;

            case R.id.check_delantera_der_x:
                check_delantera_der_x.setBackgroundResource(R.drawable.radio_x);
                check_delantera_der.setBackgroundResource(R.drawable.radio);
                delanterader  = false;
                break;
        }
    }

    public void traseraizq(View view) {
        tres =3;
        switch (view.getId()) {
            case R.id.check_trasera_izq:
                check_trasera_izq.setBackgroundResource(R.drawable.radio_chulo);
                check_trasera_izq_x.setBackgroundResource(R.drawable.radio);
                traseraizq  = true;
                break;

            case R.id.check_trasera_izq_x:
                check_trasera_izq_x.setBackgroundResource(R.drawable.radio_x);
                check_trasera_izq.setBackgroundResource(R.drawable.radio);
                traseraizq  = false;
                break;
        }
    }

    public void traserader(View view) {
        cuatro =4;
        switch (view.getId()) {
            case R.id.check_trasera_der:
                check_trasera_der.setBackgroundResource(R.drawable.radio_chulo);
                check_trasera_der_x.setBackgroundResource(R.drawable.radio);
                traserader  = true;
                break;

            case R.id.check_trasera_der_x:
                check_trasera_der_x.setBackgroundResource(R.drawable.radio_x);
                check_trasera_der.setBackgroundResource(R.drawable.radio);
                traserader  = false;
                break;
        }
    }

    public void repuestos(View view) {
        cinco =5;
        switch (view.getId()) {
            case R.id.check_repuesto:
                check_repuesto.setBackgroundResource(R.drawable.radio_chulo);
                check_repuesto_x.setBackgroundResource(R.drawable.radio);
                repuesto  = true;
                break;

            case R.id.check_repuesto_x:
                check_repuesto_x.setBackgroundResource(R.drawable.radio_x);
                check_repuesto.setBackgroundResource(R.drawable.radio);
                repuesto  = false;
                break;
        }
    }


    public void finalizar(View v) {

        if(uno == 0 || dos == 0 || tres == 0 || cuatro == 0 || cinco == 0){
            Toast.makeText(getApplicationContext(),"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }else{
            modelo.llantas.setDelantera_derecha(delanterader);
            modelo.llantas.setDelantera_izquierda(delanteraizq);
            modelo.llantas.setTrasera_derecha(traserader);
            modelo.llantas.setTrasera_izquierda(traseraizq);
            modelo.llantas.setRepuesto(repuesto);
            progressDialog.setMessage("Validando la información...");
            progressDialog.show();
            registro();

        }
    }

    @Override
    public void setConductorDocumentosTercerosListener() {

        logueo();
        Intent i = new Intent(getApplicationContext(), PresentacionUnicaVez.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        try {
            progressDialog.dismiss();
        } catch (Exception e){}
        finish();
    }

    @Override
    public void errorSetConductorDocumentosTerceros() {

       // Toast.makeText(getApplicationContext(),"Erorr con el servidor",Toast.LENGTH_SHORT).show();
    }


    ///registro

    public void  registro(){
        mAuth.createUserWithEmailAndPassword(modelo.conductoresTerceros.getCorreo().toString(),  modelo.conductoresTerceros.getPasswordConductorTercero().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        Log.d(TAG, "signInWithEmail:onComplete:"+task.isSuccessful());

                        FirebaseAuthException ex = (FirebaseAuthException) task.getException();
                        if (ex!=null){
                            Log.v("error re", "registro error"+ex.getLocalizedMessage());

                            //Toast.makeText(getApplicationContext(), "Ocurrio un error al registrarse, intente nuevamente.", Toast.LENGTH_LONG).show();
                            System.out.print(""+ex.getLocalizedMessage());

                            String error = ex.getErrorCode();

                            try {
                                progressDialog.dismiss();
                            } catch (Exception e){}
                            Log.v("log re","error re"+error);
                            if(error.equals("ERROR_EMAIL_ALREADY_IN_USE")){
                                Toast.makeText(getApplicationContext(), " Correo ya  existente.", Toast.LENGTH_LONG).show();
                            }//ERROR_WEAK_PASSWORD

                            if(error.equals("ERROR_WEAK_PASSWORD")){
                                Toast.makeText(getApplicationContext(), " Contraseña muy debil.", Toast.LENGTH_LONG).show();
                            }

                            return;
                        }


                        if(task.isSuccessful()){
                            //display some message here
                            //display some message here
                            //Toast.makeText(getApplicationContext(), "Registrado exitosamente", Toast.LENGTH_LONG).show();
                            createNewUser(task.getResult().getUser());


                        }else{
                            //display some message here
                            Toast.makeText(getApplicationContext(),"error de registro, intentelo de nuevo",Toast.LENGTH_LONG).show();
                        }



                    }
                });
    }


    private void createNewUser(FirebaseUser userFromRegistration) {
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        comandoConductorDocumentosTerceros.setConductorDocumentosTerceros(userId);

    }


    //logueo
    public void logueo(){

        // Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        progressDialog.setMessage("Validando la información...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(modelo.conductoresTerceros.getCorreo().toString(),  modelo.conductoresTerceros.getPasswordConductorTercero().toString()).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:"+task.isSuccessful());
                        FirebaseAuthException ex = (FirebaseAuthException) task.getException();
                        if (ex==null){
                            return;
                        }

                        String error = ex.getErrorCode();

                        if (error.equals("ERROR_INVALID_EMAIL")){
                            Log.d(TAG, "signInWithEmail:onComplete:"+"ERROR CON EL CORREO");
                            Toast.makeText(getApplication(),""+"Correo no valido", Toast.LENGTH_SHORT).show();
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e){}

                        }
                        if (error.equals("ERROR_USER_NOT_FOUND")){
                            Log.d(TAG, "signInWithEmail:onComplete:" + "USUARIO NUEVO");
                            Toast.makeText(getApplication(),""+"USUARIO NO REGISTRADO", Toast.LENGTH_SHORT).show();
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e){}

                        }
                        if (error.equals("ERROR_WRONG_PASSWORD")){
                            Log.d(TAG, "signInWithEmail:onComplete:" + "CONTRASEÑA INCORRECTA");
                            Toast.makeText(getApplication(),""+"CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e){}

                        }
                        if (!task.isSuccessful()){
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.getException());
                            Toast.makeText(getApplication(),""+"FALLO EN LA AUTENTICACION", Toast.LENGTH_SHORT).show();
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e){}
                        }



                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth==null || mAuthListener ==null){
            return;
        }else{
            mAuth.addAuthStateListener(mAuthListener);
        }


    }

    @Override
    public void onStop() {
        super.onStop();


        if(mAuth==null || mAuthListener ==null){
            return;
        }else{
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}

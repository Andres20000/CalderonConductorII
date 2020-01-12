package calderonconductor.tactoapps.com.calderonconductor;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdParams;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdParams.onGetParams;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor.OnConductorListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoValidarUsuario;
import calderonconductor.tactoapps.com.calderonconductor.registroDos.RegistroDos;

public class MainActivity extends Activity implements ComandoValidarUsuario.OnValidarUsuarioChangeListener {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email, password;
    TextView txtRegistrarme, registrarme;
    private ProgressBar progressBar;
    Button button;
    Modelo modelo = Modelo.getInstance();
    private ProgressDialog progressDialog;
    ComandoValidarUsuario comandoValidarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        if (modelo.params.diseñoRegistroTax1){
            setContentView(R.layout.activity_main_tax1);
        }else {
            setContentView(R.layout.activity_main);
        }


        if (savedInstanceState != null) {
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        boolean result = Utility.checkPermission(MainActivity.this);


        modelo.limpiarregistro();

        email = (EditText) findViewById(R.id.correo);
        password = (EditText) findViewById(R.id.contrasena);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.button);
        registrarme = findViewById(R.id.resgirtarme);
        txtRegistrarme = findViewById(R.id.textView5);

        progressDialog = new ProgressDialog(this);
        // autenticacion de loguin
        comandoValidarUsuario = new ComandoValidarUsuario(this);

        registrarme.setVisibility(View.GONE);
        txtRegistrarme.setVisibility(View.GONE);

        CmdParams.getParams(new onGetParams() {
            @Override
            public void cargoParams() {
            }

            @Override
            public void cargoOpcionesGenerales() {
                if (!modelo.params.ocultarBotonRegistroEnConductor){
                    registrarme.setVisibility(View.VISIBLE);
                    txtRegistrarme.setVisibility(View.VISIBLE);
                }
            }
        });




        if (estaConectado()) {

            //Toast.makeText(getApplication(),"Conectado a internet", Toast.LENGTH_SHORT).show();


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in

                        modelo.uid = user.getUid();
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        progressDialog.setMessage("Validando la información...");
                        progressDialog.setCancelable(false);
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e){}
                        Crashlytics.setUserIdentifier(modelo.uid);
                        validarUsuario();

                    } else {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e){}
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out" + user + "no logueado");
                        //Toast.makeText(getApplication(),"...."+user+"no logueado", Toast.LENGTH_SHORT).show();

                    }

                }
            };

        }
    }


    @Override
    public void onStart() {
        super.onStart();

        if (mAuth == null || mAuthListener == null) {
            return;
        } else {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void login(View v) {
        if (email.getText().toString().equals("") || password.getText().equals("")) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Campos Vacios");
            dialogo1.setMessage("Todos los camopos son obligatorios");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        } else {
            logueo();
        }
    }


    public void logueo() {
        progressDialog.setMessage("Validando la información...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (task.getException() instanceof FirebaseAuthException) {

                            FirebaseAuthException ex = (FirebaseAuthException) task.getException();

                            if (ex == null) {
                                return;
                            }

                            String error = ex.getErrorCode();

                            if (error.equals("ERROR_INVALID_EMAIL")) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + "correo nada que ver");
                                // Toast.makeText(getApplication(), "...." + "correo nada que ver", Toast.LENGTH_SHORT).show();
                                showAlertDialogLogin();
                                progressBar.setVisibility(View.GONE);
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e){}
                                button.setEnabled(true);
                            }
                            if (error.equals("ERROR_USER_NOT_FOUND")) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + "USUARIO NUEVO");
                                // Toast.makeText(getApplication(), "...." + "USUARIO NUEVO", Toast.LENGTH_SHORT).show();
                                showAlertDialogLogin();
                                progressBar.setVisibility(View.GONE);
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e){}
                                button.setEnabled(true);
                            }
                            if (error.equals("ERROR_WRONG_PASSWORD")) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + "CONTRASEÑA INCORRECTA");
                                //Toast.makeText(getApplication(), "...." + "CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                                showAlertDialogLogin();
                                progressBar.setVisibility(View.GONE);
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e){}
                                button.setEnabled(true);
                            }
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.getException());
                                //Toast.makeText(getApplication(), "...." + "FALLO EN LA AUTENTICACION", Toast.LENGTH_SHORT).show();
                                showAlertDialogLogin();
                                progressBar.setVisibility(View.GONE);
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e){}
                                button.setEnabled(true);
                            } else {
                                try {
                                    progressDialog.dismiss();
                                } catch (Exception e){}
                                Intent i = new Intent(MainActivity.this, Pgina_Principa.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

                            }
                        }

                        if (task.getException() instanceof FirebaseNetworkException) {
                            FirebaseNetworkException ex = (FirebaseNetworkException) task.getException();
                            showAlertDialogRed(MainActivity.this, "" + ex.getLocalizedMessage());

                            progressBar.setVisibility(View.GONE);
                            try {
                                progressDialog.dismiss();
                            } catch (Exception e){}
                            button.setEnabled(true);
                        }

                    }
                });
    }


    public void showAlertDialogRed(Context context, String texto) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Error de red");
        alertDialog.setMessage("No se pudo loguear. Revise conexión a internet y/o que tenga Google play service activo. " + texto);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }


    protected Boolean estaConectado() {
        if (conectadoWifi()) {
            Log.v("wifi", "Tu Dispositivo tiene Conexion a Wifi.");
            return true;
        } else {
            if (conectadoRedMovil()) {

                Log.v("Datos", "Tu Dispositivo tiene Conexion Movil.");
                return true;
            } else {
                showAlertDialog(MainActivity.this, "Conexion a Internet",
                        "Tu Dispositivo no tiene Conexion a Internet.", false);
                return false;
            }
        }
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) { //&& info.getReason() != null
                    return true;
                }
            }
        }
        return false;
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(gpsOptionsIntent, 0);
            }
        });

        alertDialog.show();
    }

    public void olvidopassword(View v) {
        Intent i = new Intent(MainActivity.this, OlvidoSuPassword.class);
        startActivity(i);

    }




    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar", "cerrar");
        }
        return false;
    }


    public void click_resgirtarme(View v) {

        if (estaConectado()) {

            comandoValidarUsuario.getVehiculos();
        } else {
            showAlertDialog(MainActivity.this, "Conexion a Internet",
                    "Tu Dispositivo no tiene Conexion a Internet.", false);
        }

    }


    private void validarUsuario() {

        CmdParams.getSytem(this, new CmdParams.OnSystemListener() {
            @Override
            public void estaActualizado() {
                comandoValidarUsuario.validandoUsuarioTercero();
            }

            @Override
            public void puedeActualizar() {
                String ms = "Disfruta de  nuestra última versión , por favor descárgala.";
                hacerDialogo(ms, true);

            }

            @Override
            public void debeActualizar() {
                String ms = "Disfruta de  nuestra última versión , por favor descárgala.";
                hacerDialogo(ms, false);
            }

        });


    }


    // alertas de actualizacion
    private void hacerDialogo(String msj, final boolean cancel) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // asignar el titulo
        alertDialogBuilder.setTitle("Nueva Actualización");

        // asignar el mensaje
        alertDialogBuilder
                .setMessage(msj)
                .setCancelable(cancel)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        irAAppStore();

                    }
                });


        if (cancel) {

            alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    comandoValidarUsuario.validandoUsuarioTercero();    // Aca continua la accion si no quiere actualizar

                }
            });
        }

        // se crea el dialogo
        AlertDialog alertDialog = alertDialogBuilder.create();

        // sse muestra
        try {
            alertDialog.show();
        } catch (Exception e){}

    }

    private void irAAppStore() {

        String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            finish();
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            finish();
        }
    }

    public void logueado() {
        try {
            progressDialog.dismiss();
        } catch (Exception e){}

        Intent i = new Intent(MainActivity.this, Pgina_Principa.class);
        i.putExtra("vistaPosicion", "dos");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        finish();
    }

    @Override
    public void validandoConductorOK() {
        modelo.tipoConductor = "conductor";

        ComandoConductor.getConductor(new OnConductorListener() {
            @Override
            public void cargoConductor() {
                logueado();
            }
        });



    }

    @Override
    public void validandoConductorTerceroOK() {
        modelo.tipoConductor = "conductorTercero";
        ComandoConductor.getConductor(new OnConductorListener() {
            @Override
            public void cargoConductor() {
                logueado();
            }
        });

    }

    @Override
    public void validandoConductorError() {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "Datos Erróneos", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        i.putExtra("vistaPosicion", "dos");
        startActivity(i);
    }

    @Override
    public void getListadoVehiculos() {

        Intent i;
        if (modelo.params.diseñoRegistroTax1){
            i = new Intent(getApplicationContext(), RegistroDos.class);

        }else {
            i = new Intent(getApplicationContext(), RegistroConductor.class);

        }

        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);


    }


    public void showAlertDialogLogin() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage("" + getString(R.string.alert_login));
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }
}

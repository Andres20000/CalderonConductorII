package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdParams;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdParams.OnSystemListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdParams.onGetParams;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor.OnConductorListener;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoValidarUsuario;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoValidarUsuario.OnValidarUsuarioChangeListener;

public class Splash extends Activity implements OnValidarUsuarioChangeListener {

    PackageInfo pInfo;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    Modelo modelo = Modelo.getInstance();
    ComandoValidarUsuario comandoValidarUsuario;


    TextView textView20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        textView20 = (TextView)findViewById(R.id.textView20);

        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CmdParams.getParams(new onGetParams() {
                                @Override
                                public void cargoParams() {

                                }

                                @Override
                                public void cargoOpcionesGenerales() {

                                }
                            });


        textView20.setText("" + "Versión " + pInfo.versionName + " (" + pInfo.versionCode + ")");

        comandoValidarUsuario = new ComandoValidarUsuario(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                   //s mAuth.signOut();
                    modelo.uid = user.getUid();
                    Crashlytics.setUserIdentifier(modelo.uid);

                    validarUsuario();

                } else {

                    CmdParams.getParams(new onGetParams() {
                        @Override
                        public void cargoParams() {

                        }

                        @Override
                        public void cargoOpcionesGenerales() {

                            Intent i = new Intent(Splash.this, MainActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                            finish();
                        }
                    });




                }

            }
        };


    }

    private void validarUsuario() {

        CmdParams.getSytem(this, new OnSystemListener() {
            @Override
            public void estaActualizado() {
                comandoValidarUsuario.validandoUsuarioTercero();
            }

            @Override
            public void puedeActualizar() {
                String ms = "Disfruta de  nuestra última versión, por favor descárgala.";
                hacerDialogo(ms,true);

            }

            @Override
            public void debeActualizar() {
                String ms = "Disfruta de  nuestra última versión , por favor descárgala.";
                hacerDialogo(ms,false);
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
        Intent i = new Intent(Splash.this, MainActivity.class);
        i.putExtra("vistaPosicion", "dos");
        startActivity(i);
    }

    @Override
    public void getListadoVehiculos() {
        Intent i = new Intent(getApplicationContext(), RegistroConductor.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();

    }

    public void logueado() {
        try {

        } catch (Exception e){}

        Intent i = new Intent(Splash.this, Pgina_Principa.class);
        i.putExtra("vistaPosicion", "dos");
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        finish();
    }
}

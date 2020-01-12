package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoConductor.OnConductorListener;

public class Tap_1 extends Activity {
    private static final String TAG ="EmailPassword";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Modelo modelo = Modelo.getInstance();

    TextView textView7, version;
    EditText editText, editText2;
    Button button2;
    ImageView imageView3;
    ComandoConductor comandoConductor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tap_1);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        imageView3 = (ImageView)findViewById(R.id.imageView3);
        textView7 = (TextView)findViewById(R.id.textView7);
        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
        button2 = (Button)findViewById(R.id.button2);

        version = findViewById(R.id.version);
        version.setText(Utility.getVersionParaUsuario(this));


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent i = new Intent(Tap_1.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    finish();
                    Toast.makeText(getApplication(), "...." + user + "no logueado", Toast.LENGTH_SHORT).show();

                }

            }
        };

        comandoConductor = new ComandoConductor();
        comandoConductor.getConductor(new OnConductorListener() {
            @Override
            public void cargoConductor() {
                editText.setText(""+modelo.conductor.getNombre()+" "+modelo.conductor.getApellido());
                editText2.setText(""+modelo.conductor.getCelular());
            }
        });



    }



    public void cerrar(View v){
        Toast.makeText(getApplication(),  "Cerrarndo sessión", Toast.LENGTH_SHORT).show();
        mAuth.signOut();
        modelo.uid = "";
        modelo.getOrdenes().clear();
        modelo.getHistorial().clear();
        Intent i = new Intent(Tap_1.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
        finish();

    }


    public void editarperfil(View v){
        Intent i = new Intent(Tap_1.this, EditarPerfil.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
    }

}

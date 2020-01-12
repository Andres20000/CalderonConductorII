package calderonconductor.tactoapps.com.calderonconductor;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidoSuPassword extends Activity {

    EditText corrreo;
    ProgressBar progressBar4;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_olvido_su_password);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        corrreo =(EditText)findViewById(R.id.corrreo);
        progressBar4 =(ProgressBar)findViewById(R.id.progressBar4);

    }

    public void atras(View v){
        atras2();
    }

    public void atras2(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }


    //bhtn_atras_hadware
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            atras2();
        }
        return true;
    }


    public void resetPassword(View v){
        if(corrreo.getText().toString().equals("")){
            Toast.makeText(OlvidoSuPassword.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
        }else {
            progressBar4.setVisibility(View.VISIBLE);
            String emailId = corrreo.getText().toString();
            mAuth.sendPasswordResetEmail(emailId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(OlvidoSuPassword.this, "Las instrucciones han sido enviados a su correo electrónico", Toast.LENGTH_SHORT).show();
                                progressBar4.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(OlvidoSuPassword.this, "Datos incorrectos.", Toast.LENGTH_SHORT).show();
                                progressBar4.setVisibility(View.GONE);
                            }

                        }
                    });
        }
    }



    //bhtn_atras_hadware

}

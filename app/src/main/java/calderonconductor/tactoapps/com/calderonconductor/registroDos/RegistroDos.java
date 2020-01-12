package calderonconductor.tactoapps.com.calderonconductor.registroDos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Conductor;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Vehiculo;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdConductor;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdConductor.OnCrearConductor;
import calderonconductor.tactoapps.com.calderonconductor.MainActivity;
import calderonconductor.tactoapps.com.calderonconductor.PresentacionUnicaVez;
import calderonconductor.tactoapps.com.calderonconductor.R;

public class RegistroDos extends Activity {


    EditText nombres, apellidos, celular, cedula, correo, password, direccion, tipoVehiculo, placa, marca, referencia, modelo;

    ImageView foto;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Uri uriSavedImage1;
    Bitmap original;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG ="REGDOS";
    String token ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_dos);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
            return;
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        nombres = findViewById(R.id.nombres);
        apellidos = findViewById(R.id.apellidos);
        celular = findViewById(R.id.celular);
        cedula = findViewById(R.id.cedula);
        correo = findViewById(R.id.correo);
        password = findViewById(R.id.password);
        direccion = findViewById(R.id.direccion);
        //tipoVehiculo = findViewById(R.id.tipoVehiculo);
        placa = findViewById(R.id.placa);
        marca = findViewById(R.id.marca);
        referencia = findViewById(R.id.referencia);
        modelo = findViewById(R.id.modelo);
        foto = findViewById(R.id.foto);

        progressDialog = new ProgressDialog(this);

        token = FirebaseInstanceId.getInstance().getToken();

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("OPT","RECARGAR");
    }

    public void didTapOKContinuar(View view) {

        if (nombres.getText().toString().length() < 4) {
            nombres.setError("Debes incluir tu nombre");
            nombres.requestFocus();
            return;
        }

        if (apellidos.getText().toString().length() < 4) {
            apellidos.setError("Debes incluir tu apellido");
            apellidos.requestFocus();
            return;
        }

        if (celular.getText().toString().length() != 10  ||  !Utility.isNumeric(celular.getText().toString())) {
            celular.setError("El celular está incorrecto");
            celular.requestFocus();
            return;
        }

        if (cedula.getText().toString().length() < 7 ) {
            cedula.setError("La cédula está incorrecta");
            cedula.requestFocus();
            return;
        }

        if (direccion.getText().toString().length() < 7 ) {
            direccion.setError("La dirección está incorrecta");
            direccion.requestFocus();
            return;
        }

        if (!Utility.isEmailValid(correo.getText().toString())) {
            correo.setError("El correo está incorrecto");
            correo.requestFocus();
            return;
        }

        if (password.getText().toString().length() < 5 ) {
            password.setError("La contraseña está muy corta");
            password.requestFocus();
            return;
        }

        if (original ==  null) {
            password.setError("Debes incluir una foto tuya.");
            return;
        }

        if (placa.getText().toString().length() != 6 ) {
            placa.setError("La placa debe tener 6 caracteres");
            placa.requestFocus();
            return;
        }

        if (marca.getText().toString().length() < 3 ) {
            marca.setError("La marca está incorrecta");
            marca.requestFocus();
            return;
        }

        if (referencia.getText().toString().length() < 2 ) {
            referencia.setError("La referencia está incorrecta");
            referencia.requestFocus();
            return;
        }



        if (modelo.getText().toString().length() != 4  ||  !Utility.isNumeric(modelo.getText().toString())) {
            modelo.setError("El modelo está incorrecto");
            modelo.requestFocus();
            return;
        }


        progressDialog.setMessage("Cargando la información Por favor, espere...");
        progressDialog.show();

        foto.setVisibility(View.GONE);


        registro();

    }


    ///registro

    public void  registro(){
        mAuth.createUserWithEmailAndPassword(correo.getText().toString(),  password.getText().toString())
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
                            //Toast.makeText(getApplicationContext(), "Registrado exitosamente", Toast.LENGTH_LONG).show();
                            uploadImage(task.getResult().getUser().getUid());
                            createNewUser(task.getResult().getUser());


                        }else{
                            //display some message here
                            Toast.makeText(getApplicationContext(),"error de registro, intentelo de nuevo",Toast.LENGTH_LONG).show();
                        }



                    }
                });
    }

    private void createNewUser(FirebaseUser userFromRegistration) {
        String userId = userFromRegistration.getUid();

        Conductor cond = new Conductor();
        cond.setNombre(nombres.getText().toString());
        cond.setApellido(apellidos.getText().toString());
        cond.setCelular(celular.getText().toString());
        cond.cedula = cedula.getText().toString();
        cond.setDireccion(direccion.getText().toString());
        cond.setCorreo(correo.getText().toString());
        cond.password = password.getText().toString();

        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setPlaca(placa.getText().toString());
        vehiculo.setMarca(marca.getText().toString());
        vehiculo.referencia = referencia.getText().toString();
        vehiculo.modelo =  modelo.getText().toString();



        CmdConductor.crearConductor(userId, cond, vehiculo, token,  new OnCrearConductor() {
            @Override
            public void creoConductor() {
               /* Intent i = new Intent(getApplicationContext(), PresentacionUnicaVez.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                try {
                    progressDialog.dismiss();
                } catch (Exception e){}
                finish();*/

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();

            }

            @Override
            public void error() {

            }
        });

    }

    public void didTapFondo(View v) {


        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch(Exception e) {

        }
    }

    public void didTapSubirFoto(View view) {

        galleryIntent();
    }

    private void uploadImage(String idConductor) {


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReference().child("conductores/propios/" + idConductor  + "/fotoperfil");


        foto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        foto.layout(0, 0, foto.getMeasuredWidth(), foto.getMeasuredHeight());

        Bitmap bitmap = original;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Error, no pudo subir la foto. Tiene acceso a Internet", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                //progressDialog.dismiss();

               /* Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();*/

            }
        });


    }


    private void galleryIntent()
    {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECT_FILE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }

        }
    }


    //galeri
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;

        File destination = null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                int nh = (int) ( bm.getHeight() * (512.0 / bm.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bm, 512, nh, true);

                foto.setImageBitmap(scaled);
                original = scaled;
                uriSavedImage1 = Uri.fromFile(destination);

            } catch (IOException e) {
                e.printStackTrace();
            }
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



}

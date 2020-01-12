package calderonconductor.tactoapps.com.calderonconductor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import calderonconductor.tactoapps.com.calderonconductor.Clases.ImageUtil;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Utility;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoValidarCorreoFirebase;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class RegistroConductor2 extends Activity implements ComandoValidarCorreoFirebase.OnValidarCorreoFirebaseChangeListener, TimePickerDialog.OnTimeSetListener, android.app.TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    EditText txt_nombres, txt_apellidos, txt_celular, txt_correo ,txt_passwor,txt_direccion, txt_cedula;
    Modelo modelo = Modelo.getInstance();
    String token = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ComandoValidarCorreoFirebase comandoValidarCorreoFirebase;
    EditText txt_Licencia_de_transito, txt_categoria_licencia;

    final Context context = this;

    //variables camara
    //foto
    String userChoosenTask="";
    boolean img_cam1 = false;
    private int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    ImageView camara1;
    TextView txt_camara1;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private static final String TAG ="AndroidBash";
    String idClientes ="";
    String foto ="";
    Date date;
    DateFormat hourFormat;

    int setHora = 0;



    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 3;
    private final int SELECT_PICTURE = 300;
    private String mPath = "";
    private RelativeLayout mRlView;

    Uri photoURI = null;
    String ruta1 = null;
    static final int REQUEST_TAKE_PHOTO = 3;
    String mCurrentPhotoPath = "";

    //fin camara
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro_conductor2);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        boolean result = Utility.checkPermission(RegistroConductor2.this);

        txt_nombres = (EditText)findViewById(R.id.txt_nombres);
        txt_apellidos = (EditText)findViewById(R.id.txt_apellidos);
        txt_celular = (EditText)findViewById(R.id.txt_celular);
        txt_correo = (EditText)findViewById(R.id.txt_correo);
        txt_passwor = (EditText)findViewById(R.id.txt_passwor);
        txt_direccion = (EditText)findViewById(R.id.txt_direccion);
        txt_cedula = (EditText)findViewById(R.id.txt_cedula);

        camara1 = (ImageView)findViewById(R.id.camara1);
        txt_camara1 = (TextView) findViewById(R.id.txt_camara1);
        mRlView = (RelativeLayout) findViewById(R.id.mRlView);

        txt_Licencia_de_transito = (EditText)findViewById(R.id.txt_Licencia_de_transito);
        txt_categoria_licencia = (EditText)findViewById(R.id.txt_categoria_licencia);

        token = FirebaseInstanceId.getInstance().getToken();
        comandoValidarCorreoFirebase = new ComandoValidarCorreoFirebase(this);

        loadDatos2();
    }

    private void loadDatos2() {


        //modelo.conductoresTerceros.setTokenDevice(token);
        //convertir string base 64 a bitmap

        if(!modelo.conductoresTerceros.getFoto().equals("")){
            Bitmap bitmap = ImageUtil.convert(modelo.conductoresTerceros.getFoto());
            camara1.setImageBitmap(getCircularBitmap(bitmap));
        }
        txt_nombres.setText(""+modelo.conductoresTerceros.getNombre());
        txt_apellidos.setText(""+modelo.conductoresTerceros.getApellido());
        txt_cedula.setText(""+ modelo.conductoresTerceros.getCedula());
        txt_celular.setText(""+  modelo.conductoresTerceros.getCelular());
        txt_direccion.setText(""+modelo.conductoresTerceros.getDireccion());
        txt_correo.setText(""+modelo.conductoresTerceros.getCorreo());
        txt_passwor.setText(""+ modelo.conductoresTerceros.getPasswordConductorTercero());
        txt_Licencia_de_transito.setText(""+modelo.conductoresTerceros.getLicencia_de_transito());
        txt_categoria_licencia.setText(""+modelo.conductoresTerceros.getCategoria_licencia());
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
        limpiarDatos2();
        Intent i = new Intent(getApplicationContext(), RegistroConductor.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
    }

    private void limpiarDatos2() {
        modelo.conductoresTerceros.setCedula("");
        modelo.conductoresTerceros.setApellido("");
        modelo.conductoresTerceros.setCelular("");
        modelo.conductoresTerceros.setDireccion("");
        modelo.conductoresTerceros.setCorreo("");
        modelo.conductoresTerceros.setNombre("");
        modelo.conductoresTerceros.setFoto("");
        modelo.conductoresTerceros.setPasswordConductorTercero("");
        modelo.conductoresTerceros.setTokenDevice("");
        modelo.conductoresTerceros.setCategoria_licencia("");
        modelo.conductoresTerceros.setLicencia_de_transito("");

    }

    //validar email
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void continuar2(View v){

        if(txt_cedula.getText().toString().length() < 3){
            txt_cedula.setError("Cédula muy corta");
        }
        else if(txt_nombres.getText().toString().length() < 3){
            txt_nombres.setError("Información muy corta");
        }
        else if(txt_apellidos.getText().toString().length() < 3){
            txt_apellidos.setError("Información muy corta");
        }

        else if(txt_celular.getText().toString().length() < 10){
            txt_celular.setError("Información muy corta");
        }

        else if(txt_correo.getText().toString().length() < 5){
            txt_correo.setError("Email muy corta");
        }

        else if (!validarEmail(txt_correo.getText().toString())) {
            txt_correo.setError("Email no válido");
        }

        else  if(txt_direccion.getText().toString().length() < 5){
            txt_direccion.setError("Dirección muy corta");
        }


        else if(txt_passwor.getText().toString().length() < 8){
            txt_passwor.setError("Contraseña muy corta");
        }
        else if(txt_categoria_licencia.getText().toString().length() < 4){
            txt_categoria_licencia.setError("Información muy corta");
        }
        else if(txt_Licencia_de_transito.getText().toString().length() < 4){
            txt_Licencia_de_transito.setError("Información muy corta");
        }else{

            comandoValidarCorreoFirebase.checkAccountEmailExistInFirebase(txt_correo.getText().toString());
        }

    }

    @Override
    public void cargoValidarCorreoFirebase() {
        modelo.conductoresTerceros.setCedula(txt_cedula.getText().toString());
        modelo.conductoresTerceros.setApellido(txt_apellidos.getText().toString());
        modelo.conductoresTerceros.setCelular(txt_celular.getText().toString());
        modelo.conductoresTerceros.setDireccion(txt_direccion.getText().toString());
        modelo.conductoresTerceros.setCorreo(txt_correo.getText().toString());
        modelo.conductoresTerceros.setNombre(txt_nombres.getText().toString());
        modelo.conductoresTerceros.setFoto(foto);
        modelo.conductoresTerceros.setPasswordConductorTercero(txt_passwor.getText().toString());
        modelo.conductoresTerceros.setTokenDevice(token);
        modelo.conductoresTerceros.setCategoria_licencia(txt_categoria_licencia.getText().toString());
        modelo.conductoresTerceros.setLicencia_de_transito(txt_Licencia_de_transito.getText().toString());


        Intent i  = new Intent(getApplicationContext(), RegistroConductor3.class);
        startActivity(i);
        finish();
    }

    @Override
    public void cargoValidarCorreoFirebaseEroor() {
        txt_correo.setError("Email ya registrado");
    }


    ///imagen
    public void camara1(View v){
        if(Utility.checkPermission(getApplicationContext())){
            camara1.setEnabled(true);
            showOptions();
        }

        else{
            camara1.setEnabled(false);

        }

    }

    //manejo camara y galeria
    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConductor2.this);
        builder.setTitle("Seleccione una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = Utility.checkPermission(getApplicationContext());
                if(option[which] == "Tomar foto"){

                    if (result) {
                        openCamera();
                    }
                }else if(option[which] == "Elegir de galería"){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
       /* File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }*/

        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ruta1 = photoFile.getAbsolutePath();
                photoURI = FileProvider.getUriForFile(this,
                        //"com.tactoapps.android.fileprovider",
                        getString(R.string.authorities),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix //
                ".jpg",         // suffix //
                storageDir      // directory //
        );

        // Save a file: path for use with ACTION_VIEW intents
        mPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    camara1.setImageBitmap(getCircularBitmap(bitmap));

                    txt_camara1.setText("Cambiar Foto");
                    foto = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);

                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();

                    try {
                        Bitmap imagen = getBitmapFromUri (path);
                        camara1.setImageBitmap(getCircularBitmap(imagen));
                        foto = encodeToBase64(imagen, Bitmap.CompressFormat.JPEG, 100);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // camara1.setImageURI(path);
                    txt_camara1.setText("Cambiar Foto");

                    break;

            }
        }
        else {
            Log.v("Code",""+requestCode);
        }
    }


    //convertir uri en bitmap
    private Bitmap getBitmapFromUri ( Uri uri ) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver (). openFileDescriptor ( uri , "r" );
        FileDescriptor fileDescriptor = parcelFileDescriptor . getFileDescriptor ();
        Bitmap image = BitmapFactory . decodeFileDescriptor ( fileDescriptor );
        parcelFileDescriptor . close ();
        return image ;
    }

    //convertir bitmap a base64
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(RegistroConductor2.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                camara1.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConductor2.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }
    //fin manejo de galeria y camara




    //imagen circular
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void fecha(View v){
        customTimePickerDialog();
    }

    //__methode will be call when we click on "Custom Date Picker Dialog" and will be show the custom date selection dilog.
    public void customTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datepickerdialog = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) RegistroConductor2.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        datepickerdialog.setThemeDark(true); //set dark them for dialog?
        datepickerdialog.vibrate(true); //vibrate on choosing date?
        datepickerdialog.dismissOnPause(true); //dismiss dialog when onPause() called?
        datepickerdialog.showYearPickerFirst(false); //choose year first?
        datepickerdialog.setAccentColor(Color.parseColor("#80ae49")); // custom accent color
        datepickerdialog.setTitle("Please select a date"); //dialog title
        datepickerdialog.show(getFragmentManager(), "Datepickerdialog"); //show dialog
    }

    //__methode will be call when we click on "Custom Date Picker Dialog" and will be show the custom date selection dilog.
    public void customTimePickerDialog1() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);
        dpd.setAccentColor(getResources().getColor(R.color.colorVerdeOscuro));
        dpd.show(getFragmentManager(), "Timepickerdialog");
    }

    //___this is the listener callback method will be call on time selection by default date picker.
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        //Toast.makeText(this, "Selected by default time picker : " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
    }

    //___this is the listener callback method will be call on time selection by custom date picker.
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        date=datetime.getTime();
        //Toast.makeText(this, "Selected by custom time picker : " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),""+hourFormat.format(date), Toast.LENGTH_LONG).show();

        //fecha_expedicion_tarjeta_propiedad.setText(""+hourFormat.format(date));
    }


    /**
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (++monthOfYear) + "/" + year;

        txt_Licencia_de_transito.setText(""+date);
    }

    public void listadoCategorias(View v){
        showListadoCategorias();
    }

    public void showListadoCategorias(){
        final CharSequence[] items = {"Categoría 4", "Categoría 5", "Categoría 6"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Categoria licencia");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                txt_categoria_licencia.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



}



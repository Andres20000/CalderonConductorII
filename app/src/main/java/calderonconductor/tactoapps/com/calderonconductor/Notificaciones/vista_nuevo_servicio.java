package calderonconductor.tactoapps.com.calderonconductor.Notificaciones;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Globales;
import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.CmdOrdenes;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoCancelarServicio;
import calderonconductor.tactoapps.com.calderonconductor.Comandos.ComandoOrdenesConductorTerceros;
import calderonconductor.tactoapps.com.calderonconductor.InformacionServicio;
import calderonconductor.tactoapps.com.calderonconductor.R;
import calderonconductor.tactoapps.com.calderonconductor.principal.DetalleServicio;

public class vista_nuevo_servicio extends AppCompatActivity  {


    String idServicio;
    String conductor;
    int Codigo;
    Modelo modelo = Modelo.getInstance();

    private Button Button_NUSE_Aceptar;
    private Button Button_NUSE_Cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_nuevo_servicio);

        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        Button_NUSE_Aceptar = (Button) findViewById(R.id.Button_NUSE_Aceptar);
        Button_NUSE_Cancelar = (Button) findViewById(R.id.Button_NUSE_Cancelar);

        Globales.ventana_nuevo_servicio = this;



        long tiempo = modelo.params.tiempoEsperaPreAsignadoConductorEnSegundos;
        new IniciarRechazoAutomatico().execute(tiempo);

        Button_NUSE_Aceptar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                rechazoautomatico = false;

                Button_NUSE_Aceptar.setEnabled(false);
                Button_NUSE_Cancelar.setEnabled(false);

                Toast.makeText(getApplicationContext(), "Se envio la aceptacion del servicio ", Toast.LENGTH_LONG).show();
                Bundle bundle = getIntent().getExtras();
                idServicio = bundle.getString("id");
                conductor = bundle.getString("conductor");

                /*
        DatabaseReference myRef3 = FirebaseDatabase.getInstance().getReference("ordenes/pendientes/"+ idServicio );//ruta path
                DatabaseReference myRef2 = myRef3.child("asignaciontemporal"); //Write your child reference if any
                myRef2.setValue(true);
                 */



                Intent i = new Intent(getApplicationContext(), DetalleServicio.class);
                i.putExtra("id", "" + idServicio);// se envia el id de la orden segun la posicion
                i.putExtra("condicion", "aceptar" );// se envia el id de la orden segun la posicion
                i.putExtra("conductor", "" + conductor);// se envia el id de la orden segun la posicion
                startActivity(i);
                finish();


            }
        });

        Button_NUSE_Cancelar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Button_NUSE_Aceptar.setEnabled(false);
                Button_NUSE_Cancelar.setEnabled(false);

                rechazoautomatico = false;
                RechazarOrden();

            }
        });

    }


    public  boolean rechazoautomatico = true;

    private class IniciarRechazoAutomatico extends AsyncTask<Long, Boolean, Boolean> {

        @Override
        protected Boolean doInBackground(Long... tiempo) {


            try {
                Thread.sleep(tiempo[0] * 1000);
            } catch (InterruptedException e) {

            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            if(rechazoautomatico == true){
                RechazarOrden();
            }
        }

    }


    private void RechazarOrden(){
        Bundle bundle = getIntent().getExtras();
        idServicio = bundle.getString("id");
        conductor = bundle.getString("conductor");
        Codigo = bundle.getInt("codigonotificacion");
        ValidarSiExisteOrden();



    }

     boolean cambiodetectado = false;
    public void ValidarSiExisteOrden(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref2 = database.getReference("ordenes/pendientes/"+ idServicio);//ruta path
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                if (snap.hasChild("estado")){

                    if(cambiodetectado == false){
                        cambiodetectado = true;

                        Toast.makeText(getApplicationContext(), "Se envio la cancelacion del servicio ", Toast.LENGTH_LONG).show();

                        crearRechazo(conductor,idServicio);

                        DatabaseReference remover = FirebaseDatabase.getInstance().getReference("ordenes/pendientes/"+ idServicio );
                        remover.child("asignadoPor").removeValue();
                        remover.child("conductor").removeValue();
                        remover.child("color").removeValue();
                        remover.child("marca").removeValue();
                        remover.child("matricula").removeValue();
                        remover.child("referencia").removeValue();

                        DatabaseReference myRef3 = FirebaseDatabase.getInstance().getReference("ordenes/pendientes/"+ idServicio );//ruta path
                        DatabaseReference myRef2 = myRef3.child("estado"); //Write your child reference if any
                        myRef2.setValue("NoAsignado");


                    }

                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(Codigo);

        finish();


    }


    public void crearRechazo(String conductor, String idServicio) {


        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("ordenes/pendientes/"+ idServicio +"/rechazos");//ruta path
        DatabaseReference myRef = myRef1.child(conductor); //Write your child reference if any
        myRef.setValue(true);




    }

}
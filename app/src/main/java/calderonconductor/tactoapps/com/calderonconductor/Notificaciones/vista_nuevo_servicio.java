package calderonconductor.tactoapps.com.calderonconductor.Notificaciones;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class vista_nuevo_servicio extends AppCompatActivity implements ComandoOrdenesConductorTerceros.OnOrdenesConductorTercerosChangeListener {


    String idServicio;
    String conductor;

    private Button Button_NUSE_Aceptar;
    private Button Button_NUSE_Cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Window win= getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_nuevo_servicio);

        Button_NUSE_Aceptar = (Button) findViewById(R.id.Button_NUSE_Aceptar);
        Button_NUSE_Cancelar = (Button) findViewById(R.id.Button_NUSE_Cancelar);

        Globales.ventana_nuevo_servicio = this;

        final ComandoOrdenesConductorTerceros[] comandoOrdenesConductorTerceros = {null};

        Button_NUSE_Aceptar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
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
                Toast.makeText(getApplicationContext(), "Se envio la cancelacion del servicio ", Toast.LENGTH_LONG).show();
                Bundle bundle = getIntent().getExtras();
                idServicio = bundle.getString("id");
                conductor = bundle.getString("conductor");
                crearRechazo(conductor,idServicio);

                DatabaseReference myRef3 = FirebaseDatabase.getInstance().getReference("ordenes/pendientes/"+ idServicio );//ruta path
                DatabaseReference myRef2 = myRef3.child("estado"); //Write your child reference if any
                myRef2.setValue("NoAsignado");

                finish();
            }
        });

    }



    public void crearRechazo(String conductor, String idServicio) {


        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("ordenes/pendientes/"+ idServicio +"/rechazos");//ruta path
        DatabaseReference myRef = myRef1.child(conductor); //Write your child reference if any
        myRef.setValue(true);




    }

    @Override
    public void cargoUnaOrdenesConductorTerceros() {

    }

    @Override
    public void resultadoAlAsignar(boolean exitoso) {

    }

    @Override
    public void resultadoAlAsignarCancel(boolean cancel) {

    }

    @Override
    public void cargoUnaOrdenesConductorTercerosCoti() {

    }
}
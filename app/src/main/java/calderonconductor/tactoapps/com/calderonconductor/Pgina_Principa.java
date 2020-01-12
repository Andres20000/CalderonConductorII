package calderonconductor.tactoapps.com.calderonconductor;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;
import calderonconductor.tactoapps.com.calderonconductor.principal.ListaServicios;

public class Pgina_Principa extends TabActivity {

    Modelo modelo = Modelo.getInstance();
    String vistaPosicion;
    TabHost tabHost;
    TabHost.TabSpec tab1,tab2,tab3;

    private FusedLocationProviderClient fClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pgina__principa);

        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }

        String tipoconductor= modelo.tipoConductor;
        //modelo.llamarServicios();
        Resources res = getResources();

        // create the TabHost that will contain the Tabs

        // create the TabHost that will contain the Tabs
        tabHost = (TabHost)findViewById(android.R.id.tabhost);


        tab1 = tabHost.newTabSpec("primer Tab");
        tab2 = tabHost.newTabSpec("segundo Tab");
        tab3 = tabHost.newTabSpec("tercero tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("",getResources().getDrawable(R.drawable.tap_perfil));
        tab1.setContent(new Intent(this, Tap_1.class));

        tab2.setIndicator("", getResources().getDrawable(R.drawable.tap_servicio));
        tab2.setContent(new Intent(this, ListaServicios.class));
        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);


        tab3.setIndicator("", getResources().getDrawable(R.drawable.tap_historico));
        tab3.setContent(new Intent(this, Tap_3.class));

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);


        // fondo del indicador
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.TRANSPARENT);
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.TRANSPARENT);
        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(Color.TRANSPARENT);
        //indicamos que inicie de la posicion 1 {0,1,2}
        //tabHost.setCurrentTab(1);
        //Bundle bundle = getIntent().getExtras();
        //vistaPosicion = bundle.getString("vistaPosicion");

        fClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationPermission();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getUltimaUbicacion();
    }




    //bhtn_atras_hadware

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar","cerrar");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
            // mandar apk a segundo plano
            moveTaskToBack(true);
        }
        return true;
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Pgina_Principa.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //startLocationUpdates();
            getUltimaUbicacion();



        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            }

        }


    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //startLocationUpdates();
                    tabHost.setCurrentTab(1);
                    Bundle bundle = getIntent().getExtras();
                    vistaPosicion = bundle.getString("vistaPosicion");
                }
                else{
                    //permisos.setVisibility(View.VISIBLE);
                    //TODO: Que pasa si no comparte ubicacion.
                }
            }
        }
        //updateLocationUI();
    }


    @SuppressLint("MissingPermission")
    private void getUltimaUbicacion(){


        fClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location loc) {

                // Got last known location. In some rare situations this can be null.
                if (loc != null  &&  loc.getLatitude() != 0) {
                    modelo.latitud = loc.getLatitude();
                    modelo.longitud = loc.getLongitude();
                    modelo.cLoc = loc;

                    tabHost.setCurrentTab(1);
                    Bundle bundle = getIntent().getExtras();
                    vistaPosicion = bundle.getString("vistaPosicion");
                }
            }
        });
    }





}

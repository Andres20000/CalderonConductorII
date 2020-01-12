package calderonconductor.tactoapps.com.calderonconductor;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import calderonconductor.tactoapps.com.calderonconductor.Clases.Modelo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String idServicio;
    String origen;
    String destino;
    Modelo modelo = Modelo.getInstance();

    //bd
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (savedInstanceState != null){
            Intent i = new Intent(getApplicationContext(), Splash.class);
            startActivity(i);
            finish();
            return;
        }



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Bundle bundle = getIntent().getExtras();

        idServicio = bundle.getString("id");
        origen = bundle.getString("origen");
        destino = bundle.getString("destino");

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Mi ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

*/



        if(modelo.latitudD != 0.0 && modelo.longitudD != 0.0){
            LatLng ctg = new LatLng(modelo.latitudD, modelo.longitudD);// colombia
            CameraPosition possiCameraPosition = new CameraPosition.Builder().target(ctg).zoom(15).bearing(0).tilt(0).build();
            CameraUpdate cam3 =
                    CameraUpdateFactory.newCameraPosition(possiCameraPosition);
            mMap.animateCamera(cam3);
            Marker marker = mMap.addMarker(new MarkerOptions().position(ctg).title("Destino: "+destino));
            marker.setTag(""+modelo.latitudD+"_"+modelo.longitudD);

            float azul = BitmapDescriptorFactory.HUE_BLUE;
            //marcadorColor(modelo.latitud, modelo.longitud, "Pais Colombia", azul);
        }

        if(modelo.latitudO != 0.0 && modelo.longitudO != 0.0){
            LatLng ctg = new LatLng(modelo.latitudO, modelo.longitudO);// colombia
            CameraPosition possiCameraPosition = new CameraPosition.Builder().target(ctg).zoom(15).bearing(0).tilt(0).build();
            CameraUpdate cam3 =
                    CameraUpdateFactory.newCameraPosition(possiCameraPosition);
            mMap.animateCamera(cam3);
            Marker marker =  mMap.addMarker(new MarkerOptions().position(ctg).title("Origen: "+origen));

            marker.setTag(""+modelo.latitudO+"_"+modelo.longitudO);
            float verde = BitmapDescriptorFactory.HUE_GREEN;
            marcadorColor2(modelo.latitud, modelo.longitud, "Pais Colombia", verde);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTag() != null){
                    String marjet_ubi = marker.getTag().toString();
                    String[] parts = marjet_ubi.split("_");
                    String part1 = parts[0]; // 004
                    String part2 = parts[1]; // 034556


                    modelo.latitudO = Double.parseDouble(part1);
                    modelo.longitudO = Double.parseDouble(part2);

                }
                return false;
            }
        });

    }

    private void marcadorColor(double lat, double lng, String pais, float color) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(pais).icon(BitmapDescriptorFactory.defaultMarker(color)));
    }

    private void marcadorColor2(double lat, double lng, String pais, float color) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(pais).icon(BitmapDescriptorFactory.defaultMarker(color)));
    }

    public void atras(View v) {
        atras2();
    }

    public void atras2() {

        Intent intent = new Intent(getApplicationContext(), InformacionServicio.class);
        intent.putExtra("id", idServicio);
        startActivity(intent);
    }


    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            Log.v("cerrar", "cerrar");
            atras2();
        }
        return true;
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void compatirubicacion(View v) {

        //String uri = "waze://?ll="+modelo.latitud+","+modelo.longitud+"&navigate=yes";
       String uri = "geo: "+modelo.latitudO+","+modelo.longitudO+"";
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(uri)));
    }


    public void actualizarUbicacion(Location location){

        if(location != null){
            double latitud1  = location.getLatitude();
            double longitud1  = location.getLongitude();
        }

    }





}

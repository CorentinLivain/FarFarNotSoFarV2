package com.example.farfarnotsofarv2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public EditText rep;
    public boolean mLocationPermissionGranted;
    public FusedLocationProviderClient mFusedLocationProviderClient;
    public double longitude, latitude;
    public LatLng sydney;
    public TextView bal;
    public Context context;
    private ArrayList<Balise> balises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rep = (EditText) findViewById(R.id.reponse);
        bal = (TextView) findViewById(R.id.balises);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        context = getApplicationContext();

        parseXML();
    }

    private void parseXML(){
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("data.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException | IOException e) {
        }
    }

    private void processParsing(XmlPullParser parser) throws XmlPullParserException, IOException {
        balises = new ArrayList<>();
        int eventType = parser.getEventType();
        Balise currentBalise = null;

        while(eventType != XmlPullParser.END_DOCUMENT){
            String eltName = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();

                    if ("balise".equals(eltName)){
                        currentBalise = new Balise();
                        balises.add(currentBalise);
                        currentBalise.context = this;
                    } else if (currentBalise != null){
                        if ("ville".equals(eltName)){
                            currentBalise.titre = parser.nextText();
                        } else if("latlng".equals(eltName)){
                            String[] latLng = parser.nextText().split(",");
                            double parseLatitude = Double.parseDouble(latLng[0]);
                            double parseLongitude = Double.parseDouble(latLng[1]);
                            currentBalise.coordonnees = new LatLng(parseLatitude,parseLongitude);
                        }
                    }
                    break;
            }
            eventType = parser.next();
        }

        printBalises(balises);
    }

    private void printBalises(ArrayList<Balise> balises){
        StringBuilder builder = new StringBuilder();

        for (Balise balise : balises){
            builder.append(balise.titre).append("\n").append(balise.coordonnees).append("\n\n");
        }

        bal.setText(builder.toString());
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
            return;
        }

        // si le GPS n'est pas activé
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //demander à l'utilisateur de l'activer
            activerGPSWindow();
        }

        mMap.setMyLocationEnabled(true);
        mLocationPermissionGranted = true;

        // Add a marker in Sydney and move the camera
        sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        getCurrentLocation();
    }

    private void activerGPSWindow() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    public void afficherRep(View view) {
        int reponse = Integer.parseInt(rep.getText().toString());
        if(rep.getText() == null){
            Toast.makeText(context, "Entré une réponse", Toast.LENGTH_LONG).show();
        } else if (reponse > 20037){
            Toast.makeText(context, "La distance ne peut pas être supérieur à la circonférence/2 de la terre", Toast.LENGTH_LONG).show();
        } else {
            getCurrentLocation();
            /*distRep.setText("distance : " + Integer.toString(calculerDistance()));
            dif.setText("différence : " + Integer.toString(difDistance()));*/
        }
    }

    public void getCurrentLocation() {
        if (mLocationPermissionGranted) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        Location mLastKnownLocation = (Location) task.getResult();
                        latitude = mLastKnownLocation.getLatitude();
                        longitude = mLastKnownLocation.getLongitude();
                    }
                }
            });
        }
    }

    public int calculerDistance(){
        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, sydney.latitude, sydney.longitude, results);
        int distance = (int)results[0]/1000;
        return distance;
    }

    public int difDistance(){
        int dif = calculerDistance() - Integer.parseInt(rep.getText().toString());
        return dif;
    }
}
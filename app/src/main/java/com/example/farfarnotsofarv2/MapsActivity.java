package com.example.farfarnotsofarv2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
    public boolean mLocationPermissionGranted;
    public FusedLocationProviderClient mFusedLocationProviderClient;
    public Context context;

    private EditText rep;
    private TextView scoreText;
    private Button valider;
    private ProgressBar progressBar;
    private double longitudeAct, latitudeAct;
    private ArrayList<Balise> balises;
    private ArrayList<String> villes, reponses, distances;
    private Balise balise;
    private int i = 0;
    private int score = 0;
    private int scoreTot;
    private String fileName, zone;
    private int bal = 0;
    private int nbBalise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = getApplicationContext();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fileName = extras.getString("fileName");
            nbBalise = extras.getInt("nbBalise");
        }

        rep = (EditText) findViewById(R.id.reponse);
        rep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    boutonPress();
                    handled = true;
                }
                return handled;
            }
        });

        valider = (Button) findViewById(R.id.valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boutonPress();
            }
        });
        scoreText = (TextView) findViewById(R.id.score);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setMax(nbBalise);

        villes = new ArrayList<>();
        reponses = new ArrayList<>();
        distances = new ArrayList<>();

        scoreTot = nbBalise * 10;

        setTextScreen();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        parseXML();
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
            return;
        }

        mMap.setMyLocationEnabled(true);
        mLocationPermissionGranted = true;

        getCurrentLocation();

        afficherBalise();
    }

    private void parseXML(){
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open(fileName);
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
                            currentBalise.ville = parser.nextText();
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
    }

    public void boutonPress() {
        if (rep.getText().toString().isEmpty()){
            Toast.makeText(context, "Entré une réponse", Toast.LENGTH_SHORT).show();
        } else {
            gestionRep();
        }
    }

    private void gestionRep() {
        int reponse = getRep();
        if (reponse > 20037){
            Toast.makeText(context, "La distance ne peut pas être supérieur à la circonférence/2 de la terre", Toast.LENGTH_SHORT).show();
        } else {
            getCurrentLocation();
            scoreCalc();
            allText();
            rep.getText().clear();
            if (i != nbBalise){
                mMap.clear();
                afficherBalise();
                closeKeyboard();
                setTextScreen();
            } else {
                endGame();
            }
        }
    }

    private void getCurrentLocation() {
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
                        latitudeAct = mLastKnownLocation.getLatitude();
                        longitudeAct = mLastKnownLocation.getLongitude();
                    }
                }
            });
        }
    }

    private int calculerDistance(){
        float[] results = new float[1];
        Location.distanceBetween(latitudeAct, longitudeAct, balise.getLatitude(), balise.getLongitude(), results);
        int distance = (int)results[0]/1000;
        return distance;
    }

    private int difDistance(){
        int dif = calculerDistance() - getRep();
        return  Math.abs(dif);
    }

    private void afficherBalise(){
        int nbAle = 0 + (int)(Math.random() * ((balises.size() - 0) + 1));
        balise = balises.get(nbAle);
        balise.creerMarqueur(mMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(balise.coordonnees));
        balises.remove(nbAle);
        i++;
    }

    private void scoreCalc(){
        if (difDistance() <= (int)(calculerDistance()*0.05)){
            score = score +10;
            Toast.makeText(context, "Bravo ! Score parfait ! 10/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.1)){
            score = score +9;
            Toast.makeText(context, "Pas loin du tout ! 9/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.2)){
            score = score +8;
            Toast.makeText(context, "Pas mal ! 8/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.3)){
            score = score +7;
            Toast.makeText(context, "Bien ! 7/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.4)){
            score = score +6;
            Toast.makeText(context, "Un petit effort et t'y est ! 6/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.5)){
            score = score +5;
            Toast.makeText(context, "La moyenne ! 5/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.6)){
            score = score +4;
            Toast.makeText(context, "C'est tout juste ! 4/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.7)){
            score = score +3;
            Toast.makeText(context, "C'est loin ! 3/10", Toast.LENGTH_SHORT).show();
        } else if (difDistance() <= (int)(calculerDistance()*0.8)){
            score = score +2;
            Toast.makeText(context, "Alors ! 2/10", Toast.LENGTH_SHORT).show();
        }else if (difDistance() <= (int)(calculerDistance()*0.9)){
            score = score +1;
            Toast.makeText(context, "Il va falloir réviser sa géo ! 1/10", Toast.LENGTH_SHORT).show();
        } else {
            score = score + 0;
            Toast.makeText(context, "... 0/10", Toast.LENGTH_SHORT).show();
        }
    }

    private void allText(){
        villes.add(balise.ville);
        reponses.add(Integer.toString(getRep()));
        distances.add(Integer.toString(calculerDistance()));
    }

    private int getRep(){
        return Integer.parseInt(rep.getText().toString());
    }

    private void setTextScreen() {
        scoreText.setText("score : " + score + "/" + scoreTot);
        progressBar.setProgress(++bal);
    }

    private void endGame() {
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("score",score);
        intent.putExtra("scoreTot",scoreTot);
        intent.putExtra("nbBalise", nbBalise);
        intent.putExtra("villes", villes);
        intent.putExtra("distances", distances);
        intent.putExtra("reponses", reponses);
        switch (fileName){
            case "franceData.xml" :
                intent.putExtra("zone", "FR");
                break;
            case "europeData.xml" :
                intent.putExtra("zone", "EU");
                break;
            case "mondeData.xml" :
                intent.putExtra("zone", "WO");
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Quitter la partie ?");
        alertDialog.setMessage("Voulez vous vraiment quitter la partie ? \n Votre progression sera perdue.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chargeMenu();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void chargeMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
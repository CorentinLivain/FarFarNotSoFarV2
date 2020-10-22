package com.example.farfarnotsofarv2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Balise {
    public MapsActivity context;
    public String ville;
    public LatLng coordonnees;
    public Marker marqueur;

    public void creerMarqueur(GoogleMap pMap) {
        marqueur = pMap.addMarker(new MarkerOptions()
                .position(coordonnees)
                .title(ville)
                /*.icon(BitmapDescriptorFactory.fromResource(R.drawable.gorilla))*/);

        marqueur.showInfoWindow();
    }

    public double getLatitude(){
        return coordonnees.latitude;
    }

    public double getLongitude(){
        return coordonnees.longitude;
    }
}

package com.example.farfarnotsofarv2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Balise {
    public MapsActivity context;
    public String titre;
    public LatLng coordonnees;
    public Marker marqueur;

    /*public Balise(MapsActivity context, String titre ,LatLng coordonnees) {
        this.context = context;
        this.titre = titre;
        this.coordonnees = coordonnees;
        this.valide = false;
        this.marqueur = null;
    }*/

    public void creerMarqueur(GoogleMap pMap) {
        marqueur = pMap.addMarker(new MarkerOptions()
                .position(coordonnees)
                .title(titre));
    }
}

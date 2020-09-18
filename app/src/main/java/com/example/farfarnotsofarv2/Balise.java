package com.example.farfarnotsofarv2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Balise {
    public MapsActivity context;
    public LatLng coordonnees;
    public String titre;
    public boolean valide;
    public Marker marqueur;
    public int numero;

    public Balise(MapsActivity context, LatLng coordonnees, String titre, int numero) {
        this.context = context;
        this.coordonnees = coordonnees;
        this.titre = titre;
        this.valide = false;
        this.marqueur = null;
        this.numero = numero;
    }

    public void creerMarqueur(GoogleMap pMap) {
        marqueur = pMap.addMarker(new MarkerOptions()
                .position(coordonnees)
                .title(titre));
    }
}

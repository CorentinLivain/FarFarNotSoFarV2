package com.example.farfarnotsofarv2;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntroFragment;

public class TutorielActivity extends com.github.paolorotolo.appintro.AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro);

        addSlide(AppIntroFragment.newInstance("Far Far ... Not So Far !","Bonjour et Bienvenue dans Far Far ... Not So Far !",R.drawable.title, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("But du jeu","Dans ce jeu, vous devez deviner la distance entre vous (le point bleu) et le point rouge sur la carte.",R.drawable.jeux_tuto, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Donner votre réponse","Pour cela, entrez une distance et validez votre réponse.",R.drawable.reponse_tuto, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Avant de jouer","Vous devez tout d'abord sélectionner une zone et un nombre de balise.",R.drawable.choix_tuto, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Tableau de fin","A la fin de votre partie, vous aurez accès à un récapitulatif des stats de votre partie.",R.drawable.fin_tuto, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Tableau des scores","Vous avez aussi accès à un tableau des scores depuis le menu principal.",R.drawable.tableau_tuto, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
    }

    @Override
    public void onDonePressed(Fragment currentfragment){
        super.onDonePressed(currentfragment);
        Intent intent=new Intent(this, MenuActivity.class);
        startActivity(intent);

    }

    @Override
    public void onSkipPressed(Fragment currentfragment){
        super.onSkipPressed(currentfragment);
        Intent intent=new Intent(this, MenuActivity.class);
        startActivity(intent);

    }
}

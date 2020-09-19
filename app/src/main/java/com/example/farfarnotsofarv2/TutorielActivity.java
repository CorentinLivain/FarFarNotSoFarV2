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

        addSlide(AppIntroFragment.newInstance("HOLLA","Bonjour",R.drawable.euh, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));
    }

    @Override
    public void onDonePressed(Fragment currentfragment){
        super.onDonePressed(currentfragment);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void onSkipPressed(Fragment currentfragment){
        super.onSkipPressed(currentfragment);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}

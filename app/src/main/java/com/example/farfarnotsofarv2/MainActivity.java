package com.example.farfarnotsofarv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void choice(View view) {
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);
    }

    public void tuto(View view) {
        Intent intent = new Intent(this, TutorielActivity.class);
        startActivity(intent);
    }

    public void info(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void resetSave(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Supprimer les meilleurs scores ?");
        alertDialog.setMessage("Voulez vous vraiment supprimer vos meilleurs scores ? \n Vous ne pourrez pas les reprendres.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sharedPreferences = getBaseContext().getSharedPreferences("SCORES", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){

    }
}
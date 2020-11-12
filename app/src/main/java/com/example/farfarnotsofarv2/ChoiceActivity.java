package com.example.farfarnotsofarv2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiceActivity extends AppCompatActivity {

    private String fileName;
    private int nbBalise = 0, zoom;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        context = getApplicationContext();

    }

    public void radioEndroit(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.france:
                if (checked)
                    fileName = "franceData.xml";
                    zoom = 5;
                    break;
            case R.id.europe:
                if (checked)
                    fileName = "europeData.xml";
                    zoom = 4;
                    break;
            case R.id.monde:
                if (checked)
                    fileName = "mondeData.xml";
                    zoom = 0;
                    break;
        }
    }

    public void radioNbBalise(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.cinq:
                if (checked)
                    nbBalise = 5;
                break;
            case R.id.dix:
                if (checked)
                    nbBalise = 10;
                break;
            case R.id.vingtCinq:
                if (checked)
                    nbBalise = 25;
                break;
            case R.id.cinquante:
                if (checked)
                    nbBalise = 50;
                break;
            case R.id.cent:
                if (checked)
                    nbBalise = 100;
                break;
        }
    }

    public void valide(View view) {
        if ((fileName == null) || (nbBalise == 0)) {
            Toast.makeText(context, "Veuillez sélectionner un endroit ET un nombre de balise", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("fileName", fileName);
            intent.putExtra("nbBalise", nbBalise);
            intent.putExtra("zoom", zoom);
            startActivity(intent);
        }

    }

    public void menu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}

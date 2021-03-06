package com.example.farfarnotsofarv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EndActivity extends AppCompatActivity {

    private TextView scoreAff, topScore;
    private int score, scoreTot, nbBalise, scoreSave, bestScore;
    private String zone, nomSave;
    private ArrayList<String> villes, reponses, distances;
    private SharedPreferences sharedPreferences;
    private boolean newBestScore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            score = extras.getInt("score");
            scoreTot = extras.getInt("scoreTot");
            nbBalise = extras.getInt("nbBalise");
            villes = extras.getStringArrayList("villes");
            reponses = extras.getStringArrayList("reponses");
            distances = extras.getStringArrayList("distances");
            zone = extras.getString("zone");
        }

        scoreAff = (TextView) findViewById(R.id.scoreAff);
        topScore = (TextView) findViewById(R.id.topScore);

        nomSave = zone + "_" + nbBalise;

        createTable();

        saveScore();

        setTextScreen();
    }

    /**
     * Regarde si une sauvegarde existe pour la partie
     * Si oui, regarde quel est le meilleur score et enregistre le meilleur
     * Si non, créer une nouvelle sauvegarde avec le score
     */
    private void saveScore() {
        sharedPreferences = getBaseContext().getSharedPreferences("SCORES", MODE_PRIVATE);

        if (sharedPreferences.contains(nomSave)) {
            scoreSave = sharedPreferences.getInt(nomSave, 0);
            if(scoreSave < score){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(nomSave, score);
                editor.commit();
                bestScore = score;
                newBestScore = true;
            } else {
                bestScore = scoreSave;
            }

        } else {
            sharedPreferences.edit().putInt(nomSave, score).apply();
            bestScore = score;
            newBestScore = true;
        }
    }

    /**
     * Créer un tableau regroupant toutes les réponses de la partie en ajoutant une ligne pour chaque balise
     */
    private void createTable() {
        String[] columns = new String[] {"_id", "col1", "col2", "col3"};

        MatrixCursor matrixCursor= new MatrixCursor(columns);
        startManagingCursor(matrixCursor);
        int y = 0;
        int i = 0;

        while (y < nbBalise) {

            if (i == 0){
                matrixCursor.addRow(new Object[] { i,"Ville : ","Votre réponse : ","Bonne réponse : "});
                i++;
            }
            matrixCursor.addRow(new Object[] { y + 1,villes.get(y),reponses.get(y) + " Km",distances.get(y) + " Km"});
            y++;
        }

        String[] from = new String[] {"col1", "col2", "col3"};

        int[] to = new int[] { R.id.textViewCol1, R.id.textViewCol2, R.id.textViewCol3};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_item_3, matrixCursor, from, to, 0);

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);

    }

    /**
     * Mets à jour les textes de score sur l'écran
     */
    private void setTextScreen() {
        scoreAff.setText(score + "/" + scoreTot);

        if (newBestScore) {
            topScore.setText("Nouveau Meilleur score : " + bestScore);
        } else {
            topScore.setText("Meilleur score : " + bestScore);
        }
    }

    public void menu(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){

    }
}

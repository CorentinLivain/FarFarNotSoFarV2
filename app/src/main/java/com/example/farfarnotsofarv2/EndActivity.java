package com.example.farfarnotsofarv2;

import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EndActivity extends AppCompatActivity {

    private TextView scoreAff;
    private int score, scoreTot, nbBalise;
    private String balises;
    private ArrayList<String> villes, reponses, distances;

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

        }

        scoreAff = (TextView) findViewById(R.id.scoreAff);

        createTable();

        scoreAff.setText(score + "/" + scoreTot);
    }

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

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_item, matrixCursor, from, to, 0);

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);

    }

    public void menu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){

    }
}

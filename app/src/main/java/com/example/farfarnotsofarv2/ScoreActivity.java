package com.example.farfarnotsofarv2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String zone, nomSave;
    private int nbBalise, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        sharedPreferences = getBaseContext().getSharedPreferences("SCORES", MODE_PRIVATE);
    }

    public void selectZone(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.france:
                if (checked)
                    zone = "FR_";
                break;
            case R.id.europe:
                if (checked)
                    zone = "EU_";
                break;
            case R.id.monde:
                if (checked)
                    zone = "WO_";
                break;
        }

        showBestScore();
    }

    private void showBestScore() {
        String[] columns = new String[] {"_id", "col1", "col2"};

        MatrixCursor matrixCursor= new MatrixCursor(columns);
        startManagingCursor(matrixCursor);
        int y = 0;
        int i = 0;

        while (y < 5) {

            if (i == 0){
                matrixCursor.addRow(new Object[] { i,"Nombre de balise : ","Meilleur score : "});
                i++;
            }

            switch (y) {
                case 0 :
                    nbBalise = 5;
                    nomSave = zone + nbBalise;
                    score = sharedPreferences.getInt(nomSave, 1001);
                    break;
                case 1 :
                    nbBalise = 10;
                    nomSave = zone + nbBalise;
                    score = sharedPreferences.getInt(nomSave, 1001);
                    break;
                case 2 :
                    nbBalise = 25;
                    nomSave = zone + nbBalise;
                    score = sharedPreferences.getInt(nomSave, 1001);
                    break;
                case 3 :
                    nbBalise = 50;
                    nomSave = zone + nbBalise;
                    score = sharedPreferences.getInt(nomSave, 1001);
                    break;
                case 4 :
                    nbBalise = 100;
                    nomSave = zone + nbBalise;
                    score = sharedPreferences.getInt(nomSave, 1001);
                    break;
            }

            if (score == 1001) {
                matrixCursor.addRow(new Object[] { y + 1,nbBalise,"pas de score"});
                y++;
            } else {
                matrixCursor.addRow(new Object[] { y + 1,nbBalise,score});
                y++;
            }
        }

        String[] from = new String[] {"col1", "col2"};

        int[] to = new int[] { R.id.textViewCol1, R.id.textViewCol2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_item_2, matrixCursor, from, to, 0);

        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);
    }

    public void resetSave(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Supprimer les meilleurs scores ?");
        alertDialog.setMessage("Voulez vous vraiment supprimer vos meilleurs scores ? \n Vous ne pourrez pas les reprendres.");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                showBestScore();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    public void menu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

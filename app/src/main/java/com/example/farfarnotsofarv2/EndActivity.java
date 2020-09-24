package com.example.farfarnotsofarv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    private TextView scoreAff;
    private int score, scoreTot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            score = extras.getInt("score", 0);
            scoreTot = extras.getInt("scoreTot",0);
        }

        scoreAff = (TextView) findViewById(R.id.scoreAff);

        scoreAff.setText(score + "/" + scoreTot);
    }

    public void menu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){

    }
}

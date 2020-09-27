package com.example.farfarnotsofarv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiceActivity extends AppCompatActivity {

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

    }

    public void franceBtn(View view){
        fileName = "franceData.xml";
        game(view);
    }

    public void europeBtn(View view){
        fileName = "europeData.xml";
        game(view);
    }

    public void mondeBtn(View view){
        fileName = "mondeData.xml";
        game(view);
    }

    public void game(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }
}

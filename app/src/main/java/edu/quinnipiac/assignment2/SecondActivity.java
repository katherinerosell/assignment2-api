package edu.quinnipiac.assignment2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        //have share option available in toolbar
        String gamesOfGenre = (String) getIntent().getExtras().get("List of games");
        TextView textGameTitles = (TextView) findViewById(R.id.listofgames);
        textGameTitles.setText(gamesOfGenre);
    }

}

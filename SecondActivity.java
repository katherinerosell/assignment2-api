package edu.quinnipiac.assignment2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.URL;

/**
 * SecondActivity
 * Author: Katherine Rosell
 * 2/29/2020
 * SecondActivity class is the second activity where all the games within a certain
 * genre, selected in MainActivity, are held. This class holds the text and image view
 * objects we load information into thanks to the intent's information we sent over.
 */

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        //have share option available in toolbar
        String gamesOfGenre = (String) getIntent().getExtras().get("List of Games");
        String genrePicked = (String) getIntent().getExtras().get("Genre");
        String imgURL = (String) getIntent().getExtras().get("Img");
        loadImage(imgURL);//using the string, get the actual image from the internet

        TextView textGameTitles = (TextView) findViewById(R.id.listofgames);
        textGameTitles.setText("Games of Genre, " + genrePicked + ":  " +gamesOfGenre);
    }

    /**
     * Send in the string url of the image we want to actually load in
     * set the imageview to the bitmap re retrieved
     * @param theURL
     */

    private void loadImage(String theURL){
        Bitmap bitmapImg = null;
        try{
            InputStream inputStream = new URL(theURL).openStream();
            bitmapImg = BitmapFactory.decodeStream(inputStream);
        }catch(Exception err){
            Log.e("*******  SECOND ACTIVITY  ****** ", "  " + err.toString());
        }
        ImageView my_image = findViewById(R.id.genre_img);
        my_image.setImageBitmap(bitmapImg);
    }


}
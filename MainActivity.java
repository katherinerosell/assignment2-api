package edu.quinnipiac.assignment2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * MainActivity
 * @Authors: Katherine Rosell, Jenna Saleh
 */


public class MainActivity extends AppCompatActivity {
    ShareActionProvider provider;
    //urls of api here!
    private String urlHost = "https://rawg-video-games-database.p.rapidapi.com/genres";
    private String key = "cf3ce5d779mshb68e7898cff4e2ep1b8712jsn1cd80cdd8b5b"; //katie's specific key, different from Jenna's for some reason
    private static GenreHandler genreHandler = new GenreHandler();
    private static String genrePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //just a precaution for version errors
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Spinner object
        final Spinner genreSpinner = findViewById(R.id.genre_spinner);
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genreHandler.GENRES);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);
        //the Find Games button is what really allows the user to go to the next activity using the spinner
        //we only want to go to the next activity when the user clicks the find games button
        Button searchButton = findViewById(R.id.searchB);//search Button
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String spinnerText = genreSpinner.getSelectedItem().toString();
                Log.d("SPINNER", spinnerText);
                genrePicked = spinnerText;
                RetrieveGenreGames myFetchRequest = (RetrieveGenreGames) new RetrieveGenreGames().execute(genrePicked);

            }
        });

    }

    /**
     * RetrieveGenreGames
     * AsyncTask handles the internet connection to the api information we are using.
     */

    private class RetrieveGenreGames extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection mainConnection = null;
            ArrayList<String> gamesOfGenre;

            Log.d("RetrieveGenreGames", "RetrieveGenreGames");
            try {
                Log.d("RetrieveGenreGames", "---------in try block--------");
                URL url = new URL(urlHost); //+ "/" + genrePicked //no need for ?mashape-key=....
                mainConnection = (HttpsURLConnection) url.openConnection();
                mainConnection.setRequestMethod("GET");
                mainConnection.setRequestProperty("x-rapidapi-key", "cf3ce5d779mshb68e7898cff4e2ep1b8712jsn1cd80cdd8b5b");
                mainConnection.connect();

                if (mainConnection.getInputStream() == null) {
                    Log.d("NO INTERNET", "NO INTERNET");
                    return null;
                }
                gamesOfGenre = getStringRead(
                        new BufferedReader(new InputStreamReader((mainConnection.getInputStream()))), strings[0]);
                //Log.d("games in genre", gamesOfGenre.toString());
            } catch (Exception ex){
                Log.e("ERR in RETREIVE", ex.toString());
                return null;
            } finally{
                if (mainConnection != null) mainConnection.disconnect();
            }
            return String.valueOf(gamesOfGenre);
        }


        private ArrayList<String> getStringRead(BufferedReader bufferedReader, String currGenere) throws Exception {
            StringBuffer buffer = new StringBuffer();
            String ln; //the line we are reading from
            while((ln=bufferedReader.readLine()) != null){  buffer.append(ln + '\n');  }
            if (bufferedReader != null){
                try{ bufferedReader.close(); }
                catch(IOException err){Log.d("oops no mainAct", "Error" + err.getMessage());}
            }
            return genreHandler.getGamesofGenre(buffer.toString(), currGenere);
        }

        /**
         * OnPOstExecute
         * Using the "res" String we creates, which is the list of games we were able to retrieve,
         * send that information into the second activity to be displayed.
         * As well as the image url of the certain genre
         * @param res
         */
        @Override
        protected void onPostExecute(String res){
            if (res != null){
                Log.d("*************   MAIN ACTIVITY  *************", " on post execute : res   " + res + " ------------ TYPE OF res:  "  + res.getClass());
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("List of Games", res);
                intent.putExtra("Genre", genrePicked);
                intent.putExtra("Img", genreHandler.getImageBackground());
                startActivity(intent);
            }
        }

    }

//Jenna's implementation of Action Bar
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the app bar
    getMenuInflater().inflate(R.menu.menu_main, menu);
    MenuItem menuItem = menu.findItem(R.id.action_share);
    provider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
    setShareActionIntent("Hi");
    return super.onCreateOptionsMenu(menu);
}

    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        provider.setShareIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id== R.id.action_help){
            Intent help = new Intent(this, HelpActivity.class);
            startActivity(help);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello");
            if (provider != null) {
                provider.setShareIntent(intent);
            } else
                Toast.makeText(this, "no provider", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}

package edu.quinnipiac.assignment2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    //urls of api here!
    private String urlHost = "https://rawg-video-games-database.p.rapidapi.com/genres";
    //private String urlHost ="https://rawg-video-games-database.p.rapidapi.com/games";
    //private String urlGenre = "/genres?fragment=true&json=true";//so I don't have to hard code it in
    private String key = "cf3ce5d779mshb68e7898cff4e2ep1b8712jsn1cd80cdd8b5b";
    //private String key = "01eb93a0d5msh5b02026cb0b8933p125f54jsn0256762dca37";
    GenreHandler genreHandler = new GenreHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
/**
 * Get JSON object from genres, "games"
 */
        //Jenna is handling the toolbar - we have different versions and mine ~sometimes~ breaks when I restart my computer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // to String[] from GenreHandler
        final Spinner genreSpinner = findViewById(R.id.genre_spinner);
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genreHandler.GENRES);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        //the Find Games! button is what really allows the user to go to the next activity using the spinner
        Button searchButton = findViewById(R.id.searchB);//search Button
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String spinnerText = genreSpinner.getSelectedItem().toString();
                Log.d("SPINNER", spinnerText);
                final String genrePicked = spinnerText;
                RetrieveGenreGames myFetchRequest = (RetrieveGenreGames) new RetrieveGenreGames().execute(genrePicked);

                //send whatever is in the spinner, the GENRE, to 2nd activity
                //spinner sends it's genre picked to ASYNC TASK - sends genre to retrieve list of games

                genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String genrePicked = (String) parent.getItemAtPosition(position);
                        new RetrieveGenreGames().execute(genrePicked);
                        Log.d("Selected Genre", genrePicked);
                    }
                //don't do anything right now if nothing is selected
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
                 }
        });//end of onClickListener for Search Button
    }//end of onCreate method

    /**
     * LittleAdapterClass
     * small basic class to handle the selected item in the spinner
     * the button actually makes an instance of this class and uses it
     */
    /**
    private class LittleAdapterClass implements AdapterView.OnItemSelectedListener {
        //simple constructor
        public LittleAdapterClass(Context context) {
            super();
        }
     //from listener
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final String genrePicked = (String) parent.getItemAtPosition(position);
            new RetrieveGenreGames().execute(genrePicked);
            Log.d("Selected Genre", genrePicked);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
**/
    /**
     * getStringRead
     */


    private class RetrieveGenreGames extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection mainConnection = null;
            ArrayList<String> gamesOfGenre= new ArrayList<String>();

            Log.d("RetrieveGenreGames", "RetrieveGenreGames");
            try {
                URL url = new URL(urlHost);
                mainConnection = (HttpsURLConnection) url.openConnection();
                mainConnection.setRequestMethod("GET");
                mainConnection.setRequestProperty("x-rapidapi-key", "cf3ce5d779mshb68e7898cff4e2ep1b8712jsn1cd80cdd8b5b");
                mainConnection.connect();

                if (mainConnection.getInputStream() == null) {
                    Log.d("NO INTERNET", "NO INTERNET");
                    return null;
                }
                gamesOfGenre = getStringRead(
                        new BufferedReader(new InputStreamReader((mainConnection.getInputStream()))));
                Log.d("games in genre", gamesOfGenre.toString());
            } catch (Exception ex){
                Log.e("ERR in RETREIVE", ex.toString());
                return null;
            } finally{
                if (mainConnection != null) mainConnection.disconnect();
            }
            return String.valueOf(gamesOfGenre);
        }

        private ArrayList<String> getStringRead(BufferedReader bufferedReader) throws Exception {
            StringBuffer buffer = new StringBuffer();
            String ln; //the line we are reading from
            while((ln=bufferedReader.readLine()) != null){  buffer.append(ln + '\n');  }
            if (bufferedReader != null){
                try{ bufferedReader.close(); }
                catch(IOException err){Log.d("oops no mainAct", "Error" + err.getMessage());}
            }
            Log.d("Games of Genre", buffer.toString());
            return genreHandler.getGamesofGenre(buffer.toString());
        }

        @Override
        protected void onPostExecute(String res){
            if (res != null){
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("List of games", res);
                startActivity(intent);
            }
        }

    }//end of RetrieveGenreGames


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;//if player clicks settings - don't do anything atm
        }
        return super.onOptionsItemSelected(item);
    }
}

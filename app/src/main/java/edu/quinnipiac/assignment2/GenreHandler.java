package edu.quinnipiac.assignment2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

/**
 * @Author: Katherine Rosell
 * The GenreHandler class holds the
 */
public class GenreHandler {
    final public static String[] GENRES = new String[19];//there are 19 genres, 0-18 index
    public static ArrayList<String> myGamesofGenre = new ArrayList<String>(5);

    public GenreHandler(){
        //get all genre names into the array
        //possibly open connection to url and obtain list instead...?
        GENRES[0] = "Action";
        GENRES[1] = "Indie";
        GENRES[2] = "Adventure";
        GENRES[3] = "RPG";
        GENRES[4] = "Shooter";
        GENRES[5] = "Strategy";
        GENRES[6] = "Casual";
        GENRES[7] = "Simulation";
        GENRES[8] = "Puzzle";
        GENRES[9] = "Arcade";
        GENRES[10] = "Platformer";
        GENRES[11] = "Racing";
        GENRES[12] = "Sports";
        GENRES[13] = "Massively Multiplayer";
        GENRES[14] = "Family";
        GENRES[15] = "Fighting";
        GENRES[16] = "Board Games";
        GENRES[17] = "Educational";
        GENRES[18] = "Card";
    }

    public String getGenre(String genreJSONString) throws Exception{
        JSONObject genreJSONObj = new JSONObject(genreJSONString);
        return genreJSONObj.getString("text");
    }


    public ArrayList<String> getGamesofGenre(String gamesofGenre) throws Exception{
        JSONObject genreJSONObj = new JSONObject(gamesofGenre);
        //JSONArray games = genreJSONObj.getJSONArray("games");//get collection of objects
        //search through "results" array first and look by GENRE
        JSONArray resultArray = genreJSONObj.getJSONArray("results");

        for (int a = 0; a < resultArray.length(); a++){
            JSONObject currGenre = resultArray.getJSONObject(a);
            if (currGenre.getJSONObject("name").toString().equals(gamesofGenre)){
                Log.d("GENRE HANDLER", "Current Genre" + currGenre);
                //if object in array equals genre picked in spinner, print all names
                JSONArray gamesArr = currGenre.getJSONArray("games");
                for (int b = 0; b < gamesArr.length(); b++){
                    JSONObject gameObj = resultArray.getJSONObject(b);
                    String gameTitle = gameObj.getString("name");
                    myGamesofGenre.add(gameTitle);
                }
            } else{}
        }
        return myGamesofGenre;
    }




}

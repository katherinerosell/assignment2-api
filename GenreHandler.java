package edu.quinnipiac.assignment2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * GenreHandler
 * @Author: Katherine Rosell, Jenna Saleh
 * 2/29/2020
 * The GenreHandler class holds the main for loop fuction that saves the games and image background
 * within each genre.
 *
 */
public class GenreHandler {
    final public static String[] GENRES = new String[19];//there are 19 genres, 0-18 index
    public static ArrayList<String> myGamesofGenre = new ArrayList<String>(5);
    public static String imageBKG;

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

    /**
     * getGamesofGenre
     *
     * The meat and potatoes of this application.
     * Iterates over JSONArrays and compares JSONObject "name" attributes to the genre selected in
     * MainActivity. Checks the JSON name attributes and looks for JSONArray "games" within the
     * JSONObj that matched the genre picked.
     *
     * @param gamesofGenre
     * @param myCurrGenre
     * @return
     * @throws Exception
     */

    public ArrayList<String> getGamesofGenre(String gamesofGenre, String myCurrGenre) throws Exception{
        JSONObject allGenresJSONObj = new JSONObject(gamesofGenre);
        JSONArray resultArray = allGenresJSONObj.getJSONArray("results");
        for (int i = 0; i < resultArray.length(); i++){
            JSONObject genreSection = resultArray.getJSONObject(i);//results array is a list of all attributes per genre per their individual id tags
            if (genreSection.getString("name").equals(myCurrGenre)){
                JSONArray gamesIDs = genreSection.getJSONArray("games");
                imageBKG = genreSection.getString("image_background"); //found the image_background url, save it for later use.
                for (int j = 0; j < gamesIDs.length(); j++){
                    JSONObject gameID = gamesIDs.getJSONObject(j);
                    myGamesofGenre.add(gameID.getString("name"));
                }
            }
        }
        //Log.d("GENRE HANDLER", "  ------------    GAME TITLES     -------------  " + myGamesofGenre);
        return myGamesofGenre;
    }

    /**
     * getImageBackground()
     * get method for accessing the specific image background per the genre that was selected.
     * @return
     */

    public String getImageBackground(){
        String imgURL = imageBKG.toString();
        return imgURL;
    }


}

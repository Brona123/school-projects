/**
 * @author Sami Joutsijoki
 * The class that handles http requests to get high scores.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HighscoreHandler {
	
	/**
	 * The name of the player.
	 */
	private String name;
	
	/**
	 * The score of the player.
	 */
	private int score;
	
	/**
	 * The streak of the player.
	 */
	private int streak;
	
	/**
	 * The highscore JSON in string format.
	 */
	private static String jsonString = "";
	
	/**
	 * Indicates whether the json has been fetched or not, false by default.
	 */
	private static boolean jsonFetched = false;
	
	/**
	 * Creates a new highscorehandler object with the given parameters.
	 * @param name The name of the player.
	 * @param score The score of the player.
	 * @param streak The streak of the player.
	 */
	public HighscoreHandler(String name, int score, int streak){
		this.name = name;
		this.score = score;
		this.streak = streak;
	}
	
	/**
	 * Inserts a new highscore entry to the database.
	 */
	public void insertHighscore(){
		Thread networkThread = new Thread(new Runnable(){
			@Override
			public void run() {
				BufferedReader in = null;
				try {
					URL highScoreUrl = new URL("..." 
												+ "?nick=" + HighscoreHandler.this.name
												+ "&streak=" + String.valueOf(HighscoreHandler.this.streak)
												+ "&score=" + String.valueOf(HighscoreHandler.this.score));
					URLConnection con = highScoreUrl.openConnection();
					
					in = new BufferedReader(
							new InputStreamReader(
								con.getInputStream()));
					String inputLine;
						
					while ((inputLine = in.readLine()) != null){
						Log.d("HighscoreHandler", inputLine);
					}
					
					in.close();
				} catch (IOException e){
					Log.d("HighscoreHandler", e.getMessage());
				} finally {
					try{
						if (in != null){
							in.close();
						}
					} catch (IOException e){
						Log.d("HighscoreHandler", e.getMessage());
					}
				}
			}
			
		});
		networkThread.start();
	}
	
	/**
	 * Fetches the high score list from the database.
	 */
	public static void fetchScoreList(){
		jsonFetched = false;
		Thread networkThread = new Thread(new Runnable(){
			@Override
			public void run() {
				BufferedReader in = null;
				try {
					HighscoreHandler.jsonString = "";
					Log.d("HighscoreHandler", "TRYING TO FETCH JSON");
					URL highScoreUrl = new URL("...");
					URLConnection con = highScoreUrl.openConnection();
					
					in = new BufferedReader(
							new InputStreamReader(
								con.getInputStream()));
					String inputLine;
						
					while ((inputLine = in.readLine()) != null){
						appendJSON(inputLine);
					}
					HighscoreHandler.jsonFetched = true;
					in.close();
				} catch (IOException e){
					Log.d("HighscoreHandler", e.getMessage());
				} finally {
					try{
						if (in != null){
							in.close();
						}
					} catch (IOException e){
						Log.d("HighscoreHandler", e.getMessage());
					}
				}
			}
			
		});
		networkThread.start();
	}
	
	/**
	 * Appends to the json string the given line.
	 * @param line The line to be appended.
	 */
	public static void appendJSON(String line){
		jsonString += line;
	}
	
	/**
	 * Transforms the json string to the given ArrayList<String> object
	 * @param target The list to be transformed.
	 */
	public static void transformJSON(ArrayList<String> target){
		JSONArray jsonArray = null;
		
		try {
			jsonArray = new JSONArray(jsonString);
		} catch (JSONException e) {
			Log.d("HighscoreHandler", e.getMessage());
		}
		
		for (int i = 0; i < jsonArray.length(); i++){
			try {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String nick = jsonObject.getString("nick");
				int score = jsonObject.getInt("score");
				int streak = jsonObject.getInt("streak");
				
				String row = nick + " " + String.valueOf(score) + " " + String.valueOf(streak);
				target.add(row);
			} catch (JSONException e) {
				Log.d("HighscoreHandler", e.getMessage());
			}
		}
	}
	
	/**
	 * Returns true if json is fetched, false otherwise.
	 * @return True if json is fetched, false otherwise.
	 */
	public static boolean isJSONFetched(){
		return jsonFetched;
	}
	
	/**
	 * Returns true if the given context can access highscores.
	 * @param context The given context.
	 * @return True if the given context can access highscores.
	 */
	public static boolean canAccesHighscores(Context context){
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}

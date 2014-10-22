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
import android.widget.ListView;

public class HighscoreHandler {
	private String name;
	private int score;
	private int streak;
	private static String jsonString = "";
	private static boolean jsonFetched = false;
	
	public HighscoreHandler(String name, int score, int streak){
		this.name = name;
		this.score = score;
		this.streak = streak;
	}
	
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
	
	public static void appendJSON(String line){
		jsonString += line;
		Log.d("HighscoreHandler", jsonString);
	}
	
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
				Log.d("HighscoreHandler", row);
				target.add(row);
			} catch (JSONException e) {
				Log.d("HighscoreHandler", e.getMessage());
			}
		}
	}
	
	public static boolean isJSONFetched(){
		return jsonFetched;
	}
	
	public static boolean canAccesHighscores(Context context){
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}

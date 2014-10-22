package fi.tamk.tiko.joutsijoki.bugsmash;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class HighscoreActivity extends Activity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscoreview);
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		fetchScoreList();
	}
	
	public void fetchScoreList(){
		if (HighscoreHandler.canAccesHighscores(this)){
			HighscoreHandler.fetchScoreList();
			ArrayList<String>highScoreList = new ArrayList<String>();
			
			boolean fetched = false;
			while (!fetched){
				if (HighscoreHandler.isJSONFetched()){
					Log.d("HighscoreActivity", "JSON ON FETCHATTU");
					HighscoreHandler.transformJSON(highScoreList);
					fetched = true;
				}
			}
			TableLayout tl = (TableLayout)findViewById(R.id.hiScoreTable);
			TableRow firstRow = new TableRow(this);
			firstRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			
			TextView nickHeader = new TextView(this);
			nickHeader.setText("Player");
			nickHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(nickHeader, (new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f)));
			
			TextView scoreHeader = new TextView(this);
			scoreHeader.setText("Score");
			scoreHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(scoreHeader, (new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f)));
			
			TextView streakHeader = new TextView(this);
			streakHeader.setText("Streak");
			streakHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(streakHeader, (new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,1f)));
			
			tl.addView(firstRow);
			
			for (String row : highScoreList){
				TableRow newRow = new TableRow(this);
				newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			
				String [] items = row.split(" ");
				
				TextView nick = new TextView(this);
				nick.setText(items[0]);
				newRow.addView(nick, (new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
				
				TextView score = new TextView(this);
				score.setText(items[1]);
				newRow.addView(score, (new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
				
				TextView streak = new TextView(this);
				streak.setText(items[2]);
				newRow.addView(streak, (new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,0.8f)));
				
				tl.addView(newRow);
			}
		} else {
			Toast.makeText(this, "An internet connection is required", Toast.LENGTH_LONG).show();
		}
	}
}

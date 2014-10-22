/**
 * @author Sami Joutsijoki
 * The class that displays the high scores.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class HighscoreActivity extends Activity {
	
	/**
	 * The first column horizontal layout weight.
	 */
	private float firstColumnWeight = 0.1f;
	
	/**
	 * The second column horizontal layout weight.
	 */
	private float secondColumnWeight = 0.4f;
	
	/**
	 * The third column horizontal layout weight.
	 */
	private float thirdColumnWeight = 0.3f;
	
	/**
	 * The fourth column horizontal layout weight.
	 */
	private float fourthColumnWeight = 0.2f;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.highscoreview);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		fetchScoreList();
	}
	
	/**
	 * Fetches the score list and transforms it to a visible table.
	 */
	public void fetchScoreList(){
		if (HighscoreHandler.canAccesHighscores(this)){
			HighscoreHandler.fetchScoreList();
			ArrayList<String>highScoreList = new ArrayList<String>();
			
			boolean fetched = false;
			while (!fetched){
				if (HighscoreHandler.isJSONFetched()){
					HighscoreHandler.transformJSON(highScoreList);
					fetched = true;
				}
			}
			TableLayout tl = (TableLayout)findViewById(R.id.hiScoreTable);
			TableRow firstRow = new TableRow(this);
			firstRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			
			TextView rankingHeader = new TextView(this);
			rankingHeader.setText("Rank");
			rankingHeader.setGravity(Gravity.CENTER);
			rankingHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(rankingHeader, (new TableRow.LayoutParams(0
																	  , TableRow.LayoutParams.WRAP_CONTENT
																	  , firstColumnWeight)));
			
			TextView nickHeader = new TextView(this);
			nickHeader.setText("Player");
			nickHeader.setGravity(Gravity.CENTER);
			nickHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(nickHeader, (new TableRow.LayoutParams(0
																   , TableRow.LayoutParams.WRAP_CONTENT
																   , secondColumnWeight)));
			
			TextView scoreHeader = new TextView(this);
			scoreHeader.setText("Score");
			scoreHeader.setGravity(Gravity.CENTER);
			scoreHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(scoreHeader, (new TableRow.LayoutParams(0
																	, TableRow.LayoutParams.WRAP_CONTENT
																	, thirdColumnWeight)));
			
			TextView streakHeader = new TextView(this);
			streakHeader.setText("Streak");
			streakHeader.setGravity(Gravity.CENTER);
			streakHeader.setTextColor(Color.parseColor("#FF1234"));
			firstRow.addView(streakHeader, (new TableRow.LayoutParams(0
																	 , TableRow.LayoutParams.WRAP_CONTENT
																	 , fourthColumnWeight)));
			
			tl.addView(firstRow);
			
			int ranking = 1;
			for (String row : highScoreList){
				TableRow newRow = new TableRow(this);
				newRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
				String [] items = row.split(" ");
				
				TextView rankingNumber = new TextView(this);
				rankingNumber.setText(String.valueOf(ranking));
				rankingNumber.setGravity(Gravity.CENTER);
				newRow.addView(rankingNumber, (new TableRow.LayoutParams(0
																		, TableRow.LayoutParams.WRAP_CONTENT
																		, firstColumnWeight)));
				
				TextView nick = new TextView(this);
				nick.setText(items[0]);
				nick.setGravity(Gravity.CENTER);
				newRow.addView(nick, (new TableRow.LayoutParams(0
															   , TableRow.LayoutParams.WRAP_CONTENT
															   , secondColumnWeight)));
				
				TextView score = new TextView(this);
				score.setText(items[1]);
				score.setGravity(Gravity.CENTER);
				newRow.addView(score, (new TableRow.LayoutParams(0
																, TableRow.LayoutParams.WRAP_CONTENT
																, thirdColumnWeight)));
				
				TextView streak = new TextView(this);
				streak.setText(items[2]);
				streak.setGravity(Gravity.CENTER);
				newRow.addView(streak, (new TableRow.LayoutParams(0
																 , TableRow.LayoutParams.WRAP_CONTENT
																 , fourthColumnWeight)));
				
				setStyle(rankingNumber, nick, score, streak, ranking);
				
				tl.addView(newRow);
				ranking++;
			}
		} else {
			Toast.makeText(this, "An internet connection is required", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Sets the style for each row in the high score table 
	 * based on the given rank.
	 * @param rankingView The ranking view to be styled.
	 * @param nick The nickname view to be styled.
	 * @param score The score view to be styled.
	 * @param streak The streak view to be styled.
	 * @param ranking The rank that determines the style.
	 */
	private void setStyle(TextView rankingView
						  , TextView nick
						  , TextView score
						  , TextView streak
						  , int ranking){
		int color = 0;
		float size = 0f;
		if (ranking == 1){
			color = Color.parseColor("#FFD700");
			size = 25f;
		} else if (ranking == 2){
			color = Color.parseColor("#C0C0C0");
			size = 22f;
		} else if (ranking == 3){
			color = Color.parseColor("#cd7f32");
			size = 20f;
		} else if (ranking > 3 && ranking <= 50){
			color = Color.parseColor("#006887");
			size = 15f;
		} else {
			color = Color.parseColor("#FFFFFF");
			size = 10f;
		}
		rankingView.setTextSize(size);
		rankingView.setTextColor(color);
		
		nick.setTextColor(color);
		nick.setTextSize(size);
		
		score.setTextColor(color);
		score.setTextSize(size);
		
		streak.setTextColor(color);
		streak.setTextSize(size);
	}
}

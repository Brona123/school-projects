/**
 * @author Sami Joutsijoki
 * The game activity class that holds game arena fragment and score fragment.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements ScoreChangedListener
													  , MapCompletedListener
													  , MapProgressChanged
													  , GameCompletedListener {
	
	/**
	 * Displays the current factor/streak.
	 */
	private TextView factorView;
	
	/**
	 * Displays the current score.
	 */
	private TextView scoreView;
	
	/**
	 * Displays the current progress of the stage.
	 */
	private ProgressBar mapProgressBar;
	
	/**
	 * The factor/streak.
	 */
	private int factor;
	
	/**
	 * The stage score.
	 */
	private int stageScore;
	
	/**
	 * The total score.
	 */
	private int score;
	
	/**
	 * The current highest streak/factor.
	 */
	private int highestFactor;
	
	/**
	 * The total amount of bugs spawned.
	 */
	private int totalBugsSpawned;
	
	/**
	 * The total amount of bugs smashed.
	 */
	private int totalBugsSmashed;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		factor = 0;
		score = 0;
		highestFactor = 0;
		
		setContentView(R.layout.gamepanel);
	}

	@Override
	public void onScoreChanged() {
		factor++;
		if (factor > highestFactor){
			highestFactor = factor;
		}
		stageScore += 1 * factor;
		updateScore();
	}
	
	/**
	 * Updates the scoreview with the new score and factor/streak.
	 */
	public void updateScore(){
		if (factorView == null){
			factorView = (TextView)findViewById(R.id.factorView);
		}
		if (scoreView == null){
			scoreView = (TextView)findViewById(R.id.scoreView);
		}
		factorView.setText("Streak " + String.valueOf(factor) + "x");
		scoreView.setText("Score " + String.valueOf(stageScore));
	}
	
	/**
	 * Resets the factor/streak.
	 */
	public void resetFactor(){
		factor = 1;
		updateScore();
	}

	@Override
	public void mapCompleted(final int touchedAmount, final int bugsSpawned, final int bugsSmashed) {
		this.score += stageScore;
		this.totalBugsSpawned += bugsSpawned;
		this.totalBugsSmashed += bugsSmashed;
		final Fragment gameFragment = getFragmentManager().findFragmentById(R.id.gameArenaFragment);
		final VictoryFragment victory = (VictoryFragment)getFragmentManager().findFragmentById(R.id.victoryFragment);
		victory.setCallback(this);
		runOnUiThread(new Runnable(){
			public void run(){
				victory.getView().setVisibility(View.VISIBLE);
				victory.setResults(touchedAmount, bugsSpawned, bugsSmashed, highestFactor, stageScore);
				victory.loadAds();
				gameFragment.getView().setVisibility(View.INVISIBLE);
				
			}
		});
		
	}

	@Override
	public void progressChanged(int progress) {
		if (mapProgressBar == null){
			mapProgressBar = (ProgressBar)findViewById(R.id.mapProgressBar);
		}
		mapProgressBar.setProgress(progress);
	}
	
	/**
	 * Starts the next level.
	 */
	public void startNextLevel(){
		stageScore = 0;
		Log.d("GameActivity", "NEXT LEVEL");
		final GameArenaFragment gameFragment = (GameArenaFragment)getFragmentManager().findFragmentById(R.id.gameArenaFragment);
		gameFragment.startNextLevel();
		gameFragment.getView().setVisibility(View.VISIBLE);
	}
	
	/**
	 * Returns back to the main menu.
	 */
	public void returnToMainMenu(){
		finish();
	}

	@Override
	public void onGameCompleted() {
		setContentView(R.layout.scoresave);
		final TextView finalScoreView = (TextView) findViewById(R.id.finalScoreView);
		final TextView finalStreakView = (TextView) findViewById(R.id.finalStreakView);
		final TextView finalBugsSmashedView = (TextView) findViewById(R.id.totalBugsSmashedView);
		runOnUiThread(new Runnable(){
			public void run(){
				finalScoreView.setText(String.valueOf(score));
				finalStreakView.setText(String.valueOf(highestFactor));
				finalBugsSmashedView.setText(String.valueOf(totalBugsSmashed) + "/" + String.valueOf(totalBugsSpawned));
			}
		});
		
		final EditText nameField = (EditText) findViewById(R.id.nameEditText);
		Button submitScore = (Button) findViewById(R.id.scoreSubmitButton);
		submitScore.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				String nick = nameField.getText().toString().trim();
				if (nick.length() > 0){
					if (HighscoreHandler.canAccesHighscores(GameActivity.this)){
						HighscoreHandler hh = new HighscoreHandler(nick, score, highestFactor);
						hh.insertHighscore();
						GameActivity.super.onBackPressed();
					} else {
						Toast.makeText(GameActivity.this, "An internet connection is required", Toast.LENGTH_LONG).show();
					}
					
				} else {
					Toast.makeText(getBaseContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
				}
			}

		});
		
		Button backToMainMenuButton = (Button)findViewById(R.id.backToMainMenuButton);
		backToMainMenuButton.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				GameActivity.super.onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		final GamePanel gp = (GamePanel)findViewById(R.id.backgroundView);	
		final Fragment gameFragment = getFragmentManager().findFragmentById(R.id.gameArenaFragment);
		
		if (gameFragment.getView().getVisibility() == View.VISIBLE 
				&& (gp != null) 
				&& (gp.getVisibility() == View.VISIBLE)){
			gp.pauseGame();
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Closing Activity")
		        .setMessage("Exit game?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	finish();
		        }
	
		    })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Thread temp = new Thread(new Runnable(){
						@Override
						public void run(){
							Log.d("GameActivity", "RESUMING THREAD");
							gp.resumeGame();
						}
					});
					temp.start();
					dialog.cancel();
				}
			})
		    .show();
		}
	}
}

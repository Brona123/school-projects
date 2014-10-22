package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends Activity implements ScoreChangedListener
													  , MapCompletedListener
													  , MapProgressChanged {
	private TextView factorView;
	private TextView scoreView;
	private ProgressBar mapProgressBar;
	private int factor;
	private int score;
	private int highestFactor;
	
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
		score += 1 * factor;
		updateScore();
	}
	
	public void updateScore(){
		if (factorView == null){
			factorView = (TextView)findViewById(R.id.factorView);
		}
		if (scoreView == null){
			scoreView = (TextView)findViewById(R.id.scoreView);
		}
		factorView.setText("Streak " + String.valueOf(factor) + "x");
		scoreView.setText("Score " + String.valueOf(score));
	}
	
	public void resetFactor(){
		factor = 1;
		updateScore();
	}

	@Override
	public void mapCompleted() {
		final Fragment gameFragment = getFragmentManager().findFragmentById(R.id.gameArenaFragment);
		final VictoryFragment victory = (VictoryFragment)getFragmentManager().findFragmentById(R.id.victoryFragment);
		victory.setCallback(this);
		runOnUiThread(new Runnable(){
			public void run(){
				victory.getView().setVisibility(View.VISIBLE);
				victory.setResults(highestFactor, score);
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
	
	public void startNextLevel(){
		Log.d("GameActivity", "NEXT LEVEL");
		final GameArenaFragment gameFragment = (GameArenaFragment)getFragmentManager().findFragmentById(R.id.gameArenaFragment);
		gameFragment.startNextLevel();
		gameFragment.getView().setVisibility(View.VISIBLE);
	}
	
	public void returnToMainMenu(){
		Log.d("GameActivity", "MAIN MENU");
		finish();
	}
}

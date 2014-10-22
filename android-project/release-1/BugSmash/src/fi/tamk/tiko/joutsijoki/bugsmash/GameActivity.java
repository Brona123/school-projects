package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends Activity implements ScoreChangedListener {
	private TextView factorView;
	private TextView scoreView;
	private int factor;
	private int score;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		factor = 0;
		score = 0;
		
		setContentView(R.layout.gamepanel);
	}

	@Override
	public void onScoreChanged() {
		factor++;
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
	
}

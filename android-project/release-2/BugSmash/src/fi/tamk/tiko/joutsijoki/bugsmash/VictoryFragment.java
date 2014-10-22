package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class VictoryFragment extends Fragment implements OnClickListener{
	private Button continueButton;
	private Button mainMenuButton;
	private GameActivity callback;
	private TextView streakView;
	private TextView scoreView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
							 ViewGroup container, 
							 Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.win,
									container,
									false);
		continueButton = (Button)view.findViewById(R.id.victoryContinueButton);
		mainMenuButton = (Button)view.findViewById(R.id.victoryMainMenuButton);
		streakView = (TextView)view.findViewById(R.id.victoryStreakView);
		scoreView = (TextView)view.findViewById(R.id.victoryScoreView);
		
		continueButton.setOnClickListener(this);
		mainMenuButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View src) {
		if (src == continueButton){
			Log.d("Victory", "CONTINUE");
			this.getView().setVisibility(View.INVISIBLE);
			callback.startNextLevel();
		} else if (src == mainMenuButton){
			Log.d("Victory", "MAINMENU");
			this.getView().setVisibility(View.INVISIBLE);
			callback.returnToMainMenu();
		}
		
	}
	
	public void setCallback(GameActivity host){
		this.callback = host;
	}
	
	public void setResults(int highestFactor, int score){
		streakView.setText("Your highest streak was: " + String.valueOf(highestFactor));
		scoreView.setText("Stage score: " + String.valueOf(score));
	}
}

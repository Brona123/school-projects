/**
 * @author Sami Joutsijoki
 * The main activity of the app. Holds the main menu.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	/**
	 * The button that starts the game.
	 */
	private Button startButton;
	
	/**
	 * The button that starts the highscore activity.
	 */
	private Button hiScoreButton;
	
	/**
	 * The button that starts the credits activity.
	 */
	private Button creditsButton;
	
	/**
	 * The button that exits the game.
	 */
	private Button exitButton;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		initButtons();
	}
	
	/**
	 * Initializes the buttons and sets onclicklisteners.
	 */
	private void initButtons(){
		startButton = (Button)findViewById(R.id.startGame);
		hiScoreButton = (Button)findViewById(R.id.hiScore);
		creditsButton = (Button)findViewById(R.id.creditsButton);
		exitButton = (Button)findViewById(R.id.exit);
		
		startButton.setOnClickListener(this);
		hiScoreButton.setOnClickListener(this);
		creditsButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View src) {
		
		if (src == startButton){
			Intent i = new Intent(this, GameActivity.class);
			startActivity(i);
		} else if (src == hiScoreButton){
			Intent i = new Intent(this, HighscoreActivity.class);
			startActivity(i);
		} else if (src == exitButton){
			finish();
		} else if (src == creditsButton){
			Intent i = new Intent(this, CreditsActivity.class);
			startActivity(i);
		}
	}
}

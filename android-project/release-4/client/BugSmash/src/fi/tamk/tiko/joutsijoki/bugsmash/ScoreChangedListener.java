/**
 * @author Sami Joutsijoki
 * The callback interface that handles the score changed event.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

public interface ScoreChangedListener {
	
	/**
	 * Called when the score changes.
	 */
	public void onScoreChanged();
	
	/**
	 * Called when the streak/factor should be reset.
	 */
	public void resetFactor();
}

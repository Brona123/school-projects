/**
 * @author Sami Joutsijoki
 * An interface used to communicate between fragments, 
 * called when the game is completed.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

public interface GameCompletedListener {
	/**
	 * Called when the game is completed.
	 */
	public void onGameCompleted();
}

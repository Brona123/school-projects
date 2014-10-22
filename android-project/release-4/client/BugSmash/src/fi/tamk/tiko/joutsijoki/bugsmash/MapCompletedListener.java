/**
 * @author Sami Joutsijoki
 * The callback interface to control map/stage completion event.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

public interface MapCompletedListener {
	
	/**
	 * Called when the map/stage is completed.
	 * @param touchedAmount The amount of times user touched the screen.
	 * @param bugsSpawned The total amount of bugs spawned.
	 * @param bugsSmashed The total amount of bugs smashed.
	 */
	public void mapCompleted(int touchedAmount, int bugsSpawned, int bugsSmashed);
}

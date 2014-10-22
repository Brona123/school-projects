/**
 * @author Sami Joutsijoki
 * The callback interface to display map/stage progress.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

public interface MapProgressChanged {
	
	/**
	 * Called when the map/stage progress is changed.
	 * @param progress The current progress.
	 */
	public void progressChanged(int progress);
}

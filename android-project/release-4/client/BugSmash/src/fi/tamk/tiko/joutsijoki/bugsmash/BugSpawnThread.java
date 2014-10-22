/**
 * @author Sami Joutsijoki
 * This class handles the bug spawning logic in a different thread.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import java.util.ArrayList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class BugSpawnThread extends Thread {
	
	/**
	 * The boolean deciding if the thread is running or not.
	 * False by default.
	 */
	private boolean running = false;
	
	/**
	 * The resources object where the images are decoded from.
	 */
	private Resources res;
	
	/**
	 * Contains all the spawned bugs.
	 */
	private ArrayList<Bug> bugContainer;
	
	/**
	 * The amount of bugs to be spawned at a time.
	 */
	private int bugAmount = 20;
	
	/**
	 * Indicates the width of the spawning area.
	 */
	private int areaWidth;
	
	/**
	 * Indicates the height of the spawning area.
	 */
	private int areaHeight;
	
	/**
	 * Indicates the interval at when to spawn bugs.
	 */
	private int spawnInterval;
	
	/**
	 * Indicates if the thread is paused.
	 */
	private boolean paused;
	
	/**
	 * Indicates the total amount of bugs spawned.
	 */
	private int bugsSpawned;

	/**
	 * Creates a new bug spawning thread with the given spawn interval.
	 * @param spawnInterval The time between the bug spawnings.
	 */
	public BugSpawnThread(int spawnInterval){
		super();
		this.spawnInterval = spawnInterval;
		bugContainer = new ArrayList<Bug>();
	}
	
	/**
	 * Sets the spawning area width and height to the given values.
	 * @param width The width of the spawning area.
	 * @param height The height of the spawning area.
	 */
	public void setAreaDimensions(int width, int height){
		this.areaWidth = width;
		this.areaHeight = height;
	}
	
	@Override
	public void run(){
		while (areaWidth == 0 && areaHeight == 0){}
		Log.d("BugSpawnThread", "CALLED RUN");
		
		while(running){
			if (!paused){
				spawnBugs();
				Log.d("BugSpawnThread", "RUNNING");
				bugsSpawned += bugAmount;
				try {
					Thread.sleep(spawnInterval);
				} catch (InterruptedException e) {
					Log.d("BugSpawnThread", e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Spawns bugs with a certain formation, random formation is the default.
	 */
	private void spawnBugs(){
		spawnRandomFormation();
	}
	
	/**
	 * Spawns bugs at a random formation, bounded by the area width and height.
	 */
	private void spawnRandomFormation(){
		int bug = (int)(Math.random()*3);
		
		if (bug == 0){
			for (int i = 0; i < bugAmount; i++){
				double x = Math.random();
				double y = Math.random();
				Bitmap img = BitmapFactory.decodeResource(res, R.drawable.ant_spritesheet_large);
				bugContainer.add(new Bug("ant"
										 , img
										 , (int)((-areaWidth - img.getWidth()) * x)
										 , (int)((areaHeight - img.getHeight()) * y)
										 , (int)(Math.random()*6)+3
										 , 2));
			}
		} else if (bug == 1){
			for (int i = 0; i < bugAmount; i++){
				double x = Math.random();
				double y = Math.random();
				Bitmap img = BitmapFactory.decodeResource(res, R.drawable.roach_spritesheet_large);
				bugContainer.add(new Bug("roach"
										 , img
										 , (int)((-areaWidth - img.getWidth()) * x)
										 , (int)((areaHeight - img.getHeight()) * y)
										 , (int)(Math.random()*6)+3
										 , 2));
			}
		} else if (bug == 2){
			for (int i = 0; i < bugAmount; i++){
				double x = Math.random();
				double y = Math.random();
				Bitmap img = BitmapFactory.decodeResource(res, R.drawable.mosquito_spritesheet);
				bugContainer.add(new Bug("mosquito"
										 , img
										 , (int)((-areaWidth - img.getWidth()) * x)
										 , (int)((areaHeight - img.getHeight()) * y)
										 , (int)(Math.random()*6)+3
										 , 3));
			}
		}
	}
	
	/**
	 * Sets the running boolean to the given flag.
	 * @param flag The flag to control the thread.
	 */
	public void setRunning(boolean flag){
		this.running = flag;
	}
	
	/**
	 * Draws all the bugs in the bug container to the given canvas.
	 * @param canvas The canvas to be drawn to.
	 */
	public void drawBugs(Canvas canvas){
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				temp.draw(canvas);
			}
		}
	}
	
	/**
	 * Moves all the bugs in the bug container accordingly
	 * and manages the destroying of bugs if they get out of area bounds.
	 * @param leftBorder The left side of the viewport.
	 * @param rightBorder The right side of the viewport.
	 * @param backgroundScrolling Indicates if the background is scrolling.
	 */
	public void moveBugs(int leftBorder, int rightBorder, boolean backgroundScrolling){
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				temp.update(System.currentTimeMillis());
				if (!temp.isDead()){
					if (temp.getPosX() > rightBorder){
						temp.destroy();
						bugContainer.remove(temp);
					} else {
						temp.setPosX(temp.getPosX() + temp.getMovementSpeed()/3);
					}
				} else {
					
					if (backgroundScrolling){
						temp.setPosX(temp.getPosX() - 1);
					}
					
					if (temp.getPosX() + temp.getWidth() + leftBorder < leftBorder){
						temp.destroy();
						bugContainer.remove(temp);
					}
				}
			}
		}
	}
	
	/**
	 * Checks the collision of all the bugs in the bug container
	 * with the given coordinates and updates the score accordingly.
	 * @param x The x-coordinate to be checked.
	 * @param y The y-coordinate to be checked.
	 * @param host The callback for updating the score.
	 * @return true if collision happened, false otherwise.
	 */
	public boolean checkCollision(float x, float y, ScoreChangedListener host){
		boolean hit = false;
		for (int i = 0; i < bugContainer.size(); i++){
			Bug temp;
			if ((temp = bugContainer.get(i)) != null){
				if (temp.collidesWith(x, y) && !temp.isDead()){
					temp.kill(res);
					host.onScoreChanged();
					hit = true;
				}
			}
		}
		return hit;
	}
	
	/**
	 * Returns the bug container.
	 * @return The bug container.
	 */
	public ArrayList<Bug> getBugContainer(){
		return this.bugContainer;
	}
	
	/**
	 * Sets the bug amount to the given value.
	 * @param amount The amount of bugs to spawn.
	 */
	public void setBugAmount(int amount){
		this.bugAmount = amount;
	}
	
	/**
	 * Sets the resources object to the given value.
	 * @param res The resources object.
	 */
	public void setResources(Resources res){
		this.res = res;
	}
	
	/**
	 * Cleans up the bug container.
	 */
	public void cleanUp(){
		bugContainer.clear();
		System.gc();
	}
	
	/**
	 * Pauses the thread.
	 */
	public void pauseThread(){
		this.paused = true;
	}
	
	/**
	 * Resumes the thread.
	 */
	public void resumeThread(){
		this.paused = false;
	}
	
	/**
	 * Returns the amount of bugs spawned.
	 * @return The amount of bugs spawned.
	 */
	public int getBugsSpawned(){
		return this.bugsSpawned;
	}
	
	/**
	 * Resets the amount of bugs spawned to 0.
	 */
	public void resetBugsSpawned(){
		this.bugsSpawned = 0;
	}
}

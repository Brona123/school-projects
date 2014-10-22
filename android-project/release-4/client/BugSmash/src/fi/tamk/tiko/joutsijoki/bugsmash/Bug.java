/**
 * @author Sami Joutsijoki
 * This class is a helper class to handle all the bug related logic.
 */
package fi.tamk.tiko.joutsijoki.bugsmash;

import android.content.res.Resources;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Bug {
	
	/**
	 * The x-coordinate of the bug.
	 */
	private int posX;
	
	/**
	 * The y-coordinate of the bug.
	 */
	private int posY;
	
	/**
	 * The width of the bug's image.
	 */
	private int width;
	
	/**
	 * The height of the bug's image.
	 */
	private int height;

	/**
	 * The image/spritesheet of the bug.
	 */
    private Bitmap image;
    
    /**
     * The rectangle to be drawn from the image/spritesheet.
     */
    private Rect sourceRect;
    
    /**
     * The number of frames in the spritesheet.
     */
    private int frameNr;
    
    /**
     * The current frame of the spritesheet.
     */
    private int currentFrame;
    
    /**
     * The time of the last frame update.
     */
    private long frameTicker;
    
    /**
     * The milliseconds between each frame (1000 / fps).
     */
    private int framePeriod;
    
    /**
     * The width of the spritesheet.
     */
    private int spriteWidth;
    
    /**
     * The height of the spritesheet.
     */
    private int spriteHeight;
    
    /**
     * The tag used for debugging.
     */
    private static final String TAG = "Bug";
    
    /**
     * The name of the bug.
     */
    private String name;
    
    /**
     * Indicates if the bug is dead or alive.
     * False by default.
     */
    private boolean dead = false;
    
    /**
     * Indicates the speed of the bug.
     */
    private int movementSpeed;
	
    /**
     * Creates a new bug with the given parameters.
     * @param name The name of the bug.
     * @param spritesheet The spritesheet used for animation.
     * @param x The starting x-coordinate.
     * @param y The starting y-coordinate.
     * @param fps The fps of the animation sequence.
     * @param frameCount The amount of frames in the spritesheet.
     */
	public Bug(String name, Bitmap spritesheet, int x, int y, int fps, int frameCount){
		this.name = name;
		this.image = spritesheet;
        this.posX = x;
        this.posY = y;
        currentFrame = 0;
        frameNr = frameCount;
        spriteWidth = spritesheet.getWidth() / frameCount;
        spriteHeight = spritesheet.getHeight();
        this.width = spriteWidth;
        this.height = spriteHeight;
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
        framePeriod = 1000 / fps;
        frameTicker = 0l;
        this.movementSpeed = fps;
	}
	
	/**
	 * Switches the current rectangle of the spritesheet to be drawn.
	 * @param gameTime Used to control a constant fps.
	 */
	public void update(long gameTime) {
	    if (gameTime > frameTicker + framePeriod) {
	        frameTicker = gameTime;
	        currentFrame++;
	        if (currentFrame >= frameNr) {
	            currentFrame = 0;
	        }
	    }
	    this.sourceRect.left = currentFrame * spriteWidth;
	    this.sourceRect.right = this.sourceRect.left + spriteWidth;
	}

	/**
	 * Sets the bug's x-coordinate to the given value.
	 * @param x The new x-coordinate of the bug.
	 */
	public void setPosX(int x){
		this.posX = x;
	}
	
	/**
	 * Returns the current x-coordinate of the bug.
	 * @return The current x-coordinate of the bug.
	 */
	public int getPosX(){
		return this.posX;
	}
	
	/**
	 * Sets the y-coordinate of the bug to the given value.
	 * @param y The new y-coordinate.
	 */
	public void setPosY(int y){
		this.posY = y;
	}
	
	/**
	 * Returns the current y-coordinate of the bug.
	 * @return The current y-coordinate of the bug.
	 */
	public int getPosY(){
		return this.posY;
	}
	
	/**
	 * Draws the bug to the given canvas.
	 * @param canvas The canvas to be drawn to.
	 */
	public void draw(Canvas canvas) {
		if (!this.image.isRecycled() && canvas != null){
			
			Rect destRect = new Rect(posX, posY, posX + spriteWidth, posY + spriteHeight);
			try {
				if (!dead){
					canvas.drawBitmap(this.image, sourceRect, destRect, null);
				} else {
					canvas.drawBitmap(this.image, posX, posY, null);
				}
			} catch (RuntimeException e){
				Log.d(TAG, "TRYING TO USE RECYCLED BITMAP");
			}
		}
    }

	/**
	 * Returns true if the bug collides with the given coordinates.
	 * @param x The x-coordinate to be checked.
	 * @param y The y-coordinate to be checked.
	 * @return true if bug collides and it's image is not recycled, 
	 * false otherwise.
	 */
	public boolean collidesWith(float x, float y){
		if (x > posX
		    && x < posX + this.width
		    && y > posY
		    && y < posY + this.height){
			
			if (!image.isRecycled()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Destroys the resource used by this object by recycling it's image.
	 */
	public void destroy(){
		this.image.recycle();
	}
	
	/**
	 * Sets the current image of the bug to the corresponding death image.
	 * @param res The resources object where the death image is decoded from.
	 */
	public void kill(Resources res){
		if (this.name == "ant"){
			this.image = BitmapFactory.decodeResource(res, R.drawable.ant_death_large);
			this.spriteWidth = this.image.getWidth();
			this.spriteHeight = this.image.getHeight();
		} else if (this.name == "roach"){
			this.image = BitmapFactory.decodeResource(res, R.drawable.roach_death_large);
			this.spriteWidth = this.image.getWidth();
			this.spriteHeight = this.image.getHeight();
		} else if (this.name == "mosquito"){
			this.image = BitmapFactory.decodeResource(res, R.drawable.mosquito_death);
			this.spriteWidth = this.image.getWidth();
			this.spriteHeight = this.image.getHeight();
		}
		this.frameNr = 1;
		this.dead = true;
	}
	
	/**
	 * Returns true if the bug is dead, false otherwise.
	 * @return True if the bug is dead, false otherwise.
	 */
	public boolean isDead(){
		return this.dead;
	}
	
	/**
	 * Returns the width of one sprite in the spritesheet.
	 * @return The width of one sprite in the spritesheet.
	 */
	public int getWidth(){
		return (this.spriteWidth / this.frameNr);
	}
	
	/**
	 * Returns the movement speed of the bug.
	 * @return The movement speed of the bug.
	 */
	public int getMovementSpeed(){
		return this.movementSpeed;
	}
}

/**
 * @author Sami Joutsijoki
 * This class is a helper class for a scrolling background.
 */

package fi.tamk.tiko.joutsijoki.bugsmash;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Background {
	
	/**
	 * Indicates the x-coordinate of where the drawn background will start.
	 */
	private int startX;
	
	/**
	 * Indicates the y-coordinate of where the drawn background will start.
	 */
	private int startY;
	
	/**
	 * Indicates the width of the image used for background.
	 */
	private int width;
	
	/**
	 * Indicates the amount of offset between viewport and 
	 * the background starting x-coordinate.
	 */
	private int offsetX;
	
	/**
	 * The image used as a background.
	 */
	private Bitmap image;
	
	/**
	 * Indicates if the background is scrolling.
	 * False by default.
	 */
	private boolean scrolling = false;
	
	/**
	 * Indicates the amount of pixels the background scrolls
	 * per update if scrolling is true.
	 */
	private int scrollingSpeed;
	
	/**
	 * The tag used for debugging.
	 */
	private static final String TAG = "Background";
	
	/**
	 * The constructor that takes the image and screen height as parameters.
	 * Also scales the image to the right screen height.
	 * @param image The image used as a background.
	 * @param screenHeight The height used for scaling the image to right height.
	 */
	public Background(Bitmap image, int screenHeight){
		this.image = image;
		this.startX = 0;
		this.startY = 0;
		this.width = image.getWidth();
		scaleImage(screenHeight);
	}
	
	/**
	 * Sets the background scrolling speed.
	 * @param speed The amount of pixels to scroll per update.
	 */
	public void setScrollingSpeed(int speed){
		this.scrollingSpeed = speed;
	}
	
	/**
	 * Returns the scrolling speed.
	 * @return The scrolling speed.
	 */
	public int getScrollingSpeed(){
		return this.scrollingSpeed;
	}
	
	/**
	 * Sets the scrolling to true or false.
	 * @param flag The flag to control scrolling.
	 */
	public void setScrolling(boolean flag){
		this.scrolling = flag;
	}
	
	/**
	 * Returns true if background is scrolling, false otherwise.
	 * @return True if background is scrolling, false otherwise.
	 */
	public boolean getScrolling(){
		return this.scrolling;
	}
	
	/**
	 * Scales the image used as a background, to the correct screen height.
	 * @param sHeight The screen height.
	 */
	private void scaleImage(int sHeight){
		this.image = Bitmap.createScaledBitmap(image, width, sHeight, true);
	}
	
	/**
	 * Draws the background to the given canvas, and scrolls it if set to
	 * scrolling.
	 * @param canvas The canvas to be drawn to.
	 */
	public void draw(Canvas canvas) {
		if (canvas != null && !image.isRecycled()){
			try {
				canvas.drawBitmap(image, startX, startY, null);
			} catch (RuntimeException e){
				Log.d(TAG, e.getMessage());
			}
		}
        
        if (scrolling){
	        startX -= scrollingSpeed;
	        offsetX += scrollingSpeed;
        }
    }
	
	/**
	 * Returns the x-coordinate of the background.
	 * @return The x-coordinate of the background.
	 */
	public int getX(){
		return this.startX;
	}
	
	/**
	 * Returns the offset of the viewport in relation to the background.
	 * @return The offset of the viewport in relation to the background.
	 */
	public int getOffsetX(){
		return this.offsetX;
	}
	
	/**
	 * Returns the width of the image used as a background.
	 * @return The width of the image used as a background.
	 */
	public int getWidth(){
		return this.width;
	}
	
	/**
	 * Cleans up the resource used by the object by recycling the image.
	 */
	public void cleanUp(){
		this.image.recycle();
	}
}

package fi.tamk.tiko.joutsijoki.bugsmash;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
	private int startX;
	private int startY;
	private int width;
	private int height;
	private int offsetX;
	private Bitmap image;
	private boolean scrolling = false;
	private int scrollingSpeed;
	
	public Background(Bitmap image, int screenHeight){
		this.image = image;
		this.startX = 0;
		this.startY = 0;
		this.width = image.getWidth();
		this.height = image.getHeight();
		scaleImage(screenHeight);
	}
	
	public void setScrollingSpeed(int speed){
		this.scrollingSpeed = speed;
	}
	
	public int getScrollingSpeed(){
		return this.scrollingSpeed;
	}
	
	public void setScrolling(boolean flag){
		this.scrolling = flag;
	}
	
	public boolean getScrolling(){
		return this.scrolling;
	}
	
	private void scaleImage(int sHeight){
		this.image = Bitmap.createScaledBitmap(image, width, sHeight, true);
	}
	
	public void draw(Canvas canvas) {
        canvas.drawBitmap(image, startX, startY, null);
        
        if (scrolling){
        	
	        startX -= scrollingSpeed;
	        offsetX += scrollingSpeed;
        }
    }
	
	public int getX(){
		return this.startX;
	}
	
	public int getOffsetX(){
		return this.offsetX;
	}
	
	public int getWidth(){
		return this.width;
	}
	
}

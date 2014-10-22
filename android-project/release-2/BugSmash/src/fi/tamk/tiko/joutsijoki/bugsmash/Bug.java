package fi.tamk.tiko.joutsijoki.bugsmash;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Bug {
	private int posX;
	private int posY;
	private int width;
	private int height;
	private int degree;

    private Bitmap image;      // the animation sequence
    private Rect sourceRect;    // the rectangle to be drawn from the animation bitmap
    private int frameNr;        // number of frames in animation
    private int currentFrame;   // the current frame
    private long frameTicker;   // the time of the last frame update
    private int framePeriod;    // milliseconds between each frame (1000/fps)
    private int spriteWidth;    // the width of the sprite to calculate the cut out rectangle
    private int spriteHeight;   // the height of the sprite
	
	public Bug(Bitmap image, int startX, int startY){
		this.image = image;
		this.posX = startX;
		this.posY = startY;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.degree = 0;
	}
	
	public Bug(Bitmap spritesheet, int x, int y, int fps, int frameCount){
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
	}
	
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

	
	public void setPosX(int x){
		this.posX = x;
	}
	
	public int getPosX(){
		return this.posX;
	}
	
	public void setPosY(int y){
		this.posY = y;
	}
	
	public int getPosY(){
		return this.posY;
	}
	
	public void draw(Canvas canvas) {
		if (canvas != null && !image.isRecycled()){
			Rect destRect = new Rect(posX, posY, posX + spriteWidth, posY + spriteHeight);
        	canvas.drawBitmap(image, sourceRect, destRect, null);
        }
    }

	
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
	
	public void destroy(){
		this.image.recycle();
	}
	
	public void rotate(){
	    degree++;
    	Matrix matrix = new Matrix();
	    matrix.postRotate(degree/100);
    	this.image = Bitmap.createBitmap(this.image
							    		, 0
							    		, 0
							    		, image.getWidth()
							    		, image.getHeight()
							    		, matrix
							    		, true);
	    
	}
}

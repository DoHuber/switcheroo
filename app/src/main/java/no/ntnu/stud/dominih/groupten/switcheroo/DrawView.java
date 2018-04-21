package no.ntnu.stud.dominih.groupten.switcheroo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * Created by Wolfer on 21/04/2018.
 */

public class DrawView extends View{

    public int width;
    public int height;
    private Bitmap myBitmap;
    private Canvas myCanvas;
    private Paint myPaint;
    private Path myPath;
    private float pointX,pointY;
    private static final float TOLERANCE = 5;

    public DrawView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        myPath = new Path();
        myPaint = new Paint();
        myPaint.setAntiAlias(true);
        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeJoin(Paint.Join.ROUND);
        myPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(myPath,myPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        myBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(myBitmap);
    }
    private void onStartTouch(float x, float y){
        myPath.moveTo(x,y);
        pointX = x;
        pointY = y;
    }

    private void moveTouch(float x,float y){
        float difX = Math.abs(x-pointX);
        float difY = Math.abs(y-pointY);
        if(difX >= TOLERANCE || difY>=TOLERANCE){
            myPath.quadTo(pointX,pointY,(x+pointX)/2,(y+pointY)/2);
            pointX = x;
            pointY = y;
        }
    }
    public void clearCanvas(){
        myPath.reset();
        invalidate();
    }
    private void upTouch(){
        myPath.lineTo(pointX,pointY);
    }

    public byte[] toJPEG(){
        ByteArrayOutputStream jpeg = new ByteArrayOutputStream();
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,jpeg);
        return jpeg.toByteArray();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x= event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onStartTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}

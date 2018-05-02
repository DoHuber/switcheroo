package no.ntnu.stud.dominih.groupten.switcheroo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wolfer on 21/04/2018.
 */

public class DrawView extends View {

    public static final int BLACK =1;
    public static final int RED =2;
    public static final int YELLOW =3;
    public static final int GREEN =4;
    public static final int BLUE =5;



    private final Paint myPaint;
    private Path myPath;
    private float pointX, pointY;
    private static final float TOLERANCE = 5;

    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Path> undonePaths = new ArrayList<>();
    public static int selectedColor=Color.BLACK;
    private Map<Path, Integer> colorsMap = new HashMap<>();

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        selectedColor = Color.BLACK;
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

        for (Path p : paths) {
            myPaint.setColor(colorsMap.get(p));
            canvas.drawPath(p, myPaint);
        }
        myPaint.setColor(selectedColor);
        canvas.drawPath(myPath, myPaint);

    }

    private void onStartTouch(float x, float y) {
        myPath.reset();
        myPath.moveTo(x, y);
        pointX = x;
        pointY = y;
    }

    private void moveTouch(float x, float y) {
        float difX = Math.abs(x - pointX);
        float difY = Math.abs(y - pointY);
        if (difX >= TOLERANCE || difY >= TOLERANCE) {
            myPath.quadTo(pointX, pointY, (x + pointX) / 2, (y + pointY) / 2);
            pointX = x;
            pointY = y;
        }
    }

    public void clearCanvas() {
        if (myPath != null) {
            paths.clear();
        }
        invalidate();
    }

    private void upTouch() {
        myPath.lineTo(pointX, pointY);
        paths.add(myPath);
        colorsMap.put(myPath, selectedColor);
        myPath = new Path();
        myPath.reset();
        invalidate();    }

    public byte[] getByteArray() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        getBitmap().compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return outputStream.toByteArray();
    }

    public void onClickUndo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        } else {

        }

    }

    public void setColor(int color){

        switch (color){
            case BLACK:
                selectedColor =Color.BLACK;

                break;
            case RED:
                selectedColor =Color.RED;
                break;
            case YELLOW:
                selectedColor =Color.YELLOW;
                break;

            case GREEN:
                selectedColor =Color.GREEN;
                break;
            case BLUE:
                selectedColor =Color.BLUE;
                break;
        }

    }

    private Bitmap getBitmap() {

        int width = this.getWidth();
        int height = this.getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        Paint white = new Paint();
        white.setColor(Color.WHITE);

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0f, 0f, (float) width, (float) height, white);
        this.draw(canvas);

        canvas.drawRect(0f, 0f, (float) width, 5f, paint);
        canvas.drawRect(0f, (float) (height - 5), (float) width, (float) height, paint);
        canvas.drawRect(0f, 0f, 5f, (float) height, paint);
        canvas.drawRect((float) (width - 5), 0f, (float) width, (float) height, paint);

        return bitmap;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onStartTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
            default:
                    performClick();
        }
        return true;
    }
}

package no.ntnu.stud.dominih.groupten.switcheroo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Enables exporting a list of GameTransactions to a single Bitmap to send to the other
 * players and to share via Social Media.
 *
 *
 * @author Dominik Huber
 */
public class ImageExporter {

    public Bitmap exportToBitmap(List<GameTransaction> transactions) {

        List<Bitmap> bitmaps = new ArrayList<>();

        for (GameTransaction element : transactions) {

            if (element.type.equals(GameTransaction.TYPE_TEXT)) {

                Log.d("ImageExport", "Processing text payload: " + element.payload);
                bitmaps.add(textToBitmap(element.payload));

            } else if (element.type.equals(GameTransaction.TYPE_IMG)) {

                Log.d("ImageExport", "Processing img payload.");
                bitmaps.add(base64toBitmap(element.payload));

            }

        }

        return stitchBitmaps(bitmaps);
    }

    // Slightly modified from: https://stackoverflow.com/questions/8799290/convert-string-text-to-bitmap?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    private Bitmap textToBitmap(String text) {

        Bitmap bitmap = Bitmap.createBitmap(600, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint white = new Paint();
        white.setColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLUE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(64.0f);
        StaticLayout sl= new StaticLayout(text, textPaint, bitmap.getWidth()-8, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        canvas.translate(6, 40);
        canvas.drawRect(0f, 0f, 600f, 200f, white);
        sl.draw(canvas);
        return bitmap;

    }

    private Bitmap base64toBitmap(String base64) {

        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

    }

    private Bitmap stitchBitmaps(List<Bitmap> bitmaps) {

        int width = 0;
        int height = 0;

        for (Bitmap element: bitmaps) {

            if (element.getWidth() > width) {

                width = element.getWidth();

            }

            height += element.getHeight();

        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int currentTop = 0;
        Canvas resultCanvas = new Canvas(result);

        for (Bitmap element: bitmaps) {

            resultCanvas.drawBitmap(element, 0.0f, (float) currentTop, null);
            currentTop += element.getHeight();
            element.recycle();

        }

        return result;

    }

}

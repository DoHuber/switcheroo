package no.ntnu.stud.dominih.groupten.switcheroo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class ImageExporter {

    public Bitmap exportToBitmap(List<GameTransaction> transactions) {

        List<Bitmap> bitmaps = new ArrayList<>();

        for (GameTransaction element : transactions) {

            if (element.type.equals(GameTransaction.TYPE_TEXT)) {

                bitmaps.add(textToBitmap(element.payload, Color.BLACK));

            } else if (element.type.equals(GameTransaction.TYPE_IMG)) {

                bitmaps.add(base64toBitmap(element.payload));

            }

        }

        return stitchBitmaps(bitmaps);
    }

    // Slightly modified from: https://stackoverflow.com/questions/8799290/convert-string-text-to-bitmap?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    private Bitmap textToBitmap(String text, int textColor) {

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(12.0f);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;

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

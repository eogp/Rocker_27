package com.ladoe.rocker.CargaAsincorinca;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by enriquegomezpena on 22/3/18.
 */

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private AppCompatActivity activity;

    public LoadImage(ImageView imageView, AppCompatActivity activity) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.activity=activity;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            return downloadBitmap(params[0]);
        } catch (Exception e) {
            Log.e("LoadImage class", "doInBackground() " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    // Convert the ImageView image to a rounded corners image with border
                    RoundedBitmapDrawable drawable = createRoundedBitmapDrawableWithBorder(bitmap);
                    // Set the ImageView image as drawable object
                    imageView.setImageDrawable(drawable);
                    //imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.e("LoadImage class", "Descargando imagen desde url: " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    //CODIGO PARA IMAGEN REDONDA
    private RoundedBitmapDrawable createRoundedBitmapDrawableWithBorder(Bitmap bitmap){
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int borderWidthHalf = 10; // In pixels
        //Toast.makeText(mContext,""+bitmapWidth+"|"+bitmapHeight,Toast.LENGTH_SHORT).show();

        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth,bitmapHeight)/2;

        int bitmapSquareWidth = Math.min(bitmapWidth,bitmapHeight);
        //Toast.makeText(mContext,""+bitmapMin,Toast.LENGTH_SHORT).show();

        int newBitmapSquareWidth = bitmapSquareWidth+borderWidthHalf;
        //Toast.makeText(mContext,""+newBitmapMin,Toast.LENGTH_SHORT).show();

        /*
            Initializing a new empty bitmap.
            Set the bitmap size from source bitmap
            Also add the border space to new bitmap
        */
        Bitmap roundedBitmap = Bitmap.createBitmap(newBitmapSquareWidth,newBitmapSquareWidth,Bitmap.Config.ARGB_8888);

        /*
            Canvas
                The Canvas class holds the "draw" calls. To draw something, you need 4 basic
                components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing
                into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint
                (to describe the colors and styles for the drawing).

            Canvas(Bitmap bitmap)
                Construct a canvas with the specified bitmap to draw into.
        */
        // Initialize a new Canvas to draw empty bitmap
        Canvas canvas = new Canvas(roundedBitmap);

        /*
            drawColor(int color)
                Fill the entire canvas' bitmap (restricted to the current clip) with the specified
                color, using srcover porterduff mode.
        */
        // Draw a solid color to canvas
        canvas.drawColor(Color.RED);

        // Calculation to draw bitmap at the circular bitmap center position
        int x = borderWidthHalf + bitmapSquareWidth - bitmapWidth;
        int y = borderWidthHalf + bitmapSquareWidth - bitmapHeight;

        /*
            drawBitmap(Bitmap bitmap, float left, float top, Paint paint)
                Draw the specified bitmap, with its top/left corner at (x,y), using the specified
                paint, transformed by the current matrix.
        */
        /*
            Now draw the bitmap to canvas.
            Bitmap will draw its center to circular bitmap center by keeping border spaces
        */
        canvas.drawBitmap(bitmap, x, y, null);

        // Initializing a new Paint instance to draw circular border
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidthHalf*2);
        borderPaint.setColor(Color.WHITE);

        /*
            drawCircle(float cx, float cy, float radius, Paint paint)
                Draw the specified circle using the specified paint.
        */
        /*
            Draw the circular border to bitmap.
            Draw the circle at the center of canvas.
         */
        canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, newBitmapSquareWidth/2, borderPaint);

        /*
            RoundedBitmapDrawable
                A Drawable that wraps a bitmap and can be drawn with rounded corners. You can create
                a RoundedBitmapDrawable from a file path, an input stream, or from a Bitmap object.
        */
        /*
            public static RoundedBitmapDrawable create (Resources res, Bitmap bitmap)
                Returns a new drawable by creating it from a bitmap, setting initial target density
                based on the display metrics of the resources.
        */
        /*
            RoundedBitmapDrawableFactory
                Constructs RoundedBitmapDrawable objects, either from Bitmaps directly, or from
                streams and files.
        */
        // Create a new RoundedBitmapDrawable
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(),roundedBitmap);

        /*
            setCornerRadius(float cornerRadius)
                Sets the corner radius to be applied when drawing the bitmap.
        */
        // Set the corner radius of the bitmap drawable
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);

        /*
            setAntiAlias(boolean aa)
                Enables or disables anti-aliasing for this drawable.
        */
        roundedBitmapDrawable.setAntiAlias(true);

        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }


}

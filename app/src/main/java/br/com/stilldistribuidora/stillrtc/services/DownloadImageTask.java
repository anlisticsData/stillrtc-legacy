package br.com.stilldistribuidora.stillrtc.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Still Technology and Development Team on 13/08/2018.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
    public DownloadImageTask()
    {

    }
    public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

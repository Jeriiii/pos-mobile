package pos.android.DownloadManager;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import pos.android.R;

/**
 * Created by Petr on 16.6.2015.
 */
public class StreamImageView {

    private static final String TAG = "okhttp";

    /** obrázek, co se má nastavit */
    private ImageView imageView;

    /** placeholder nevyplňeného obrázku */
    private Drawable icLauncher;

    /** obrázek s chybou */
    private Drawable failedImg;

    /** problém s response */
    private Drawable resProblemImg;

    public boolean isImageDisplaying = false;
    public boolean isRequestProblem = false;
    public boolean isResponseProblem = false;

    public StreamImageView(ImageView imageView, Activity activity) {
        this.imageView = imageView;

        Resources res = activity.getResources();
        this.icLauncher = res.getDrawable(R.drawable.ic_launcher);
        this.failedImg = res.getDrawable(R.drawable.failed);
        this.resProblemImg = res.getDrawable(R.drawable.response_problem);
    }

    /**
     * Nahraje k obrázku bitmapu.
     * @param bitmap
     */
    public void updateImage(final Bitmap bitmap) {
        isRequestProblem = false;
        isResponseProblem = false;

        Log.i(TAG, "Updating image");
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.getLayoutParams().height = bitmap.getHeight();
                    imageView.getLayoutParams().width = bitmap.getWidth();
                    isImageDisplaying = true;
                } else {
                    imageView.setImageDrawable(icLauncher);
                }
            }
        });
    }

    /**
     * Nastaví chybový obrázek.
     */
    public void setFailedImg() {
        isRequestProblem = true;

        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(failedImg);
            }
        });
    }

    /**
     * Nastaví obrázek při problému s odpovědí.
     */
    public void setResponseProblemImg() {
        isResponseProblem = true;
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(resProblemImg);
            }
        });
    }
}

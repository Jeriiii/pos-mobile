package pos.android.DownloadManager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import pos.android.R;

/**
 * Třída pro stáhnutí obrázku z webu
 *
 * Created by Petr on 14.6.2015.
 */
public class DownloadImageManager implements Callback {

    public static final int SUCCESSFUL_DOWNLOAD = 200;

    private StreamImageView imageView;
    private static final String TAG = "okhttp";

    public DownloadImageManager (StreamImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        e.printStackTrace();
        imageView.setFailedImg();
    }

    @Override
    public void onResponse(Response response) throws IOException {
        getResponseDetails(response);

        if (!response.isSuccessful()) {
            imageView.setResponseProblemImg();
            throw new IOException("Unexpected code " + response);
        }

        if(response.code()==SUCCESSFUL_DOWNLOAD){
            ResponseBody in = response.body();
            InputStream inputStream = in.byteStream();
            imageView.updateImage(BitmapFactory.decodeStream(inputStream));
        } else {
            imageView.setResponseProblemImg();
        }
    }

    private void getResponseDetails(Response response) {
        Headers headers = response.headers();
        Log.i(TAG, "Response code: " + String.valueOf(response.code()));
        Log.i(TAG, "Response message: " + response.message());
        Log.i(TAG, "Protocol: " + response.protocol());
        Log.i(TAG, "Number headers: " + headers.size());
        for (int i = 0; i < headers.size(); i++) {
            Log.i(TAG, headers.name(i) + "=" + headers.value(i));
        }
    }
}

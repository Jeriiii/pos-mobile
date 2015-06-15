package pos.android.Activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import pos.android.R;

/*created using Android Studio (Beta) 0.8.14
www.101apps.co.za*/

public class AsynchronousImageDownload extends Activity {

    private static final String TAG = "okhttp";
    String imageUrl = "http://www.101apps.co.za/images/headers/101_logo_very_small.jpg";
    private ImageView imageView;
    private OkHttpClient client;
    private boolean isImageDisplaying = false;
    private boolean isRequestProblem = false;
    private boolean isResponseProblem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynchronous_image_download);

        client = new OkHttpClient();

        if (savedInstanceState != null) {
            isImageDisplaying = savedInstanceState.getBoolean("isImageDisplaying");
            isRequestProblem = savedInstanceState.getBoolean("isRequestProblem");
            isResponseProblem = savedInstanceState.getBoolean("isResponseProblem");
        }

        File cacheDirectory = new File(this.getCacheDir(), "http");
        int cacheSize = 10 * 1024 * 1024;
/*        try {*/
            Cache cache = new Cache(cacheDirectory, cacheSize);
            client.setCache(cache);
/*        } catch (IOException e) {
            e.printStackTrace();
        }*/
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called");
        if (isRequestProblem) {
            imageView.setImageDrawable(getResources()
                    .getDrawable(R.drawable.failed));
        } else if (isResponseProblem) {
            imageView.setImageDrawable(getResources()
                    .getDrawable(R.drawable.response_problem));
        } else if (isImageDisplaying) {
            Log.i(TAG, "onResume - AsynchronousDownloadImage");
            if (!isOnline()) {
                Toast.makeText(this, "No internet connection",
                        Toast.LENGTH_SHORT).show();
            }
            AsynchronousDownloadImage(imageUrl);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isImageDisplaying", isImageDisplaying);
        outState.putBoolean("isRequestProblem", isRequestProblem);
        outState.putBoolean("isResponseProblem", isResponseProblem);
    }

    public void onClickDownloadImage(View view) {
        if (!isOnline()) {
            Toast.makeText(this, "No internet connection",
                    Toast.LENGTH_SHORT).show();
        }
        AsynchronousDownloadImage(imageUrl);
    }

    private void AsynchronousDownloadImage(String imageUrl) {

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {

            public static final int SUCCESSFUL_DOWNLOAD = 200;

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                isRequestProblem = true;
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageDrawable(getResources()
                                .getDrawable(R.drawable.failed));
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                getResponseDetails(response);

                if (!response.isSuccessful()) {
                    isResponseProblem = true;
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(getResources()
                                    .getDrawable(R.drawable.response_problem));
                        }
                    });
                    throw new IOException("Unexpected code " + response);
                }

                if(response.code()==SUCCESSFUL_DOWNLOAD){
                    isRequestProblem = false;
                    isResponseProblem = false;
                    ResponseBody in = response.body();
                    InputStream inputStream = in.byteStream();
                    updateImage(BitmapFactory.decodeStream(inputStream));
                } else {
                    isResponseProblem = true;
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(getResources()
                                    .getDrawable(R.drawable.response_problem));
                        }
                    });
                }
            }
        });
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

    private void updateImage(final Bitmap bitmap) {
        Log.i(TAG, "Updating image");
        imageView.post(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    isImageDisplaying = true;
                } else {
                    imageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_launcher));
                }
            }
        });
    }

    public boolean isOnline() {
//        check if there is an internet connection
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

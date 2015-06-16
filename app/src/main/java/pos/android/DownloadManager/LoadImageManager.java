package pos.android.DownloadManager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.File;

import pos.android.Activities.Stream.exts.Item.Item;
import pos.android.Http.HttpConection;
import pos.android.R;

/**
 * Created by Petr on 15.6.2015.
 */
public class LoadImageManager {

    private OkHttpClient client;

    private Activity activity;

    public LoadImageManager(Activity activity) {
        client = new OkHttpClient();
        this.activity = activity;
    }

    public void loadImg(Item item, View view, int viewId) {
        Request request = new Request.Builder()
                .url(HttpConection.host + HttpConection.path + item.imgUrl)
                .build();

        File cacheDirectory = new File(activity.getCacheDir(), "http");
        int cacheSize = 10 * 1024 * 1024;
/*        try {*/
        Cache cache = new Cache(cacheDirectory, cacheSize);
        client.setCache(cache);
/*        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ImageView imageView = (ImageView) view.findViewById(viewId);

        StreamImageView streamImageView = new StreamImageView(imageView, activity);
        client.newCall(request).enqueue(new DownloadImageManager(
                streamImageView
        ));
    }



}

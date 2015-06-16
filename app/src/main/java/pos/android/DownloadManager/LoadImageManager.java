package pos.android.DownloadManager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
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

    private static final String TAG = "imageLoader";

    private OkHttpClient client;

    private Activity activity;

    /** Slouží pro ukládání obrázků na disk */
    private MemoryCache memoryCache;

    public LoadImageManager(Activity activity) {
        client = new OkHttpClient();
        this.activity = activity;
        memoryCache = new MemoryCache();
    }

    /**
     * Nastaví obrázek pro view nalezený pomoví viewId. Pokud se obrázek nalézá a disku, tak ho
     * využije. Pokud ne, tak ho nejdříve stáhne, pak ho uloží na disk a nastaví ho šabloně.
     * @param item Data k obrázku, co se mají stáhnout.
     * @param view View, kde se má obrázek hledat.
     * @param viewId Id obrázku, co se má nastavit.
     */
    public void loadImg(Item item, View view, int viewId) {
        ImageView imageView = (ImageView) view.findViewById(viewId);
        StreamImageView streamImageView = new StreamImageView(imageView, activity);

        if(memoryCache.isIn(item.imgUrl)) {
            Log.i(TAG, "Load image from cache");
            Bitmap bmp = memoryCache.get(item.imgUrl);
            streamImageView.updateImage(bmp);
        } else {
            Log.i(TAG, "Load download from web");
            downloadImage(streamImageView, item);
        }
    }

    /**
     * Stáhne obrázek z webu.
     * @param streamImageView Obrázek co se má po stáhnutí nastavit.
     * @param item Data k obrázku, co se mají stáhnout.
     */
    private void downloadImage(StreamImageView streamImageView, Item item) {
        Request request = new Request.Builder()
                .url(HttpConection.host + HttpConection.path + item.imgUrl)
                .build();

        File cacheDirectory = new File(activity.getCacheDir(), "http");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(cacheDirectory, cacheSize);
        client.setCache(cache);

        client.newCall(request).enqueue(new DownloadImageManager(
                streamImageView, memoryCache, item.imgUrl
        ));
    }



}

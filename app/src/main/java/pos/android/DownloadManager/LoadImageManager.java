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
 * Třída pro načítání obrázku z cache. Pokud není obrázek v cache, načte se z webu a uloží se do cache.
 *
 * Created by Petr on 15.6.2015.
 */
public class LoadImageManager {
    /** Tag pro logy. */
    private static final String TAG = "imageLoader";

    /** Client pro stažení obrázku. */
    private OkHttpClient client;

    /** Aktivita, která používá manager */
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
     * @param imgUrl Krátká verze url k obrázku. (bez hosta a path)
     * @param imageView Obrázek, který se má nastavit.
     */
    public void loadImg(String imgUrl, ImageView imageView) {
        StreamImageView streamImageView = new StreamImageView(imageView, activity);

        if(memoryCache.isIn(imgUrl)) {
            Log.i(TAG, "Load image from cache");
            Bitmap bmp = memoryCache.get(imgUrl);
            streamImageView.updateImage(bmp);
        } else {
            Log.i(TAG, "Load download from web");
            downloadImage(streamImageView, imgUrl);
        }
    }

    /**
     * Stáhne obrázek z webu.
     * @param streamImageView Obrázek co se má po stáhnutí nastavit.
     * @param imgUrl Url k obrázku, co se má stáhnout.
     */
    private void downloadImage(StreamImageView streamImageView, String imgUrl) {
        Request request = new Request.Builder()
                .url(HttpConection.host + HttpConection.path + imgUrl)
                .build();

        File cacheDirectory = new File(activity.getCacheDir(), "http");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(cacheDirectory, cacheSize);
        client.setCache(cache);

        client.newCall(request).enqueue(new DownloadImageManager(
                streamImageView, memoryCache, imgUrl
        ));
    }



}

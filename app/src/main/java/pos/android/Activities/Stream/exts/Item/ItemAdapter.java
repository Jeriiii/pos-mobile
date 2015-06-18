package pos.android.Activities.Stream.exts.Item;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.protocol.HttpContext;

import java.util.ArrayList;

import pos.android.Activities.Stream.exts.LikeOnClickListener;
import pos.android.DownloadManager.LoadImageManager;
import pos.android.R;

/**
 * Nastavuje view příspěvků.
 * Created by Petr on 20.5.2015.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    /** výška seznamu */
    public int listHeight = 0;

    /** kontext aplikace */
    private HttpContext httpContext;

    /** Sloužba pro načítání obrázků ze serveru. */
    private LoadImageManager loadImageManager;

    /** Kontext aplikace = aktivita. */
    private Context context;

    public ItemAdapter(Activity activity, ArrayList<Item> users) {
        super(activity, R.layout.stream_list_entry, users);
        this.loadImageManager = new LoadImageManager(activity);
        this.context = activity;
    }

    /**
     * Setter na httpContext
     */
    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    /**
     * Správně nastaví právě vykreslovaný příspěvek a přiřadí mu data.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.stream_list_entry, parent, false);

        // Get the data item for this position
        Item item = getItem(position);

        /* přidá výšku elementu do výšky listu */
        listHeight = listHeight + getViewHeight(convertView);
        convertView.getLayoutParams().width = parent.getWidth();

        // Lookup view for data population
        // Populate the data into the template view using the data object
        //setTextView(convertView, Integer.toString(item.id), R.id.id);
        setTextView(convertView, item.name, R.id.name);
        setTextView(convertView, item.userName, R.id.userName);
        setTextView(convertView, item.message, R.id.message);
        setLikesView(convertView, item.countLikes, R.id.likes, item);
        setImgView(convertView, R.id.image, item, position);
        setCommentsView(convertView, item.countComments, R.id.comments);

        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * Zobrazí políčko - text - obrázek, pokud existuje a naplní ho daty.
     */
    private void setTextView(View convertView, String itemParam, int viewId) {
        TextView tvName = (TextView) convertView.findViewById(viewId);
        if(! itemParam.equals("")) {
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(itemParam);
        } else {
            tvName.setVisibility(View.GONE);
        }
    }

    /**
     * Nastaví liky u příspěvku.
     */
    private void setLikesView(View convertView, int countLikes, int viewId, Item item) {
        String text = "Líbí";
        if(countLikes != 0) {
            text = text + " " + "(" + Integer.toString(countLikes) + ")" ;
        }

        TextView tvLike = (TextView) convertView.findViewById(viewId);
        tvLike.setOnClickListener(new LikeOnClickListener(getContext(), item, httpContext));

        setTextView(convertView, text, viewId);
    }

    /**
     * Nastaví tlačítko přidat komentář.
     */
    private void setCommentsView(View convertView, int countComments, int viewId) {
        String text = "Přidat komentář";
        if(countComments != 0) {
            text = "Komentáře " + "(" + Integer.toString(countComments) + ")" ;
        }
        setTextView(convertView, text, viewId);
    }

    /**
     * Nastaví obrázek, pokud existuje.
     */
    private void setImgView(View convertView, int viewId, Item item, int position) {
        ImageView image = (ImageView) convertView.findViewById(viewId);
        if(item.imgUrl != null) {
            image.setVisibility(View.VISIBLE);
            ImageView imageView = (ImageView) convertView.findViewById(viewId);
            loadImageManager.loadImg(item.imgUrl, imageView);
        } else {
            image.setVisibility(View.GONE);
        }
    }

    /**
     * Vrátí výšku seznamu.
     */
    private int getViewHeight(View mView) {
        mView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return mView.getMeasuredHeight();
    }
}
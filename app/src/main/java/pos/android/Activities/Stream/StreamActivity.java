package pos.android.Activities.Stream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;


import java.util.ArrayList;

import pos.android.Activities.BaseActivities.BaseListActivity;
import pos.android.Activities.Stream.exts.Item.Item;
import pos.android.Activities.Stream.exts.Item.ItemAdapter;
import pos.android.Activities.Stream.exts.Item.ItemHolder;
import pos.android.Activities.Stream.exts.Item.LoadStream;
import pos.android.Config.Config;
import pos.android.R;


/**
 * Aktivita zobrazující nekonečný seznam příspěvků. Umožňuje vložit příspěvek nebo nahrát foto.
 */
public class StreamActivity extends BaseListActivity {

    /** Sata která se mají načíst do streamu. */
    public ArrayList<Item> streamItems;

    /** Adapter na data ve streamu. */
    public ItemAdapter adapter;

    /** Zobrazí se při načítání dalších příspěvků. */
    public ProgressBar bar;

    /** Má již tlačítko k načtení dalších příspěvků. */
    public boolean haveMoreButton = false;

    /** Má již formulář k přidání dalších příspěvků. */
    public boolean haveItemForm = false;

    /** Tagy aplikace. */
    private static final String TAG_STREAM_ITEMS = "data";
    private static final String TAG_ID = "id";
    public static final String TAG_ITEM = "stream_item";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stream_list);

        streamItems = new ArrayList<Item>();
        adapter = new ItemAdapter(this, streamItems);

        adapter.setHttpContext(httpContext);

        bar = (ProgressBar) this.findViewById(R.id.progressBar);

        addItems();
    }

    /**
     * Načte nově přidané příspěvky do streamu.
     */
    public void notifyAdapter() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * Načte další příspěvky do streamu.
     */
    public void addItems() {
        new LoadStream(this, httpContext, streamItems).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ItemActvity.class);

        Item item = adapter.getItem(position - 1);
        ItemHolder.getInstance().item = item;

        startActivity(intent);

        finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Vytáhne obrázek z proměnné data a pošle ho aktivitě pro upload */
        if (requestCode == Config.LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Začne číst data obrázku - je to Uri
            Uri pickedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            Intent i = new Intent(this, UploadImageActivity.class);
            i.putExtra("filePath", imagePath);
            i.putExtra("isImage", true);
            startActivity(i);
            finish();

            ImageView image = (ImageView)findViewById(R.id.image);

            image.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            cursor.close();
        }
    }

}



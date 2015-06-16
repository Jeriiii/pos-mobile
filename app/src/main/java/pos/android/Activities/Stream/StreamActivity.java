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
import pos.android.R;



public class StreamActivity extends BaseListActivity {

    // Image loading result to pass to startActivityForResult method.
    private static int LOAD_IMAGE_RESULTS = 1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.loginRouter();

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ItemActvity.class);

        Item item = adapter.getItem(position - 1);
        ItemHolder.getInstance().item = item;

        startActivity(intent);

        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
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
            // Now we need to set the GUI ImageView data with data read from the picked file.
            image.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

}



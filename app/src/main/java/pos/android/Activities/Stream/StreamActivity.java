package pos.android.Activities.Stream;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.android.Activities.BaseActivities.BaseListActivity;
import pos.android.Activities.Stream.exts.Item.Item;
import pos.android.Activities.Stream.exts.Item.ItemAdapter;
import pos.android.Activities.Stream.exts.Item.ItemHolder;
import pos.android.Activities.Stream.exts.Item.JsonToItems;
import pos.android.Activities.Stream.exts.Item.LoadStream;
import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;
import pos.android.R;



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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_example);

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
     * Zobrazí tlačítko když někdo začne psát text.
     */
    public void onAddStausTextClick() {
        View addStatusBtn = findViewById(R.id.addStatusForm);
        addStatusBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ItemActvity.class);

        Item item = adapter.getItem(position);
        ItemHolder.getInstance().item = item;

        startActivity(intent);

        finish();
    }

}



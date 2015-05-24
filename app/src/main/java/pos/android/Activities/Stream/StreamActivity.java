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
import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;
import pos.android.R;



public class StreamActivity extends BaseListActivity {

    /** Sata která se mají načíst do streamu. */
    ArrayList<Item> streamItems;

    /** Adapter na data ve streamu. */
    ItemAdapter adapter;

    /** Zobrazí se při načítání dalších příspěvků. */
    ProgressBar bar;

    /** Má již tlačítko k načtení dalších příspěvků. */
    boolean haveMoreButton = false;

    /** Má již formulář k přidání dalších příspěvků. */
    boolean haveItemForm = false;

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

    class LoadStream extends AsyncTask<String, String, String> {

        /**
         * Http kontext pro čtení dat přihlášeného uživatele.
         */
        private HttpContext httpContext;

        /**
         * Stream aktivity
         */
        StreamActivity streamActivity;

        /**
         * Seznam položek ve streamu.
         */
        ArrayList<Item> listItems;

        public LoadStream(StreamActivity streamActivity, HttpContext httpContext, ArrayList<Item> listItems) {
            this.streamActivity = streamActivity;
            this.httpContext = httpContext;
            this.listItems = listItems;
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bar.setVisibility(View.VISIBLE);
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            String url = HttpConection.host + HttpConection.path + "/one-page/stream-in-json";

            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
            int countItems = listItems.size();
            urlParams.add(new BasicNameValuePair("offset", Integer.toString(countItems)));

            JSONParser con = new JSONParser();
            JSONObject json = con.getJSONmakeHttpRequest(url, "GET", urlParams, httpContext);

            if(json != null) {
                JsonToItems jsonToItems = new JsonToItems(listItems);
                jsonToItems.saveItemsToList(json);
            } else {
                //připojení se nezdařilo
            }

            streamActivity.notifyAdapter();

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            ListView lv = (ListView)findViewById(android.R.id.list);

            addMoreButton(lv);
            addItemForm(lv);
            setListHeight(lv);
            lv.setSelection(adapter.getCount() - 1);

            setListAdapter(adapter);

            bar.setVisibility(View.GONE);
            setListeners();
        }

        /**
         * Přidá tlačítko pro načtení více příspěvků do streamu.
         */
        private void addMoreButton(ListView layout) {
            if(! streamActivity.haveMoreButton) {
                View moreButton = getLayoutInflater().inflate(R.layout.stream_more_button, null);
                layout.addFooterView(moreButton);
                streamActivity.haveMoreButton = true;
            }
        }

        /**
         * Přidá na začátek seznamu formulář pro odeslání příspěvku.
         */
        private void addItemForm(ListView layout) {
            if(! streamActivity.haveItemForm) {
                View addItem = getLayoutInflater().inflate(R.layout.stream_add_item, null);
                layout.addHeaderView(addItem);
                streamActivity.haveItemForm = true;

                /* Schová tlačítko na odeslání formuláře. */
                View addStatusForm = findViewById(R.id.addStatusForm);
                addStatusForm.setVisibility(View.GONE);

                View showStatusFormbtn = findViewById(R.id.showAddStatus);
                showStatusFormbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onAddStausTextClick();
                    }
                });
            }
        }

        /**
         * Dočasná metoda pro nastavení výšky streamu.
         */
        private void setListHeight(ListView layout) {
            //findViewById(R.id.layout_to_hide).setMinimumHeight(View.VISIBLE);
            layout = (ListView)findViewById(android.R.id.list);
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = layout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = -1;
        }

        private int getTotalHeightofListView() {
            ListView lv = (ListView)findViewById(android.R.id.list);
            int listviewElementsheight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View mView = adapter.getView(i, null, lv);
                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                listviewElementsheight += mView.getMeasuredHeight();
            }
            return listviewElementsheight;
        }

        /**
         * Nastaví listenery
         */
        private void setListeners() {
            Button buttonOne = (Button) findViewById(R.id.more_button);
            buttonOne.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    streamActivity.addItems();
                }
            });
        }
    }

}



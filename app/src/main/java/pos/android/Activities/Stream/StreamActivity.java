package pos.android.Activities.Stream;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pos.android.Activities.BaseActivities.BaseActivity;
import pos.android.Activities.BaseActivities.BaseListActivity;
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

    /** Tagy aplikace. */
    private static final String TAG_STREAM_ITEMS = "data";

    private static final String TAG_ID = "id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_example);

        streamItems = new ArrayList<Item>();
        adapter = new ItemAdapter(this, streamItems);

        bar = (ProgressBar) this.findViewById(R.id.progressBar);

        addItems();

    }

    /**
     * Načte další příspěvky do streamu.
     */
    public void addItems() {
        new LoadStream(this, httpContext, streamItems).execute();
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

    class LoadStream extends AsyncTask<String, String, String> {

        JSONArray items = null;

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
            String url = "http://10.0.2.2/nette/pos/public/www/one-page/stream-in-json";

            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
            int countItems = listItems.size();
            urlParams.add(new BasicNameValuePair("offset", Integer.toString(countItems)));

            JSONParser con = new JSONParser();
            JSONObject json = con.getJSONmakeHttpRequest(url, "GET", urlParams, httpContext);

            if(json != null) {
                saveItems(json);
            } else {
                //připojení se nezdařilo
            }

            streamActivity.notifyAdapter();

            return null;
        }

        /**
         * Uloží příspěvky.
         */
        private void saveItems(JSONObject jsonItems) {
            JSONObject c;
            int i;

            try {
                // products found
                // Getting Array of Products
                items = jsonItems.getJSONArray(TAG_STREAM_ITEMS);

               // looping through All Products
               for (i = 0; i < items.length(); i++) {

                   c = items.getJSONObject(i);
                   Item item = getItem(c);

                   listItems.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private Item getItem(JSONObject jsonObject) throws JSONException{

            // Storing each json item in variable

            Item item = new Item();

            if(isset(jsonObject, "userGalleryID")) {
                item =  getUserGallery(jsonObject);
            }
            if(isset(jsonObject, "statusID")) {
                item = getStreamStatus(jsonObject);
            }
            if(isset(jsonObject, "confessionID")) {
                item = getConfession(jsonObject);
            }

            addUserData(item, jsonObject);

            return item;
        }

        /**
         * Přidá k příspěvku data o uživateli.
         * @param item Příspěvek, kam je potřeba přidat data.
         * @param jsonObject Objekt, ze kterého se data čtou.
         * @return Objekt, ke kterému byla přidána data o uživateli, pokud existují.
         * @throws JSONException
         */
        private Item addUserData(Item item, JSONObject jsonObject) throws JSONException{
            if(isset(jsonObject, "user")) {
                JSONObject userObj = jsonObject.getJSONObject("user");
                item.userName = userObj.getString("user_name");
            }

            return item;
        }

        /**
         * Vrátí, zda je JSON hodnota nastavená, nebo je null či false
         * @param jsonObject Objekt který se prohledává.
         * @param paramName Název parametru, který se hledá.
         * @return TRUE = hodnota je nastavená, jinak FALSE
         */
        private boolean isset(JSONObject jsonObject, String paramName) throws JSONException {
            String user = jsonObject.getString(paramName);
            if(! user.equals("null") && ! user.equals("false")) {
                return true;
            }

            return false;
        }

        /**
         * Vrátí uživatelskou galerii.
         * @param jsonObject
         * @return
         * @throws JSONException
         */
        private Item getUserGallery(JSONObject jsonObject) throws JSONException{
            JSONObject galleryObject = jsonObject.getJSONObject("userGallery");
            String name = galleryObject.getString("name");
            Item gallery = new Item(name);

            return gallery;
        }

        /**
         * Vrátí uživatelský status.
         * @param jsonObject
         * @return
         * @throws JSONException
         */
        private Item getStreamStatus(JSONObject jsonObject) throws JSONException{
            JSONObject galleryObject = jsonObject.getJSONObject("status");
            String message = galleryObject.getString("message");

            Item status = new Item();
            status.message = message;

            return status;
        }

        /**
         * Vrátí přiznání.
         * @param jsonObject
         * @return
         * @throws JSONException
         */
        private Item getConfession(JSONObject jsonObject) throws JSONException{
            JSONObject galleryObject = jsonObject.getJSONObject("confession");
            String message = galleryObject.getString("note");

            Item conf = new Item();
            conf.message = message;

            return conf;
        }

        /**
         * Rozhodne, zda jsou data ze streamu důležitá k uložení nebo ne.
         * @param name Název dat ze streamu k posouzení.
         * @param var Data ze streamu k posouzení.
         * @return TRUE = data se mají uložit k pozdějšímu zobrazení, jinak FALSE
         */
        private boolean isImportant(String name, String var) {
            if(var.equals("null") || var.equals("false")) {
                return false;
            }
            if(name.equals("tallness")) {
                return false;
            }
            if(name.equals("categoryID")) {
                return false;
            }
            if(name.equals("type")) {
                return false;
            }

            return true;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            ListView lv = (ListView)findViewById(android.R.id.list);

            this.addMoreButton(lv);
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
         * Dočasná metoda pro nastavení výšky streamu.
         */
        private void setListHeight(ListView layout) {
            //findViewById(R.id.layout_to_hide).setMinimumHeight(View.VISIBLE);
            layout = (ListView)findViewById(android.R.id.list);
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = layout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 1000;
        }

        private int getTotalHeightofListView() {
            ListView lv = (ListView)findViewById(android.R.id.list);
            ListAdapter LvAdapter = lv.getAdapter();
            int listviewElementsheight = 0;
            for (int i = 0; i < LvAdapter.getCount(); i++) {
                View mView = LvAdapter.getView(i, null, lv);
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



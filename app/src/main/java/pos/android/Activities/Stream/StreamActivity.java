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
    ArrayList<HashMap<String, String>> streamItems;

    ItemAdapter adapter;

    ProgressBar bar;

    /** Tagy aplikace. */
    private static final String TAG_STREAM_ITEMS = "data";

    private static final String TAG_ID = "id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_example);

        ArrayList<Item> arrayOfItems = new ArrayList<Item>();
        adapter = new ItemAdapter(this, arrayOfItems);

        bar = (ProgressBar) this.findViewById(R.id.progressBar);

        addItems();

    }

    /**
     * Načte další příspěvky do streamu.
     */
    public void addItems() {
        new LoadStream(this, httpContext).execute();
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

        public LoadStream(StreamActivity streamActivity, HttpContext httpContext) {
            this.streamActivity = streamActivity;
            this.httpContext = httpContext;
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

            JSONParser con = new JSONParser();
            JSONObject json = con.getJSONmakeHttpRequest(url, "GET", urlParams, httpContext);

            if(json != null) {
                saveItems(json);
            } else {
                //připojení se nezdařilo
            }



            int i = 1;

            return null;
        }

        /**
         * Uloží příspěvky.
         */
        private void saveItems(JSONObject jsonItems) {
            try {
                // products found
                // Getting Array of Products
                items = jsonItems.getJSONArray(TAG_STREAM_ITEMS);

               // looping through All Products
               for (int i = 0; i < items.length(); i++) {

                   JSONObject c = items.getJSONObject(i);
                   //HashMap<String, String> item = getItem(c);
                   Item item = getItem(c);

                   adapter.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private Item getItem(JSONObject jsonObject) throws JSONException{

            // Storing each json item in variable

            Item item = new Item();

            if(! jsonObject.getString("userGalleryID").equals("null")) {
                item =  getUserGallery(jsonObject);
            }
            if(! jsonObject.getString("statusID").equals("null")) {
                item = getStreamStatus(jsonObject);
            }
            if(! jsonObject.getString("confessionID").equals("null")) {
                item = getConfession(jsonObject);
            }

            addUserData(item, jsonObject);

            return item;
        }

        private Item addUserData(Item item, JSONObject jsonObject) throws JSONException{
            JSONObject user = jsonObject.getJSONObject("user");

            if(! user.equals("null")) {
                item.userName = user.getString("user_name");
            }

            return item;
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
            ListView layout = (ListView)findViewById(android.R.id.list);
            View moreButton = getLayoutInflater().inflate(R.layout.stream_more_button, null);

            layout.addFooterView(moreButton);

            setListAdapter(adapter);
            //findViewById(R.id.layout_to_hide).setMinimumHeight(View.VISIBLE);
            layout = (ListView)findViewById(android.R.id.list);
            // Gets the layout params that will allow you to resize the layout
            ViewGroup.LayoutParams params = layout.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 1000;

            /*layout = (ListView)findViewById(android.R.id.list);
            ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnPreDrawListener(new LayoutListener(streamActivity, adapter));*/
            bar.setVisibility(View.GONE);
            setListeners();
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



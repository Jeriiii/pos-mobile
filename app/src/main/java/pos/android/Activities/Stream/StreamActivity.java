package pos.android.Activities.Stream;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

    /** Tagy aplikace. */
    private static final String TAG_STREAM_ITEMS = "data";

    private static final String TAG_ID = "id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_stream);

        streamItems = new ArrayList<HashMap<String, String>>();

        new LoadStream(httpContext).execute();

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {R.id.name_entry}; // The TextView in simple_list_item_1

        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this,
                R.layout.list_example_entry, null,
                fromColumns, toViews, 0);
        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);

        /*// Get listview
        ListView lv = getListView();

        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

               *//* // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        EditProductActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);*//*
            }
        });*/


    }

    class LoadStream extends AsyncTask<String, String, String> {

        JSONArray items = null;

        /**
         * Http kontext pro čtení dat přihlášeného uživatele.
         */
        private HttpContext httpContext;

        public LoadStream(HttpContext httpContext) {
            this.httpContext = httpContext;
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                   HashMap<String, String> item = getItem(c);

                   streamItems.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private HashMap<String, String> getItem(JSONObject item) throws JSONException{

            // Storing each json item in variable
            String id = null;
            HashMap<String, String> map = new HashMap<String, String>();

            Iterator<String> itr = item.keys();
            while(itr.hasNext()) {
                Object nextItem = itr.next();
                String name = (String) nextItem;
                String var = item.getString(name);

                /*if ( nextItem.get(key) instanceof JSONObject ) {

                }
                try {
                    JSONObject jsonObject = item.getJSONObject(name);
                } catch (JSONException e) {
                    //toto pole je schválně prázdné
                }*/
                if(isImportant(name, var)) {
                    map.put(name, var);
                }
            }

            return map;
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
            /*// dismiss the dialog after getting all products
            *//*pDialog.dismiss();*//*
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    *//**
                     * Updating parsed JSON data into ListView
                     * *//*
                    ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[] { TAG_PID,
                            TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });*/

        }
    }

}



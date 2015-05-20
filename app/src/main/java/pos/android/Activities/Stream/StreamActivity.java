package pos.android.Activities.Stream;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        new LoadStream(httpContext).execute();
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
            String id = null;
            Item item = new Item(jsonObject.getString("id"));

            /*Iterator<String> itr = item.keys();
            while(itr.hasNext()) {
                Object nextItem = itr.next();
                String name = (String) nextItem;
                String var = item.getString(name);

                *//*if ( nextItem.get(key) instanceof JSONObject ) {

                }
                try {
                    JSONObject jsonObject = item.getJSONObject(name);
                } catch (JSONException e) {
                    //toto pole je schválně prázdné
                }*//*
                if(isImportant(name, var)) {
                    map.put(name, var);
                }
            }*/

            return item;
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

            setListAdapter(adapter);
            bar.setVisibility(View.GONE);
        }
    }

}



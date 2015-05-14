package pos.android.Activities.Stream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


import org.apache.http.NameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pos.android.Activities.BaseActivities.BaseActivity;
import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;
import pos.android.R;

public class StreamActivity extends BaseActivity {

    /** Sata která se mají načíst do streamu. */
    ArrayList<HashMap<String, String>> streamItems;

    private static final String TAG_STREAM_ITEMS = "items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stream_list);

        streamItems = new ArrayList<HashMap<String, String>>();

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
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            String url = "http://priznaniosexu.cz/one-page/stream-in-json";

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

               /* // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME);
                    String price = c.getString(TAG_PRICE);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_PRICE, price);

                    // adding HashList to ArrayList
                    productsList.add(map);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
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



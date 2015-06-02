package pos.android.Activities.Stream.exts.Item;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.android.Activities.Stream.StreamActivity;
import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;
import pos.android.R;

/**
 * Created by Petr on 24.5.2015.
 */
public class LoadStream extends AsyncTask<String, String, String> {

    private static int LOAD_IMAGE_RESULTS = 1;

    /** Http kontext pro čtení dat přihlášeného uživatele. */
    private HttpContext httpContext;

    /** Stream aktivity */
    StreamActivity streamActivity;

    /** Seznam položek ve streamu. */
    ArrayList<Item> listItems;

    /** Id vybraného obrázku. */
    int pickImageId;

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
        streamActivity.bar.setVisibility(View.VISIBLE);
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
        ListView lv = (ListView)streamActivity.findViewById(android.R.id.list);

        addMoreButton(lv);
        addItemForm(lv);
        setListHeight(lv);
        lv.setSelection(streamActivity.adapter.getCount() - 1);

        streamActivity.setListAdapter(streamActivity.adapter);

        streamActivity.bar.setVisibility(View.GONE);
        setListeners();
    }

    /**
     * Přidá tlačítko pro načtení více příspěvků do streamu.
     */
    private void addMoreButton(ListView layout) {
        if(! streamActivity.haveMoreButton) {
            View moreButton = streamActivity.getLayoutInflater().inflate(R.layout.stream_more_button, null);
            layout.addFooterView(moreButton);
            streamActivity.haveMoreButton = true;
        }
    }

    /**
     * Přidá na začátek seznamu formulář pro odeslání příspěvku.
     */
    private void addItemForm(ListView layout) {
        if(! streamActivity.haveItemForm) {
            View addItem = streamActivity.getLayoutInflater().inflate(R.layout.stream_add_item, null);
            layout.addHeaderView(addItem);
            streamActivity.haveItemForm = true;

            hideStatusForm();
            hideImageForm();

            View showStatusFormBtn = streamActivity.findViewById(R.id.showAddStatus);
            showStatusFormBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusForm();
                    hideStatusForm();
                }
            });

            View showImageFormBtn = streamActivity.findViewById(R.id.showAddImage);
            showImageFormBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageForm();
                    hideStatusForm();
                }
            });
        }
    }

    public void onClickAddImage()
    {

        /*Intent intent= new Intent();
        intent.setType("image*//*");
        intent.setAction(intent.ACTION_SEND);
        streamActivity.startActivityForResult(intent.createChooser(intent, "Select Picture"), pickImageId);*/

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        streamActivity.startActivityForResult(i, LOAD_IMAGE_RESULTS);
    }

    /** Zobrazí formulář na odeslání statusu. */
    public void showStatusForm() {
        View addStatusForm = streamActivity.findViewById(R.id.addStatusForm);
        addStatusForm.setVisibility(View.VISIBLE);
    }

    /** Schová formulář na odeslání statusu. */
    public void hideStatusForm() {
        View addStatusForm = streamActivity.findViewById(R.id.addStatusForm);
        addStatusForm.setVisibility(View.GONE);
    }

    /** Zobrazí formulář na odeslání obrázku. */
    public void showImageForm() {
        View addStatusForm = streamActivity.findViewById(R.id.addImageForm);
        addStatusForm.setVisibility(View.VISIBLE);
    }

    /** Schová formulář na odeslání obrázku. */
    public void hideImageForm() {
        View addStatusForm = streamActivity.findViewById(R.id.addImageForm);
        addStatusForm.setVisibility(View.GONE);
    }

    /**
     * Dočasná metoda pro nastavení výšky streamu.
     */
    private void setListHeight(ListView layout) {
        //findViewById(R.id.layout_to_hide).setMinimumHeight(View.VISIBLE);
        layout = (ListView)streamActivity.findViewById(android.R.id.list);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = -1;
    }

    private int getTotalHeightofListView() {
        ListView lv = (ListView)streamActivity.findViewById(android.R.id.list);
        int listviewElementsheight = 0;
        for (int i = 0; i < streamActivity.adapter.getCount(); i++) {
            View mView = streamActivity.adapter.getView(i, null, lv);
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
        Button buttonOne = (Button) streamActivity.findViewById(R.id.more_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                streamActivity.addItems();
            }
        });
    }
}

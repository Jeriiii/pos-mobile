package pos.android.Activities.Stream.exts.Item;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pos.android.Activities.SignInActivity;
import pos.android.Activities.Stream.StreamActivity;
import pos.android.Config.Config;
import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;
import pos.android.Http.PersistentCookieStore;
import pos.android.R;

/**
 * Created by Petr on 24.5.2015.
 */
public class LoadStream extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = LoadStream.class.getSimpleName();

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static int LOAD_IMAGE_RESULTS = 1;

    /** Http kontext pro čtení dat přihlášeného uživatele. */
    private HttpContext httpContext;

    /** Stream aktivity */
    StreamActivity streamActivity;

    /** Seznam položek ve streamu. */
    ArrayList<Item> listItems;

    /** Id vybraného obrázku. */
    int pickImageId;

    /** Seznam nově přidaých příspěvků */
    ArrayList<Item> newListItems;

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
    protected Boolean doInBackground(Void... args) {
        String url = HttpConection.host + HttpConection.path + "/one-page/stream-in-json";

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        int countItems = listItems.size();
        urlParams.add(new BasicNameValuePair("offset", Integer.toString(countItems)));

        JSONParser con = new JSONParser();
        JSONObject json = con.getJSONmakeHttpRequest(url, "GET", urlParams, httpContext);

        if(json != null) {
            JsonToItems jsonToItems = new JsonToItems(listItems);
            newListItems = jsonToItems.saveItemsToList(json);
        } else {
            return false;
        }

        streamActivity.notifyAdapter();

        return true;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(Boolean success) {
        if(!success) {
            Intent i = new Intent(streamActivity.getApplicationContext(), SignInActivity.class);

            PersistentCookieStore mCookieStore = new PersistentCookieStore(
                    streamActivity.getApplicationContext());
            mCookieStore.clear();

            streamActivity.startActivity(i);
            streamActivity.finish();
        }

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

            /* tlačítko na ukázání formuláře s přidáním nového statusu */
            View showStatusFormBtn = streamActivity.findViewById(R.id.showAddStatus);
            showStatusFormBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusForm();
                    hideImageForm();
                }
            });

            /* tlačítko na ukázání formuláře s přidáním nového obrázku */
            View showImageFormBtn = streamActivity.findViewById(R.id.showAddImage);
            showImageFormBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageForm();
                    hideStatusForm();
                }
            });

            /* tlačítko na přidání nového statusu */
            View addStausBtn = streamActivity.findViewById(R.id.addStausBtn);
            addStausBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAddStatus();
                }
            });

            /* tlačítko na přidání nové fotky */
            View addImageBtn = streamActivity.findViewById(R.id.addImageBtn);
            addImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAddImage();
                }
            });

            View cameraBtn = streamActivity.findViewById(R.id.cameraBtn);
            cameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCamera();
                }
            });
        }
    }

    private void onClickCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // Intent pro kameru
        streamActivity.startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onClickAddStatus() {
        EditText et = (EditText)streamActivity.findViewById(R.id.addStatus);
        String status = et.getText().toString();

        if(status.equals("")) {
            et.setError("Napište komentář.");
        } else {
            SendStatus st = new SendStatus(status);
            st.execute();

            et.setText("");
        }
    }


    /**
     * Přesměrování do galerie pro výběr obrázku
     */
    public void onClickAddImage()
    {
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
     * Pro nastavení výšky streamu.
     */
    private void setListHeight(ListView layout) {
        layout = (ListView)streamActivity.findViewById(android.R.id.list);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = -1;
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

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Vrátí obrázek / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Odešle status uzivatele.
     */
    class SendStatus extends AsyncTask<String, String, String> {

        /**
         * Komentář co se má odeslat na server.
         */
        private String newStatus;

        public SendStatus(String newStatus) {
            this.newStatus = newStatus;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast toast = Toast.makeText(streamActivity.getApplicationContext(), "Status byl přidán.", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = HttpConection.host + HttpConection.path + "/http-one-page/";
            List<NameValuePair> urlParams = getParams();

            HttpConection con = new HttpConection();
            con.makeHttpRequest(url, "GET", urlParams, httpContext);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        /**
         * Vrátí správně nastavení parametry url.
         * @return
         */
        private List<NameValuePair> getParams() {
            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

            urlParams.add(new BasicNameValuePair("status", newStatus));
            urlParams.add(new BasicNameValuePair("do", "addStatus"));

            return urlParams;
        }
    }


}

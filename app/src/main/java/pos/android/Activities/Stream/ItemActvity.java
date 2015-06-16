package pos.android.Activities.Stream;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.android.Activities.BaseActivities.BaseListActivity;
import pos.android.Activities.Stream.exts.Comment.Comment;
import pos.android.Activities.Stream.exts.Comment.CommentAdapter;
import pos.android.Activities.Stream.exts.Item.Item;
import pos.android.Activities.Stream.exts.Item.ItemAdapter;
import pos.android.Activities.Stream.exts.Item.ItemHolder;
import pos.android.Activities.Stream.exts.Comment.JsonToComments;
import pos.android.Activities.Stream.exts.LikeOnClickListener;
import pos.android.DownloadManager.LoadImageManager;
import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;
import pos.android.R;

/**
 * Zobrazení jedno příspěvku ve streamu.
 */
public class ItemActvity extends BaseListActivity {

    /** Zobrazovaný příspěvek. */
    private Item item;

    /** Zobrazí se při načítání dalších příspěvků. */
    ProgressBar bar;

    /** Seznam komentářů u příspěvku. */
    ArrayList<Comment> listComments;

    /** Načítání obrázků */
    LoadImageManager loadImageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_actvity);

        loadImageManager = new LoadImageManager(this);

        listComments = new ArrayList<Comment>();
        adapter = new CommentAdapter(this, listComments);

        this.setViewData();
        this.setComments();
    }

    /**
     * Načte základní data pro příspěvek, tedy všechny data mimo komentářů
     */
    private void setViewData() {
        item = ItemHolder.getInstance().item;

        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        ((TextView)findViewById(R.id.name)).setText(item.name);
        TextView messageView = ((TextView) findViewById(R.id.message));
        if(item.message != "") {
            messageView.setText(item.message);
        } else {
            messageView.setVisibility(View.GONE);
        }

        ImageView image = ((ImageView) findViewById(R.id.image));
        if(item.imgUrl != null) {
            loadImageManager.loadImg(item.imgUrl, image);
        } else {
            image.setVisibility(View.GONE);
        }
        ((TextView)findViewById(R.id.userName)).setText(item.userName);
        setLikes(item);


    }

    /**
     * Načte komentáře k příspěvku.
     */
    private void setComments() {
        View addCommentBtn = findViewById(R.id.addComment);
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        (new LoadComments(httpContext, listComments, this)).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_actvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Nastaví počet liků.
     * @param item
     */
    private void setLikes(Item item) {
        String text = "Líbí";
        TextView tvLike = (TextView) findViewById(R.id.likes);
        if(item.countLikes != 0) {
            text = text + " " + "(" + Integer.toString(item.countLikes) + ")" ;
        }
        tvLike.setText(text);
        tvLike.setOnClickListener(new LikeOnClickListener(getApplicationContext(), item, httpContext));
    }

    /**
     * Přidá komentář.
     */
    public void addComment() {
        EditText et = (EditText)findViewById(R.id.comment_msg);
        String comment = et.getText().toString();
        if(comment.equals("")) {
            et.setError("Napište komentář.");
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Komentář byl odeslán.", Toast.LENGTH_SHORT);
            toast.show();
            Comment comm = new Comment();
            comm.comment = comment;
            comm.userName = "Váš komentář";

            listComments.add(comm);
            adapter.notifyDataSetChanged();
            et.setText("");
            (new SendComment(comm)).execute();
        }
    }

    class LoadComments extends AsyncTask<String, String, String> {

        /** Http kontext pro čtení dat přihlášeného uživatele. */
        private HttpContext httpContext;

        /** Seznam komentářů u příspěvku. */
        ArrayList<Comment> listComments;

        /** Nadřazená aktivita */
        ItemActvity itemActivity;

        public LoadComments(HttpContext httpContext, ArrayList<Comment> listComments, ItemActvity itemActivity) {
            this.httpContext = httpContext;
            this.listComments = listComments;
            this.itemActivity = itemActivity;
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
            String url = getUrl();
            List<NameValuePair> urlParams = getParams();

            JSONParser con = new JSONParser();
            JSONObject json = con.getJSONmakeHttpRequest(url, "GET", urlParams, httpContext);

            if(json != null) {
                JsonToComments jsonToComments = new JsonToComments(listComments);
                jsonToComments.saveToList(json);
            } else {
                //připojení se nezdařilo
            }

            itemActivity.notifyAdapter();

            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            setListAdapter(adapter);

            bar.setVisibility(View.GONE);
            item.countComments = listComments.size();
        }

        /**
         * Vrátí správně url.
         * @return
         */
        private String getUrl() {
            String url = HttpConection.host + HttpConection.path + "/http-one-page/";

            if(item.isUserImage) {
                url = url + "user-image-comments/";
            }
            if(item.isStatus) {
                url = url + "status-comments/";
            }
            if(item.isConfession) {
                url = url + "confession-comments/";
            }

            return url;
        }

        /**
         * Vrátí správně nastavení parametry url.
         * @return
         */
        private List<NameValuePair> getParams() {
            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
            if(item.isUserImage) {
                urlParams.add(new BasicNameValuePair("imageId", Integer.toString(item.parentId)));
            }
            if(item.isStatus) {
                urlParams.add(new BasicNameValuePair("statusId", Integer.toString(item.parentId)));
            }
            if(item.isConfession) {
                urlParams.add(new BasicNameValuePair("confessionId", Integer.toString(item.parentId)));
            }

            return urlParams;
        }
    }

    /**
     * Odešle komentář.
     */
    class SendComment extends AsyncTask<String, String, String> {

        /**
         * Komentář co se má odeslat na server.
         */
        private Comment newComment;

        public SendComment(Comment newComment) {
            this.newComment = newComment;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = HttpConection.host + HttpConection.path + "/http-one-page/";
            List<NameValuePair> urlParams = getParams();

            HttpConection con = new HttpConection();
            con.makeHttpRequest(url, "GET", urlParams, httpContext);

            return null;
        }

        /**
         * Vrátí správně nastavení parametry url.
         * @return
         */
        private List<NameValuePair> getParams() {
            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

            urlParams.add(new BasicNameValuePair("comment", newComment.comment));

            if(item.isUserImage) {
                urlParams.add(new BasicNameValuePair("imageId", Integer.toString(item.parentId)));
                urlParams.add(new BasicNameValuePair("do", "addCommentUserGallery"));
            }
            if(item.isStatus) {
                urlParams.add(new BasicNameValuePair("statusId", Integer.toString(item.parentId)));
                urlParams.add(new BasicNameValuePair("do", "addCommentStatus"));
            }
            if(item.isConfession) {
                urlParams.add(new BasicNameValuePair("confessionId", Integer.toString(item.parentId)));
                urlParams.add(new BasicNameValuePair("do", "addCommentConfession"));
            }

            return urlParams;
        }
    }
}

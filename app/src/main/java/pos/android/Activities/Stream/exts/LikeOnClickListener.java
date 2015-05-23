package pos.android.Activities.Stream.exts;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.android.Http.HttpConection;
import pos.android.Http.JSONParser;

/**
 * Událost kliknutí na tlačítko like.
 * Created by Petr on 22.5.2015.
 */
public class LikeOnClickListener implements View.OnClickListener {

    private Context context;
    private Item item;
    private HttpContext httpContext;

    public LikeOnClickListener(Context context, Item item, HttpContext httpContext) {
        this.context = context;
        this.item = item;
        this.httpContext = httpContext;
    }

    @Override
    public void onClick(View v) {
        item.countLikes++;
        ((TextView)v).setText("Líbí ("  + item.countLikes + ")");
        v.invalidate();

        new LikeItem(httpContext).execute();

        Toast toast = Toast.makeText(context, item.name, Toast.LENGTH_SHORT);
        toast.show();
    }



    class LikeItem extends AsyncTask<String, String, String> {

        /**
         * Http kontext pro čtení dat přihlášeného uživatele.
         */
        private HttpContext httpContext;

        public LikeItem(HttpContext httpContext) {
            this.httpContext = httpContext;
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
         * Nastaví parametry do URL
         * @return urlParams
         */
        private List<NameValuePair> getParams() {
            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

            if(item.isUserImage) {
                urlParams.add(new BasicNameValuePair("imageID", Integer.toString(item.lastImageId)));
                urlParams.add(new BasicNameValuePair("ownerID", Integer.toString(item.userId)));
                urlParams.add(new BasicNameValuePair("do", "likeUserImage"));
            }
            if(item.isConfession) {
                urlParams.add(new BasicNameValuePair("confessionID", Integer.toString(item.parentId)));
                urlParams.add(new BasicNameValuePair("do", "likeStatus"));
            }
            if(item.isStatus) {
                urlParams.add(new BasicNameValuePair("statusID", Integer.toString(item.parentId)));
                urlParams.add(new BasicNameValuePair("ownerID", Integer.toString(item.userId)));
                urlParams.add(new BasicNameValuePair("do", "likeConfession"));
            }

            return urlParams;
        }
    }
}

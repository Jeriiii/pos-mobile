package pos.android.Activities.Stream.exts.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pos.android.Activities.Stream.exts.Comment.Comment;

/**
 * Created by Petr on 22.5.2015.
 * Převádí komentáře u příspěvku z formátu Json do ArrayList
 */
public class JsonToComments {

    /** Tagy aplikace. */
    private static final String TAG_STREAM_ITEMS = "data";

    /** Příspěvky ve formátu json */
    JSONArray items = null;

    /** Seznam komentářů u příspěvku. */
    ArrayList<Comment> listComments;

    public JsonToComments(ArrayList<Comment> listComments) {
        this.listComments = listComments;
    }

    /**
     * Uloží příspěvky.
     */
    public void saveToList(JSONObject jsonItems) {
        JSONObject c;
        int i;

        try {
            // products found
            // Getting Array of Products
            items = jsonItems.getJSONArray(TAG_STREAM_ITEMS);

            // looping through All Products
            for (i = 0; i < items.length(); i++) {

                c = items.getJSONObject(i);
                Comment comment = getItem(c);

                listComments.add(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Comment getItem(JSONObject jsonObject) throws JSONException{

        Comment comment = new Comment();
        comment.userId = jsonObject.getInt("userId");
        comment.userName = jsonObject.getString("userName");
        comment.comment = jsonObject.getString("comment");
        comment.countLikes = jsonObject.getInt("likes");

        return comment;
    }
}


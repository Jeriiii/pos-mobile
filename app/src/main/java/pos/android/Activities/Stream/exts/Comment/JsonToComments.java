package pos.android.Activities.Stream.exts.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pos.android.Activities.Stream.exts.Comment.Comment;

/**
 * Převádí komentáře u příspěvku z formátu Json do ArrayList
 * Created by Petr on 22.5.2015.
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
     * @param jsonItems Komentáře ve formátu JSON
     */
    public void saveToList(JSONObject jsonItems) {
        JSONObject c;
        int i;

        try {
            items = jsonItems.getJSONArray(TAG_STREAM_ITEMS);

            for (i = 0; i < items.length(); i++) {

                c = items.getJSONObject(i);
                Comment comment = getItem(c);

                listComments.add(comment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Vrátí komentář s daty poslané v JSONu
     * @param jsonObject JSON s daty komentáře.
     * @return Kometář.
     * @throws JSONException
     */
    private Comment getItem(JSONObject jsonObject) throws JSONException{

        Comment comment = new Comment();
        comment.userId = jsonObject.getInt("userId");
        comment.userName = jsonObject.getString("userName");
        comment.comment = jsonObject.getString("comment");
        comment.countLikes = jsonObject.getInt("likes");

        return comment;
    }
}


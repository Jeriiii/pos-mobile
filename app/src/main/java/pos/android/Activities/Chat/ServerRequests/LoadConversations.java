package pos.android.Activities.Chat.ServerRequests;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class LoadConversations extends ChatRequest {

    public static final String TAG_CONVERSATIONS = "conversations";

    private LinkedList<ConversationItem> list;
    private ConversationsAdapter adapter;
    private Activity activity;

    public LoadConversations(Context context, HttpContext httpContext, LinkedList<ConversationItem> list, ConversationsAdapter adapter, Activity activity){
        super(context, httpContext);
        this.list = list;
        this.adapter = adapter;
        this.activity = activity;
        setHandle("getConversations");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            JSONArray jarray = json.getJSONArray(TAG_CONVERSATIONS);
            int arrLength = jarray.length();
            for(int i = 0; i < arrLength; i++){
                addConversation(jarray.getJSONObject(i));
            }
            notifyAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addConversation(JSONObject conversation) throws JSONException{
        list.addLast(new ConversationItem(
            conversation.getString("from"),
            conversation.getString("lastMessage"),
            conversation.getInt("fromId"),
            conversation.getBoolean("readed")
        ));
    }

    private void notifyAdapter(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}

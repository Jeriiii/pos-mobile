package pos.android.Activities.Chat.ServerRequests;

import android.app.Activity;
import android.content.Context;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class LoadSingleConversation extends ChatRequest {

    public static final String TAG_CONVERSATION = "conversation";

    private LinkedList<MessageItem> list;
    private MessagesAdapter adapter;
    private Activity activity;

    public LoadSingleConversation(Context context, HttpContext httpContext, LinkedList<MessageItem> list, MessagesAdapter adapter, Activity activity){
        super(context, httpContext);
        this.list = list;
        this.adapter = adapter;
        this.activity = activity;
        setHandle("getSingleConversation");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            JSONArray jarray = json.getJSONArray(TAG_CONVERSATION);
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
        /*list.addLast(new ConversationItem(
            conversation.getString("from"),
            conversation.getString("lastMessage"),
            conversation.getInt("fromId"),
            conversation.getBoolean("readed")
        ));*/
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

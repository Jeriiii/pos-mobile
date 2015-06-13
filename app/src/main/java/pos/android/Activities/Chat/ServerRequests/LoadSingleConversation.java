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
    public static final String TAG_MESSAGES = "messages";

    private LinkedList<MessageItem> list;
    private MessagesAdapter adapter;
    private Activity activity;

    public LoadSingleConversation(Context context, HttpContext httpContext, LinkedList<MessageItem> list, MessagesAdapter adapter, Activity activity){
        super(context, httpContext);
        this.list = list;
        this.adapter = adapter;
        this.activity = activity;
        addParameter("fromId", "8447944");
        setHandle("getSingleConversation");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            JSONObject response = json.getJSONObject(TAG_CONVERSATION);
            JSONObject userInfo = response.getJSONObject("8447904");
            JSONArray messages = userInfo.getJSONArray(TAG_MESSAGES);

            int arrLength = messages.length();
            for(int i = 0; i < arrLength; i++){
                addMessage(messages.getJSONObject(i));
            }
            notifyAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(JSONObject message) throws JSONException{
        MessageItem.MessageType type = MessageItem.MessageType.TEXT;
        if(message.getInt("type") != 0){
            type = MessageItem.MessageType.INFO;
        }
        boolean fromMe = (message.getInt("fromMe") == 1);
        list.addLast(new MessageItem(
                fromMe ? "Já" : message.getString("name"),
                message.getString("text"),
                message.getInt("id"),
                type,
                (message.getInt("readed") == 1),
                fromMe,
                message.getString("sendedDate")
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

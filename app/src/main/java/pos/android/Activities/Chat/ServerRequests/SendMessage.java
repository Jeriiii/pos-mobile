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

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Http.HttpConection;

/**
 * Požadavek použitý k poslání zprávy na server.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class SendMessage extends ChatRequest {

    public static final String TEXT_MESSAGE_TYPE = "textMessage";

    private MessageItem message;
    private MessagesAdapter adapter;
    private Activity activity;

    /**
     * Konstruktor požadavku
     * @param context kontext, ze kterého se požadavek volá
     * @param httpContext http kontext použitý k odeslání požadavku
     * @param message zpráva, která má být odeslána na server
     * @param adapter adaptér starající se o seznam konverzací
     * @param activity aktivita, ze které se volá požadavek
     * @param userId id uživatele, kterému má být zpráva poslána
     */
    public SendMessage(Context context, HttpContext httpContext, MessageItem message, MessagesAdapter adapter, Activity activity, String userId){
        super(context, httpContext);
        this.message = message;
        this.adapter = adapter;
        this.activity = activity;
        if(message.getType() == MessageItem.MessageType.TEXT) {
            addParameter("type", TEXT_MESSAGE_TYPE);
        }else{
            addParameter("type", "");
        }
        addParameter("toId", userId);
        addParameter("text", message.messageText);
        setHandle("sendMessage");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            message.fromUserName = json.getString("senderName");
            message.messageId = json.getInt("id");
            message.messageTime = json.getString("sendedDate");
            notifyAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Upozornění adaptéru na změnu dat.
     */
    private void notifyAdapter(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}

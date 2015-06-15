package pos.android.Activities.Chat.ServerRequests;

import android.content.Context;

import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Noticing.INewMessageNoticable;
import pos.android.Activities.Chat.Noticing.IUnreadedCountNoticable;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class CheckNewMessages extends ChatRequest {

    public static final String TAG_MESSAGES = "messages";
    public static final String TAG_NEW_MESSAGES = "newMessages";
    public static final String TAG_UNREADED_MESSAGES_COUNT = "unreadedMessages";

    private INewMessageNoticable messageNotifier;
    private IUnreadedCountNoticable unreadedNotifier;

    public CheckNewMessages(Context context, HttpContext httpContext, INewMessageNoticable notifier, IUnreadedCountNoticable unreadedNotifier, int lastId){
        super(context, httpContext);
        this.messageNotifier = notifier;
        this.unreadedNotifier = unreadedNotifier;
        addParameter("lastId", lastId + "");
        setHandle("refreshMessages");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(JSONObject message) throws JSONException{
        MessageItem.MessageType type = MessageItem.MessageType.TEXT;
        if(message.getInt("type") != 0){
            type = MessageItem.MessageType.INFO;
        }
        new MessageItem(
                message.getString("name"),
                message.getString("text"),
                message.getInt("id"),
                type,
                (message.getInt("readed") == 1),
                (message.getInt("fromMe") == 1),
                message.getString("sendedDate")
        );
    }
}

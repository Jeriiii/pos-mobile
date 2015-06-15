package pos.android.Activities.Chat.ServerRequests;

import android.content.Context;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import pos.android.Activities.Chat.ChatManager;
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

    private boolean firstAsk = false;

    public CheckNewMessages(Context context, HttpContext httpContext, INewMessageNoticable notifier, IUnreadedCountNoticable unreadedNotifier, int lastId){
        super(context, httpContext);
        if(notifier == null || unreadedNotifier == null) throw new IllegalArgumentException("Notifikátory nemohou být null.");
        this.messageNotifier = notifier;
        this.unreadedNotifier = unreadedNotifier;
        firstAsk = (lastId == 0);
        addParameter("lastId", lastId + "");
        setHandle("refreshMessages");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            if(!checkFirstMessage()) {
                handleIncommingMessages();
            }
            notifyAboutUnreadedCount();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleIncommingMessages() throws JSONException{
        JSONArray sendersArray = json.getJSONArray(TAG_NEW_MESSAGES);
        if(sendersArray.getJSONArray(0).length() == 0){/* uživatel nemá vůbec žádné zprávy*/
            return;
        }
        JSONObject senders = sendersArray.getJSONObject(0);
        while(senders.keys().hasNext()){
            String senderKey = senders.keys().next();
            System.out.println(senderKey+"-----------------");
            JSONObject sender = senders.getJSONObject(senderKey);
            handleMessagesFromSender(senderKey, sender);
        }
    }

    private void handleMessagesFromSender(String senderKey, JSONObject sender) throws JSONException {
        LinkedList<MessageItem> list = new LinkedList<MessageItem>();
        JSONArray messages = sender.getJSONArray(TAG_MESSAGES);
        int arrLength = messages.length();
        for(int i = 0; i < arrLength; i++){
            list.add(convertToMessage(messages.getJSONObject(i)));
        }
        messageNotifier.incommingMessageFromUser(senderKey, sender.getString("name"), list);
    }

    private void notifyAboutUnreadedCount() throws JSONException {
        int unreaded = json.getInt(TAG_UNREADED_MESSAGES_COUNT);
        unreadedNotifier.onUnreadedCountIncomming(unreaded);
    }

    /**
     * Zpracování při prvním poslání zprávy
     * @return boolean zpráva byla první
     * */
    private boolean checkFirstMessage() throws JSONException{
        if(firstAsk){/* první zeptání, zpráva bude jenom jedna */
            JSONObject senders = json.getJSONArray(TAG_NEW_MESSAGES).getJSONObject(0);
            if(senders.length() == 0){/* uživatel nemá vůbec žádné zprávy*/
                return true;
            }
            JSONObject sender = senders.getJSONObject(senders.keys().next());
            ChatManager.lastId = sender.getJSONArray(TAG_MESSAGES).getJSONObject(0).getInt("id");/* nastaví podle zprávy poslední id */
            return true;
        }
        return false;
    }

    private MessageItem convertToMessage(JSONObject message) throws JSONException{
        MessageItem.MessageType type = MessageItem.MessageType.TEXT;
        if(message.getInt("type") != 0){
            type = MessageItem.MessageType.INFO;
        }
        return new MessageItem(
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

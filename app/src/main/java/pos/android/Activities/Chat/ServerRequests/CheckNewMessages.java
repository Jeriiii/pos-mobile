package pos.android.Activities.Chat.ServerRequests;

import android.content.Context;
import android.os.Message;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Noticing.INewMessageNoticable;
import pos.android.Activities.Chat.Noticing.IUnreadedCountNoticable;

/**
 * Požadavek, který se pravidelně volá, aby zjistil, zda má server nějaké nové zprávy.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class CheckNewMessages extends ChatRequest {

    public static final String TAG_MESSAGES = "messages";
    public static final String TAG_NEW_MESSAGES = "newMessages";
    public static final String TAG_UNREADED_MESSAGES_COUNT = "unreadedMessages";

    private INewMessageNoticable messageNotifier;
    private IUnreadedCountNoticable unreadedNotifier;

    private boolean firstAsk = false;

    /**
     * Konstruktor požadavku
     * @param context kontext, ze kterého se volá
     * @param httpContext http kontext, který je použit k požadavku
     * @param notifier notifikátor, který má být po odpovědi upozorněn na nové zprávy
     * @param unreadedNotifier notifikátor, který má být upozorněn na počet nepřečtených zpráv
     * @param lastId poslední (nejvyšší) id zprávy, které aplikace zná
     * @param readedMessages seznam již přečtených zpráv, o jejichž přečtení chceme informovat server (stačí vždy poslední zpráva z konverzace s daným uživatelem)
     */
    public CheckNewMessages(Context context, HttpContext httpContext, INewMessageNoticable notifier, IUnreadedCountNoticable unreadedNotifier, int lastId, HashMap<Integer, Integer> readedMessages){
        super(context, httpContext);
        if(notifier == null || unreadedNotifier == null) throw new IllegalArgumentException("Notifikátory nemohou být null.");
        this.messageNotifier = notifier;
        this.unreadedNotifier = unreadedNotifier;
        firstAsk = (lastId == 0);
        addParameter("lastId", lastId + "");
        for(Map.Entry<Integer, Integer> pair : readedMessages.entrySet()){
            addParameter("readedMessages[" + pair.getKey() + "]", pair.getValue() + "");
        }
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

    /**
     * Zpracuje příchozí json odpověď
     * @throws JSONException
     */
    private void handleIncommingMessages() throws JSONException{
        if(json.isNull(TAG_NEW_MESSAGES)){/* uživatel nemá vůbec žádné zprávy*/
            return;
        }
        JSONObject senders = json.getJSONObject(TAG_NEW_MESSAGES);
        Iterator<String> it = senders.keys();
        while(it.hasNext()){
            String senderKey = it.next();
            JSONObject sender = senders.getJSONObject(senderKey);
            handleMessagesFromSender(senderKey, sender);
        }
    }

    /**
     * Zpracuje tu část odpovědi, která obsahuje jediného uživatele
     * @param senderKey
     * @param sender
     * @throws JSONException
     */
    private void handleMessagesFromSender(String senderKey, JSONObject sender) throws JSONException {
        LinkedList<MessageItem> list = new LinkedList<MessageItem>();
        JSONArray messages = sender.getJSONArray(TAG_MESSAGES);
        int arrLength = messages.length();
        MessageItem message;
        for(int i = 0; i < arrLength; i++){
            message = convertToMessage(messages.getJSONObject(i));
            list.add(message);
            updateLastId(message.messageId);
        }
        messageNotifier.incommingMessageFromUser(senderKey, sender.getString("name"), list);
    }

    /**
     * Aktualizuje poslední známé id zprávy.
     * @param messageId
     */
    private void updateLastId(int messageId) {
        ChatManager.lastId = Math.max(ChatManager.lastId, messageId);
    }

    /**
     * Upozorní daný notifier na počet nepřečtených zpráv
     * @throws JSONException
     */
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
            if(json.isNull(TAG_NEW_MESSAGES)){/* uživatel nemá vůbec žádné zprávy*/
                return true;
            }
            JSONObject senders = json.getJSONObject(TAG_NEW_MESSAGES);
            JSONObject sender = senders.getJSONObject(senders.keys().next());
            updateLastId(sender.getJSONArray(TAG_MESSAGES).getJSONObject(0).getInt("id"));/* nastaví podle zprávy poslední id */
            return true;
        }
        return false;
    }

    /**
     * Překonvertuje zprávu z json formátu do MessageItemu
     * @param message zpráva z odpovědi serveru
     * @return
     * @throws JSONException
     */
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

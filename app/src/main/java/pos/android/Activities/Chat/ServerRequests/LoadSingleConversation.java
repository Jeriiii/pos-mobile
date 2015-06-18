package pos.android.Activities.Chat.ServerRequests;

import android.app.Activity;
import android.content.Context;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;

/**
 * Načte poslední zprávy s daným uživatelem do dané karty
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class LoadSingleConversation extends ChatRequest {

    public static final String TAG_CONVERSATION = "conversation";
    public static final String TAG_MESSAGES = "messages";

    private LinkedList<MessageItem> list;
    private MessagesAdapter adapter;
    private Activity activity;
    private String userId;

    /**
     * Konstruktor požadavku
     * @param context kontext, ze kterého se požadavek volá
     * @param httpContext http kontext použitý k volání
     * @param list seznam, který se má naplnit zprávami
     * @param adapter adaptér nad seznamem
     * @param activity aktivita, ze které se volá
     * @param userId id uživatele, se kterým si píšu
     */
    public LoadSingleConversation(Context context, HttpContext httpContext, LinkedList<MessageItem> list, MessagesAdapter adapter, Activity activity, String userId){
        super(context, httpContext);
        this.list = list;
        this.adapter = adapter;
        this.activity = activity;
        this.userId = userId;

        addParameter("fromId", userId);
        setHandle("getSingleConversation");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            JSONObject response = json.getJSONObject(TAG_CONVERSATION);
            JSONObject userInfo = response.getJSONObject(userId);
            JSONArray messages = userInfo.getJSONArray(TAG_MESSAGES);

            int arrLength = messages.length();
            for(int i = 0; i < arrLength; i++){
                addMessage(messages.getJSONObject(i));
            }
            ChatManager.getInstance().addReadedMessages(Integer.parseInt(userId), list.getLast().messageId);
            notifyAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Konvertuje zprávu z json formátu a přidá ji do seznamu
     * @param message
     * @throws JSONException
     */
    private void addMessage(JSONObject message) throws JSONException{
        MessageItem.MessageType type = MessageItem.MessageType.TEXT;
        if(message.getInt("type") != 0){
            type = MessageItem.MessageType.INFO;
        }
        list.addLast(new MessageItem(
                message.getString("name"),
                message.getString("text"),
                message.getInt("id"),
                type,
                (message.getInt("readed") == 1),
                (message.getInt("fromMe") == 1),
                message.getString("sendedDate")
        ));
    }

    /**
     * Upozorní adaptér na změnu dat
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

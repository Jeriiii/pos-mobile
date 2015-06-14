package pos.android.Activities.Chat;

import android.content.Context;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.protocol.HttpContext;

import java.util.LinkedList;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.Activities.Chat.ServerRequests.LoadSingleConversation;
import pos.android.Background.AutoRefresher;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 13.6.2015.
 */
public class ChatManager {


    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private AutoRefresher refresher;

    private ChatManager() {
        refresher = AutoRefresher.getInstance();/* start refreshování */
    }


    /**
     * Načte konverzace do daného listu
     * @param conversations list napojený na adaptér
     * @param adapter adaptér ovládající daný list
     * @param activity aktivita, ze které se volá načítání
     */
    public void loadConversations(LinkedList<ConversationItem> conversations, ConversationsAdapter adapter, ChatActivity activity) {
        new LoadConversations(activity.getApplicationContext(), activity.getHttpContext(), conversations, adapter, activity).execute();
    }


    /**
     * Načte zprávy do daného listu
     * @param messages list napojený na adaptér
     * @param adapter adaptér ovládající daný list
     * @param activity aktivita, ze které se volá načítání
     * @param userId id uživatele ve stringu (posílá se do url)
     */
    public void loadLastMessages(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity, String userId) {
        new LoadSingleConversation(activity.getApplicationContext(), activity.getHttpContext(), messages, adapter, activity, userId).execute();
    }

    public void loadOlderMessages(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity, String userId, Button moreMessagesButton) {
        Toast.makeText(activity.getBaseContext(), "textytext", Toast.LENGTH_LONG);
        messages.addFirst(new MessageItem("test", "textytext", 156, MessageItem.MessageType.TEXT, true, true, "ted"));
        messages.addFirst(new MessageItem("test", "textytext2", 156, MessageItem.MessageType.TEXT, true, true, "ted"));
    }

    public void sendMessage(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity, String userId, String text) {
        Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}

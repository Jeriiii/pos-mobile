package pos.android.Activities.Chat;

import android.content.Context;

import org.apache.http.protocol.HttpContext;

import java.util.LinkedList;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
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
     */
    public void loadLastMessages(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity) {

    }
}

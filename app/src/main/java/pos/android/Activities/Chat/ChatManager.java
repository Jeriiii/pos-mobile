package pos.android.Activities.Chat;

import android.os.Handler;
import android.widget.Button;

import java.util.LinkedList;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Noticing.INoticable;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.Activities.Chat.ServerRequests.LoadOlderMessages;
import pos.android.Activities.Chat.ServerRequests.LoadSingleConversation;
import pos.android.Activities.Chat.ServerRequests.SendMessage;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 13.6.2015.
 */
public class ChatManager implements Runnable{

    public static final int NUMBER_OF_OLDER_MESSAGES_LOADED = 5;


    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private Handler handler = new Handler();

    private ChatManager() {
        this.run();
    }

    /** Objekt, do kterého se pokud je nastaven pošlou nové zprávy */
    private INoticable activityNoticer = null;

    public void setActivityNoticer(INoticable activityNoticer) {
        this.activityNoticer = activityNoticer;
    }

    /** Vlákno pro refreshování */
    @Override
    public void run() {
        System.out.println("Running!");
        this.handleNewMessages();
        handler.postDelayed(this, 1000);
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
        int fromId = messages.getFirst().messageId;
        new LoadOlderMessages(activity.getApplicationContext(), activity.getHttpContext(), messages, adapter, activity, moreMessagesButton, userId, fromId, NUMBER_OF_OLDER_MESSAGES_LOADED).execute();
    }

    public void sendMessage(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity, String userId, String text) {
        MessageItem message = new MessageItem("", text, -1, MessageItem.MessageType.TEXT, true, true, "");
        messages.addLast(message);
        new SendMessage(activity.getApplicationContext(), activity.getHttpContext(), message, adapter, activity, userId).execute();
    }

    public void handleNewMessages() {
        if(activityNoticer != null){
            System.out.println("ChatActivity!");
        }else{
            System.out.println("Other activity!");
        }
    }
}

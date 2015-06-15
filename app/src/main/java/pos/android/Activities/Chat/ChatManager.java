package pos.android.Activities.Chat;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import org.apache.http.protocol.HttpContext;

import java.util.LinkedList;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Noticing.INewMessageNoticable;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.Noticing.IUnreadedCountNoticable;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.Activities.Chat.ServerRequests.LoadOlderMessages;
import pos.android.Activities.Chat.ServerRequests.LoadSingleConversation;
import pos.android.Activities.Chat.ServerRequests.SendMessage;
import pos.android.Http.PersistentCookieStore;
import pos.android.User.UserSessionManager;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 13.6.2015.
 */
public class ChatManager implements Runnable{

    public static final int NUMBER_OF_OLDER_MESSAGES_LOADED = 5;

    public static int lastId = 0;

    private int delayTime = 3000;


    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private Context applicationContext;



    private HttpContext httpContext;

    private Handler handler = new Handler();

    private ChatManager() {

    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    /** Objekt, do kterého se pokud je nastaven pošlou nové zprávy */
    private INewMessageNoticable messageNoticer = null;


    private IUnreadedCountNoticable unreadedNoticer = null;

    public synchronized void setMessageNoticer(INewMessageNoticable messageNoticer) {
        this.messageNoticer = messageNoticer;
    }

    public synchronized void setUnreadedNoticer(IUnreadedCountNoticable unreadedNoticer) {
        this.unreadedNoticer = unreadedNoticer;
    }


    /** Vlákno pro refreshování */
    @Override
    public void run() {
        System.out.println("Running!");
        this.handleNewMessages();
        handler.postDelayed(this, delayTime);
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

    public synchronized void  handleNewMessages() {
        UserSessionManager session = new UserSessionManager(applicationContext, new PersistentCookieStore(applicationContext));
        if(!session.isUserLoggedIn()){/* pro nepřihlášeného uživatele nedělá nic */
            return;
        }
        if(messageNoticer != null){
            noticeChatActivity();
        }else{
            noticeGlobally();
        }
    }

    private void noticeChatActivity() {

    }

    private void noticeGlobally() {

    }
}

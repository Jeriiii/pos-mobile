package pos.android.Activities.Chat;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import org.apache.http.protocol.HttpContext;

import java.util.LinkedList;

import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Noticing.GlobalNoticer;
import pos.android.Activities.Chat.Noticing.INewMessageNoticable;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.Noticing.IUnreadedCountNoticable;
import pos.android.Activities.Chat.ServerRequests.CheckNewMessages;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.Activities.Chat.ServerRequests.LoadOlderMessages;
import pos.android.Activities.Chat.ServerRequests.LoadSingleConversation;
import pos.android.Activities.Chat.ServerRequests.SendMessage;
import pos.android.Http.HttpConection;
import pos.android.Http.PersistentCookieStore;
import pos.android.User.UserSessionManager;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 13.6.2015.
 */
public class ChatManager implements Runnable{

    public static final int NUMBER_OF_OLDER_MESSAGES_LOADED = 5;

    /** minimální čas nastavitelný k opakování [ms]*/
    private static final int MINIMUM_DELAY_TIME = 500;

    /** maximální čas nastavitelný k opakování [ms]*/
    private static final int MAXIMUM_DELAY_TIME = 120000;
    private static final int INITIAL_DELAY_TIME = 3000;

    public static int lastId = 0;

    private int delayTime = INITIAL_DELAY_TIME;


    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private Context applicationContext;

    private GlobalNoticer globalNoticer = new GlobalNoticer();

    private Handler handler = new Handler();

    private ChatManager() {

    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
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

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        int value;
        value = Math.max(MINIMUM_DELAY_TIME, delayTime);
        value = Math.min(MAXIMUM_DELAY_TIME, value);
        this.delayTime = value;
    }

    public void incraseDelay(int by){
        setDelayTime(getDelayTime() + by);
    }

    public void resetDelay(){
        this.delayTime = INITIAL_DELAY_TIME;
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

    public synchronized void handleNewMessages() {
        UserSessionManager session = new UserSessionManager(applicationContext, new PersistentCookieStore(applicationContext));
        HttpContext httpContext = HttpConection.createHttpContext(applicationContext, false);
        if(!session.isUserLoggedIn()){/* pro nepřihlášeného uživatele nedělá nic */
            return;
        }
        if(messageNoticer != null && unreadedNoticer != null){
            noticeChatActivity(httpContext);
        }else{
            noticeGlobally(httpContext);
        }
    }

    private void noticeChatActivity(HttpContext httpContext) {
        new CheckNewMessages(applicationContext, httpContext, messageNoticer, unreadedNoticer, ChatManager.lastId).execute();
    }

    private void noticeGlobally(HttpContext httpContext) {
        new CheckNewMessages(applicationContext, httpContext, globalNoticer, globalNoticer, ChatManager.lastId).execute();
    }
}

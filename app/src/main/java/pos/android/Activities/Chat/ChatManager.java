package pos.android.Activities.Chat;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import org.apache.http.protocol.HttpContext;

import java.util.HashMap;
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
 * Správce chatu podle návrhového vzoru singleton - slouží jako abstrakce k operacím nad chatem - posílání zpráv, načítání zpráv apod.
 * Také se pravidelně ptá serveru na nové zprávy.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 13.6.2015.
 */
public class ChatManager implements Runnable{

    public static final int NUMBER_OF_OLDER_MESSAGES_LOADED = 5;

    /** minimální čas nastavitelný k opakování [ms]*/
    private static final int MINIMUM_DELAY_TIME = 500;

    /** maximální čas nastavitelný k opakování [ms]*/
    private static final int MAXIMUM_DELAY_TIME = 120000;

    /** počáteční čas nastavitelný k opakování [ms]*/
    private static final int INITIAL_DELAY_TIME = 3000;

    public static int lastId = 0;

    private int delayTime = INITIAL_DELAY_TIME;

    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private Context applicationContext;

    private final GlobalNoticer globalNoticer = new GlobalNoticer();

    private Handler handler = new Handler();

    private HashMap<Integer, Integer> readedMessages = new HashMap<Integer,Integer>();

    private ChatManager() {

    }

    /**
     * Nastaví kontext manageru a těm jeho třídám, které jej také potřebují
     * @param applicationContext
     */
    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
        globalNoticer.setContext(applicationContext);
    }

    /** Objekt, do kterého se pokud je nastaven pošlou nové zprávy */
    private INewMessageNoticable messageNoticer = null;


    private IUnreadedCountNoticable unreadedNoticer = null;

    /**
     * Nastaví objekt, který bude upozorňován na příchozí zprávy
     * @param messageNoticer
     */
    public synchronized void setMessageNoticer(INewMessageNoticable messageNoticer) {
        this.messageNoticer = messageNoticer;
    }

    /**
     * Nastaví objekt, který bude upozorňován na počet nepřečtených zpráv
     * @param unreadedNoticer
     */
    public synchronized void setUnreadedNoticer(IUnreadedCountNoticable unreadedNoticer) {
        this.unreadedNoticer = unreadedNoticer;
    }

    /**
     * Vrátí časové zpoždění mezi pravidelnými požadavky na server
     * @return
     */
    public int getDelayTime() {
        return delayTime;
    }

    /**
     * Nastaví časové zpoždění mezi pravidelnými požadavky na server. Toto zpoždění se bude pohybovat v daných mezích.
     * @param delayTime
     */
    public void setDelayTime(int delayTime) {
        int value;
        value = Math.max(MINIMUM_DELAY_TIME, delayTime);
        value = Math.min(MAXIMUM_DELAY_TIME, value);
        this.delayTime = value;
    }

    /**
     * Zvýší zpoždění mezi pravidelnými požadavky o určitý počet milisekund
     * @param by
     */
    public void incraseDelay(int by){
        setDelayTime(getDelayTime() + by);
    }

    /**
     * Resetuje zpoždění mezi pravidelnými požadavky na jeho počáteční hodnotu
     */
    public void resetDelay(){
        this.delayTime = INITIAL_DELAY_TIME;
    }

    /** Vlákno pro refreshování */
    @Override
    public void run() {
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

    /**
     * Načte starší zprávy do daného listu
     * @param messages list zpráv
     * @param adapter adapteré napojený na list
     * @param activity aktivita, ze které se volá načítání
     * @param userId id uživatele, se kterým si píšu ve stringu (posílá se do url)
     * @param moreMessagesButton tlačíko, které ovládá donačítání zpráv
     */
    public void loadOlderMessages(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity, String userId, Button moreMessagesButton) {
        int fromId = messages.getFirst().messageId;
        new LoadOlderMessages(activity.getApplicationContext(), activity.getHttpContext(), messages, adapter, activity, moreMessagesButton, userId, fromId, NUMBER_OF_OLDER_MESSAGES_LOADED).execute();
    }

    /**
     * Pošle zprávu na server
     * @param messages seznam zpráv, mezi které patří i nová zpráva
     * @param adapter adaptér napojený na seznam zpráv
     * @param activity aktivita, ze které se volá odesílání
     * @param userId id uživatele, kterému má být zpráva poslána
     * @param text text zprávy
     */
    public void sendMessage(LinkedList<MessageItem> messages, MessagesAdapter adapter, ChatActivity activity, String userId, String text) {
        MessageItem message = new MessageItem("", text, -1, MessageItem.MessageType.TEXT, true, true, "");
        messages.addLast(message);
        if(messageNoticer != null){
            messageNoticer.updateConversationsList(Integer.parseInt(userId), "", message);
        }
        new SendMessage(activity.getApplicationContext(), activity.getHttpContext(), message, adapter, activity, userId).execute();
    }

    /**
     * Zpracování nových zpráv, které pravidelně přicházejí. Pomocí dalších metod vyšle požadavek na server.
     */
    public synchronized void handleNewMessages() {
        UserSessionManager session = new UserSessionManager(applicationContext, new PersistentCookieStore(applicationContext));
        HttpContext httpContext = HttpConection.createHttpContext(applicationContext, false);
        if(!session.isUserLoggedIn()){/* pro nepřihlášeného uživatele nedělá nic */
            return;
        }
        if(messageNoticer != null){
            noticeChatActivity(httpContext);
        }else{
            noticeGlobally(httpContext);
        }
    }

    /**
     * Upozornění chat aktivity, že přišly nové zprávy a na informaci o počtu nepřečtených zpráv
     * @param httpContext
     */
    private void noticeChatActivity(HttpContext httpContext) {
        new CheckNewMessages(applicationContext, httpContext, messageNoticer, (unreadedNoticer != null)? unreadedNoticer : globalNoticer, ChatManager.lastId, readedMessages).execute();
        readedMessages.clear();
    }

    /**
     * Globálně upozorní na to, že přišly nové zprávy a na informaci o počtu nepřečtených zpráv
     * @param httpContext
     */
    private void noticeGlobally(HttpContext httpContext) {
        new CheckNewMessages(applicationContext, httpContext, globalNoticer, (unreadedNoticer != null)? unreadedNoticer : globalNoticer, ChatManager.lastId, readedMessages).execute();
        readedMessages.clear();
    }

    /**
     * Přidá do pravidelného požadavku informaci o tom, že uživatel
     * @param userId
     * @param lastId
     */
    public void addReadedMessages(int userId, int lastId) {
        readedMessages.put(userId, lastId);
    }
}

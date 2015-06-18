package pos.android.Activities.Chat.Noticing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Activities.Chat.ChatSlider.ChatPagerAdapter;
import pos.android.Activities.Chat.ChatSlider.ConversationsCardFragment;
import pos.android.Activities.Chat.ChatSlider.SingleConversationCardFragment;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Messages.MessageItem;

/**
 * Objekt implementující rozhraní, které je voláno při příchodu nových zpráv.
 * Používá se, když je aktivní aktivita s chatem.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.6.2015.
 */
public class NewMessagesNoticer implements INewMessageNoticable {

    private ChatPagerAdapter adapter;

    public NewMessagesNoticer(ChatActivity chatActivity) {
        this.adapter = chatActivity.getPagerAdapter();
    }

    @Override
    public void incommingMessageFromUser(String senderKey, String userName, List<MessageItem> messages) {
        int senderId = Integer.parseInt(senderKey);
        SingleConversationCardFragment card = adapter.getConversationFragment(senderId);
        if(card != null){
            addNewMessages(card, messages);
        }
        updateConversationsList(senderId, userName, messages.get(messages.size() - 1));
    }

    /**
     * Přidá příchozí zprávy do příslušné karty chatu
     * @param card karta chatu
     * @param messages seznam příchozích zpráv
     */
    private void addNewMessages(SingleConversationCardFragment card, List<MessageItem> messages){
        LinkedList<MessageItem> cardList = card.getMessages();
        cardList.addAll(messages);
        card.notifyAdapter();
    }

    /**
     * Aktualizuje seznam konverzací o dotyčnou zprávu - buď aktualizuje konverzaci již existujícího uživatele, nebo vytvoří
     * novou položku seznamu konverzací
     * @param senderId id dotyčného uživatele
     * @param userName jméno dotyčného uživatele
     * @param messageItem zpráva, která má aktualizovat položku uživatele v seznamu konverzací (typicky poslední příchozí zpráva)
     */
    public void updateConversationsList(int senderId, String userName, MessageItem messageItem) {
        ConversationsCardFragment conversationsFragment = adapter.getConversationsListFragment();
        LinkedList<ConversationItem> conversations = conversationsFragment.getConversationsList();
        ConversationItem targetItem = null;
        for(ConversationItem item : conversations){/* nalezení dotyčného itemu*/
            if(item.fromId == senderId){
                targetItem = item;
                break;
            }
        }
        if(targetItem == null){
            targetItem = new ConversationItem(userName, messageItem.messageText, senderId, false);
            conversations.addFirst(targetItem);
        }else{
            targetItem.lastMessage = messageItem.messageText;
            targetItem.readed = false;
        }
        conversationsFragment.notifyAdapter();
    }
}

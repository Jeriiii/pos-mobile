package pos.android.Activities.Chat.Noticing;

import java.util.LinkedList;
import java.util.List;

import pos.android.Activities.Chat.Messages.MessageItem;

/**
 * Objekt implementující toto rozhraní umí reagovat na přijmutí nových zpráv
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.6.2015.
 */
public interface INewMessageNoticable {

    /**
     * Nastává když přijdou nové zprávy od určitého uživatele
     * @param senderKey id odesílatele
     * @param userName jméno odesílatele
     * @param messages zprávy
     */
    void incommingMessageFromUser(String senderKey, String userName, List<MessageItem> messages);
}

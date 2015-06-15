package pos.android.Activities.Chat.Noticing;

import java.util.List;

import pos.android.Activities.Chat.Messages.MessageItem;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.6.2015.
 */
public class NewMessagesNoticer implements INewMessageNoticable {
    @Override
    public void incommingMessageFromUser(String senderKey, String userName, List<MessageItem> messages) {

    }
}

package pos.android.Activities.Chat.Noticing;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.R;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.6.2015.
 */
public class GlobalNoticer implements INewMessageNoticable, IUnreadedCountNoticable {

    private Context context;

    @Override
    public void incommingMessageFromUser(String senderKey, String userName, List<MessageItem> messages) {
        notifyAboutMessage(Integer.parseInt(senderKey), userName, messages.get(messages.size() - 1));
    }

    @Override
    public void onUnreadedCountIncomming(int unreadedCount) {
        //nedělá nic
    }


    private void notifyAboutMessage(int userId, String userName, MessageItem messageItem){
        Notification n;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        n = builder
                .setContentTitle(userName)
                .setContentText(messageItem.messageText)
                .setSmallIcon(R.drawable.ic_message)
                .build();
        n.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(userId, n);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

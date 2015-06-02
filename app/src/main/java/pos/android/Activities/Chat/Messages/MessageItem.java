package pos.android.Activities.Chat.Messages;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import pos.android.R;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class MessageItem {

    enum MessageType {
        TEXT, INFO
    }

    public static final int VIEW_ID = R.layout.chat_message_item;
    public String fromUserName = "";
    public String messageText = "";
    int messageId;
    MessageType type = MessageType.TEXT;
    boolean readed = false;
    boolean fromMe = false;
    String lastActive = "offline";
    String messageTime = "";

    public MessageItem(String fromUserName, String messageText, int messageId, MessageType type, boolean readed, boolean fromMe, String lastActive, String messageTime) {
        this.fromUserName = fromUserName;
        this.messageText = messageText;
        this.messageId = messageId;
        this.type = type;
        this.readed = readed;
        this.fromMe = fromMe;
        this.lastActive = lastActive;
        this.messageTime = messageTime;
    }

    /**
     * Naplní daný view svými daty. Daný view by měl být ten s id VIEW_ID
     * @param convertView
     * @return
     */
    public View createView(View convertView) {
        switch(type) {
            case TEXT:
            TextView TVSender = (TextView) convertView.findViewById(R.id.messageSender);
            TextView TVMessageText = (TextView) convertView.findViewById(R.id.messageText);
            TextView TVLastActive = (TextView) convertView.findViewById(R.id.chatLastActive);
            TextView TVMessageTime = (TextView) convertView.findViewById(R.id.chatMessageTime);
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.messageItemLayout);
            TVSender.setText(fromUserName);
            TVMessageText.setText(this.messageText);
            TVLastActive.setText(this.lastActive);
            TVMessageTime.setText(this.messageTime);
                break;
            case INFO:
                Toast.makeText(convertView.getContext(), messageText, Toast.LENGTH_SHORT);
                break;
        }
        return convertView;
    }
}
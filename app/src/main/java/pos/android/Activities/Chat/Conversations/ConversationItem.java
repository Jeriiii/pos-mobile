package pos.android.Activities.Chat.Conversations;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pos.android.R;

/**
 * Entita zastupující jedinou položku seznamu konverzací. Také umí vytvořit svůj view v seznamu
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class ConversationItem {

    public static final int VIEW_ID = R.layout.chat_conversation_item;
    public String userName = "";
    public String lastMessage = "";
    public int fromId;
    public boolean readed = false;

    public ConversationItem(String userName, String lastMessage, int fromId, boolean readed){
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.fromId = fromId;
        this.readed = readed;
    }

    /**
     * Naplní daný view svými daty. Daný view by měl být ten s id VIEW_ID
     * @param convertView
     * @return
     */
    public View createView(View convertView) {
        TextView TVName = (TextView) convertView.findViewById(R.id.conversationName);
        TextView TVLastMessage = (TextView) convertView.findViewById(R.id.conversationLastMessage);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.conversationItemLayout);
        TVName.setText(userName);
        TVLastMessage.setText(this.lastMessage);
        if(!this.readed){
            layout.setBackgroundColor(Color.parseColor("#efb3b3"));
        }
        return convertView;
    }

    /**
     * Vrátí id uživatele, se kterým probíhá tato konverzace
     * @return
     */
    public int getUserId(){
        return fromId;
    }
}

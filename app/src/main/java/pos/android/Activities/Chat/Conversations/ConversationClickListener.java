package pos.android.Activities.Chat.Conversations;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import pos.android.Activities.Chat.ChatActivity;


/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 30.5.2015.
 */
public class ConversationClickListener implements AdapterView.OnItemClickListener {

    private ChatActivity activity;

    public ConversationClickListener(ChatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConversationItem conversation = (ConversationItem)parent.getItemAtPosition(position);
        Toast.makeText(parent.getContext(), conversation.userName + conversation.fromId, Toast.LENGTH_LONG).show();
        activity.getPagerAdapter().addConversationCard(conversation.fromId, conversation.userName);
        activity.getTabs().notifyDataSetChanged();
    }
}

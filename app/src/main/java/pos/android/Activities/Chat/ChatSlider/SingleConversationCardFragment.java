package pos.android.Activities.Chat.ChatSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Conversations.ConversationClickListener;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Conversations.ConversationsList;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.Messages.MessagesList;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.Activities.Chat.ServerRequests.LoadSingleConversation;
import pos.android.R;

/**
 * Záložka jedné konverzace
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class SingleConversationCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static SingleConversationCardFragment newInstance(int position) {
        SingleConversationCardFragment f = new SingleConversationCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.chat_sinconversation_slide, container, false);
        final ChatActivity activity = (ChatActivity) this.getActivity();
        /* list */
        MessagesList list = (MessagesList) view.findViewById(R.id.list);
        LinkedList<MessageItem> messages = new LinkedList<MessageItem>();
        final MessagesAdapter adapter = new MessagesAdapter(activity, R.layout.chat_message_text_item, messages);
        list.setAdapter(adapter);
        position = getArguments().getInt(ARG_POSITION);

        /* closeButton */
        Button closeButton = (Button) view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getPagerAdapter().removeCard(position - ChatPagerAdapter.COUNT_OF_STATIC_TABS, activity.getPager(), activity.getTabs());
            }
        });

        ChatManager.getInstance().loadLastMessages(messages, adapter, activity);
        new LoadSingleConversation(activity.getApplicationContext(), activity.getHttpContext(), messages, adapter, activity).execute();
        return view;
    }

    public int getPosition(){
        return position;
    }


}

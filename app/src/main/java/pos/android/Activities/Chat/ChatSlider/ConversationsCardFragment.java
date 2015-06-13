package pos.android.Activities.Chat.ChatSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Conversations.ConversationClickListener;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Conversations.ConversationsList;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.R;

/**
 * Záložka se všemi konverzacemi
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class ConversationsCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static ConversationsCardFragment newInstance(int position) {
        ConversationsCardFragment fragment = new ConversationsCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.chat_conversations_slide, container, false);
        ChatActivity activity = (ChatActivity) this.getActivity();

        ConversationsList list = (ConversationsList) view.findViewById(R.id.list);
        LinkedList<ConversationItem> conversations = new LinkedList<ConversationItem>();
        ConversationsAdapter adapter = new ConversationsAdapter(activity, R.layout.chat_conversation_text_item, conversations);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new ConversationClickListener((ChatActivity)this.getActivity()));

        position = getArguments().getInt(ARG_POSITION);

        ChatManager.getInstance().loadConversations(conversations, adapter, activity);

        return view;
    }

}

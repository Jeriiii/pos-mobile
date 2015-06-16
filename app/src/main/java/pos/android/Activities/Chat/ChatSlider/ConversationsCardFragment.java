package pos.android.Activities.Chat.ChatSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Conversations.ConversationClickListener;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.R;

/**
 * Záložka se všemi konverzacemi
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class ConversationsCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private LinkedList<ConversationItem> conversations = new LinkedList<ConversationItem>();

    private int position;
    private ChatActivity activity;
    private ConversationsAdapter adapter;

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
        activity = (ChatActivity) this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.chat_conversations_slide, container, false);

        ListView list = (ListView) view.findViewById(R.id.list);
        adapter = new ConversationsAdapter(activity, R.layout.chat_conversation_text_item, conversations);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new ConversationClickListener((ChatActivity)this.getActivity()));

        position = getArguments().getInt(ARG_POSITION);

        ChatManager.getInstance().loadConversations(conversations, adapter, activity);

        return view;
    }

    public LinkedList<ConversationItem> getConversationsList() {
        return conversations;
    }

    public void notifyAdapter(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

}

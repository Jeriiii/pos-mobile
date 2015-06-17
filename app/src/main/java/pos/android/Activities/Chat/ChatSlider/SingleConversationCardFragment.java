package pos.android.Activities.Chat.ChatSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.LinkedList;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Conversations.ConversationClickListener;
import pos.android.Activities.Chat.Conversations.ConversationItem;
import pos.android.Activities.Chat.Conversations.ConversationsAdapter;
import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;
import pos.android.Activities.Chat.ServerRequests.LoadConversations;
import pos.android.Activities.Chat.ServerRequests.LoadSingleConversation;
import pos.android.R;

/**
 * Záložka jedné konverzace
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class SingleConversationCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_USER_ID_POSITION = "userId";

    private int position;
    private String userId;

    private LinkedList<MessageItem> messages = new LinkedList<MessageItem>();
    private MessagesAdapter adapter;
    private ChatActivity activity;

    public static SingleConversationCardFragment newInstance(int position, String userId) {
        SingleConversationCardFragment f = new SingleConversationCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_USER_ID_POSITION, userId);
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
        activity = (ChatActivity) this.getActivity();
        /* list */
        ListView list = (ListView) view.findViewById(R.id.list);
        adapter = new MessagesAdapter(activity, R.layout.chat_message_text_item, messages);
        list.setAdapter(adapter);
        position = getArguments().getInt(ARG_POSITION);
        userId = getArguments().getString(ARG_USER_ID_POSITION);
        addButtonClickListeners(view, activity);
        Button closeButton = (Button) view.findViewById(R.id.closeButton);
        closeButton.setText(userId);
        ChatManager.getInstance().loadLastMessages(messages, adapter, activity, userId);
        return view;
    }

    private void addButtonClickListeners(ViewGroup view, final ChatActivity activity) {
        /* closeButton */
        Button closeButton = (Button) view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getPagerAdapter().removeCard(position - ChatPagerAdapter.COUNT_OF_STATIC_TABS, activity.getPager(), activity.getTabs());
            }
        });
        /* more messages button */
        final Button moreMessagesButton = (Button) view.findViewById(R.id.moreMessagesButton);
        moreMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatManager.getInstance().loadOlderMessages(messages, adapter, activity, userId, moreMessagesButton);
                notifyAdapter();
            }
        });
        /* send new message form */
        Button sendButton = (Button) view.findViewById(R.id.sendMessageButton);
        final EditText messageInput = (EditText) view.findViewById(R.id.messageInput);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(messageInput);
            }
        });
        messageInput.setImeActionLabel("Odeslat", KeyEvent.KEYCODE_ENTER);
        messageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return sendMessage(v);
            }
        });
    }

    public void notifyAdapter() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private boolean sendMessage(TextView textInput){
        String text = textInput.getText().toString();
        if(text.isEmpty()){
            return false;
        }
        textInput.setText("");
        ChatManager.getInstance().sendMessage(messages, adapter, activity, userId, text);
        notifyAdapter();
        return true;
    }

    public int getPosition(){
        return position;
    }

    public int getUserId(){
        try {
            return Integer.parseInt(userId);
        }catch(NumberFormatException e){
            return -1;
        }
    }

    public LinkedList<MessageItem> getMessages(){
        return messages;
    }


}

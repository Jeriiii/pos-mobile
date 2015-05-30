package pos.android.Activities.Chat.Conversations;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 30.5.2015.
 */
public class ConversationClickListener implements AdapterView.OnItemClickListener {


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ConversationItem conversation = (ConversationItem)parent.getItemAtPosition(position);
        Toast.makeText(parent.getContext(), conversation.userName + conversation.fromId, Toast.LENGTH_LONG).show();
    }
}

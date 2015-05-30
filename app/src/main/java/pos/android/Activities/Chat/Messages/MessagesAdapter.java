package pos.android.Activities.Chat.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pos.android.Activities.Chat.Conversations.ConversationItem;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 25.5.2015.
 */
public class MessagesAdapter extends ArrayAdapter<MessageItem> {


    public MessagesAdapter(Context context, int resource, List<MessageItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MessageItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(MessageItem.VIEW_ID, parent, false);
            return item.createView(convertView);
        }
        return convertView;
    }
}

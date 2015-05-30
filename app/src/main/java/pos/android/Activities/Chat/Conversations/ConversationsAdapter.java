package pos.android.Activities.Chat.Conversations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pos.android.Activities.Stream.exts.Item.Item;
import pos.android.R;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 25.5.2015.
 */
public class ConversationsAdapter extends ArrayAdapter<ConversationItem> {


    public ConversationsAdapter(Context context, int resource, List<ConversationItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ConversationItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(ConversationItem.VIEW_ID, parent, false);
            return item.createView(convertView);
        }
        return convertView;
    }
}

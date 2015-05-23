package pos.android.Activities.Stream.exts.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pos.android.R;

/**
 * Created by Petr on 20.5.2015.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Comment comment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_stream_item, parent, false);
        }

        convertView.getLayoutParams().width = parent.getWidth();

        setTextView(convertView, comment.userName, R.id.userName);
        setTextView(convertView, comment.comment, R.id.comment);

        // Return the completed view to render on screen
        return convertView;
    }

    /**
     * Zobrazí políčko - text - obrázek, pokud existuje.
     * @param convertView
     * @param itemParam
     */
    private void setTextView(View convertView, String itemParam, int viewId) {
        TextView tvName = (TextView) convertView.findViewById(viewId);
        tvName.setText(itemParam);
    }
}
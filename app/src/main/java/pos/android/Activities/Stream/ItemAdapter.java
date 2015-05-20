package pos.android.Activities.Stream;

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

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_example_entry, parent);
        }
        // Lookup view for data population
        // Populate the data into the template view using the data object
        setTextView(convertView, item.name, R.id.name);
        setTextView(convertView, item.userName, R.id.userName);
        setTextView(convertView, item.message, R.id.message);

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
        if(! itemParam.equals("")) {
            tvName.setText(itemParam);
            if(itemParam.equals("message")) {
                itemParam = itemParam + "";
            }
        } else {
            tvName.setVisibility(View.GONE);
        }
    }
}
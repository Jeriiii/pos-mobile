package pos.android.Activities.Stream.exts;

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
    public int listHeight = 0;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_example_entry, parent, false);
        }

        /* přidá výšku elementu do výšky listu */
        listHeight = listHeight + getViewHeight(convertView);
        convertView.getLayoutParams().width = parent.getWidth();

        // Lookup view for data population
        // Populate the data into the template view using the data object
        //setTextView(convertView, Integer.toString(item.id), R.id.id);
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
        } else {
            //tvName.setVisibility(View.GONE);
        }
    }

    /**
     * Vrátí výšku seznamu.
     * @param mView
     * @return
     */
    private int getViewHeight(View mView) {
        mView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return mView.getMeasuredHeight();
    }
}
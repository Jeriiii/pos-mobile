package pos.android.Activities.Stream.exts;

import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import pos.android.Activities.Stream.StreamActivity;
import pos.android.Activities.Stream.exts.Item.ItemAdapter;

/**
 * Created by Petr on 21.5.2015.
 */
public class LayoutListener implements ViewTreeObserver.OnPreDrawListener {

    /* adapter seznamu ve streamu */
    ItemAdapter adapter;

    /* stream aktivita */
    StreamActivity activity;

    public LayoutListener(StreamActivity activity, ItemAdapter adapter) {
        this.adapter = adapter;
        this.activity = activity;
    }

    @Override
    public boolean onPreDraw() {
        //findViewById(R.id.layout_to_hide).setMinimumHeight(View.VISIBLE);
        ListView layout = (ListView)activity.findViewById(android.R.id.list);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = 10;

        layout.getViewTreeObserver().removeOnPreDrawListener( this );

        return true;

        //layout.getViewTreeObserver().removeOnGlobalLayoutListener( this );
    }
}

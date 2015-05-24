package pos.android.Activities.Stream.exts.Comment;

import android.widget.ScrollView;

import pos.android.R;

/**
 * Created by Petr on 23.5.2015.
 */
public class ScrollToAddComment implements Runnable {

    public ScrollView sv;

    @Override
    public void run() {
        sv.scrollTo(0, sv.getBottom());
    }
}

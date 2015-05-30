package pos.android.Activities.Chat.Conversations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Zobrazený list konverzací
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class ConversationsList extends ListView {

    public ConversationsList(Context context) {
        super(context);
        initialize();
    }

    public ConversationsList(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();

    }

    public ConversationsList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize() {
    }
}

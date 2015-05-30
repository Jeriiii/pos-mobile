package pos.android.Activities.Chat.Messages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Zobrazený list konverzací
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class MessagesList extends ListView {

    public MessagesList(Context context) {
        super(context);
    }

    public MessagesList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessagesList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

package pos.android.Activities.Chat;

import pos.android.Background.AutoRefresher;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 13.6.2015.
 */
public class ChatManager {


    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private AutoRefresher refresher;

    private ChatManager() {
        refresher = AutoRefresher.getInstance();/* start refreshování */
    }


}

package pos.android;

import android.app.Application;

import pos.android.Activities.Chat.ChatManager;

/**
 * Main celé aplikace
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class POSApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ChatManager.getInstance();//zavolání konstruktoru
    }
}

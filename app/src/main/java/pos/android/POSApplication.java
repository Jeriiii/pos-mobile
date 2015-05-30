package pos.android;

import android.app.Application;

import pos.android.Background.AutoRefresher;

/**
 * Main celé aplikace
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class POSApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AutoRefresher.getInstance();//zavolání konstruktoru
    }
}

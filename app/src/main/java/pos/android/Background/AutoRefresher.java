package pos.android.Background;

import android.os.Handler;

/**
 * Dotazuje se pravidelně na server.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 24.5.2015.
 */
public class AutoRefresher implements Runnable {
    private static AutoRefresher instance = new AutoRefresher();

    public static AutoRefresher getInstance() {
        return instance;
    }

    private Handler handler = new Handler();

    /**
     * Je zavolán jednou při startu aplikace
     */
    private AutoRefresher() {
        this.run();
    }

    @Override
    public void run() {
        System.out.println("Running!");
        handler.postDelayed(this, 1000);
    }
}

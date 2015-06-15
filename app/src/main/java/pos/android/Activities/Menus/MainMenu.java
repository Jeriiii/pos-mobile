package pos.android.Activities.Menus;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Noticing.UnreadedCountNoticer;
import pos.android.R;
import pos.android.Activities.Stream.StreamActivity;

/**
 * Created by Jan Kotalík on 9.5.2015.
 * Třída sloužící jako statická obsluha událostí nad hlavním menu v aktivitách. Aktivity volají tyto metody, aby bylo možné měnit chování menu na jednom místě.
 *
 * POZN. pokud menu přepíná pomalu i mimo emulátor, je možné, že je to touto třídou
 */
public class MainMenu {


    /**
     * Překrývá stejnojmennou metodu dané aktivity.
     * @param activity aktivita, která událost vyvolala
     * @param menu
     * @param menuInflater
     * @return
     */
    public static boolean onCreateOptionsMenu(Activity activity, Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu);
        ChatManager.getInstance().setUnreadedNoticer(new UnreadedCountNoticer());
        return true;
    }

    /**
     * Překrývá stejnojmennou metodu dané aktivity.
     * @param activity aktivita, která událost vyvolala
     * @param item předaný Item události
     * @return vrátí true pokud je možné přerušit vykonávání (tj. pokud i daná aktivita může vrátit true)
     */
    public static boolean onOptionsItemSelected(Activity activity, MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        Intent intent;
        switch (id){/* přesměrování na jinou aktivitu při kliknutí */
            case R.id.action_chat:
                intent = new Intent(activity, ChatActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.action_stream:
                intent = new Intent(activity, StreamActivity.class);
                activity.startActivity(intent);
                break;
        }
        return false;
    }
}

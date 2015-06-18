package pos.android.Activities.Chat.Noticing;

import android.view.MenuItem;

/**
 * Objekt používající se, když je v aplikaci otevřená nějaká aktivita. Reaguje na změny ohledně počtu nepřečtených zpráv.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.6.2015.
 */
public class UnreadedCountNoticer implements IUnreadedCountNoticable {


    private MenuItem menuItem;

    public UnreadedCountNoticer(MenuItem item) {
        this.menuItem = item;
    }

    @Override
    public void onUnreadedCountIncomming(int unreadedCount) {
        if(unreadedCount == 0){
            menuItem.setTitle("");
        }else{
            menuItem.setTitle(unreadedCount + "");
        }
    }
}

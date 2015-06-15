package pos.android.Activities.Chat.Noticing;

/**
 * Objekt implementující toto rozhraní umí regovat na přijmutí informace o počtu nepřečtených zpráv
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.6.2015.
 */
public interface IUnreadedCountNoticable {
    /**
     * Volá se když přijde informace o počtu nepřečtených zpráv
     * @param unreadedCount
     */
    void onUnreadedCountIncomming(int unreadedCount);
}

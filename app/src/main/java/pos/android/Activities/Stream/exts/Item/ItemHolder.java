package pos.android.Activities.Stream.exts.Item;

/**
 * Created by Petr on 22.5.2015.
 * Zapamatuje si jeden příspěvek. Používá se k předání příspěvku mezi třídami. Návrhový vzor jedináček.
 */
public class ItemHolder {
    /** Instance třídy */
    private static ItemHolder instance = null;

    /** Uchovaná instance Item */
    public Item item;

    private ItemHolder() {}

    public static ItemHolder getInstance() {
        if(instance == null) {
            instance = new ItemHolder();
        }
        return instance;
    }

}

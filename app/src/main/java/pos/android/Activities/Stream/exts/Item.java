package pos.android.Activities.Stream.exts;

/**
 * Created by Petr on 20.5.2015.
 */
public class Item {
    public int id;
    public String name = "";
    public String userName = "";
    public String message = "";

    public Item() {

    }

    public Item(String userName) {
        this.userName = userName;
    }

}
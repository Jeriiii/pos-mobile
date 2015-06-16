package pos.android.Activities.Stream.exts.Item;

/**
 * Created by Petr on 20.5.2015.
 */
public class Item {
    public int id;
    public int parentId;
    public int userId;
    public int countComments = 0;
    public int countLikes = 0;
    public String name = "";
    public String userName = "";
    public String message = "";
    public String imgUrl = null;

    /* specifické pro galerie */
    public int lastImageId;

    public boolean isStatus = false;
    public boolean isConfession = false;
    public boolean isUserImage = false;

    public Item() {

    }

    public Item(String userName) {
        this.userName = userName;
    }

}
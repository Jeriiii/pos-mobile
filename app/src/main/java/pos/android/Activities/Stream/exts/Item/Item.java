package pos.android.Activities.Stream.exts.Item;

/**
 * Přepravka na příspěvek.
 * Created by Petr on 20.5.2015.
 */
public class Item {
    /** id příspěvku */
    public int id;
    /** Id obrázku, statusu nebo přiznání. */
    public int parentId;
    /** Id uživatele. */
    public int userId;
    /** Počet komentářů */
    public int countComments = 0;
    /** Počet liků */
    public int countLikes = 0;
    /** Název příspěvku */
    public String name = "";
    /** Uživatelské jméno člověka co přispívá */
    public String userName = "";
    /** Zprává příspěvku */
    public String message = "";
    /** url k obrázku v příspěvku */
    public String imgUrl = null;

    /* specifické pro galerie */
    public int lastImageId;

    /** Jde o status? */
    public boolean isStatus = false;
    /** Jde o přiznání? */
    public boolean isConfession = false;
    /** Jde o obrázek v galerii? */
    public boolean isUserImage = false;

    public Item() {

    }

    public Item(String userName) {
        this.userName = userName;
    }

}
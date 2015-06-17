package pos.android.Activities.Stream.exts.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pos.android.Activities.Stream.exts.Item.Item;

/**
 * Převádí příspěvky ve formátu Json do ArrayList
 * Created by Petr on 22.5.2015.
 */
public class JsonToItems {

    /** Tagy aplikace. */
    private static final String TAG_STREAM_ITEMS = "data";

    /** Příspěvky ve formátu json */
    JSONArray items = null;

    /** Seznam příspěvků ve streamu. */
    ArrayList<Item> listItems;

    /** Seznam nově přidaých příspěvků */
    ArrayList<Item> newListItems;

    public JsonToItems(ArrayList<Item> listItems) {
        this.listItems = listItems;
    }

    /**
     * Uloží příspěvky z JSON do seznamu.
     */
    public ArrayList<Item> saveItemsToList(JSONObject jsonItems) {
        JSONObject c;
        int i;
        newListItems = new ArrayList<Item>();

        try {
            items = jsonItems.getJSONArray(TAG_STREAM_ITEMS);

            for (i = 0; i < items.length(); i++) {

                c = items.getJSONObject(i);
                Item item = getItem(c);

                listItems.add(item);
                newListItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newListItems;
    }

    /**
     * Vrátí správně nastavený příspěvek.
     * @param jsonObject JSON ze kterého se vytváří příspěvek.
     * @return Správně nastavený příspěvek podle svého typu.
     * @throws JSONException
     */
    private Item getItem(JSONObject jsonObject) throws JSONException{
        Item item = new Item();

        if(isset(jsonObject, "userGallery")) {
            item =  getUserGallery(jsonObject);
        }
        if(isset(jsonObject, "status")) {
            item = getStreamStatus(jsonObject);
        }
        if(isset(jsonObject, "confession")) {
            item = getConfession(jsonObject);
        }

        addUserData(item, jsonObject);
        item.id = jsonObject.getInt("id");
        item.name = jsonObject.getString("id") + " > " + item.name;

        return item;
    }

    /**
     * Přidá k příspěvku data o uživateli.
     * @param item Příspěvek, kam je potřeba přidat data.
     * @param jsonObject Objekt, ze kterého se data čtou.
     * @return Objekt, ke kterému byla přidána data o uživateli, pokud existují.
     * @throws JSONException
     */
    private Item addUserData(Item item, JSONObject jsonObject) throws JSONException{
        if(isset(jsonObject, "user")) {
            JSONObject userObj = jsonObject.getJSONObject("user");
            item.userName = userObj.getString("user_name");
        }

        return item;
    }

    /**
     * Vrátí, zda je JSON hodnota nastavená, nebo je null či false
     * @param jsonObject Objekt který se prohledává.
     * @param paramName Název parametru, který se hledá.
     * @return TRUE = hodnota je nastavená, jinak FALSE
     */
    private boolean isset(JSONObject jsonObject, String paramName) throws JSONException {
        if(!jsonObject.has(paramName)) {
            return false;
        }

        String user = jsonObject.getString(paramName);
        if(! user.equals("null") && ! user.equals("false")) {
            return true;
        }

        return false;
    }

    /**
     * Vrátí uživatelskou galerii.
     * @param jsonObject JSON ze kterého se vytváří příspěvek.
     * @return Nastavený příspěvek jako galerie.
     * @throws JSONException
     */
    private Item getUserGallery(JSONObject jsonObject) throws JSONException{
        JSONObject galleryObject = jsonObject.getJSONObject("userGallery");
        String name = galleryObject.getString("name");
        Item gallery = new Item(name);

        gallery.name = "Galerie";
        gallery.parentId = galleryObject.getInt("id");
        gallery.isUserImage = true;

        /* nastavení liků a komentářů */
        if(isset(galleryObject, "lastImage")) {
            JSONObject lastImage = galleryObject.getJSONObject("lastImage");

            gallery.countLikes = lastImage.getInt("likes");
            gallery.countComments = lastImage.getInt("comments");
            gallery.lastImageId = lastImage.getInt("id");
            gallery.imgUrl = jsonObject.getString("imgUrl");
        }

        return gallery;
    }

    /**
     * Vrátí uživatelský status.
     * @param jsonObject JSON ze kterého se vytváří příspěvek.
     * @return Nastavený příspěvek jako status.
     * @throws JSONException
     */
    private Item getStreamStatus(JSONObject jsonObject) throws JSONException{
        JSONObject statusObject = jsonObject.getJSONObject("status");
        String message = statusObject.getString("message");

        Item status = new Item();
        status.isStatus = true;
        status.parentId = statusObject.getInt("id");
        status.message = message.trim();
        status.name = "Status";
        status.countLikes = statusObject.getInt("likes");
        status.countComments = statusObject.getInt("comments");

        return status;
    }

    /**
     * Vrátí přiznání.
     * @param jsonObject JSON ze kterého se vytváří příspěvek.
     * @return Nastavený příspěvek jako přiznání.
     * @throws JSONException
     */
    private Item getConfession(JSONObject jsonObject) throws JSONException{
        JSONObject confObject = jsonObject.getJSONObject("confession");
        String message = confObject.getString("note");

        Item conf = new Item();
        conf.isConfession = true;
        conf.parentId = confObject.getInt("id");
        conf.message = message.trim();
        conf.name = "Přiznání";
        conf.countLikes = confObject.getInt("likes");
        conf.countComments = confObject.getInt("comments");

        return conf;
    }

    /**
     * Rozhodne, zda jsou data ze streamu důležitá k uložení nebo ne.
     * @param name Název dat ze streamu k posouzení.
     * @param var Data ze streamu k posouzení.
     * @return TRUE = data se mají uložit k pozdějšímu zobrazení, jinak FALSE
     */
    private boolean isImportant(String name, String var) {
        if(var.equals("null") || var.equals("false")) {
            return false;
        }
        if(name.equals("tallness")) {
            return false;
        }
        if(name.equals("categoryID")) {
            return false;
        }
        if(name.equals("type")) {
            return false;
        }

        return true;
    }

}


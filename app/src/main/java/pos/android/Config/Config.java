package pos.android.Config;

/**
 * Created by Petr on 3.6.2015.
 */
public class Config {

    /********************** presentery **************************/
    // presenter se streamem
    public static final String pres_http_one_page = "/http-one-page";
    // presenter pro přihlášení
    public static final String pres_sign = "/sign";

    /*********************** signály ***************************/
    // signál pro upload obrázku
    public static final String sig_upload_image = "/?do=uploadImage";

    // signál pro přihlášení
    public static final String sig_sign_in = "/in?do=signInForm-submit";

    /************************ složky **********************************/
    public static final String IMAGE_DIRECTORY_NAME = "images";
}

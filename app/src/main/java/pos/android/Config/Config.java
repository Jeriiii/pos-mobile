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
    // presenter se streamem
    public static final String pres_one_page = "/one-page";

    /*********************** signály ***************************/
    // signál pro upload obrázku
    public static final String sig_upload_image = "/?do=uploadImage";

    // signál pro přihlášení
    public static final String sig_sign_in = "/in?do=signInForm-submit";

    /********************** rendery ******************************/
    public static final String ren_stream_json = "/stream-in-json";

    /************************ složky **********************************/
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    /************************ pro upload obrázků **********************/
    public static int LOAD_IMAGE_RESULTS = 1;
}

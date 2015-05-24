package pos.android.Http;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.android.Activities.Chat.ChatActivity;
import pos.android.Http.JSONParser;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class DatenodeJsonRequest extends AsyncTask<String, String, String> {

    private List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

    protected Context context;
    private HttpContext httpContext;

    private JSONParser jsonParser = new JSONParser();
    /** vraceny json */
    protected JSONObject json;

    public DatenodeJsonRequest(Context context, HttpContext httpContext) {
        this.context = context;
        this.httpContext = httpContext;
    }

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     * */
    @Override
    protected String doInBackground(String... args) {
        String url = this.getContextUrl();

        json = jsonParser.getJSONmakeHttpRequest(url, "GET", this.urlParams, this.httpContext);
        return null;
    }

    protected void addParameter(String name, String value){
        this.urlParams.add(new BasicNameValuePair(this.getUrlParametersPrefix() + name, value));
    }

    /*********** PREKRYVATELNE GETRY ********************/
    protected String getUrlDomain(){
        return HttpConection.host;
    }

    protected String getUrlFilePath(){ return HttpConection.path + "/"; }

    /* pouzitelne napriklad pri volani komponent, abych nemusel do parametru psat porad to same */
    protected String getUrlParametersPrefix(){
        return "";
    }
    /*************************************************/

    /**
     * Vrátí url, které se týká aktuální dotaz
     * @return String url
     */
    protected String getContextUrl(){
        String url = getUrlDomain() + getUrlFilePath();

        return url;
    }

    /**
     * Zkontroluje, jestli je vrácený json validní
     * @return
     */
    protected boolean jsonIsValid(){
        if(this.json == null){
            System.out.println("REQUEST ERROR: Invalid json.");
            return false;
        }
        return true;
    }

}

package pos.android.Http;

/**
 * Created by Petr on 27.4.2015.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pos.android.Http.HttpConection;

public class JSONParser extends HttpConection {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject getJSONmakeHttpRequest(String url, String method, List<NameValuePair> params) {
        HttpContext httpContext = this.createHttpContext();

        return this.getJSONmakeHttpRequest(url, method, params, httpContext);
    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject getJSONmakeHttpRequest(String url, String method,
                                      List<NameValuePair> params, HttpContext httpContext) {

        // Making HTTP request
        try {
            HttpResponse httpResponse = super.makeHttpRequest(url, method, params, httpContext);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            int status = httpResponse.getStatusLine().getStatusCode();
            if(status >= 400) {
                return null; //nedokázal jsem se připojit
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}

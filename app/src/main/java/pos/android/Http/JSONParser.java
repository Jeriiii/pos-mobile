package pos.android.Http;

/**
 * Převádí response na JSON objekty.
 *
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

    public JSONParser() {

    }

    /**
     * Pošle Http request a odpověď zpracuje jako JSON
     * @param url Url, kam se má poslat.
     * @param method Metoda GET nebo POST
     * @param params Parametry requestu.
     * @param httpContext Kontext se kterým má request pracovat.
     * @return JSON objekt
     */
    public JSONObject getJSONmakeHttpRequest(String url, String method,
                                      List<NameValuePair> params, HttpContext httpContext) {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // Pošle HTTP request
        try {
            HttpResponse httpResponse = super.makeHttpRequest(url, method, params, httpContext);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
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

        /* Zkusí parsovat JSON Objekt */
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;

    }
}

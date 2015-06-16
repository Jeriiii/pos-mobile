package pos.android.Http;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.CookieStore;
import java.util.List;


/**
 * Created by Petr on 27.4.2015.
 */
public class HttpConection {

    static InputStream is = null;

    public static final String host = "http://10.0.2.2";
    public static final String path = "/nette/pos/public/www";

    /**
     * Vytvoří nový http context
     */
    public static HttpContext createHttpContext(Context context, boolean clear) {
        HttpContext httpContext = new BasicHttpContext();
        CookieStore cookieStore = new PersistentCookieStore(context);
        if(clear) {
            cookieStore.clear();
        }

        //CookieStore cookieStore = new BasicCookieStore();

        //cookieStore.clear();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        return httpContext;
    }

    /*public HttpResponse makeHttpRequest(String url, String method,
                                        List<NameValuePair> params) {

        HttpContext httpContext = this.createHttpContext();

        return this.makeHttpRequest(url, method, params, httpContext);
    }*/

    // by making HTTP POST or GET mehtod
    public HttpResponse makeHttpRequest(String url, String method,
                                      List<NameValuePair> params, HttpContext httpContext) {

        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                return httpClient.execute(httpPost, httpContext);

            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                return httpClient.execute(httpGet, httpContext);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}

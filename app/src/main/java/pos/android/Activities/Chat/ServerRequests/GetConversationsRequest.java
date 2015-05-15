package pos.android.Activities.Chat.ServerRequests;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.protocol.HttpContext;
import org.json.JSONException;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class GetConversationsRequest extends ChatRequest {

    public GetConversationsRequest(Context context, HttpContext httpContext){
        super(context, httpContext);
        addParameter("do", "test");
    }


    protected void onPostExecute(String file_url) {
        try {
            Toast.makeText(this.context, json.getString("key"), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

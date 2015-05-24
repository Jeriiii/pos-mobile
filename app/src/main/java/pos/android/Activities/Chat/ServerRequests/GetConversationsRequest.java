package pos.android.Activities.Chat.ServerRequests;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class GetConversationsRequest extends ChatRequest {

    public static final String TAG_CONVERSATIONS = "conversations";

    public GetConversationsRequest(Context context, HttpContext httpContext){
        super(context, httpContext);
        setHandle("getConversations");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            JSONArray jarray = json.getJSONArray(TAG_CONVERSATIONS);
            int arrLength = json.length();
            for(int i = 0; i < arrLength; i++){
                addConversation(jarray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addConversation(JSONObject conversation) throws JSONException{
        Toast.makeText(this.context, conversation.getString("from"), Toast.LENGTH_LONG).show();
    }
}

package pos.android.Activities.Chat.ServerRequests;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import pos.android.Activities.Chat.Messages.MessageItem;
import pos.android.Activities.Chat.Messages.MessagesAdapter;

/**
 * Požadavek na server používaný k načítání starších zpráv.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class LoadOlderMessages extends ChatRequest {

    public static final String TAG_MESSAGES = "messages";
    public static final String TAG_OLDER_MESSAGES = "oldermessages";

    private int limit;
    private LinkedList<MessageItem> list;
    private MessagesAdapter adapter;
    private Activity activity;
    private String idUser;
    private Button moreButton;

    /**
     * Konstruktor požadavku
     * @param context kontext, ze kterého se volá požadavek
     * @param httpContext http kontext, kterým se volá
     * @param list seznam zpráv, který má být naplněn požadavkem
     * @param adapter adaptér pověšený na seznam zpráv
     * @param activity aktivita, ze které se požadavek volá
     * @param moreMessagesButton tlačítko, které má zmizet, když starší zprávy neexistují
     * @param idUser id uživatele, se kterým si píšu
     * @param lastId poslední známé id zprávy (nejmenší)
     * @param limit počet zpráv, které má požadavek přidat
     */
    public LoadOlderMessages(Context context, HttpContext httpContext, LinkedList<MessageItem> list, MessagesAdapter adapter, Activity activity, Button moreMessagesButton, String idUser, int lastId, int limit){
        super(context, httpContext);
        this.adapter = adapter;
        this.activity = activity;
        this.idUser = idUser;
        this.list = list;
        this.limit = limit;
        this.moreButton = moreMessagesButton;
        addParameter("lastId", lastId + "");
        addParameter("withUserId", idUser);
        addParameter("limit", limit + 1 + "");/* o jedna větší, aby se zjistilo, jestli jsou k dispozici i další */
        setHandle("getOlderMessages");
    }

    protected void onPostExecute(String file_url) {
        if(!jsonIsValid()){
            return;
        }
        try {
            if(json.isNull(TAG_OLDER_MESSAGES)){/* objekt přišel prázdný */
                moreButton.setVisibility(View.GONE);/* zmizení tlačítka */
                return;
            }
            int condition;
            JSONArray messages = json.getJSONObject(TAG_OLDER_MESSAGES).getJSONObject(idUser).getJSONArray(TAG_MESSAGES);
            int arrLength = messages.length();
            if(arrLength == limit + 1){/* opravdu jich přišlo o jeden víc */
                condition = arrLength - 1;/* je jich o jeden (první) víc, takže ho vynechává */
            }else{/* je jich míň */
                condition = arrLength;
                moreButton.setVisibility(View.GONE);/* zmizení tlačítka */
            }
            for(int i = 0; i < condition; i++){
                addMessage(messages.getJSONObject(arrLength - i - 1));/* prochází se obráceně*/
            }
            notifyAdapter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Konvertuje zprávu z json formátu do MessageItem a přidá ji do seznamu zpráv
     * @param message
     * @throws JSONException
     */
    private void addMessage(JSONObject message) throws JSONException{
        MessageItem.MessageType type = MessageItem.MessageType.TEXT;
        if(message.getInt("type") != 0){
            type = MessageItem.MessageType.INFO;
        }
        list.addFirst(new MessageItem(
                message.getString("name"),
                message.getString("text"),
                message.getInt("id"),
                type,
                (message.getInt("readed") == 1),
                (message.getInt("fromMe") == 1),
                message.getString("sendedDate")
        ));
    }

    /**
     * Upozorní daný adaptér na změnu dat.
     */
    private void notifyAdapter(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}

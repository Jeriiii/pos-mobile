package pos.android.Activities.Chat.ServerRequests;

import android.content.Context;

import org.apache.http.protocol.HttpContext;

import pos.android.Http.DatenodeJsonRequest;
import pos.android.Http.HttpConection;

/**
 *
 * Pta se na konverzace ze serveru
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 15.5.2015.
 */
public class ChatRequest extends DatenodeJsonRequest{

    public ChatRequest(Context context, HttpContext httpContext) {
        super(context, httpContext);
    }

    @Override
    protected String getUrlFilePath(){
        return HttpConection.path + "/chat/android";
    }

    @Override
    protected String getUrlParametersPrefix(){
        return "androidChat-androidCommunicator-";
    }
}
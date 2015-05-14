package pos.android.Activities.Stream;

import android.os.Bundle;


import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pos.android.Activities.BaseActivities.BaseActivity;
import pos.android.Http.JSONParser;
import pos.android.R;

public class StreamActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

       /* String url = "http://priznaniosexu.cz/one-page/stream-in-json";

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

        JSONParser con = new JSONParser();
        JSONObject json = con.getJSONmakeHttpRequest(url, "GET", urlParams, httpContext);*/
    }

}

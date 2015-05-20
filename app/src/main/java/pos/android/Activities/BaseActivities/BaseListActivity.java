package pos.android.Activities.BaseActivities;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.apache.http.protocol.HttpContext;

import pos.android.Activities.BaseActivities.BaseActivity;
import pos.android.Http.HttpConection;
import pos.android.Http.PersistentCookieStore;
import pos.android.User.UserSessionManager;

/**
 * Created by Petr on 14.5.2015.
 * Aktivita pro zobrazení seznamu položek.
 */
public class BaseListActivity extends ListActivity {
    /** Údaje o přihlášeném uživateli. */
    protected UserSessionManager session;

    /** httpContext pro posílání požadavků */
    protected HttpContext httpContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* načtení přihlášeného uživatele */
        UserSessionManager session = new UserSessionManager(
                this.getApplicationContext(),
                new PersistentCookieStore(getApplicationContext())
        );

        /* načtení httpContextu pro posílání požadavků */
        httpContext = HttpConection.createHttpContext(getApplicationContext(), false);
    }
}

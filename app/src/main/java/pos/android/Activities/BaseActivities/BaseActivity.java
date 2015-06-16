package pos.android.Activities.BaseActivities;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.HttpContext;

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Menus.MainMenu;
import pos.android.Http.HttpConection;
import pos.android.Http.PersistentCookieStore;
import pos.android.R;
import pos.android.User.UserSessionManager;

/////////////////////////////


/**
 * A login screen that offers login via email/password.
 */
public class BaseActivity extends Activity implements LoaderCallbacks<Cursor> {

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
        ChatManager.getInstance().setApplicationContext(getApplicationContext());
    }

    /**
     * Horní menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return MainMenu.onCreateOptionsMenu(this, menu, getMenuInflater());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean done = MainMenu.onOptionsItemSelected(this, item);

        if (item.getItemId()== R.id.action_logout)
        {
            UserSessionManager session = new UserSessionManager(
                    this.getApplicationContext(),
                    new PersistentCookieStore(getApplicationContext())
            );
            session.logoutUser();
        }

        if(done){
            return done;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
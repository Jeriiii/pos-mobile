package pos.android.Activities.BaseActivities;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.apache.http.protocol.HttpContext;

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Menus.MainMenu;
import pos.android.Http.HttpConection;
import pos.android.Http.PersistentCookieStore;
import pos.android.R;
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

    // This is the Adapter being used to display the list's data
    protected ArrayAdapter adapter;

    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adapter = adapter;

         /* načtení přihlášeného uživatele */
        UserSessionManager session = new UserSessionManager(
                this.getApplicationContext(),
                new PersistentCookieStore(getApplicationContext())
        );

        /* načtení httpContextu pro posílání požadavků */
        httpContext = HttpConection.createHttpContext(getApplicationContext(), false);

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);
        ChatManager.getInstance().setApplicationContext(getApplicationContext());
    }

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    /**
     * Načte nově přidané příspěvky do streamu.
     */
    public void notifyAdapter() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

 /*   // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }*/

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
}

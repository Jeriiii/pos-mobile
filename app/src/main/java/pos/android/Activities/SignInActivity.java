package pos.android.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import pos.android.Activities.Stream.StreamActivity;
import pos.android.Config.Config;
import pos.android.Http.JSONParser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pos.android.Http.HttpConection;
import pos.android.Http.PersistentCookieStore;
import pos.android.R;
import pos.android.User.UserSessionManager;


/**
 * Pro přihlášení uživatele.
 */
public class SignInActivity extends Activity implements LoaderCallbacks<Cursor> {

    /** Asynchroní task pro přihlášení na server */
    private UserLoginTask mAuthTask = null;

    // UI
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loginRouter();

        setContentView(R.layout.activity_sign_in);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Slouží pro rozhodnutí, zda není uživatel již přihlášen.
     */
    private void loginRouter() {
        PersistentCookieStore mCookieStore = new PersistentCookieStore(
                getApplicationContext());

        mCookieStore.clearExpired(new Date());
        List<Cookie> cookies = mCookieStore.getCookies();

        for (Cookie c : cookies) {
            if (c.getName().equals("PHPSESSID")) {
                startActivity(new Intent(this, StreamActivity.class));
                finish();
            }
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Obsloužení odeslání formuláře pro přihlášení.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Resetování chybových hlášek.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            //Tady se zobrazí error a focus se zaměří na první pole s errorem
            focusView.requestFocus();
        } else {
            //Pokusí se o přihlášení na server na pozadí
            showProgress(true);
            HttpContext httpContext = HttpConection.createHttpContext(getApplicationContext(), true);
            UserSessionManager session = new UserSessionManager(
                    this.getApplicationContext(),
                    (PersistentCookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE)
            );
            mAuthTask = new UserLoginTask(email, password, httpContext, session);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Rozhoduje, zda je email validní
     * @param email Emailová adreasa.
     * @return TRUE = validní, jinak FALSE
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Rozhoduje, zda je heslo validní.
     * @param password Heslo.
     * @return TRUE = validní, jinak FALSE
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 3;
    }

    /**
     * Zobrazí progress bar dokud se nedokončí přihlášení
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /**
     * Generovaná metoda
     */
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Generovaná metoda
     */
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(SignInActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Asynchroní přihlášení na server.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        /** email */
        private final String mEmail;
        /** heslo */
        private final String mPassword;
        /** http kontext celé aplikace */
        private HttpContext httpContext;
        /** session celé aplikace */
        private UserSessionManager session;

        UserLoginTask(String email, String password, HttpContext httpContext, UserSessionManager session) {
            mEmail = email;
            mPassword = password;

            this.httpContext = httpContext;
            this.session = session;
        }

        /**
         * Samotné přihlášení http požadavkem.
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = HttpConection.host + HttpConection.path + Config.pres_sign + Config.sig_sign_in;

            List<NameValuePair> urlParams = new ArrayList<NameValuePair>();

            urlParams.add(new BasicNameValuePair("signEmail", mEmail));
            urlParams.add(new BasicNameValuePair("signPassword", mPassword));
            urlParams.add(new BasicNameValuePair("mobile", "true"));

            JSONParser con = new JSONParser();
            JSONObject json = con.getJSONmakeHttpRequest(url, "POST", urlParams, httpContext);
            try {
                if (((Integer) json.get("success")) == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        /**
         * Uložení přihlášeného uživatele nebo nastavení chyby.
         * @param success TRUE = přihlášení proběhlo úspěšně.
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                session.createUserLoginSession(null, mEmail, mPassword);

                Intent i = new Intent(getApplicationContext(), StreamActivity.class);
                startActivity(i);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}







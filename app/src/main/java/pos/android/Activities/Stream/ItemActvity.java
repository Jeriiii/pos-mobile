package pos.android.Activities.Stream;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import pos.android.Activities.BaseActivities.BaseActivity;
import pos.android.Activities.Stream.exts.Item;
import pos.android.Activities.Stream.exts.ItemHolder;
import pos.android.R;

public class ItemActvity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_actvity);

        Item item = ItemHolder.getInstance().item;

        ((TextView)findViewById(R.id.name)).setText(item.name);
        ((TextView)findViewById(R.id.message)).setText(item.message);
        ((TextView)findViewById(R.id.userName)).setText(item.userName);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_actvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

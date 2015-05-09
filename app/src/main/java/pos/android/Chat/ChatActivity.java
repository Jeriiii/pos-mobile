package pos.android.Chat;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import pos.android.BaseActivity;
import pos.android.R;

public class ChatActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

    }

}

class MyPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
            "Top New Free", "Trending" };

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        return SuperAwesomeCardFragment.newInstance(position);
    }

}

class SuperAwesomeCardFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;

    public static SuperAwesomeCardFragment newInstance(int position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
                .getDisplayMetrics());

        TextView v = new TextView(getActivity());
        params.setMargins(margin, margin, margin, margin);
        v.setLayoutParams(params);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        v.setText("CARD " + (position + 1));

        fl.addView(v);
        return fl;
    }

}
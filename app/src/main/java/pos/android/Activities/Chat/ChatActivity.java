package pos.android.Activities.Chat;

import pos.android.Activities.Chat.ChatSlider.*;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import com.astuetz.PagerSlidingTabStrip;

import pos.android.Activities.BaseActivities.BaseFragmentActivity;
import pos.android.Activities.Chat.ServerRequests.GetConversationsRequest;
import pos.android.R;

public class ChatActivity extends BaseFragmentActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the ViewPager and set an adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ChatPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        //test of request
        new GetConversationsRequest(getApplicationContext(), this.httpContext).execute();


    }

}
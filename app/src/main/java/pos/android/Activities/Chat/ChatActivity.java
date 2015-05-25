package pos.android.Activities.Chat;

import pos.android.Activities.Chat.ChatSlider.*;

import android.support.v4.view.ViewPager;
import android.os.Bundle;


import com.astuetz.PagerSlidingTabStrip;

import org.apache.http.protocol.HttpContext;

import pos.android.Activities.BaseActivities.BaseFragmentActivity;
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
    }

    public HttpContext getHttpContext(){
        return this.httpContext;
    }

}
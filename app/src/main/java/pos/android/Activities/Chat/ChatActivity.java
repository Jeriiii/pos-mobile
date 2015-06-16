package pos.android.Activities.Chat;

import pos.android.Activities.Chat.ChatSlider.*;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;


import com.astuetz.PagerSlidingTabStrip;

import org.apache.http.protocol.HttpContext;

import pos.android.Activities.BaseActivities.BaseFragmentActivity;
import pos.android.Activities.Chat.Noticing.NewMessagesNoticer;
import pos.android.R;

public class ChatActivity extends BaseFragmentActivity {

    private ViewPager pager;

    private PagerSlidingTabStrip tabs;

    private NewMessagesNoticer noticer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the ViewPager and set an adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new ChatPagerAdapter(getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        //tabs.setOnPageChangeListener(new OnChatCardChangeListener(getPagerAdapter()));
        noticer = new NewMessagesNoticer((ChatActivity)this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatManager.getInstance().setMessageNoticer(noticer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChatManager.getInstance().setMessageNoticer(null);
    }



    public HttpContext getHttpContext(){
        return this.httpContext;
    }

    public ChatPagerAdapter getPagerAdapter(){
        return (ChatPagerAdapter) pager.getAdapter();
    }

    public PagerSlidingTabStrip getTabs(){
        return tabs;
    }

    public ViewPager getPager(){
        return pager;
    }

}
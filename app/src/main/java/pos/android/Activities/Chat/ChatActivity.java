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

/**
 * Aktivita obsahující kompletní chat.
 */
public class ChatActivity extends BaseFragmentActivity {

    private ViewPager pager;

    private PagerSlidingTabStrip tabs;

    private NewMessagesNoticer noticer;

    /**
     * Vytvoří pager a nastaví mu potřebné parametry
     * @param savedInstanceState
     */
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
        tabs.setOnPageChangeListener(new OnChatCardChangeListener(getPagerAdapter()));
        noticer = new NewMessagesNoticer((ChatActivity)this);
    }

    /**
     * Registrace notifieru u chat manageru (dává tím znamení, že je tato aktivita aktivní)
     */
    @Override
    protected void onResume() {
        super.onResume();
        ChatManager.getInstance().setMessageNoticer(noticer);
    }

    /**
     * Odregistrování notifieru zpráv z chat manageru, když uživatel tuto aktivitu opustí
     */
    @Override
    protected void onPause() {
        super.onPause();
        ChatManager.getInstance().setMessageNoticer(null);
    }

    /**
     * Vrátí http kontext používaný touto aktivitou.
     * @return
     */
    public HttpContext getHttpContext(){
        return this.httpContext;
    }

    /**
     * Vrátí adaptér nad stránkovačem
     * @return
     */
    public ChatPagerAdapter getPagerAdapter(){
        return (ChatPagerAdapter) pager.getAdapter();
    }

    /**
     * Vrátí celou komponentu se stránkovačem
     * @return
     */
    public PagerSlidingTabStrip getTabs(){
        return tabs;
    }

    /**
     * Vrátí pager
     * @return
     */
    public ViewPager getPager(){
        return pager;
    }

}
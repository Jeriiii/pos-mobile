package pos.android.Activities.Chat.ChatSlider;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import java.util.LinkedList;

/**
 * Stránkovač pro chat
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class ChatPagerAdapter extends FragmentStatePagerAdapter {

    private static final int COUNT_OF_STATIC_TABS = 1;
    private static final int CONVERSATIONS_POSITION = 0;
    private static final String CONVERSATIONS_HEADER = "Konverzace";

    /* všechny headery karet*/
    private LinkedList<String> conversationsHeaders = new LinkedList<String>();
    /* zdrcadlící kolekce k headerům s id uživatelů, se kterými mám otevřenou kartu s konverzací*/
    private LinkedList<Integer> openedIds = new LinkedList<Integer>();

    public ChatPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case CONVERSATIONS_POSITION:
                return CONVERSATIONS_HEADER;
            default:
                return conversationsHeaders.get(position - COUNT_OF_STATIC_TABS);
        }

    }

    @Override
    public int getCount() {
        return conversationsHeaders.size() + COUNT_OF_STATIC_TABS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case CONVERSATIONS_POSITION:
                return ConversationsCardFragment.newInstance(position);
            default:
                return SingleConversationCardFragment.newInstance(position);
        }
    }

    /**
     * Přidá kartu s dalším uživatelem k dopisování
     * @param fromId kódované id uživatele
     * @param fromName uživatelské jméno uživatele
     */
    public void addConversationCard(int fromId, String fromName){
        conversationsHeaders.addFirst(fromName);
        openedIds.addFirst(fromId);
        this.notifyDataSetChanged();

    }



}


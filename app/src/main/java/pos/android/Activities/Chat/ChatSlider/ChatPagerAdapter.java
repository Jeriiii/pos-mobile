package pos.android.Activities.Chat.ChatSlider;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Stránkovač pro chat
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class ChatPagerAdapter extends FragmentStatePagerAdapter {

    public static final int COUNT_OF_STATIC_TABS = 1;
    private static final int CONVERSATIONS_POSITION = 0;
    private static final String CONVERSATIONS_HEADER = "Konverzace";

    /* všechny headery karet*/
    private LinkedList<String> conversationsUsernames = new LinkedList<String>();
    /* zdrcadlící kolekce k headerům s id uživatelů, se kterými mám otevřenou kartu s konverzací*/
    private LinkedList<Integer> openedIds = new LinkedList<Integer>();
    /* všechny otevřené fragmenty */
    private LinkedList<Fragment> openedObjects = new LinkedList<Fragment>();

    public ChatPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case CONVERSATIONS_POSITION:
                return CONVERSATIONS_HEADER;
            default:
                return conversationsUsernames.get(position - COUNT_OF_STATIC_TABS);
        }

    }

    @Override
    public int getCount() {
        return conversationsUsernames.size() + COUNT_OF_STATIC_TABS;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment item;
        switch (position){
            case CONVERSATIONS_POSITION:
                item = ConversationsCardFragment.newInstance(position);
                break;
            default:
                item = SingleConversationCardFragment.newInstance(position, openedIds.get(position - COUNT_OF_STATIC_TABS) + "");
                break;
        }
        openedObjects.add(position, item);
        return item;
    }

    /* překryto kvůli mazání */
    @Override
    public int getItemPosition(Object object) {
        return openedObjects.contains((Fragment)object) ? POSITION_UNCHANGED : POSITION_NONE;
    }

    /**
     * Otevře danou kartu - buď přepne na existující, nebo vytvoří novou
     * @param fromId kódované id uživatele
     * @param fromName uživatelské jméno uživatele
     * @param pager pager tabů
     * @param tabs objekt tabů - slouží pro obnovení - může být null, ale po akci se neobnoví grafika
     */
    public void openCard(int fromId, String fromName, ViewPager pager, PagerSlidingTabStrip tabs){
        if(!openedIds.contains(fromId)){//karta neexistuje
            addConversationCard(fromId, fromName, null);
        }
        switchToCard((int)openedIds.indexOf(fromId)  + COUNT_OF_STATIC_TABS, pager, tabs);
    }

    /**
     * Přidá kartu s dalším uživatelem k dopisování
     * @param fromId kódované id uživatele
     * @param fromName uživatelské jméno uživatele
     * @param tabs objekt tabů - slouží pro obnovení - může být null, pokud má být obnovení provedeno ručně
     */
    public void addConversationCard(int fromId, String fromName, PagerSlidingTabStrip tabs){
        conversationsUsernames.addFirst(fromName);
        openedIds.addFirst(fromId);
        refreshCards(tabs);
    }

    /**
     * Přepne na existující kartu
     * @param position pozice mezi všemi kartami (včetně statických)
     * @param pager pager tabů
     * @param tabs objekt tabů - slouží pro obnovení - může být null, pokud má být obnovení provedeno ručně
     */
    public void switchToCard(int position, ViewPager pager, PagerSlidingTabStrip tabs){
        pager.setCurrentItem(position, true);
        refreshCards(tabs);
    }

    /**
     * Odstraní dynamickou kartu. Statické karty odstranit nelze. Po odstranění se přepne na kartu vlevo od odstraněné
     * @param position pozice mezi DYNAMICKÝMI kartami (tj. 0 je první z karet otevřených před addConversationCard)
     * @param pager pager tabů
     * @param tabs objekt tabů - slouží pro obnovení - může být null, ale po akci se neobnoví grafika
     */
    public void removeCard(int position, ViewPager pager, PagerSlidingTabStrip tabs){
        conversationsUsernames.remove(position);
        openedIds.remove(position);
        openedObjects.remove(position + COUNT_OF_STATIC_TABS);
        refreshCards(tabs);
        switchToCard(position + COUNT_OF_STATIC_TABS - 1, pager, tabs);
    }

    /**
     * Vrátí pozici karty podle uživatelského id nebo -1, pokud není karta tohoto uživatele otevřena
     * @param fromId kódované id uživatele
     * @return
     */
    public int getCardPosition(int fromId){
        return openedIds.indexOf(fromId);
    }

    /**
     * Vrátí id uživatele podle pozice otevřené karty
     * @param position pozice mezi DYNAMICKÝMI kartami (tj. 0 je první z karet otevřených před addConversationCard)
     * @return
     */
    public int getIdFromPosition(int position){
        return openedIds.get(position);
    }

    /**
     * Vrátí jméno uživatele podle pozice otevřené karty
     * @param position pozice mezi DYNAMICKÝMI kartami (tj. 0 je první z karet otevřených před addConversationCard)
     * @return
     */
    public String getNameFromPosition(int position){
        return conversationsUsernames.get(position);
    }

    /**
     * Upozorní správce karet na změnu - změní se i grafika. Může být null, ale nebude překreslovat
     * @param tabs objekt tabů
     */
    private void refreshCards(PagerSlidingTabStrip tabs){
        this.notifyDataSetChanged();
        if(tabs != null) {
            tabs.notifyDataSetChanged();
        }
    }
}


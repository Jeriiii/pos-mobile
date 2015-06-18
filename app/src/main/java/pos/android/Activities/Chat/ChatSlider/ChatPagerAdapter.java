package pos.android.Activities.Chat.ChatSlider;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Stránkovač pro chat
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class ChatPagerAdapter extends FragmentStatePagerAdapter {

    /** počet statických karet (které nejdou zavřít) */
    public static final int COUNT_OF_STATIC_TABS = 1;

    /* pozice statické karty konverzací */
    private static final int CONVERSATIONS_POSITION = 0;
    /* název statické karty konverzací */
    private static final String CONVERSATIONS_HEADER = "Konverzace";
    /* fragment konverzací*/
    private ConversationsCardFragment conversationsCardFragment = ConversationsCardFragment.newInstance(CONVERSATIONS_POSITION);

    /* všechny headery karet*/
    private LinkedList<String> conversationsUsernames = new LinkedList<String>();
    /* zdrcadlící kolekce k headerům s id uživatelů, se kterými mám otevřenou kartu s konverzací
    * !!! pozice zde != pozice mezi kartami (toto jsou pouze dynamické karty, abychom získali skutečnou pozici, musíme přičíst počet statických tabů)*/
    private LinkedList<Integer> openedIds = new LinkedList<Integer>();

    /* kolekce otevřených DYNAMICKÝCH objektů. Obsahuje id uživatele a objekt k němu příslušící.
    * !!! pozice zde != pozice mezi kartami (toto jsou pouze dynamické karty, abychom získali skutečnou pozici, musíme přičíst počet statických tabů)*/
    private HashMap<Integer, SingleConversationCardFragment> openedObjects = new HashMap<Integer, SingleConversationCardFragment>();

    public ChatPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Implementace pro pager - název zobrazený pro danou pozici v navigaci karet
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case CONVERSATIONS_POSITION:
                return CONVERSATIONS_HEADER;
            default:
                return conversationsUsernames.get(position - COUNT_OF_STATIC_TABS) + "";
        }
    }

    /**
     * Implementace pro pager - kolik karet je otevřených
     * @return
     */
    @Override
    public int getCount() {
        return openedIds.size() + COUNT_OF_STATIC_TABS;
    }

    /**
     * Implementace pro pager - vrací fragment, který má být na dané pozici
     * @param position pozice mezi kartami
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case CONVERSATIONS_POSITION:
                return conversationsCardFragment;
            default:
                int userId = openedIds.get(position - COUNT_OF_STATIC_TABS);
                SingleConversationCardFragment item = SingleConversationCardFragment.newInstance(userId);
                item.setPosition(position);
                openedObjects.put(userId, item);
                return item;
        }
    }

    /* překryto kvůli mazání */
    @Override
    public int getItemPosition(Object object) {
        if(object instanceof ConversationsCardFragment) return CONVERSATIONS_POSITION;
        SingleConversationCardFragment fragment = (SingleConversationCardFragment) object;
        return openedIds.contains((Integer) fragment.getUserId())? POSITION_UNCHANGED : POSITION_NONE;
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
        conversationsUsernames.addLast(fromName);
        openedIds.addLast(fromId);
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
        int userId = openedIds.get(position);
        openedIds.remove(position);
        openedObjects.remove(userId);
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

    /**
     * Vrátí fragment (kartu) obsluhující uživatele s daným id. Pokud neexisuje, vrátí null
     * @param userId id uživatele, se kterým si píšu
     * @return SingleConversationCardFragment|null
     */
    public SingleConversationCardFragment getConversationFragment(int userId){
        return openedObjects.get(userId);
    }

    /**
     * Vrátí dynamický fragment (kartu) na dané pozici (vzhledem ke všem kartám včetně statických). Pokud neexisuje, vrátí null
     * @param position pozice mezi kartami
     * @return SingleConversationCardFragment|null
     */
    public SingleConversationCardFragment getConversationFragmentOnPosition(int position){
        if(position < COUNT_OF_STATIC_TABS)return null;
        return openedObjects.get(openedIds.get(position - COUNT_OF_STATIC_TABS));
    }

    /**
     * Vrátí fragment (kartu) se seznamem konverzací
     * @return
     */
    public ConversationsCardFragment getConversationsListFragment(){
        return conversationsCardFragment;
    }


}


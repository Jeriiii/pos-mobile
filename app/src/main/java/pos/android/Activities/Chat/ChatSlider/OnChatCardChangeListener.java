package pos.android.Activities.Chat.ChatSlider;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.NoSuchElementException;

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Messages.MessageItem;

/**
 * Listener naslouchající přepínání karet
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 16.6.2015.
 */
public class OnChatCardChangeListener implements ViewPager.OnPageChangeListener {

    private ChatPagerAdapter pagerAdapter;

    public OnChatCardChangeListener(ChatPagerAdapter pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * Nastane, když uživatel nějakým způsobem zobrazí stránku ve stránkovači.
     * Do pravidelného požadavku o nové zprávy pak přidá informaci o tom, že zobrazené zprávy uživatel četl
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        SingleConversationCardFragment item = pagerAdapter.getConversationFragmentOnPosition(position);
        if(item != null){
            int userId = item.getUserId();
            if(userId != -1) {/* pokud je id už nastaveno*/
               try{
                    int lastMessageId = item.getMessages().getLast().messageId;
                    ChatManager.getInstance().addReadedMessages(userId, lastMessageId);
                }catch(NoSuchElementException e){
                    /* když je seznam prázdný, nedělá nic */
               }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

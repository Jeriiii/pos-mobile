package pos.android.Activities.Chat.ChatSlider;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.NoSuchElementException;

import pos.android.Activities.Chat.ChatManager;
import pos.android.Activities.Chat.Messages.MessageItem;

/**
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

    @Override
    public void onPageSelected(int position) {
        Fragment item = pagerAdapter.getItem(position);
        if(item != null && item instanceof SingleConversationCardFragment){
            SingleConversationCardFragment convItem = (SingleConversationCardFragment) item;
            int userId = convItem.getUserId();
            if(userId != -1) {/* pokud je id už nastaveno*/
                try{
                    int lastMessageId = convItem.getMessages().getLast().messageId;
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

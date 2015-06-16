package pos.android.Activities.Chat.ChatSlider;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import pos.android.Activities.Chat.ChatManager;

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
                ChatManager.getInstance().addReadedMessages(userId, convItem.getMessages().getLast().messageId);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

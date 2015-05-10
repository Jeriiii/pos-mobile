package pos.android.Chat.ChatSlider;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;

/**
 * Stránkovač pro chat
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 10.5.2015.
 */
public class ChatPagerAdapter extends FragmentPagerAdapter {

    private LinkedList<String> headers = new LinkedList<String>();

    public ChatPagerAdapter(FragmentManager fm) {
        super(fm);
        headers.add(0, "Konverzace");
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return headers.get(position);
    }

    @Override
    public int getCount() {
        return headers.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ConversationsCardFragment.newInstance(position);
            default:
                return SingleConversationCardFragment.newInstance(position);
        }
    }

}


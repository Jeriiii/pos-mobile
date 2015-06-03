package pos.android.Activities.Chat.Messages;

import android.content.Context;

import pos.android.Data.CachedDatabase;

/**
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 3.6.2015.
 */
public class MessageDatabase extends CachedDatabase {

    public MessageDatabase(Context context, String directory, String fileName) {
        super(context, directory, fileName);
        createTablesIfNotExists();
    }

    private void createTablesIfNotExists() {
        getDatabase().execSQL("CREATE TABLE IF NOT EXISTS messages " +
                "id INT PRIMARY KEY NOT NULL," +
                "text TEXT NOT NULL," +
                "sendTime INT NOT NULL");
        getDatabase().execSQL("CREATE TABLE IF NOT EXISTS userInfo " +
                "id INT PRIMARY KEY NOT NULL," +
                "name TEXT NOT NULL," +
                "lastActive TEXT NOT NULL");
    }
}

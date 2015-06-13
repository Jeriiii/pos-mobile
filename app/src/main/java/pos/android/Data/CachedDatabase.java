package pos.android.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.File;

/**
 * Objekt pracující s Sqlite databází v cachi.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 3.6.2015.
 */
public class CachedDatabase extends CachedFile{

    private SQLiteDatabase database;

    public CachedDatabase(Context context, String directory, String fileName){
        super(context, directory, fileName);
        database = SQLiteDatabase.openOrCreateDatabase(getFile(), null);
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
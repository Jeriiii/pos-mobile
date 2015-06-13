package pos.android.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.File;

/**
 * Objekt pracující se souborem v cachi.
 * Created by Jan Kotalík <jan.kotalik.pro@gmail.com> on 3.6.2015.
 */
public class CachedFile {

    /* soubor v cache */
    private File file;

    private Storage cache;


    public CachedFile(Context context, String directory, String fileName){
        cache = getCacheStorage(context);
        cache.createDirectory(directory);
        if(!cache.isFileExist(directory, fileName)) {
            cache.createFile(directory, fileName, "");
        }
        this.file = cache.getFile(directory, fileName);
    }


    public File getFile() {
        return file;
    }

    public void delete(){
        cache.deleteFile(file.getPath(), file.getName());
    }

    /**
     * Vrátí zapisovatelné externí nebo interní úložiště podle dostupnosti
     * @param context kontext aplikace
     * @return zapisovatelné externí úložiště, pokud není dostupné, tak interní
     */
    private Storage getCacheStorage(Context context){
        Storage storage = null;
        if (SimpleStorage.isExternalStorageWritable()) {
            storage = SimpleStorage.getExternalStorage();
        }
        else {
            storage = SimpleStorage.getInternalStorage(context);
        }
        return storage;
    }
}
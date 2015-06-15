package pos.android.DownloadManager;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Petr on 14.6.2015.
 */
public class DownloadImageManager {

    final String DOWNLOAD_FILE = "http://goo.gl/w3XV3";

    final String strPref_Download_ID = "PREF_DOWNLOAD_ID";

    private DownloadManager dm;
    private SharedPreferences sp;
    private Context context;
    private BroadcastReceiver bcReceiver;
    private Activity activity;

    public DownloadImageManager (Activity activity, DownloadManager dm, SharedPreferences sp, Context context, BroadcastReceiver bcReceiver) {
        this.dm = dm;
        this.sp = sp;
        this.context = context;
        this.bcReceiver = bcReceiver;
        this.activity = activity;
    }

    public void getImg() {
        Uri downloadUri = Uri.parse(DOWNLOAD_FILE);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        long id = dm.enqueue(request);

        //Save the request id
        SharedPreferences.Editor PrefEdit = sp.edit();
        PrefEdit.putLong(strPref_Download_ID, id);
        PrefEdit.commit();

        IntentFilter intentFilter
                = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        activity.registerReceiver(downloadReceiver, intentFilter);
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(sp.getLong(strPref_Download_ID, 0));
            Cursor cursor = dm.query(query);
            if(cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int status = cursor.getInt(columnIndex);
                if(status == DownloadManager.STATUS_SUCCESSFUL){

                    //Retrieve the saved request id
                    long downloadID = sp.getLong(strPref_Download_ID, 0);

                    ParcelFileDescriptor file;
                    try {
                        file = dm.openDownloadedFile(downloadID);
                        FileInputStream fileInputStream
                                = new ParcelFileDescriptor.AutoCloseInputStream(file);
                        Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
                        //image.setImageBitmap(bm);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
    };
}

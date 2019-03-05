package company.petrifaction.boss.util;

import java.io.File;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;
import android.media.MediaScannerConnection;

public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient
{
    private Context mContext;
    private int scanTimes = 0;
    private String[] filePaths;
    private String[] mimeTypes;
    private MediaScannerConnection mediaScanConn;

    public MediaScanner(Context context)
    {
        mContext = context;
        mediaScanConn = new MediaScannerConnection(context,this);
    }

    public void onMediaScannerConnected()
    {
        for(int index = 0;index < filePaths.length;index++)
            mediaScanConn.scanFile(filePaths[index],mimeTypes[index]);
    }

    private void scanFileForOld(File file)
    {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);/*******/
        mContext.sendBroadcast(intent);
    }

    public void onScanCompleted(String path,Uri uri)
    {
        scanTimes++;
        scanFileForOld(new File(path));
        if(scanTimes == filePaths.length)
        {
            mediaScanConn.disconnect();
            filePaths = null;
            mimeTypes = null;
            scanTimes = 0;
        }
    }

    public void scanFiles(String[] filePath, String[] mimeType)
    {
        filePaths = filePath;
        mimeTypes = mimeType;
        mediaScanConn.connect();
    }
}
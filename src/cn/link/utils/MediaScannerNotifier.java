package cn.link.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-2-19
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScannerNotifier implements MediaScannerConnectionClient {

    //	private Context mContext;
    private MediaScannerConnection mConnection;
    private String mPath;
    private String mMimeType;

    public MediaScannerNotifier(Context context, String path, String mimeType) {
//		mContext = context;
        mPath = path;
        mMimeType = mimeType;
        mConnection = new MediaScannerConnection(context, this);
        mConnection.connect();
    }

    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, mMimeType);
    }

    public void onScanCompleted(String path, Uri uri) {
        try {
//			if (uri != null) {
//				Intent intent = vt Intent(Intent.ACTION_VIEW);
//				intent.setData(uri);
//				mContext.startActivity(intent);
//			}
        } finally {
            mConnection.disconnect();
//			mContext = null;
        }
    }
}


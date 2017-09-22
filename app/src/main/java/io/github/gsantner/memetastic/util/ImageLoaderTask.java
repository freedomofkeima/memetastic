package io.github.gsantner.memetastic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.File;

import io.github.gsantner.memetastic.data.MemeLibConfig;

public class ImageLoaderTask<T> extends AsyncTask<File, Void, Bitmap> {
    private final static int MAX_DIMENSION = 5000;

    public interface OnImageLoadedListener<T> {
        void onImageLoaded(Bitmap bitmap, T callbackParam);
    }

    private final Context _context;
    private final int _maxSize;
    private final OnImageLoadedListener _listener;
    private final T _callbackParam;
    private final boolean _loadThumbnail;

    public ImageLoaderTask(OnImageLoadedListener listener, Context context, boolean loadThumbnail, T callbackParam) {
        _listener = listener;
        _context = context;
        _callbackParam = callbackParam;
        _loadThumbnail = loadThumbnail;
        _maxSize = loadThumbnail ? AppSettings.get().getThumbnailQualityReal()
                : MemeLibConfig.MEME_FULLSCREEN_MAX_IMAGESIZE;
    }

    private Bitmap loadStorageImage(File pathToImage) {
        File cacheFile = new File(_context.getCacheDir(), pathToImage.getAbsolutePath().substring(1));
        ContextUtils cu = ContextUtils.get();
        Bitmap bitmap = null;
        if (_loadThumbnail) {
            if (cacheFile.exists()) {
                bitmap = cu.loadImageFromFilesystem(cacheFile.getAbsolutePath(), _maxSize);
            } else {
                bitmap = cu.loadImageFromFilesystem(pathToImage.getAbsolutePath(), _maxSize);
                cu.writeImageToFileDetectFormat(cacheFile, bitmap, 80);
            }
        } else {
            bitmap = cu.loadImageFromFilesystem(pathToImage.getAbsolutePath(), _maxSize);
        }

        return bitmap;
    }

    @Override
    protected Bitmap doInBackground(File... params) {
        return loadStorageImage(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (_listener != null)
            _listener.onImageLoaded(bitmap, _callbackParam);
    }
}

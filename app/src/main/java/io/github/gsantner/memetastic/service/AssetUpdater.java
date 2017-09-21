package io.github.gsantner.memetastic.service;


import android.annotation.SuppressLint;
import android.content.Context;

import net.gsantner.opoc.util.Callback;
import net.gsantner.opoc.util.FileUtils;
import net.gsantner.opoc.util.NetworkUtils;
import net.gsantner.opoc.util.ZipUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.gsantner.memetastic.util.AppCast;
import io.github.gsantner.memetastic.util.AppSettings;

@SuppressLint("SimpleDateFormat")
public class AssetUpdater {
    public static final SimpleDateFormat FORMAT_RFC3339 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    private static final String URL_ARCHIVE_ZIP = "https://github.com/gsantner/memetastic-assets/archive/master.zip";
    private static final String URL_API = "https://api.github.com/repos/gsantner/memetastic-assets";

    public static class UpdateThread extends Thread {
        public static final int DOWNLOAD_REQUEST_RESULT__FAILED = -1;
        public static final int DOWNLOAD_REQUEST_RESULT__DO_DOWNLOAD_ASK = 1;
        public static final int DOWNLOAD_STATUS__DOWNLOADING = 1;
        public static final int DOWNLOAD_STATUS__UNZIPPING = 2;
        public static final int DOWNLOAD_STATUS__FINISHED = 3;
        public static final int DOWNLOAD_STATUS__FAILED = -1;


        private static boolean _isAlreadyDownloading = false;

        private boolean _doDownload;
        private Context _context;
        private AppSettings _appSettings;
        private int _lastPercent = 0;

        public UpdateThread(Context context, boolean doDownload) {
            _doDownload = doDownload;
            _context = context;
            _appSettings = AppSettings.get();
        }

        @Override
        public void run() {
            String apiJsonS = NetworkUtils.performCall(URL_API, NetworkUtils.GET);
            try {
                JSONObject apiJson = new JSONObject(apiJsonS);
                String lastUpdate = apiJson.getString("pushed_at");
                Date date = FORMAT_RFC3339.parse(lastUpdate);
                if (date.after(_appSettings.getLastAssetArchiveDate())) {
                    if (!_doDownload) {
                        AppCast.DOWNLOAD_REQUEST_RESULT.send(_context, DOWNLOAD_REQUEST_RESULT__DO_DOWNLOAD_ASK);
                        return;
                    } else {
                        doDownload(date);
                    }
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            AppCast.DOWNLOAD_REQUEST_RESULT.send(_context, DOWNLOAD_REQUEST_RESULT__FAILED);
        }


        private synchronized void doDownload(Date date) throws ParseException {
            if (_isAlreadyDownloading || date.before(_appSettings.getLastAssetArchiveDate())) {
                return;
            }
            _isAlreadyDownloading = true;
            File templatesDir = new File(_appSettings.getSaveDirectory(), "templates");
            File file = new File(_appSettings.getSaveDirectory(), ".downloads");
            FileUtils.deleteRecursive(file);
            boolean ok;
            if (file.mkdirs() && (templatesDir.exists() || templatesDir.mkdirs())) {
                file = new File(file, FORMAT_RFC3339.format(date) + ".zip");
                NetworkUtils.downloadFile(URL_ARCHIVE_ZIP, file, new Callback<Float>() {
                    public void onCallback(Float aFloat) {
                        if (_lastPercent != aFloat.intValue()) {
                            AppCast.DOWNLOAD_STATUS.send(_context, DOWNLOAD_STATUS__DOWNLOADING, _lastPercent * 3 / 4);
                        }
                    }
                });
                ok = ZipUtils.unzip(file, templatesDir, false, new Callback<Float>() {
                    public void onCallback(Float aFloat) {
                        if (_lastPercent != aFloat.intValue()) {
                            AppCast.DOWNLOAD_STATUS.send(_context, DOWNLOAD_STATUS__UNZIPPING, 50 + _lastPercent / 4);
                        }
                    }
                });
                FileUtils.writeFile(new File(templatesDir, ".nomedia"), "");
                AppCast.DOWNLOAD_STATUS.send(_context, ok ? DOWNLOAD_STATUS__FINISHED : DOWNLOAD_STATUS__FAILED, 100);
                _appSettings.setLastArchiveDate(date);
                _isAlreadyDownloading = false;
            }
        }
    }
}

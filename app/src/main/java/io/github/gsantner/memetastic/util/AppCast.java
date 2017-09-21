package io.github.gsantner.memetastic.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class AppCast {
    //########################
    //## Send broadcast
    //########################
    private static void sendBroadcast(Context c, Intent i) {
        if (c != null) {
            LocalBroadcastManager.getInstance(c).sendBroadcast(i);
        }
    }

    //########################
    //## Filter
    //########################
    public static IntentFilter getLocalBroadcastFilter() {
        IntentFilter intentFilter = new IntentFilter();
        String[] BROADCAST_ACTIONS = {
                DOWNLOAD_REQUEST_RESULT.ACTION,
                DOWNLOAD_STATUS.ACTION
        };
        for (String action : BROADCAST_ACTIONS) {
            intentFilter.addAction(action);
        }
        return intentFilter;
    }

    //########################
    //## Actions
    //########################
    public static class DOWNLOAD_REQUEST_RESULT {
        public static final String ACTION = "DOWNLOAD_REQUEST_RESULT";
        public static final String EXTRA_RESULT = "EXTRA_RESULT";

        public static void send(Context c, int result) {
            Intent intent = new Intent(ACTION);
            intent.putExtra(EXTRA_RESULT, result);
            sendBroadcast(c, intent);
        }
    }

    public static class DOWNLOAD_STATUS {
        public static final String ACTION = "DOWNLOAD_STATUS";
        public static final String EXTRA_RESULT = "EXTRA_RESULT";
        public static final String EXTRA_PERCENT = "EXTRA_PERCENT";

        public static void send(Context c, int result, int percent) {
            Intent intent = new Intent(ACTION);
            intent.putExtra(EXTRA_RESULT, result);
            intent.putExtra(EXTRA_PERCENT, percent);
            sendBroadcast(c, intent);
        }
    }
}

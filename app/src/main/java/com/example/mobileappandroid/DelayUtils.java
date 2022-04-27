package com.example.mobileappandroid;

import android.os.Handler;

public class DelayUtils {


    // Delay mechanism

    public interface DelayCallback {
        void afterDelay();
    }

    public static void delay(float secs, final DelayCallback delayCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, (long) secs); // afterDelay will be executed after (secs*1000) milliseconds.
    }

}

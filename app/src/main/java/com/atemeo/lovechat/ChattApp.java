package com.atemeo.lovechat;

import android.app.Application;

import com.parse.Parse;

/**
 * The Class ChattApp is the Main Application class of this app. The onCreate
 * method of this class initializes the Parse.
 */
public class ChattApp extends Application {

    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();

// Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "7RaQGPgc54mV2HTlLpvWg6qbdpI0N0PkWXAu8OMz", "bpBxPPW2kMkDyiy8KCU5LxranGobOAzWNRM7yvk5");
    }
}

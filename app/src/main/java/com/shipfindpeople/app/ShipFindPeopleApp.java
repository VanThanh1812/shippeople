package com.shipfindpeople.app;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by sonnd on 10/21/2016.
 */

public class ShipFindPeopleApp extends Application {

    public ShipFindPeopleApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}

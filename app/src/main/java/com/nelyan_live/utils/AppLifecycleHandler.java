package com.nelyan_live.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class AppLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private AppLifecycleDelegates lifecycleDelegates;
    private boolean appInForeground = false;

    public AppLifecycleHandler(AppLifecycleDelegates delegates) {
        this.lifecycleDelegates = delegates;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i("Application", "Started");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i("Application", "Resumed");
        if (!appInForeground) {
            appInForeground = true;
            lifecycleDelegates.onAppForegrounded();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i("Application", "Paused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i("Application", "Stopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i("Application", "Destroyed");
    }

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false;
            // lifecycleDelegate instance was passed in on the constructor
            lifecycleDelegates.onAppBackgrounded();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {

    }

    @Override
    public void onLowMemory() {

    }

    public interface AppLifecycleDelegates {
        void onAppBackgrounded();

        void onAppForegrounded();
    }
}

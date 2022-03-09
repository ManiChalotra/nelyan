package com.nelyanlive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;

import java.util.Locale;

// extend with base Application instaed of Application
public class AppController extends Application implements AppLifecycleHandler.AppLifecycleDelegates {


    public static final String TAG = AppController.class.getSimpleName();

    public static AppController mInstance;
    SharedPreferences preferences;
    SharedPreferences prefToken;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorToken;
    public static final String PREF_TOKEN = "dummy";

    private AppLifecycleHandler lifecycleHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //mSocketManager = getSocketManager();

       // SocketManager.INSTANCE.connectSocketMain();

        lifecycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifecycleHandler);
     //   FirebaseApp.initializeApp(this);
        initializePreferences();
        initializePreferencesToken();

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(new     MediaLoader())
                .setLocale(Locale.getDefault())
                .build());

        // to disable the night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }



    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static boolean hasNetwork() {
        return mInstance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    // initialize shared preferences
    private void initializePreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    // initialize shared preferences for token
    private void initializePreferencesToken() {
        prefToken = getSharedPreferences(PREF_TOKEN, Context.MODE_PRIVATE);
        editorToken = prefToken.edit();
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }


    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }


    public void clearData() {
        preferences.edit().clear().commit();
    }

    public void setDeviceToken(String key, String value) {
        editorToken.putString(key, value);
        editorToken.commit();
    }

    public String getDeviceToken(String key, String def_value) {
        return prefToken.getString(key, def_value);
    }



    @Override
    public void onAppForegrounded()
    {
       /* if (!mSocketManager.isConnected() || mSocketManager.getmSocket() == null)
        {
            mSocketManager.init();
        }*/
    }

    @Override
    public void onAppBackgrounded()
    {
       // mSocketManager.disconnectAll();
    }




}
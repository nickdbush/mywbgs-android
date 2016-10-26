package com.nickdbush.mywbgs;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyWBGS extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Leak Canary
        // if (BuildConfig.DEBUG) {
        //     if (LeakCanary.isInAnalyzerProcess(this)) {
        //         return;
        //     }
        //     LeakCanary.install(this);
        // }

        // JodaTime
        JodaTimeAndroid.init(this);
        // Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        // Calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Montserrat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

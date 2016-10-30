package com.nickdbush.mywbgs;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyWBGS extends Application {

    // Consts
    public static final int SCHEMA_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        // JodaTime
        JodaTimeAndroid.init(this);

        // Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(SCHEMA_VERSION)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        if (oldVersion == 1) {

                        }
                    }
                })
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

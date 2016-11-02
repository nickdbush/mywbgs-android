package com.nickdbush.mywbgs;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyWBGS extends Application {

    // Consts
    public static final int SCHEMA_VERSION = 1;
    public static final String SHARED_PREFERENCES_FILENAME = "com.nickdbush.mywbgs";
    public static final String REALM_FILENAME = "com.nickdbush.mywbgs.realm";

    @Override
    public void onCreate() {
        super.onCreate();

        // JodaTime
        JodaTimeAndroid.init(this);

        // Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(SCHEMA_VERSION)
                .name(REALM_FILENAME)
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        if (oldVersion == 1) {
                            // Migrate
                        }
                    }
                })
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILENAME, MODE_PRIVATE);
        int spVerision = sharedPreferences.getInt("schema_version", SCHEMA_VERSION);
        if (spVerision != SCHEMA_VERSION) {
            if (spVerision == 1) {
                // Migrate
            }
        }

        // Calligraphy
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Montserrat-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

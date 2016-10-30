package com.nickdbush.mywbgs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.nickdbush.mywbgs.components.HomeworkNotificationManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, new SettingsFragment());
        ft.commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private static final String HOMEWORK_NOTIFICATION = "pref.homework_notif";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.app_preferences);
            getPreferenceManager().setSharedPreferencesName("com.nickdbush.mywbgs");
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(HOMEWORK_NOTIFICATION)) {
                SwitchPreferenceCompat preference = (SwitchPreferenceCompat) findPreference(HOMEWORK_NOTIFICATION);
                HomeworkNotificationManager.setEnabled(getContext(), preference.isEnabled(), true);
            }
        }
    }

}

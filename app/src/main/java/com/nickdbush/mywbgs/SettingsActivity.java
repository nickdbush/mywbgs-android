package com.nickdbush.mywbgs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.nickdbush.mywbgs.components.HomeworkNotificationReceiver;

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
        private static final String HOMEWORK_NOTIFICATION = "pref_homework_notif";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.app_preferences);
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

                // TODO: 27/10/2016 Merge with MainActivity code (DRY)
                Intent notifyIntent = new Intent(getContext(), HomeworkNotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), MainActivity.HOMEWORK_ALARM, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

                if (preference.isChecked()) {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, MainActivity.NOTIFICATION_TIME.getMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                } else {
                    alarmManager.cancel(pendingIntent);
                }
            }
        }
    }

}

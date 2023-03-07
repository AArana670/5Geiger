package com.example.a5geigir;

import android.os.Bundle;
import android.telephony.TelephonyManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.preferences, rootKey);

            Preference tokenPref = findPreference("token");
            if (tokenPref != null) {
                tokenPref.setSummaryProvider(new Preference.SummaryProvider<Preference>() {  //https://developer.android.com/develop/ui/views/components/settings/customize-your-settings#java
                    @Override
                    public CharSequence provideSummary(Preference preference) {
                        return "Token: " + "Merequetengue";
                    }
                });
            }
        }
    }
}
package com.example.a5geigir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

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

            /*Preference tokenPref = findPreference("token");  //Currently unused feature
            if (tokenPref != null) {
                tokenPref.setSummaryProvider(new Preference.SummaryProvider<Preference>() {  //https://developer.android.com/develop/ui/views/components/settings/customize-your-settings#java
                    @Override
                    public CharSequence provideSummary(Preference preference) {  //Set the summary of Token field
                        return "Token: " + "Merequetengue";
                    }
                });
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerChangeListener();
    }

    private void registerChangeListener () {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                updateLanguage(sharedPreferences);
            }
        });
    }


    private void updateLanguage(SharedPreferences prefs) {

        Log.d("LanguageDebug", "Updating activity...");
        String lang = prefs.getString("language","def");

        if (lang == "def")
            lang = Locale.getDefault().getLanguage();

        Locale nuevaloc = new Locale(lang);  //https://omegat.sourceforge.io/manual-standard/es/appendix.languages.html
        Locale.setDefault(nuevaloc);
        Configuration configuration =
                getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context =
                getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
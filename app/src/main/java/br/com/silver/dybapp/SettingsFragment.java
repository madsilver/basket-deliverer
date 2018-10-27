package br.com.silver.dybapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    EditTextPreference etpTotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);

        etpTotal = (EditTextPreference) findPreference(getString(R.string.pref_scheduled));
        fillSummary(etpTotal);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();
        if(preference.equals(etpTotal)){
            etpTotal.setText(stringValue);
        }
        return true;
    }

    private void fillSummary(Preference preference){
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Object value = pref.getString(preference.getKey(),"");
        onPreferenceChange(preference, value);
    }
}

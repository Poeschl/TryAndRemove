package de.poeschl.apps.debuganddelete.preferences;

import android.content.SharedPreferences;

/**
 * Taken from the sample app u2020 from Jake Wharton.
 *
 * @see <a href="https://github.com/JakeWharton/u2020">GitHub</a>
 */
public class BooleanPreference {
    private final SharedPreferences preferences;
    private final String key;
    private final boolean defaultValue;

    public BooleanPreference(SharedPreferences preferences, String key) {
        this(preferences, key, false);
    }

    public BooleanPreference(SharedPreferences preferences, String key, boolean defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public boolean get() {
        return preferences.getBoolean(key, defaultValue);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}
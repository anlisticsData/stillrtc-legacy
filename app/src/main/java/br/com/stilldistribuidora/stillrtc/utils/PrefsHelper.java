package br.com.stilldistribuidora.stillrtc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

public class PrefsHelper {

    public static final String PREF_IS_LOGINED = "is_logined";
    public static final String PREF_DEVICE_ID = "device_id";
    public static final String PREF_DEVICE_IDENTIFIER = "device_identifier";
    public static final String PREF_DEVICE_OPERATOR = "device_operator";
    public static final String PREF_DEVICE_API_KEY = "device_api_key";
    public static final String PREF_DEVICE_PWD = "device_pwd";
    public static final String PREF_DEVICE_UUID = "user_uuid";


    public static final String PREF_TEMP_OPERATION_ID = "temp_operation_id";


    public static final String PREF_CLIENT_ID = "client_id";
    public static final String PREF_LEVEL = "level";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_USER_API_KEY = "user_api_key";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static PrefsHelper instance;

    public static PrefsHelper getPrefsHelper() {
        return instance;
    }

    public PrefsHelper(Context context) {
        instance = this;
        String prefsFile = context.getPackageName();
        //sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void delete(String key) {
        if (sharedPreferences.contains(key)) {
            editor.remove(key).commit();
        }
    }

    public void savePref(String key, Object value) {
        delete(key);

        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }

        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public boolean isPrefExists(String key) {
        return sharedPreferences.contains(key);
    }
}
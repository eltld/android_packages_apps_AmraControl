/*
 *  Copyright (C) 2013 Alexander "Evisceration" Martinz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.regulus.amra.amracontrol.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {
    //==============================================================================================
    // Fields
    //==============================================================================================
    private static PreferenceHelper  ourInstance;
    private static SharedPreferences mSharedPrefs;

    //==============================================================================================
    // Initialization
    //==============================================================================================

    public static PreferenceHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new PreferenceHelper(context);
        }
        return ourInstance;
    }

    private PreferenceHelper(Context context) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //==============================================================================================
    // Generic
    //==============================================================================================

    public static void remove(String key) {
        mSharedPrefs.edit().remove(key).commit();
    }

    public static int getInt(String key) {
        return PreferenceHelper.getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return mSharedPrefs.getInt(key, defaultValue);
    }

    public static String getString(String key) {
        return mSharedPrefs.getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return mSharedPrefs.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return mSharedPrefs.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPrefs.getBoolean(key, defaultValue);
    }

    public static void setString(final String key, final String value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSharedPrefs.edit().putString(key, value).commit();
            }
        }).start();
    }

    public static void setInt(final String key, final int value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSharedPrefs.edit().putInt(key, value).commit();
            }
        }).start();
    }

    public static void setBoolean(final String key, final boolean value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSharedPrefs.edit().putBoolean(key, value).commit();
            }
        }).start();
    }

    //==============================================================================================
    // Specific
    //==============================================================================================

    public static int getTransformerId() {
        return Integer.parseInt(mSharedPrefs.getString(
                "prefs_jf_appearance_custom_transformer", "0"));
    }

    public static void setTransformerId(String value) {
        mSharedPrefs.edit().putString("prefs_jf_appearance_custom_transformer", value).commit();
    }

    public static boolean getCustomAnimations() {
        return mSharedPrefs.getBoolean("prefs_jf_appearance_custom_animations", true);
    }

    public static void setCustomAnimations(boolean value) {
        mSharedPrefs.edit().putBoolean("prefs_jf_appearance_custom_animations", value).commit();
    }
}

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
package org.regulus.amra.amracontrol.fragments.main;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.utils.constants.DeviceConstants;
import org.regulus.amra.amracontrol.utils.helpers.PreferenceHelper;
import org.regulus.amra.amracontrol.preferences.CustomCheckBoxPreference;

/**
 * Created by alex on 18.12.13.
 */
public class PreferencesFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener, DeviceConstants {

    //==============================================================================================
    // Debug
    //==============================================================================================
    private CustomCheckBoxPreference mExtensiveLogging;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml._device_control);

        final Activity activity = getActivity();
        PreferenceHelper.getInstance(activity);

        mExtensiveLogging = (CustomCheckBoxPreference) findPreference(EXTENSIVE_LOGGING);
        mExtensiveLogging.setOnPreferenceChangeListener(this);

        final Preference mVersion = findPreference("prefs_version");
        mVersion.setEnabled(false);
        try {
            final PackageInfo pInfo = activity.getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0);
            mVersion.setTitle(getString(R.string.app_version_name, pInfo.versionName));
            mVersion.setSummary(getString(R.string.app_version_code, pInfo.versionCode));
        } catch (Exception ignored) {
            final String unknown = getString(R.string.unknown);
            mVersion.setTitle(unknown);
            mVersion.setSummary(unknown);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        view.setBackgroundResource(R.drawable.preference_drawer_background);

        return view;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean changed = false;

        if (preference == mExtensiveLogging) {
            final boolean value = (Boolean) newValue;
            PreferenceHelper.setBoolean(EXTENSIVE_LOGGING, value);
            org.regulus.amra.amracontrol.Application.IS_LOG_DEBUG = value;
            changed = true;
        }

        return changed;
    }

}

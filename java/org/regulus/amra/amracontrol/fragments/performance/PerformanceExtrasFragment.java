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

package org.regulus.amra.amracontrol.fragments.performance;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.view.View;

import org.regulus.amra.amracontrol.Application;
import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.activities.MainActivity;
import org.regulus.amra.amracontrol.fragments.parents.AttachPreferenceFragment;
import org.regulus.amra.amracontrol.utils.Scripts;
import org.regulus.amra.amracontrol.utils.Utils;
import org.regulus.amra.amracontrol.utils.constants.DeviceConstants;
import org.regulus.amra.amracontrol.utils.constants.FileConstants;
import org.regulus.amra.amracontrol.utils.helpers.CpuUtils;
import org.regulus.amra.amracontrol.utils.helpers.PreferenceHelper;
import org.regulus.amra.amracontrol.utils.threads.WriteAndForget;
import org.regulus.amra.amracontrol.preferences.CustomCheckBoxPreference;
import org.regulus.amra.amracontrol.preferences.SeekBarPreference;

import java.util.ArrayList;
import java.util.List;

public class PerformanceExtrasFragment extends AttachPreferenceFragment
        implements DeviceConstants, FileConstants, Preference.OnPreferenceChangeListener {

    public static final int ID = 220;

    //==============================================================================================
    // Files
    //==============================================================================================
    public static final String  sLcdPowerReduceFile   = Utils.checkPaths(FILES_LCD_POWER_REDUCE);
    public static final boolean sLcdPowerReduce       = !sLcdPowerReduceFile.equals("");
    //----------------------------------------------------------------------------------------------
    public static final String  sMcPowerSchedulerFile = Utils.checkPaths(FILES_MC_POWER_SCHEDULER);
    public static final boolean sMcPowerScheduler     = !sMcPowerSchedulerFile.equals("");
    //==============================================================================================
    // Fields
    //==============================================================================================
    private static      boolean IS_LOW_RAM_DEVICE     = false;
    //----------------------------------------------------------------------------------------------
    private CustomCheckBoxPreference mForceHighEndGfx;
    //----------------------------------------------------------------------------------------------
    private CustomCheckBoxPreference mLcdPowerReduce;
    private SeekBarPreference        mMcPowerScheduler;
    //----------------------------------------------------------------------------------------------
    private CustomCheckBoxPreference mIntelliPlug;
    private CustomCheckBoxPreference mIntelliPlugEco;

    //==============================================================================================
    // Overridden Methods
    //==============================================================================================

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof MainActivity) {
            super.onAttach(activity, PerformanceExtrasFragment.ID);
        } else {
            super.onAttach(activity);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MainActivity.mSlidingMenu != null && MainActivity.mSlidingMenu.isMenuShowing()) {
            MainActivity.mSlidingMenu.toggle(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.performance_general);

        IS_LOW_RAM_DEVICE = Utils.getLowRamDevice(getActivity());

        if (IS_LOW_RAM_DEVICE) {
            mForceHighEndGfx = (CustomCheckBoxPreference) findPreference(FORCE_HIGHEND_GFX_PREF);
        } else {
            getPreferenceScreen().removePreference(findPreference(FORCE_HIGHEND_GFX_PREF));
        }

        //------------------------------------------------------------------------------------------
        // Power Saving
        //------------------------------------------------------------------------------------------

        PreferenceGroup preferenceGroup = (PreferenceGroup) findPreference(CATEGORY_POWERSAVING);

        mLcdPowerReduce = (CustomCheckBoxPreference) findPreference(KEY_LCD_POWER_REDUCE);
        if (sLcdPowerReduce) {
            mLcdPowerReduce.setChecked(Utils.readOneLine(sLcdPowerReduceFile).equals("1"));
            mLcdPowerReduce.setOnPreferenceChangeListener(this);
        } else {
            preferenceGroup.removePreference(mLcdPowerReduce);
        }

        mMcPowerScheduler = (SeekBarPreference) findPreference(KEY_MC_POWER_SCHEDULER);
        if (sMcPowerScheduler) {
            mMcPowerScheduler
                    .setProgress(Integer.parseInt(Utils.readOneLine(sMcPowerSchedulerFile)));
            mMcPowerScheduler.setOnPreferenceChangeListener(this);
        } else {
            preferenceGroup.removePreference(mMcPowerScheduler);
        }

        removeIfEmpty(preferenceGroup);

        //------------------------------------------------------------------------------------------
        // Intelli-Plug
        //------------------------------------------------------------------------------------------

        preferenceGroup = (PreferenceGroup) findPreference(GROUP_INTELLI_PLUG);

        mIntelliPlug = (CustomCheckBoxPreference) findPreference(KEY_INTELLI_PLUG);
        if (CpuUtils.hasIntelliPlug()) {
            mIntelliPlug.setChecked(CpuUtils.getIntelliPlugActive());
            mIntelliPlug.setOnPreferenceChangeListener(this);
        } else {
            preferenceGroup.removePreference(mIntelliPlug);
        }

        mIntelliPlugEco = (CustomCheckBoxPreference) findPreference(KEY_INTELLI_PLUG_ECO);
        if (CpuUtils.hasIntelliPlug() && CpuUtils.hasIntelliPlugEcoMode()) {
            mIntelliPlugEco.setChecked(CpuUtils.getIntelliPlugEcoMode());
            mIntelliPlugEco.setOnPreferenceChangeListener(this);
        } else {
            preferenceGroup.removePreference(mIntelliPlugEco);
        }

        removeIfEmpty(preferenceGroup);

        new PerformanceCpuTask().execute();

    }

    private void removeIfEmpty(PreferenceGroup preferenceGroup) {
        if (preferenceGroup.getPreferenceCount() == 0) {
            getPreferenceScreen().removePreference(preferenceGroup);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        boolean changed = false;

        if (preference == mForceHighEndGfx) {
            Utils.runRootCommand(Scripts.toggleForceHighEndGfx());
            changed = true;
        } else if (preference == mLcdPowerReduce) {
            boolean value = (Boolean) o;
            new WriteAndForget(sLcdPowerReduceFile, (value ? "1" : "0")).run();
            PreferenceHelper.setBoolean(KEY_LCD_POWER_REDUCE, value);
            changed = true;
        } else if (preference == mIntelliPlug) {
            boolean value = (Boolean) o;
            CpuUtils.enableIntelliPlug(value);
            PreferenceHelper.setBoolean(KEY_INTELLI_PLUG, value);
            changed = true;
        } else if (preference == mIntelliPlugEco) {
            boolean value = (Boolean) o;
            CpuUtils.enableIntelliPlugEcoMode(value);
            PreferenceHelper.setBoolean(KEY_INTELLI_PLUG_ECO, value);
            changed = true;
        } else if (preference == mMcPowerScheduler) {
            int value = (Integer) o;
            new WriteAndForget(sMcPowerSchedulerFile, "" + o).run();
            PreferenceHelper.setInt(KEY_MC_POWER_SCHEDULER, value);
            changed = true;
        }

        return changed;
    }

    //==============================================================================================
    // Methods
    //==============================================================================================

    public void setResult(List<Boolean> paramResult) {
        int i = 0;
        if (IS_LOW_RAM_DEVICE && Application.HAS_ROOT) {
            mForceHighEndGfx.setChecked(paramResult.get(i));
            mForceHighEndGfx.setOnPreferenceChangeListener(this);
            i++;
        }
    }

    public static boolean isSupported(Context context) {
        return (sLcdPowerReduce
                || CpuUtils.hasIntelliPlug() // includes eco mode
                || Utils.getLowRamDevice(context));
    }

    //==============================================================================================
    // Internal Classes
    //==============================================================================================

    class PerformanceCpuTask extends AsyncTask<Void, Integer, List<Boolean>>
            implements DeviceConstants {

        @Override
        protected List<Boolean> doInBackground(Void... voids) {
            List<Boolean> tmpList = new ArrayList<Boolean>();

            if (IS_LOW_RAM_DEVICE && Application.HAS_ROOT) {
                tmpList.add(Scripts.getForceHighEndGfx());
            }

            return tmpList;
        }

        @Override
        protected void onPostExecute(List<Boolean> booleans) {
            setResult(booleans);
        }
    }
}

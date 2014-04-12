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

package org.regulus.amra.amracontrol;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

import com.stericson.roottools.RootTools;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import org.regulus.amra.amracontrol.utils.Utils;
import org.regulus.amra.amracontrol.utils.constants.DeviceConstants;
import org.regulus.amra.amracontrol.utils.helpers.PreferenceHelper;

@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formKey = "",
        formUri = "http://reports.amra" +
                ".org/acra-amracontrol/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "amrareporter",
        formUriBasicAuthPassword = "weareopentoeveryone",
        mode = ReportingInteractionMode.SILENT)
public class Application extends android.app.Application implements DeviceConstants {

    public static boolean IS_LOG_DEBUG = false;
    public static boolean IS_DEBUG     = false;

    public static       boolean HAS_ROOT    = false;
    public static final boolean IS_AMRA = Utils.isAmra();

    public static  AlarmManager   alarmManager;
    private static PackageManager packageManager;

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        applicationContext = getApplicationContext();

        PreferenceHelper.getInstance(this);
        IS_LOG_DEBUG = PreferenceHelper.getBoolean(EXTENSIVE_LOGGING, false);
        IS_DEBUG = Utils.existsInBuildProp("ro.amra.debug=1");

        if (Application.IS_DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            RootTools.debugMode = true;
        }

        logDebug("Is Amra: " + (IS_AMRA ? "true" : "false"));

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        packageManager = getPackageManager();

        // we need to detect SU for some features :)
        HAS_ROOT = RootTools.isRootAvailable() && RootTools.isAccessGiven();
        if (HAS_ROOT) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RootTools.remount(Environment.getExternalStorageDirectory()
                            .getAbsolutePath(), "rw");
                }
            }).start();
        }

        try {
            if (packageManager.getResourcesForApplication("com.android.settings")
                    .getIdentifier("amra_control_settings", "string",
                            "com.android.settings") > 0) {
                logDebug("Implemented into system!");
                disableLauncher(true);
            } else {
                logDebug("Not implemented into system!");
                disableLauncher(false);
            }
        } catch (PackageManager.NameNotFoundException exc) {
            logDebug("You dont have settings? That's weird.");
            disableLauncher(false);
        }

    }

    private void disableLauncher(boolean shouldDisable) {
        final ComponentName component = new ComponentName(PACKAGE_NAME,
                PACKAGE_NAME + ".activities.DummyLauncher");
        packageManager.setComponentEnabledSetting(component,
                (shouldDisable
                        ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_ENABLED),
                PackageManager.DONT_KILL_APP
        );
    }

    /**
     * Logs a message to logcat if boolean param is true.<br />
     * This is very useful for debugging, just set debug to false on a release build<br />
     * and it wont show any debug messages.
     *
     * @param msg The message to log
     */
    public static void logDebug(final String msg) {
        if (IS_LOG_DEBUG) {
            Log.e("AmraControl", msg);
        }
    }
}

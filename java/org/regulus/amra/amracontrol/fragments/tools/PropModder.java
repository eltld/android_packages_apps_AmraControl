package org.regulus.amra.amracontrol.fragments.tools;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.widget.EditText;

import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.fragments.dynamic.PressToLoadFragment;
import org.regulus.amra.amracontrol.preferences.CustomPreference;
import org.regulus.amra.amracontrol.utils.Utils;
import org.regulus.amra.amracontrol.utils.cmdprocessor.CMDProcessor;

import static org.regulus.amra.amracontrol.Application.logDebug;
import static org.regulus.amra.amracontrol.utils.constants.DeviceConstants.PREF_FULL_EDITOR;


public class PropModder extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String APPEND_CMD               = "echo \"%s=%s\" >> /system/build.prop";
    private static final String KILL_PROP_CMD            =
            "busybox sed -i \"/%s/D\" /system/build.prop";
    private static final String REPLACE_CMD              =
            "busybox sed -i \"/%s/ c %<s=%s\" /system/build.prop";
    private static final String REMOUNT_CMD              =
            "busybox mount -o %s,remount -t yaffs2 /dev/block/mtdblock1 /system";
    private static final String PROP_EXISTS_CMD          = "grep -q %s /system/build.prop";
    private static final String DISABLE                  = "disable";
    private static final String SHOWBUILD_PATH           = "/system/tmp/showbuild";
    private static final String WIFI_SCAN_PREF           = "pref_wifi_scan_interval";
    private static final String WIFI_SCAN_PROP           = "wifi.supplicant_scan_interval";
    private static final String WIFI_SCAN_PERSIST_PROP   = "persist.wifi_scan_interval";
    private static final String WIFI_SCAN_DEFAULT        = System.getProperty(WIFI_SCAN_PROP);
    private static final String MAX_EVENTS_PREF          = "pref_max_events";
    private static final String MAX_EVENTS_PROP          = "windowsmgr.max_events_per_sec";
    private static final String MAX_EVENTS_PERSIST_PROP  = "persist.max_events";
    private static final String MAX_EVENTS_DEFAULT       = System.getProperty(MAX_EVENTS_PROP);
    private static final String USB_MODE_PROP            = "ro.default_usb_mode";
    private static final String USB_MODE_DEFAULT         = System.getProperty(USB_MODE_PROP);
    private static final String RING_DELAY_PREF          = "pref_ring_delay";
    private static final String RING_DELAY_PROP          = "ro.telephony.call_ring.delay";
    private static final String RING_DELAY_PERSIST_PROP  = "persist.call_ring.delay";
    private static final String RING_DELAY_DEFAULT       = System.getProperty(RING_DELAY_PROP);
    private static final String VM_HEAPSIZE_PREF         = "pref_vm_heapsize";
    private static final String VM_HEAPSIZE_PROP         = "dalvik.vm.heapsize";
    private static final String VM_HEAPSIZE_PERSIST_PROP = "persist.vm_heapsize";
    private static final String VM_HEAPSIZE_DEFAULT      = System.getProperty(VM_HEAPSIZE_PROP);
    private static final String FAST_UP_PREF             = "pref_fast_up";
    private static final String FAST_UP_PROP             = "ro.ril.hsxpa";
    private static final String FAST_UP_PERSIST_PROP     = "persist.fast_up";
    private static final String FAST_UP_DEFAULT          = System.getProperty(FAST_UP_PROP);
    private static final String PROX_DELAY_PREF          = "pref_prox_delay";
    private static final String PROX_DELAY_PROP          = "mot.proximity.delay";
    private static final String PROX_DELAY_PERSIST_PROP  = "persist.prox.delay";
    private static final String PROX_DELAY_DEFAULT       = System.getProperty(PROX_DELAY_PROP);

    private static final String MOD_LCD_PROP         = "ro.sf.lcd_density";
    private static final String MOD_LCD_PREF         = "pref_lcd_density";
    private static final String MOD_LCD_PERSIST_PROP = "persist.lcd_density";

    private static final String SLEEP_PREF             = "pref_sleep";
    private static final String SLEEP_PROP             = "pm.sleep_mode";
    private static final String SLEEP_PERSIST_PROP     = "persist.sleep";
    private static final String SLEEP_DEFAULT          = System.getProperty(SLEEP_PROP);
    private static final String TCP_STACK_PREF         = "pref_tcp_stack";
    private static final String TCP_STACK_PERSIST_PROP = "persist_tcp_stack";
    private static final String TCP_STACK_PROP_0       = "net.tcp.buffersize.default";
    private static final String TCP_STACK_PROP_1       = "net.tcp.buffersize.wifi";
    private static final String TCP_STACK_PROP_2       = "net.tcp.buffersize.umts";
    private static final String TCP_STACK_PROP_3       = "net.tcp.buffersize.gprs";
    private static final String TCP_STACK_PROP_4       = "net.tcp.buffersize.edge";
    private static final String TCP_STACK_BUFFER       = "4096,87380,256960,4096,16384,256960";
    private static final String JIT_PREF               = "pref_jit";
    private static final String JIT_PERSIST_PROP       = "persist_jit";
    private static final String JIT_PROP               = "dalvik.vm.execution-mode";
    private static final String THREE_G_PREF           = "pref_g_speed";
    private static final String THREE_G_PERSIST_PROP   = "persist_3g_speed";
    private static final String THREE_G_PROP_0         = "ro.ril.enable.3g.prefix";
    private static final String THREE_G_PROP_1         = "ro.ril.hep";
    private static final String THREE_G_PROP_2         = FAST_UP_PROP;
    private static final String THREE_G_PROP_3         = "ro.ril.enable.dtm";
    private static final String THREE_G_PROP_4         = "ro.ril.gprsclass";
    private static final String THREE_G_PROP_5         = "ro.ril.hsdpa.category";
    private static final String THREE_G_PROP_6         = "ro.ril.enable.a53";
    private static final String THREE_G_PROP_7         = "ro.ril.hsupa.category";
    private static final String GPU_PREF               = "pref_gpu";
    private static final String GPU_PERSIST_PROP       = "persist_gpu";
    private static final String GPU_PROP               = "debug.sf.hw";

    private CustomPreference   mFullEditor;
    private ListPreference     mWifiScanPref;
    private ListPreference     mMaxEventsPref;
    private ListPreference     mRingDelayPref;
    private ListPreference     mVmHeapsizePref;
    private ListPreference     mFastUpPref;
    private ListPreference     mProxDelayPref;
    private EditTextPreference mLcdPref;
    private ListPreference     mSleepPref;
    private CheckBoxPreference mTcpStackPref;
    private CheckBoxPreference mJitPref;
    private CheckBoxPreference m3gSpeedPref;
    private CheckBoxPreference mGpuPref;

    private boolean result = false;

    private final CMDProcessor cmd = new CMDProcessor();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prop_modder);
        final PreferenceScreen prefSet = getPreferenceScreen();

        mFullEditor = (CustomPreference) prefSet.findPreference(PREF_FULL_EDITOR);

        mWifiScanPref = (ListPreference) prefSet.findPreference(WIFI_SCAN_PREF);
        mWifiScanPref.setOnPreferenceChangeListener(this);

        mMaxEventsPref = (ListPreference) prefSet.findPreference(MAX_EVENTS_PREF);
        mMaxEventsPref.setOnPreferenceChangeListener(this);

        mRingDelayPref = (ListPreference) prefSet.findPreference(RING_DELAY_PREF);
        mRingDelayPref.setOnPreferenceChangeListener(this);

        mVmHeapsizePref = (ListPreference) prefSet.findPreference(VM_HEAPSIZE_PREF);
        mVmHeapsizePref.setOnPreferenceChangeListener(this);

        mFastUpPref = (ListPreference) prefSet.findPreference(FAST_UP_PREF);
        mFastUpPref.setOnPreferenceChangeListener(this);

        mProxDelayPref = (ListPreference) prefSet.findPreference(PROX_DELAY_PREF);
        mProxDelayPref.setOnPreferenceChangeListener(this);

        mSleepPref = (ListPreference) prefSet.findPreference(SLEEP_PREF);
        mSleepPref.setOnPreferenceChangeListener(this);

        mTcpStackPref = (CheckBoxPreference) prefSet.findPreference(TCP_STACK_PREF);

        mJitPref = (CheckBoxPreference) prefSet.findPreference(JIT_PREF);

        mLcdPref = (EditTextPreference) prefSet.findPreference(MOD_LCD_PREF);
        final String lcd = Utils.findBuildPropValueOf(MOD_LCD_PROP);
        if (mLcdPref != null) {
            final EditText lcdET = mLcdPref.getEditText();
            if (lcdET != null) {
                InputFilter lengthFilter = new LengthFilter(3);
                lcdET.setFilters(new InputFilter[]{lengthFilter});
                lcdET.setSingleLine(true);
            }
            mLcdPref.setSummary(String.format(getString(R.string.pref_lcd_alt_summary), lcd));
            mLcdPref.setText(lcd);
        }
        //logDebug( String.format("ModPrefHoler = '%s' found build number = '%s'",  mod));
        mLcdPref.setOnPreferenceChangeListener(this);

        m3gSpeedPref = (CheckBoxPreference) prefSet.findPreference(THREE_G_PREF);

        mGpuPref = (CheckBoxPreference) prefSet.findPreference(GPU_PREF);

        updateScreen();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;
        if (preference == mFullEditor) {
            final PressToLoadFragment ptlFragment = ((PressToLoadFragment) getParentFragment());
            if (ptlFragment != null) {
                ptlFragment.replaceFragment(
                        ToolsEditor.newInstance(PressToLoadFragment.FRAGMENT_BUILD_PROP), true
                );
            }
            return true;
        } else if (preference == mTcpStackPref) {
            logDebug("mTcpStackPref.onPreferenceTreeClick()");
            value = mTcpStackPref.isChecked();
            return doMod(null, TCP_STACK_PROP_0, String.valueOf(value ? TCP_STACK_BUFFER : DISABLE))
                    && doMod(null, TCP_STACK_PROP_1,
                    String.valueOf(value ? TCP_STACK_BUFFER : DISABLE))
                    && doMod(null, TCP_STACK_PROP_2,
                    String.valueOf(value ? TCP_STACK_BUFFER : DISABLE))
                    && doMod(null, TCP_STACK_PROP_3,
                    String.valueOf(value ? TCP_STACK_BUFFER : DISABLE))
                    && doMod(TCP_STACK_PERSIST_PROP, TCP_STACK_PROP_4,
                    String.valueOf(value ? TCP_STACK_BUFFER : DISABLE));
        } else if (preference == mJitPref) {
            logDebug("mJitPref.onPreferenceTreeClick()");
            value = mJitPref.isChecked();
            if (value) {
                return doMod(JIT_PERSIST_PROP, JIT_PROP, "int:jit");
            } else {
                return doMod(JIT_PERSIST_PROP, JIT_PROP, "int:fast");
            }
        } else if (preference == m3gSpeedPref) {
            value = m3gSpeedPref.isChecked();
            return doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_0, String.valueOf(value ? 1 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_1,
                    String.valueOf(value ? 1 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_2,
                    String.valueOf(value ? 2 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_3,
                    String.valueOf(value ? 1 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_4,
                    String.valueOf(value ? 12 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_5,
                    String.valueOf(value ? 8 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_6,
                    String.valueOf(value ? 1 : DISABLE))
                    && doMod(THREE_G_PERSIST_PROP, THREE_G_PROP_7,
                    String.valueOf(value ? 5 : DISABLE));
        } else if (preference == mGpuPref) {
            value = mGpuPref.isChecked();
            return doMod(GPU_PERSIST_PROP, GPU_PROP, String.valueOf(value ? 1 : DISABLE));
        }

        return false;
    }

    /* handle ListPreferences and EditTextPreferences */
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (newValue != null) {
            logDebug("New preference selected: " + newValue);
            if (preference == mWifiScanPref) {
                return doMod(WIFI_SCAN_PERSIST_PROP, WIFI_SCAN_PROP,
                        newValue.toString());
            } else if (preference == mMaxEventsPref) {
                return doMod(MAX_EVENTS_PERSIST_PROP, MAX_EVENTS_PROP,
                        newValue.toString());
            } else if (preference == mRingDelayPref) {
                return doMod(RING_DELAY_PERSIST_PROP, RING_DELAY_PROP,
                        newValue.toString());
            } else if (preference == mVmHeapsizePref) {
                return doMod(VM_HEAPSIZE_PERSIST_PROP, VM_HEAPSIZE_PROP,
                        newValue.toString());
            } else if (preference == mFastUpPref) {
                return doMod(FAST_UP_PERSIST_PROP, FAST_UP_PROP,
                        newValue.toString());
            } else if (preference == mProxDelayPref) {
                return doMod(PROX_DELAY_PERSIST_PROP, PROX_DELAY_PROP,
                        newValue.toString());
            } else if (preference == mLcdPref) {
                return doMod(MOD_LCD_PERSIST_PROP, MOD_LCD_PROP,
                        newValue.toString());
            } else if (preference == mSleepPref) {
                return doMod(SLEEP_PERSIST_PROP, SLEEP_PROP,
                        newValue.toString());
            }
        }

        return false;
    }

    /* method to handle mods */
    private boolean doMod(final String persist, final String key, final String value) {

        result = false;

        class AsyncDoModTask extends AsyncTask<Void, Void, Boolean> {

            private ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                pd = new ProgressDialog(getActivity());
                pd.setIndeterminate(true);
                pd.setMessage("Applying values...Please wait");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                logDebug(String.format("Calling script with args '%s' and '%s'", key, value));
                backupBuildProp();
                if (!mount("rw")) {
                    throw new RuntimeException("Could not remount /system rw");
                }
                boolean success = false;
                try {
                    if (!propExists(key) && value.equals(DISABLE)) {
                        logDebug(String.format(
                                "We want {%s} DISABLED however it doesn't exist so we " +
                                        "do nothing and move on", key
                        ));
                    } else if (propExists(key)) {
                        if (value.equals(DISABLE)) {
                            logDebug(String.format("value == %s", DISABLE));
                            success =
                                    cmd.su.runWaitFor(String.format(KILL_PROP_CMD, key)).success();
                            cmd.su.runWaitFor(String.format(KILL_PROP_CMD, key)).success();
                        } else {
                            logDebug(String.format("value != %s", DISABLE));
                            success = cmd.su.runWaitFor(String.format(REPLACE_CMD, key, value))
                                    .success();
                        }
                    } else {
                        logDebug("append command starting");
                        success =
                                cmd.su.runWaitFor(String.format(APPEND_CMD, key, value)).success();
                    }

                } catch (Exception exc) {
                    logDebug(exc.getMessage());
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean res) {
                // result holds what you return from doInBackground
                super.onPostExecute(res);
                result = res;
                if (!res) {
                    restoreBuildProp();
                } else {
                    updateScreen();
                }
                mount("ro");
                pd.dismiss();
            }
        }
        new AsyncDoModTask().execute();
        return result;
    }


    public boolean mount(String read_value) {
        logDebug("Remounting /system " + read_value);
        return cmd.su.runWaitFor(String.format(REMOUNT_CMD, read_value)).success();
    }

    public boolean propExists(String prop) {
        logDebug("Checking if prop " + prop + " exists in /system/build.prop");
        return cmd.su.runWaitFor(String.format(PROP_EXISTS_CMD, prop)).success();
    }

    public boolean backupBuildProp() {
        logDebug("Backing up build.prop to /system/tmp/pm_build.prop");
        return cmd.su.runWaitFor("cp /system/build.prop /system/tmp/pm_build.prop").success();
    }

    public boolean restoreBuildProp() {
        logDebug("Restoring build.prop from /system/tmp/pm_build.prop");
        return cmd.su.runWaitFor("cp /system/tmp/pm_build.prop /system/build.prop").success();
    }

    public void updateScreen() {
        final String wifi = Utils.findBuildPropValueOf(WIFI_SCAN_PROP);
        if (!wifi.equals(DISABLE)) {
            mWifiScanPref.setValue(wifi);
            mWifiScanPref.setSummary(
                    String.format(getString(R.string.pref_wifi_scan_alt_summary), wifi));
        } else {
            mWifiScanPref.setValue(WIFI_SCAN_DEFAULT);
        }
        final String maxE = Utils.findBuildPropValueOf(MAX_EVENTS_PROP);
        if (!maxE.equals(DISABLE)) {
            mMaxEventsPref.setValue(maxE);
            mMaxEventsPref.setSummary(
                    String.format(getString(R.string.pref_max_events_alt_summary), maxE));
        } else {
            mMaxEventsPref.setValue(MAX_EVENTS_DEFAULT);
        }
        final String ring = Utils.findBuildPropValueOf(RING_DELAY_PROP);
        if (!ring.equals(DISABLE)) {
            mRingDelayPref.setValue(ring);
            mRingDelayPref.setSummary(
                    String.format(getString(R.string.pref_ring_delay_alt_summary), ring));
        } else {
            mRingDelayPref.setValue(RING_DELAY_DEFAULT);
        }
        final String vm = Utils.findBuildPropValueOf(VM_HEAPSIZE_PROP);
        if (!vm.equals(DISABLE)) {
            mVmHeapsizePref.setValue(vm);
            mVmHeapsizePref.setSummary(
                    String.format(getString(R.string.pref_vm_heapsize_alt_summary), vm));
        } else {
            mVmHeapsizePref.setValue(VM_HEAPSIZE_DEFAULT);
        }
        final String fast = Utils.findBuildPropValueOf(FAST_UP_PROP);
        if (!fast.equals(DISABLE)) {
            mFastUpPref.setValue(fast);
            mFastUpPref
                    .setSummary(String.format(getString(R.string.pref_fast_up_alt_summary), fast));
        } else {
            mFastUpPref.setValue(FAST_UP_DEFAULT);
        }
        final String prox = Utils.findBuildPropValueOf(PROX_DELAY_PROP);
        if (!prox.equals(DISABLE)) {
            mProxDelayPref.setValue(prox);
            mProxDelayPref.setSummary(
                    String.format(getString(R.string.pref_prox_delay_alt_summary), prox));
        } else {
            mProxDelayPref.setValue(PROX_DELAY_DEFAULT);
        }
        final String sleep = Utils.findBuildPropValueOf(SLEEP_PROP);
        if (!sleep.equals(DISABLE)) {
            mSleepPref.setValue(sleep);
            mSleepPref.setSummary(String.format(getString(R.string.pref_sleep_alt_summary), sleep));
        } else {
            mSleepPref.setValue(SLEEP_DEFAULT);
        }
        final String tcp = Utils.findBuildPropValueOf(TCP_STACK_PROP_0);
        if (tcp.equals(TCP_STACK_BUFFER)) {
            mTcpStackPref.setChecked(true);
        } else {
            mTcpStackPref.setChecked(false);
        }
        final String jit = Utils.findBuildPropValueOf(JIT_PROP);
        if (jit.equals("int:jit")) {
            mJitPref.setChecked(true);
        } else {
            mJitPref.setChecked(false);
        }

        final String lcd = Utils.findBuildPropValueOf(MOD_LCD_PROP);
        mLcdPref.setSummary(String.format(getString(R.string.pref_lcd_alt_summary), lcd));

        final String g0 = Utils.findBuildPropValueOf(THREE_G_PROP_0);
        final String g3 = Utils.findBuildPropValueOf(THREE_G_PROP_3);
        final String g6 = Utils.findBuildPropValueOf(THREE_G_PROP_6);
        if (g0.equals("1") && g3.equals("1") && g6.equals("1")) {
            m3gSpeedPref.setChecked(true);
        } else {
            m3gSpeedPref.setChecked(false);
        }
        final String gpu = Utils.findBuildPropValueOf(GPU_PROP);
        if (!gpu.equals(DISABLE)) {
            mGpuPref.setChecked(true);
        } else {
            mGpuPref.setChecked(false);
        }
    }
}

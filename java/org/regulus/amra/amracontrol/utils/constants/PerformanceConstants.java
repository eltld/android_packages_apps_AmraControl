package org.regulus.amra.amracontrol.utils.constants;

/**
 * Created by alex on 28.02.14.
 */
public interface PerformanceConstants {

    //==============================================================================================
    // Paths
    //==============================================================================================
    public static final String   POSSIBLE_CPUS              = "/sys/devices/system/cpu/possible";
    public static final String   PRESENT_CPUS               = "/sys/devices/system/cpu/present";
    public static final String   CORE1_ONLINE               = "/sys/devices/system/cpu/cpu1/online";
    public static final String   CORE2_ONLINE               = "/sys/devices/system/cpu/cpu2/online";
    public static final String   CORE3_ONLINE               = "/sys/devices/system/cpu/cpu3/online";
    //----------------------------------------------------------------------------------------------
    public static final String   CPU0_FREQ_CURRENT_PATH     =
            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    public static final String   CPU1_FREQ_CURRENT_PATH     =
            "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq";
    public static final String   CPU2_FREQ_CURRENT_PATH     =
            "/sys/devices/system/cpu/cpu2/cpufreq/scaling_cur_freq";
    public static final String   CPU3_FREQ_CURRENT_PATH     =
            "/sys/devices/system/cpu/cpu3/cpufreq/scaling_cur_freq";
    //----------------------------------------------------------------------------------------------
    public static final String   FREQ0_INFO_MAX_PATH        =
            "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
    public static final String   FREQ0_INFO_MIN_PATH        =
            "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
    public static final String   FREQ0_MAX_PATH             =
            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq";
    public static final String   FREQ0_MIN_PATH             =
            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq";
    public static final String   FREQ1_INFO_MAX_PATH        =
            "/sys/devices/system/cpu/cpu1/cpufreq/cpuinfo_max_freq";
    public static final String   FREQ1_INFO_MIN_PATH        =
            "/sys/devices/system/cpu/cpu1/cpufreq/cpuinfo_min_freq";
    public static final String   FREQ1_MAX_PATH             =
            "/sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq";
    public static final String   FREQ1_MIN_PATH             =
            "/sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq";
    public static final String   FREQ2_INFO_MAX_PATH        =
            "/sys/devices/system/cpu/cpu2/cpufreq/cpuinfo_max_freq";
    public static final String   FREQ2_INFO_MIN_PATH        =
            "/sys/devices/system/cpu/cpu2/cpufreq/cpuinfo_min_freq";
    public static final String   FREQ2_MAX_PATH             =
            "/sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq";
    public static final String   FREQ2_MIN_PATH             =
            "/sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq";
    public static final String   FREQ3_INFO_MAX_PATH        =
            "/sys/devices/system/cpu/cpu3/cpufreq/cpuinfo_max_freq";
    public static final String   FREQ3_INFO_MIN_PATH        =
            "/sys/devices/system/cpu/cpu3/cpufreq/cpuinfo_min_freq";
    public static final String   FREQ3_MAX_PATH             =
            "/sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq";
    public static final String   FREQ3_MIN_PATH             =
            "/sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq";
    //----------------------------------------------------------------------------------------------
    public static final String   FREQ_AVAILABLE_PATH        =
            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
    public static final String   FREQ_TIME_IN_STATE_PATH    =
            "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";
    //----------------------------------------------------------------------------------------------
    public static final String   GOV0_CURRENT_PATH          =
            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
    public static final String   GOV1_CURRENT_PATH          =
            "/sys/devices/system/cpu/cpu1/cpufreq/scaling_governor";
    public static final String   GOV2_CURRENT_PATH          =
            "/sys/devices/system/cpu/cpu2/cpufreq/scaling_governor";
    public static final String   GOV3_CURRENT_PATH          =
            "/sys/devices/system/cpu/cpu3/cpufreq/scaling_governor";
    //----------------------------------------------------------------------------------------------
    public static final String   GOV_AVAIALBLE_PATH         =
            "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors";
    //----------------------------------------------------------------------------------------------
    public static final String   INTELLI_PLUG_PATH          =
            "/sys/module/intelli_plug/parameters/intelli_plug_active";
    public static final String   INTELLI_PLUG_ECO_MODE_PATH =
            "/sys/module/intelli_plug/parameters/eco_mode_active";
    public static final String   INTELLI_PLUG_SUSPEND_PATH  =
            "/sys/kernel/intelliplug/sleep_active_status";
    //----------------------------------------------------------------------------------------------
    public static final String   CPU_TEMP_PATH              =
            "/sys/class/thermal/thermal_zone0/temp";
    //----------------------------------------------------------------------------------------------
    public static final String[] IO_SCHEDULER_PATH          = {
            "/sys/block/mmcblk0/queue/scheduler",
            "/sys/block/mmcblk1/queue/scheduler"};
    //----------------------------------------------------------------------------------------------
    public static final String   PREF_HIDE_CPU_INFO         = "pref_hide_cpu_info";
    public static final String   PREF_INTERVAL_CPU_INFO     = "pref_interval_cpu_info";
    public static final String   PREF_MAX_CPU               = "pref_max_cpu";
    public static final String   PREF_MIN_CPU               = "pref_min_cpu";
    public static final String   PREF_GOV                   = "pref_gov";
    public static final String   PREF_IO                    = "pref_io";
    public static final String   GOV_SETTINGS               = "gov_settings";
    public static final String   GOV_NAME                   = "gov_name";
}


/*
 * Copyright (C) 2014 Alexander "Evisceration" Martinz
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses
 */

package org.regulus.amra.amracontrol.utils.helpers;

import android.app.Activity;

import org.regulus.amra.amracontrol.activities.MainActivity;

/**
 * Helper class for Activity Actions
 */
public class ActivityHelper {

    public static void refreshMenuItems(final Activity activity) {
        if (activity != null) {
            ((MainActivity) activity).restoreActionBar();
        }
    }
}

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
package org.regulus.amra.amracontrol.fragments.parents;

import android.app.Activity;
import android.app.Fragment;

import org.regulus.amra.amracontrol.activities.MainActivity;

/**
 * Created by alex on 18.12.13.
 */
public class AttachFragment extends Fragment {

    protected void onAttach(Activity activity, int number) {
        super.onAttach(activity);

        ((MainActivity) activity).onSectionAttached(number);
    }
}

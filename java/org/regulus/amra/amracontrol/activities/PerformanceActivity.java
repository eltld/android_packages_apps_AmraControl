/*
 *  Copyright (C) 2013-2014 Alexander "Evisceration" Martinz
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
package org.regulus.amra.amracontrol.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.stericson.roottools.RootTools;

import org.regulus.amra.amracontrol.Application;
import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.fragments.performance.PerformanceFragment;
import org.regulus.amra.amracontrol.utils.Utils;
import org.regulus.amra.amracontrol.utils.helpers.PreferenceHelper;

import java.io.IOException;

import static org.regulus.amra.amracontrol.Application.logDebug;

public class PerformanceActivity extends Activity {

    //==============================================================================================
    // Fields
    //==============================================================================================
    final Object lockObject = new Object();

    //==============================================================================================
    // Overridden Methods
    //==============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_drawers);

        if (!Application.HAS_ROOT) {
            Toast.makeText(this
                    , getString(R.string.app_warning_root, getString(R.string.app_name))
                    , Toast.LENGTH_LONG).show();
        }

        PreferenceHelper.getInstance(this);

        Utils.setupDirectories();
        Utils.createFiles(this, true);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.performance);
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new PerformanceFragment())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (lockObject) {
            try {
                logDebug("closing shells");
                RootTools.closeAllShells();
            } catch (IOException e) {
                logDebug("Shell error: " + e.getMessage());
            }
        }
    }
}

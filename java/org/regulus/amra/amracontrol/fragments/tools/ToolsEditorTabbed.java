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

package org.regulus.amra.amracontrol.fragments.tools;

import android.app.Activity;
import android.os.Bundle;
import android.support.v13.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.regulus.amra.amracontrol.R;
import org.regulus.amra.amracontrol.activities.MainActivity;
import org.regulus.amra.amracontrol.fragments.dynamic.PressToLoadFragment;
import org.regulus.amra.amracontrol.fragments.parents.AttachFragment;

/**
 * Created by alex on 23.01.14.
 */
public class ToolsEditorTabbed extends AttachFragment {

    public static final String TAG = "tools_editor_tabbed";
    public static final int    ID  = 310;

    private FragmentTabHost mTabHost;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity, ToolsEditorTabbed.ID);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        View v = layoutInflater.inflate(R.layout.fragment_tabhost, viewGroup, false);

        mTabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        /* VM EDITOR */
        Bundle b = new Bundle();
        b.putInt(PressToLoadFragment.ARG_FRAGMENT, PressToLoadFragment.FRAGMENT_VM);
        b.putInt(PressToLoadFragment.ARG_IMG, R.mipmap.ic_launcher);

        mTabHost.addTab(mTabHost
                .newTabSpec("vmeditor")
                .setIndicator(getString(R.string.vm))
                , PressToLoadFragment.class, b);

        /* SYSCTL EDITOR */
        b = new Bundle();
        b.putInt(PressToLoadFragment.ARG_FRAGMENT, 1);
        b.putInt(PressToLoadFragment.ARG_IMG, R.mipmap.ic_launcher);

        mTabHost.addTab(mTabHost
                .newTabSpec("sysctl")
                .setIndicator(getString(R.string.sysctl))
                , PressToLoadFragment.class, b);

        /* BUILD.PROP EDITOR */
        b = new Bundle();
        b.putInt(PressToLoadFragment.ARG_FRAGMENT, 2);
        b.putInt(PressToLoadFragment.ARG_IMG, R.mipmap.ic_launcher);

        mTabHost.addTab(mTabHost
                        .newTabSpec("buildprop")
                        .setIndicator(getString(R.string.buildprop)),
                PressToLoadFragment.class, b
        );

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MainActivity.mSlidingMenu != null && MainActivity.mSlidingMenu.isMenuShowing()) {
            MainActivity.mSlidingMenu.toggle(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}
